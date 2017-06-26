package lin.comm.http;

/**
 * Created by lin on 1/9/16.
 */

import android.content.Context;
import android.os.Handler;


import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
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

import lin.comm.httpdns.HttpDNS;
import lin.util.Action;


/**
 *
 * @author 王江林
 * @date 2013-7-29 下午9:13:22
 *
 */
public abstract class AbstractHttpCommunicateImpl implements HttpCommunicateImpl{

    protected Context context;
    private int mTimeout = 10000;
    private String name;
    private Map<String,String> defaultHeaders = new HashMap<String,String>();
    private HttpDNS httpDNS;

    private boolean mDebug = false;
    /**
     * 通信 URL
     */
    private URL baseUri = null;

//    private HttpCommunicate.Params defaultParams = new HttpCommunicate.Params();

    private static long cacheSize = 200 * 1024 * 1024;

    private boolean mMainThread = false;

    //HttpCommunicateImpl(){}
    protected AbstractHttpCommunicateImpl(String name,HttpCommunicate c) {
        if(c == null){
            throw new RuntimeException();
        }
        this.name = name;
        //创建HttpClientBuilder
//        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
//        //HttpClient
//        http = httpClientBuilder.build();
//        CookieStore cookie = new BasicCookieStore();
//        http = HttpClients.custom().useSystemProperties()
//                .setDefaultCookieStore(cookie)
//                .build();
    }


    @Override
    public void init(Context context){
        if(this.context == null && context != null) {
            this.context = context;
            CacheDownloadFile.init(context);
        }
    }

    public Context getContext(){
        return this.context;
    }

    @Override
    public int getTimeout() {
        return mTimeout;
    }

    @Override
    public void setTimeout(int timeout) {
        this.mTimeout = timeout;
    }
    //	public HttpCommunicateImpl(){
//	}


    @Override
    public HttpDNS getHttpDNS() {
        return httpDNS;
    }

    @Override
    public void setHttpDNS(HttpDNS httpDNS) {
        this.httpDNS = httpDNS;
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
        return this.defaultHeaders;
    }
    @Override
    public void addHeader(String name, String value){
        defaultHeaders.put(name,value);
    }


    @Override
    public void removeHeader(String name){
        defaultHeaders.remove(name);
    }

    @Override
    public String getName(){
        return name;
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
                baseUri = new URL(uriString.substring(0, uriString.length() - 1));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }else{
            try {
                baseUri = new URL(uriString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public URL getCommUrl(){
        return baseUri;
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
    /**
     *
     * @param credsProvider
     */
//		public void setCredentialsProvider(CredentialsProvider credsProvider){
//			credentialsProvider = credsProvider;
//		}

    private List<SoftReference<HttpRequestListener>> listeners = new ArrayList<SoftReference<HttpRequestListener>>();

    @Override
    public void addHttpRequestListener(HttpRequestListener listener){
        SoftReference<HttpRequestListener> slistener = new SoftReference<HttpRequestListener>(listener);
        listeners.add(slistener);
    }

    @Override
    public void removeHttpRequestListener(HttpRequestListener listener){
        for(SoftReference<HttpRequestListener> item : listeners){
            if(item.get() == listener){
                listeners.remove(item);
                break;
            }
        }
    }

    private void fireRequestResultListener(HttpPackage pack,Object obj, List<lin.comm.http.Error> warning){
        for(SoftReference<HttpRequestListener> item : listeners){
            HttpRequestListener listener = item.get();
            if(listener != null){
                listener.requestComplete(this, pack, obj, warning);
            }
        }
    }

    private void fireRequestFaultListener(HttpPackage pack,Error error){
        for(SoftReference<HttpRequestListener> item : listeners){
            HttpRequestListener listener = item.get();
            if(listener != null){
                listener.requestFault(this, pack, error);
            }
        }
    }

    private void fireRequestListener(HttpPackage pack){
        for(SoftReference<HttpRequestListener> item : listeners){
            HttpRequestListener listener = item.get();
            if(listener != null){
                listener.request(this, pack);
            }
        }
    }

//    private CookieStore cookieStore = new BasicCookieStore();
//    CookieStore getCookieStore(){
//        return cookieStore;
//    }


//    @Override
//    public void newSession(){
//        cookieStore = new BasicCookieStore();
//    }

//	public HttpCommunicateResult request(lin.client.http.TcpPackage pack,ResultListener listener){
//		if(listener != null){
//			return request(pack,listener::result,listener::fault);
//		}
//		return request(pack,null,null);
//	}

//	public HttpCommunicateResult request(lin.client.http.TcpPackage pack,ResultFunction result){
//		return request(pack,result,null);
//	}

//	private CloseableHttpClient http;// = HttpClients.createDefault();
//	private DefaultHttpClient http = new DefaultHttpClient();

    //Java调用
//	loseableHttpClient httpclient = HttpClients.custom()
//            .setDefaultCookieStore(cookies)
//            .build();
//Android修改为

//    private CloseableHttpClient http;// = HttpClients.custom().useSystemProperties()
//            .setDefaultCookieStore(cookies)
//            .build();


    //	private AndroidHTTPClient c = AndroidHttpClient.newInstance("");


    @Override
    public HttpCommunicateResult<Object> request(final lin.comm.http.HttpPackage pack){
        return request(pack,null);
    }

//    @Override
//    public HttpCommunicateResult<Object> request(final lin.comm.http.HttpPackage pack, final ResultListener listener){
//        return request(pack,listener,defaultParams);
//    }

    protected abstract HttpCommunicateRequest getRequest();

    private void processPackHttpHeaders(HttpPackage pack, HttpCommunicate.Params params){
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
    public HttpCommunicateResult<Object> request(final lin.comm.http.HttpPackage pack, final ResultListener listener){

        this.fireRequestListener(pack);

        HttpCommunicate.Params params = new HttpCommunicate.Params();

        params.setDebug(this.isDebug(pack));
        params.setMainThread(this.isMainThread(pack));
        params.setTimeout(this.getTimeout(pack));

        processPackHttpHeaders(pack,params);

        final HttpCommunicateResult<Object> httpHesult = new HttpCommunicateResult<Object>();

        HttpCommunicateRequest request = this.getRequest();

        //		final AutoResetEvent set = new AutoResetEvent(false);
//        HttpURLConnectionRequest request = new HttpURLConnectionRequest(this,pack, new ResultListener() {
        ResultListener listenerImpl = new ResultListener() {

            @Override
            public void result(final Object obj, final List<Error> warning) {
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

            //			@Override
            //			public void progress(long count, long total) {
            //				//HttpUtils.fireProgress(ProgressFunction,count,total);
            //			}

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
        };//);

        request.setPackage(pack);
        request.setImpl(this);
        request.setListener(listenerImpl);
        request.setParams(params);

        httpHesult.request = request;
        //		httpHesult.set = set;
        request.request();
        return httpHesult;
    }

//    public HttpCommunicateResult request2(final lin.client.http.HttpPackage pack,final ResultListener listener){
//
//        this.fireRequestListener(pack);
//
//        final HttpCommunicateResult httpHesult = new HttpCommunicateResult();
////		final AutoResetEvent set = new AutoResetEvent(false);
//        HttpRequest request = new HttpRequest(this,pack, new ResultListener() {
//
//            @Override
//            public void result(final Object obj, final List<Error> warning) {
//                lin.util.thread.ActionExecute.execute(new Action() {
//                    @Override
//                    public void action() {
//                        httpHesult.setResult(true,obj);
//                        fireResult(httpHesult,listener, obj, warning);
//                    }
////					}, new Action() {
////
////						@Override
////						public void action() {
////
////							set.set();
////						}
//                },new Action(){
//
//                    @Override
//                    public void action() {
//                        fireRequestResultListener(pack, obj, warning);
//                    }
//
//                });
//            }
//
////			@Override
////			public void progress(long count, long total) {
////				//HttpUtils.fireProgress(ProgressFunction,count,total);
////			}
//
//            @Override
//            public void fault(final Error error) {
//                lin.util.thread.ActionExecute.execute(new Action() {
//                    @Override
//                    public void action() {
//                        httpHesult.setResult(false,null);
//                        fireFault(httpHesult,listener,error);
//                    }
////				}, new Action() {
////
////					@Override
////					public void action() {
////						set.set();
////					}
//                },new Action(){
//
//                    @Override
//                    public void action() {
//                        fireRequestFaultListener(pack, error);
//                    }});
//            }
//        },httpHesult,http);
//        httpHesult.request = request;
////		httpHesult.set = set;
//        request.request();
//        return httpHesult;
//    }

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
            if(this.isMainThread() && httpResult.threadId != mHandler.getLooper().getThread().getId()){
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
    //	private void fireProgress(HttpCommunicateResult httpHesult,ResultListener listener,long count,long total){
//			if(listener != null){
//				listener.progress(count, total);
//			}
//		}
    private void fireFault(final HttpCommunicateResult httpResult,final ResultListener listener,final Error error){
        if(listener != null){
            if(this.isMainThread() && httpResult.threadId != mHandler.getLooper().getThread().getId()){
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
//			System.out.println("3 progress:"+progress+"\ttotal:"+total);
            if(this.isMainThread() && httpResult.threadId != mHandler.getLooper().getThread().getId()){
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

//	public HttpCommunicateResult upload(File file,ResultListener listener){
//		return null;
//	}

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


    private static ThreadPoolExecutor downloadExecutor = new ThreadPoolExecutor(2, 5, 10,
            TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(3000),
            new ThreadPoolExecutor.CallerRunsPolicy());
    @Override
    public HttpCommunicateResult<FileInfo> download(final URL file, final ResultListener listener, HttpCommunicate.Params params){
        if (params == null){
            params = new HttpCommunicate.Params();

            params.setDebug(this.isDebug());
            params.setMainThread(this.isMainThread());
            params.setTimeout(this.getTimeout());
        }

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

        final ProgressResultListener listenerImpl = new ProgressResultListener(){
//            private boolean isDeleteFile = false;
            @Override
            public void result(final Object obj,final List<Error> warning) {
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
//				System.out.println("2 progress:"+progress+"\ttotal:"+total);
                if (finalPListener != null){
                    fireProgress(httpHesult, finalPListener,progress,total);
                }
            }
        };//);

//        request.setPackage(pack);


        request.setImpl(this);
        request.setListener(listenerImpl);
        request.setParams(params);

        httpHesult.request = request;

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



//        downloadFile.context = context;
////		downloadFile.http = this.http;
//        downloadFile.download(file);
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
                downloadExecutor.execute(task);
            }
        }finally {
            lock.unlock();
        }
    }

    private void popDownload(String url){
        lock.lock();
        try{

            if(notDownloadUrls.containsKey(url)){
                downloadExecutor.execute(notDownloadUrls.remove(url));
            }
            downloadUrls.remove(url);
        }finally {
            lock.unlock();
        }
    }


    public boolean isMainThread() {
        return mMainThread;
    }

    private Handler mHandler = new Handler();

    public void setMainThread(boolean mainThread) {
        this.mMainThread = mainThread;
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