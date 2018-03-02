package lin.comm.http;

/**
 * Created by lin on 1/9/16.
 */

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lin.comm.http.auth.Authentication;
import lin.comm.httpdns.HttpDNS;
import lin.util.Action;

/**
 * @author 王江林
 * @date 2013-7-29 下午9:13:22
 *
 */
public abstract class AbstractHttpCommunicateImpl implements HttpCommunicateImpl{


    private static ThreadPoolExecutor mExecutor = new ThreadPoolExecutor(10, 50, 15,
            TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(3000),
            new ThreadPoolExecutor.CallerRunsPolicy());

    protected Context mContext;
    private int mTimeout = 10000;
    private String mName;
    private Map<String,String> mDefaultHeaders = new HashMap<String,String>();
    private HttpDNS mHttpDNS;

    private SessionInfo mSessionInfo;

    private boolean mDebug = false;
    /**
     * 通信 URL
     */
    private URL mBaseUrl = null;

    private static long cacheSize = 200 * 1024 * 1024;

    private boolean mMainThread = false;

    private HttpCommunicate.Mock mMock;

    private Handler mHandler = null;

    private Authentication authentication;

    protected AbstractHttpCommunicateImpl(String name,HttpCommunicate c) {
        if(c == null){
            throw new RuntimeException();
        }
        this.mName = name;
        mSessionInfo = new SessionInfo();

        if(Looper.myLooper() == null){
            mHandler = new Handler(Looper.getMainLooper());
        }else{
            mHandler = new Handler();
        }
    }


    @Override
    public void init(Context context){
        if(this.mContext == null && context != null) {
            this.mContext = context;
            CacheDownloadFile.init(context);
        }
    }

    public Context getContext(){
        return this.mContext;
    }

    @Override
    public int getTimeout() {
        return mTimeout;
    }

    @Override
    public void setTimeout(int timeout) {
        this.mTimeout = timeout;
    }

    @Override
    public HttpDNS getHttpDNS() {
        return mHttpDNS;
    }

    @Override
    public void setHttpDNS(HttpDNS httpDNS) {
        this.mHttpDNS = httpDNS;
    }

    @Override
    public long getCacheSize() {
        return cacheSize;
    }

    @Override
    public void setCacheSize(long cacheSize) {
        this.cacheSize = cacheSize;
    }

    public Map<String,String> defaultHeaders(){
        return this.mDefaultHeaders;
    }
    @Override
    public void addHeader(String name, String value){
        mDefaultHeaders.put(name,value);
    }


    @Override
    public void removeHeader(String name){
        mDefaultHeaders.remove(name);
    }

    @Override
    public String getName(){
        return mName;
    }


    @Override
    public HttpCommunicate.Mock getMock() {
        if(mMock != null){
            return mMock;
        }
        synchronized (this){
            if(mMock == null) {
                mMock = new HttpCommunicate.Mock();
            }
        }
        return mMock;
    }

    /**
     * 设置通信 URL
     * @param url
     */

    @Override
    public void setCommUrl(URL url){
        String uriString = url.toString();
        if(uriString.endsWith("/")){
            try {
                mBaseUrl = new URL(uriString.substring(0, uriString.length() - 1));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }else{
            try {
                mBaseUrl = new URL(uriString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public URL getCommUrl(){
        return mBaseUrl;
    }

    @Override
    public boolean isDebug() {
        return mDebug;
    }

    @Override
    public void setDebug(boolean debug) {
        this.mDebug = debug;
    }

    //		/**
//		 * 代理对象
//		 */
//		private AuthenticationHandler authenticationHandler=null;
//		/**
//		 * 设置代理
//		 * @param proxy
//		 */
//		public void setAuthenticationHandler(AuthenticationHandler proxy){
//			authenticationHandler = proxy;
//		}
//
//		private CredentialsProvider credentialsProvider;
//
//    /**
//     *
//     * @param credsProvider
//     */
//		public void setCredentialsProvider(CredentialsProvider credsProvider){
//			credentialsProvider = credsProvider;
//		}

    private List<HttpRequestListener> mListeners = new ArrayList<HttpRequestListener>();

    @Override
    public void addHttpRequestListener(HttpRequestListener listener){
        mListeners.add(listener);
    }

    @Override
    public void removeHttpRequestListener(HttpRequestListener listener){
        mListeners.remove(listener);
    }

    private void fireRequestResultListener(HttpPackage pack,Object obj, List<lin.comm.http.Error> warning){
        for(HttpRequestListener listener : mListeners){
            if(listener != null){
                listener.requestComplete(this, pack, obj, warning);
            }
        }
    }

    private void fireRequestFaultListener(HttpPackage pack,Error error){
        for(HttpRequestListener listener : mListeners){
            if(listener != null){
                listener.requestFault(this, pack, error);
            }
        }
    }

    private void fireRequestListener(HttpPackage pack){
        for(HttpRequestListener listener : mListeners){
            if(listener != null){
                listener.request(this, pack);
            }
        }
    }


    @Override
    public HttpCommunicateResult<Object> request(final lin.comm.http.HttpPackage pack){
        return request(pack,null);
    }

//    protected abstract HttpCommunicateRequest getRequest();

    protected abstract HttpCommunicateHandler getHandler();

    private void processPackHttpHeaders(HttpPackage pack, HttpCommunicate.Params params) {
        if (mSessionInfo.mCookieStore != null) {
            List<HttpCookie> cookies = mSessionInfo.mCookieStore.getCookies();
//            try {
//                cookies = mSessionInfo.mCookieStore.get(this.getCommUrl().toURI());
//            } catch (URISyntaxException e) {
//            }
            if(cookies != null && cookies.size() > 0) {

                StringBuffer buffer = new StringBuffer();
                for(HttpCookie cookie : cookies){
                    if(cookie.hasExpired()){
                        continue;
                    }
                    buffer.append(cookie.toString());
                    buffer.append("; ");
                }
                if(buffer.length() > 0){
                    buffer.deleteCharAt(buffer.length()-2);
                }
                params.addHeader("Cookie", buffer.toString());
            }
        }
        if(pack == null){
            return;
        }
        pack.getRequestHandle().preprocess(pack,params);

        Map<String,String> headers = pack.getHeaders();
        if(headers == null || headers.size() == 0){
            return;
        }
        for(Map.Entry<String,String> item : headers.entrySet()){
            params.addHeader(item.getKey(),item.getValue());
        }
    }

    @Override
//    	public HttpCommunicateResult request(lin.client.http.TcpPackage pack,final ResultFunction result,final FaultFunction fault){
//    public HttpCommunicateResult<Object> request(final lin.comm.http.HttpPackage pack, final ResultListener listener, HttpCommunicate.Params params){
    public <T> HttpCommunicateResult<T> request(final lin.comm.http.HttpPackage<T> pack, final ResultListener<T> listener){

        this.fireRequestListener(pack);

        final HttpCommunicate.Params params = new HttpCommunicate.Params();

        params.setDebug(this.isDebug(pack));
        params.setMainThread(this.isMainThread(pack));
        params.setTimeout(this.getTimeout(pack));

        processPackHttpHeaders(pack,params);

        final HttpCommunicateResult<T> httpHesult = new HttpCommunicateResult<T>();

        final ResultListener listenerImpl = new ResultListener<T>() {

            @Override
            public void result(final T obj, final List<Error> warning) {
                lin.util.thread.ActionExecute.execute(new Action() {
                    @Override
                    public void action() {
                        httpHesult.setResult(true,obj,warning,null);
                        fireResult(httpHesult,listener, obj, warning);
                    }
                    //					}, new Action() {
                    //
                    //						@Override
                    //						public void action() {
                    //
                    //							set.set();
                    //						}
                },new Action(){

                    @Override
                    public void action() {
                        fireRequestResultListener(pack, obj, warning);
                    }

                });
            }

            @Override
            public void fault(final Error error) {
                lin.util.thread.ActionExecute.execute(new Action() {
                    @Override
                    public void action() {
                        httpHesult.setResult(false,null,null,error);
                        fireFault(httpHesult,listener,error);
                    }
                    //				}, new Action() {
                    //
                    //					@Override
                    //					public void action() {
                    //						set.set();
                    //					}
                },new Action(){

                    @Override
                    public void action() {
                        fireRequestFaultListener(pack, error);
                    }});
            }
        };

        HttpCommunicate.Mock mock = this.getMock();

        if(mock != null && mock.process(mExecutor,listenerImpl,pack)){
            return httpHesult;
        }

        final HttpCommunicateHandler handler = this.getHandler();


        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if(authentication != null){
                    params.addHeader("Authorization",authentication.auth());
                }

                handler.setPackage(pack);
                handler.setImpl(AbstractHttpCommunicateImpl.this);
                handler.setParams(params);

                handler.process(new HttpCommunicateHandler.Listener() {
                    @Override
                    public void response(HttpClientResponse response) {

                        processCookie(response);
                        pack.getRequestHandle().response(pack,response,listenerImpl);
                    }
                });
            }
        });

        httpHesult.mRequest = handler;

        return httpHesult;
    }

    private void processCookie(HttpClientResponse response){
        List<String> cookies = response.getHeaders("Set-Cookie");
        if(cookies == null || cookies.isEmpty()){
            return;
        }
        for(String cookieStr : cookies) {

            if (cookieStr != null && !"".equals(cookieStr)) {

                URI uri = null;
                try {
                    uri = mBaseUrl.toURI();
                } catch (URISyntaxException e) {
                }
                for (HttpCookie cookie : HttpCookie.parse(cookieStr)) {
                    mSessionInfo.mCookieStore.add(uri, cookie);
                }
            }
        }
    }


    private boolean isMainThread(HttpPackage pack){
        if(pack.getCommParams() == null || pack.getCommParams().isMainThread() != null){
            return pack.getCommParams().isMainThread();
        }
        return this.mMainThread;
    }

    protected boolean isDebug(HttpPackage pack){
        if(pack.getCommParams() == null || pack.getCommParams().isDebug() != null){
            return pack.getCommParams().isDebug();
        }
        return this.mDebug;
    }

    protected int getTimeout(HttpPackage pack){
        if(pack.getCommParams() == null || pack.getCommParams().getTimeout() != null){
            return pack.getCommParams().getTimeout();
        }
        return this.mTimeout;
    }
    private boolean fireResult(final HttpCommunicateResult httpResult,final ResultListener listener,final Object obj,final List<Error> warning){
        if(listener != null){
            if(this.isMainThread() && httpResult.mThreadId != mHandler.getLooper().getThread().getId()){
                mHandler.post(new Runnable(){

                    @Override
                    public void run() {
                        try{
                            listener.result(obj, warning);
                        }finally{
                            httpResult.getAutoResetEvent().set();
//								if(obj instanceof File){
//									((File) obj).delete();
//								}
                        }
                    }});
                return false;
            }else{
                try{
                    listener.result(obj, warning);
                }finally{
                    httpResult.getAutoResetEvent().set();
                }
                return true;
            }
        }else{
            httpResult.getAutoResetEvent().set();
        }
        return true;
    }

    private void fireFault(final HttpCommunicateResult httpResult,final ResultListener listener,final Error error){
        if(listener != null){
            if(this.isMainThread() && httpResult.mThreadId != mHandler.getLooper().getThread().getId()){
                mHandler.post(new Runnable(){

                    @Override
                    public void run() {
                        try{
                            listener.fault(error);
                        }finally{
                            httpResult.getAutoResetEvent().set();
                        }
                    }});
            }else{
                try{
                    listener.fault(error);
                }finally{
                    httpResult.getAutoResetEvent().set();
                }
            }
        }else{
            httpResult.getAutoResetEvent().set();
        }
    }
    private void fireProgress(final HttpCommunicateResult httpResult,final ProgressResultListener listener,final long progress,final long total){
        if(listener != null){

            if(this.isMainThread() && httpResult.mThreadId != mHandler.getLooper().getThread().getId()){
                mHandler.post(new Runnable(){

                    @Override
                    public void run() {
                        listener.progress(progress,total);
                    }});
            }else{
                listener.progress(progress,total);
            }
        }
    }


    @Override
    public HttpCommunicateResult<FileInfo> download(String file){
        return download(file,null,null);
    }

    @Override
    public HttpCommunicateResult<FileInfo> download(String file, final ResultListener listener){
        return download(file,listener,null);
    }
    @Override
    public HttpCommunicateResult<FileInfo> download(String file, final ResultListener listener, HttpCommunicate.Params params){

        URL url = null;
        try {
            url = new URL(file);
        } catch (MalformedURLException e) {

            //AutoResetEvent set = new AutoResetEvent();
            HttpCommunicateResult result = new HttpCommunicateResult();
            //result.set = set;

            fireFault(result, listener, new Error(-2,null,null,null));

//			result.getAutoResetEvent().set();
            return result;
        }
        if(params == null){
            params = new HttpCommunicate.Params();
            params.setDebug(this.isDebug());
            params.setMainThread(this.isMainThread());
            params.setTimeout(this.getTimeout());
        }
        return download(url, listener,params);

    }

    @Override
    public HttpCommunicateResult<FileInfo> download(URL file) {
        return download(file,null,null);
    }
    @Override
    public HttpCommunicateResult<FileInfo> download(URL file, final ResultListener listener) {
        return download(file,listener,null);
    }

    protected abstract HttpCommunicateDownloadFile downloadRequest();


    @Override
    public HttpCommunicateResult<FileInfo> download(final URL file, final ResultListener listener, HttpCommunicate.Params params){
        if (params == null){
            params = new HttpCommunicate.Params();

            params.setDebug(this.isDebug());
            params.setMainThread(this.isMainThread());
            params.setTimeout(this.getTimeout());
        }

        processPackHttpHeaders(null,params);

        ProgressResultListener pListener = null;
        if(listener instanceof ProgressResultListener) {
            pListener = (ProgressResultListener) listener;
        }

        final HttpCommunicateResult<FileInfo> httpHesult = new HttpCommunicateResult<FileInfo>();
//		final AutoResetEvent set = new AutoResetEvent(false);
//		httpHesult.set = set;
        final ProgressResultListener finalPListener = pListener;
        fireRequestListener(null);

        final HttpCommunicateDownloadFile request = this.downloadRequest();

        final ProgressResultListener listenerImpl = new ProgressResultListener<FileInfo>(){
//            private boolean isDeleteFile = false;
            @Override
            public void result(final FileInfo obj,final List<Error> warning) {
                lin.util.thread.ActionExecute.execute(new Action() {
                    @Override
                    public void action() {
                        popDownload(file.toString());
                    }
                },new Action() {
                    @Override
                    public void action() {
                        if(obj instanceof FileInfo) {
                            CacheDownloadFile.save((FileInfo)obj);
                        }
                    }
				}, new Action() {

					@Override
					public void action() {
                        httpHesult.setResult(true,(FileInfo)obj,warning,null);
                        fireResult(httpHesult,listener, obj, warning);
					}
                },new Action(){

                    @Override
                    public void action() {
                        fireRequestResultListener(null, obj, warning);
                    }

                });
            }

            @Override
            public void fault(final Error error) {
                lin.util.thread.ActionExecute.execute(new Action() {
                    @Override
                    public void action() {
                        httpHesult.setResult(false,null,null,error);
                        fireFault(httpHesult, listener, error);
                    }
//				}, new Action() {//fireFault中已经set了
//
//					@Override
//					public void action() {
//						httpHesult.getAutoResetEvent().set();
//					}
                },new Action(){

                    @Override
                    public void action() {
                        fireRequestFaultListener(null, error);
                    }});
            }

            @Override
            public void progress(long progress, long total) {

                if (finalPListener != null){
                    fireProgress(httpHesult, finalPListener,progress,total);
                }
            }
        };


        request.setImpl(this);
        request.setListener(listenerImpl);
        request.setParams(params);

        httpHesult.mRequest = request;

        Runnable task = new Runnable() {
            @Override
            public void run() {
                HttpCommunicateDownloadFile.HttpFileInfo info = request.getFileInfo(file);

                FileInfo cacheFile = CacheDownloadFile.getFileInfo(file.toString());

                if(cacheFile != null && info != null){
                    if(cacheFile.getFile().exists()){
                        if((info.getFileSize() <=0 || cacheFile.getFile().length() == info.getFileSize())
                                && cacheFile.getLastModified() == info.getLastModified()
                                ){

                            listenerImpl.result(cacheFile,null);
                            return;
                        }
                    }
                }

                request.download(file);
            }
        };


        pushDownloadTask(file.toString(),task);

        return httpHesult;
    }

    private final static Map<String,Boolean> downloadUrls = new HashMap<String,Boolean>();
    private final static Map<String,Runnable> notDownloadUrls = new HashMap<String,Runnable>();
    private final static Lock lock = new ReentrantLock();

    private void pushDownloadTask(String url,Runnable task){

        lock.lock();
        try {
            if (downloadUrls.containsKey(url)) {
                notDownloadUrls.put(url, task);
            } else {
                downloadUrls.put(url, true);
                mExecutor.execute(task);
            }
        }finally {
            lock.unlock();
        }
    }

    private void popDownload(String url){
        lock.lock();
        try{

            if(notDownloadUrls.containsKey(url)){
                mExecutor.execute(notDownloadUrls.remove(url));
            }
            downloadUrls.remove(url);
        }finally {
            lock.unlock();
        }
    }


    public boolean isMainThread() {
        return mMainThread;
    }

    public void setMainThread(boolean mainThread) {
        this.mMainThread = mainThread;
    }

    @Override
    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public void newSession() {
        mSessionInfo = new SessionInfo();
    }
//    protected Error error(long code,String message,String cause,String stackTrace){
//        Error error = new Error();
//        error.setCode(code);
//        error.setMessage(message);
//        error.setStackTrace(stackTrace);
//        error.setCause(cause);
//
//        return error;
//    }
}