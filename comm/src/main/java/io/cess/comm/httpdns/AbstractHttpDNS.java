package io.cess.comm.httpdns;

import android.content.Context;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.cess.util.JsonUtil;
import io.cess.util.LocalStorage;
import io.cess.util.LocalStorageImpl;

/**
 * @author lin
 * @date 4/25/16.
 */
public abstract class AbstractHttpDNS implements HttpDNS {

    /**
     * 最大线程数
     */
    private static final int MAX_THREAD_NUM = 5;

    /**
     * 如果 http dns 返回有多个 ip，客户用ip的策略
     */
    private HttpDNS.SessionMode mSessionMode = SessionMode.Sticky;

    /**
     * 表示如果ip已经过期，是否还可用,true：表示，ip过期了，必须从服务获取新的ip
     */
    private boolean mIsExpiredIpAvailable = true;

    /**
     * 同步锁
     */
    private Lock mLock = new ReentrantLock();

    private class QueryHostTask implements Callable<HostObject> {
        /**
         * 主机名
         */
        private String mHostName;
        /**
         * 超时时间，以毫秒为单位
         */
        private int mTimeout;
//        private boolean isRequestRetried = false;

        //timeout以毫秒为单位
        public QueryHostTask(String hostToQuery,int timeout) {
            this.mHostName = hostToQuery;
            this.mTimeout = timeout;
        }

        @Override
        public HostObject call() {
            HostObject hostObject = fetch(this.mHostName,this.mTimeout);
            return pushHostObject(hostObject,this.mHostName);
        }

    }

    /**
     * http dns 查询的结果缓存
     */
    private ConcurrentMap<String, HostObject> mHostManager = new ConcurrentHashMap<String, HostObject>();
    /**
     * 线程池
     */
    private ExecutorService mPool = new ThreadPoolExecutor(1, MAX_THREAD_NUM,
            200L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * http dns 降级策略
     */
    private DegradationFilter mDegradationFilter = null;

    /**
     * http dsn 存储，实现当app退出第二次进入时，仍可以使用上次的结果，提高app打开时的响应速度
     */
    private LocalStorageImpl mHttpDnsStoreage = null;

    public AbstractHttpDNS(Context context) {
        LocalStorage.init(context);
        if(context == null) {
            mHttpDnsStoreage = LocalStorage.get("http_dns");
        }
    }

    // 是否允许过期的IP返回
    public void setExpiredIpAvailable(boolean flag) {
        mIsExpiredIpAvailable = flag;
    }

    public boolean isExpiredIpAvailable() {
        return mIsExpiredIpAvailable;
    }

    /**
     * 预加载
     * @param hosts
     */
    @Override
    public void setPreResolveHosts(final String ... hosts){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(String hostName : hosts){
                    getIpByHost(hostName,20000);
                }
            }
        }).start();
    }

    /**
     *
     * @param hostName
     * @param timeout
     * @return
     */
    protected abstract HostObject fetch(String hostName,int timeout);

    /**
     *
     * @param hostObject
     * @param hostName
     * @return
     */
    private HostObject pushHostObject(HostObject hostObject,String hostName){

        if(hostObject == null){
            hostObject = new HostObject();
            hostObject.setHostName(hostName);
        }
        if(hostObject.getTtl() <= 0){
            hostObject.setTtl(120);
        }
        hostObject.setSessionMode(mSessionMode);
        hostObject.status(mHostManager.get(hostName));
        hostObject.setQueryTime(System.currentTimeMillis() / 1000);
        mHostManager.put(hostName, hostObject);
        if(mHttpDnsStoreage != null) {
            mHttpDnsStoreage.setItem(hostName, JsonUtil.serialize(hostObject));
        }
        return hostObject;
    }

    private HostObject fetchWithCache(String hostName,int timeout){
        String hostObjectStr = null;
        if(mHttpDnsStoreage != null){
            hostObjectStr = mHttpDnsStoreage.getItem(hostName);
        }
//        HostObject.prase(hostObjectStr)

        HostObject hostObject = null;

        try{
            hostObject = JsonUtil.deserialize(hostObjectStr,HostObject.class);
        }catch (Throwable e){}

        Future<HostObject> future = mPool.submit(new QueryHostTask(hostName, timeout));

        if(hostObject != null){
            mHostManager.put(hostName, hostObject);
            if(System.currentTimeMillis() / 1000 - hostObject.getQueryTime() > 86400) {
                if(mHttpDnsStoreage != null) {
                    mHttpDnsStoreage.remove(hostName);
                }
            }
            return hostObject;
        }

        try {
            hostObject = future.get(2, TimeUnit.MINUTES);
        } catch (Throwable e) {
        }
        return pushHostObject(hostObject, hostName);
    }


    public String getIpByHost(String hostName) {
        return getIpByHost(hostName,3000);
    }

    private String getIpByHost(String hostName,int timeout) {
        if (mDegradationFilter != null) {
            if (mDegradationFilter.shouldDegradeHttpDNS(hostName)) {
                return null;
            }
        }
        HostObject host = mHostManager.get(hostName);
        if (host == null || (host.isExpired() && !isExpiredIpAvailable())) {
            mLock.lock();
            host = mHostManager.get(hostName);
            if (host == null || (host.isExpired() && !isExpiredIpAvailable())) {
                fetchWithCache(hostName, timeout);
//                Future<HostObject> future = pool.submit(new QueryHostTask(hostName, timeout));
//                HostObject hostObject = null;
//                try {
//                    hostObject = future.get(2, TimeUnit.SECONDS);
//                } catch (Throwable e) {
//                }
//                pushHostObject(hostObject, hostName);
            }
            mLock.unlock();
        }

        host = mHostManager.get(hostName);
        if(host == null){
            return null;
        }else if (host.isExpired()) {
            // 同步返回，异步更新
            host.setQueryTime(System.currentTimeMillis()/1000);
            mPool.submit(new QueryHostTask(hostName,20000));
            return host.getIp();
        }
        return host.getIp();
    }

    public void setDegradationFilter(DegradationFilter filter) {
        mDegradationFilter = filter;
    }

    public SessionMode getSessionMode() {
        return mSessionMode;
    }

    @Override
    public void setSessionMode(SessionMode sessionMode) {
        this.mSessionMode = sessionMode;
    }
}
