package lin.client.http.httpurlconnection;

import org.apache.http.impl.client.CloseableHttpClient;

import lin.client.http.AbstractHttpCommunicateImpl;
import lin.client.http.HttpCommunicate;
import lin.client.http.HttpCommunicateDownloadFile;
import lin.client.http.HttpCommunicateRequest;

/**
 * 
 * @author 王江林
 * @date 2013-7-29 下午9:13:22
 *
 */
public class HttpURLConnectionCommunicateImpl extends AbstractHttpCommunicateImpl {
	public HttpURLConnectionCommunicateImpl(String name, HttpCommunicate c) {
		super(name, c);
	}

	private SessionInfo sessionInfo = new SessionInfo();
//	private Context context;
//	public void init(Context context){
//		this.context = context;
//	}
//	private String name;

	//HttpCommunicateImpl(){}
//	public HttpURLConnectionCommunicateImpl(String name,HttpCommunicate c) {
//		if(c == null){
//			throw new RuntimeException();
//		}
//		this.name = name;
		//创建HttpClientBuilder  
//        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
//        //HttpClient  
//        http = httpClientBuilder.build();  
//		CookieStore  cookie = new BasicCookieStore();
//		 http = HttpClients.custom().useSystemProperties()
//		            .setDefaultCookieStore(cookie)
//		            .build();
//	}

	@Override
	protected HttpCommunicateRequest getRequest() {
		return new HttpURLConnectionRequest(sessionInfo);
	}

	@Override
	protected HttpCommunicateDownloadFile downloadRequest() {
		return new DownloadFile();
	}

//	private int timeout = 10000;
//
//	public int getTimeout() {
//		return timeout;
//	}
//
//	public void setTimeout(int timeout) {
//		this.timeout = timeout;
//	}
//	//	public HttpCommunicateImpl(){
////	}
//
//
//	Map<String,String> defaultHeaders = new HashMap<String,String>();
//
//	public void addHeader(String name,String value){
//		defaultHeaders.put(name,value);
//	}
//
//	public void removeHeader(String name){
//		defaultHeaders.remove(name);
//	}
//	public String getName(){
//		return name;
//	}
//
//	private boolean debug = false;
//	/**
//	 * 通信 URL
//	 */
//		private URL baseUri = null;
//
//		/**
//		 * 设置通信 URL
//		 * @param url
//		 */
//		public void setCommUrl(URL url){
//			String uriString = url.toString();
//			if(uriString.endsWith("/")){
//				try {
//					baseUri = new URL(uriString.substring(0, uriString.length() - 1));
//				} catch (MalformedURLException e) {
//					e.printStackTrace();
//				}
//			}else{
//				try {
//					baseUri = new URL(uriString);
//				} catch (MalformedURLException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//
//		public URL getCommUrl(){
//			return baseUri;
//		}
//
//		public boolean isDebug() {
//			return debug;
//		}
//
//		public void setDebug(boolean debug) {
//			this.debug = debug;
//		}

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

//		private List<SoftReference<HttpRequestListener>> listeners = new ArrayList<SoftReference<HttpRequestListener>>();
//	public void addHttpRequestListener(HttpRequestListener listener){
//		SoftReference<HttpRequestListener> slistener = new SoftReference<HttpRequestListener>(listener);
//		listeners.add(slistener);
//		}
//	public void removeHttpRequestListener(HttpRequestListener listener){
//		for(SoftReference<HttpRequestListener> item : listeners){
//			if(item.get() == listener){
//				listeners.remove(item);
//				break;
//			}
//		}
//	}
//
//	private void fireRequestResultListener(HttpPackage pack,Object obj, List<lin.client.http.Error> warning){
//		for(SoftReference<HttpRequestListener> item : listeners){
//			if(item.get() != null){
//				item.get().requestComplete(this, pack, obj, warning);
//			}
//		}
//	}
//
//	private void fireRequestFaultListener(HttpPackage pack,Error error){
//		for(SoftReference<HttpRequestListener> item : listeners){
//			if(item.get() != null){
//				item.get().requestFault(this, pack, error);
//			}
//		}
//	}
//
//	private void fireRequestListener(HttpPackage pack){
//		for(SoftReference<HttpRequestListener> item : listeners){
//			if(item.get() != null){
//				item.get().request(this, pack);
//			}
//		}
//	}
//
//	private CookieStore cookieStore = new BasicCookieStore();
//	 CookieStore getCookieStore(){
//		return cookieStore;
//	}
//
//	 public void newSession(){
//		 cookieStore = new BasicCookieStore();
//	 }

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

private CloseableHttpClient http;// = HttpClients.custom().useSystemProperties()

	@Override
	public void newSession() {

	}
//            .setDefaultCookieStore(cookies)
//            .build();       


//	private AndroidHTTPClient c = AndroidHttpClient.newInstance("");
//	private HttpCommunicate.Params defaultParams = new HttpCommunicate.Params();

//	public HttpCommunicateResult request(final lin.client.http.HttpPackage pack,final ResultListener listener){
//		return request(pack,listener,defaultParams);
//	}
//	public HttpCommunicateResult request(lin.client.http.TcpPackage pack,final ResultFunction result,final FaultFunction fault){
//	public HttpCommunicateResult request(final lin.client.http.HttpPackage pack,final ResultListener listener,HttpCommunicate.Params params){
//
//		if (params == null){
//			params = defaultParams;
//		}
//
//		this.fireRequestListener(pack);
//
//		final HttpCommunicateResult httpHesult = new HttpCommunicateResult();
//	//		final AutoResetEvent set = new AutoResetEvent(false);
//		HttpURLConnectionRequest request = new HttpURLConnectionRequest(this,pack, new ResultListener() {
//
//			@Override
//			public void result(final Object obj, final List<Error> warning) {
//				lin.util.thread.ActionExecute.execute(new Action() {
//					@Override
//					public void action() {
//						httpHesult.setResult(true,obj);
//						fireResult(httpHesult,listener, obj, warning);
//					}
//	//					}, new Action() {
//	//
//	//						@Override
//	//						public void action() {
//	//
//	//							set.set();
//	//						}
//				},new Action(){
//
//					@Override
//					public void action() {
//						fireRequestResultListener(pack, obj, warning);
//					}
//
//				});
//			}
//
//	//			@Override
//	//			public void progress(long count, long total) {
//	//				//HttpUtils.fireProgress(ProgressFunction,count,total);
//	//			}
//
//			@Override
//			public void fault(final Error error) {
//				lin.util.thread.ActionExecute.execute(new Action() {
//					@Override
//					public void action() {
//						httpHesult.setResult(false,null);
//						fireFault(httpHesult,listener,error);
//					}
//	//				}, new Action() {
//	//
//	//					@Override
//	//					public void action() {
//	//						set.set();
//	//					}
//				},new Action(){
//
//					@Override
//					public void action() {
//						fireRequestFaultListener(pack, error);
//					}});
//			}
//		});
//		httpHesult.request = request;
//	//		httpHesult.set = set;
//		request.request();
//		return httpHesult;
//	}

//	public HttpCommunicateResult request2(final lin.client.http.HttpPackage pack,final ResultListener listener){
//
//		this.fireRequestListener(pack);
//
//		final HttpCommunicateResult httpHesult = new HttpCommunicateResult();
////		final AutoResetEvent set = new AutoResetEvent(false);
//		HttpRequest request = new HttpRequest(this,pack, new ResultListener() {
//
//			@Override
//			public void result(final Object obj, final List<Error> warning) {
//				lin.util.thread.ActionExecute.execute(new Action() {
//					@Override
//					public void action() {
//						httpHesult.setResult(true,obj);
//						fireResult(httpHesult,listener, obj, warning);
//					}
////					}, new Action() {
////
////						@Override
////						public void action() {
////
////							set.set();
////						}
//				},new Action(){
//
//					@Override
//					public void action() {
//						fireRequestResultListener(pack, obj, warning);
//					}
//
//				});
//			}
//
////			@Override
////			public void progress(long count, long total) {
////				//HttpUtils.fireProgress(ProgressFunction,count,total);
////			}
//
//			@Override
//			public void fault(final Error error) {
//				lin.util.thread.ActionExecute.execute(new Action() {
//					@Override
//					public void action() {
//						httpHesult.setResult(false,null);
//						fireFault(httpHesult,listener,error);
//					}
////				}, new Action() {
////
////					@Override
////					public void action() {
////						set.set();
////					}
//				},new Action(){
//
//					@Override
//					public void action() {
//						fireRequestFaultListener(pack, error);
//					}});
//			}
//		},httpHesult,http);
//		httpHesult.request = request;
////		httpHesult.set = set;
//		request.request();
//		return httpHesult;
//	}
	
//	private boolean fireResult(final HttpCommunicateResult httpResult,final ResultListener listener,final Object obj,final List<Error> warning){
//			if(listener != null){
//				if(this.mainThread && httpResult.threadId != mHandler.getLooper().getThread().getId()){
//					mHandler.post(new Runnable(){
//
//						@Override
//						public void run() {
//							try{
//								listener.result(obj, warning);
//							}finally{
////								httpResult.set.set();
//								httpResult.getAutoResetEvent().set();
////								if(obj instanceof File){
////									((File) obj).delete();
////								}
//							}
//						}});
//					return false;
//				}else{
//					try{
//						listener.result(obj, warning);
//					}finally{
////						httpResult.set.set();
//						httpResult.getAutoResetEvent().set();
//					}
//					return true;
//				}
//			}
//		return true;
//	}
////	private void fireProgress(HttpCommunicateResult httpHesult,ResultListener listener,long count,long total){
////			if(listener != null){
////				listener.progress(count, total);
////			}
////		}
//	private void fireFault(final HttpCommunicateResult httpResult,final ResultListener listener,final Error error){
//		if(listener != null){
//			if(this.mainThread && httpResult.threadId != mHandler.getLooper().getThread().getId()){
//				mHandler.post(new Runnable(){
//
//					@Override
//					public void run() {
//						try{
//							listener.fault(error);
//						}finally{
//							//httpResult.set.set();
//							httpResult.getAutoResetEvent().set();
//						}
//					}});
//			}else{
//				try{
//					listener.fault(error);
//				}finally{
//					//httpResult.set.set();
//					httpResult.getAutoResetEvent().set();
//				}
//			}
//		}
//	}
//	private void fireProgress(final HttpCommunicateResult httpResult,final ProgressResultListener listener,final long progress,final long total){
//		if(listener != null){
////			System.out.println("3 progress:"+progress+"\ttotal:"+total);
//			if(this.mainThread && httpResult.threadId != mHandler.getLooper().getThread().getId()){
//				mHandler.post(new Runnable(){
//
//					@Override
//					public void run() {
//						listener.progress(progress,total);
//					}});
//			}else{
//				listener.progress(progress,total);
//			}
//		}
//	}
//
////	public HttpCommunicateResult upload(File file,ResultListener listener){
////		return null;
////	}
//
//	public HttpCommunicateResult download(String file, final ResultListener listener){
//		return download(file,listener,defaultParams);
//	}
//	public HttpCommunicateResult download(String file, final ResultListener listener,HttpCommunicate.Params params){
//
//		URL url = null;
//		try {
//			url = new URL(file);
//		} catch (MalformedURLException e) {
//
//			//AutoResetEvent set = new AutoResetEvent();
//			HttpCommunicateResult result = new HttpCommunicateResult();
//			//result.set = set;
//
//			fireFault(result, listener, new Error());
//
////			result.getAutoResetEvent().set();
//			return result;
//		}
//		return download(url, listener,params);
//
//	}
//
//	public HttpCommunicateResult download(URL file,final ResultListener listener) {
//		return download(file,listener,defaultParams);
//	}
//	public HttpCommunicateResult download(URL file,final ResultListener listener,HttpCommunicate.Params params){
//		if (params == null){
//			params = defaultParams;
//		}
//
//		ProgressResultListener pListener = null;
//		if(listener instanceof ProgressResultListener) {
//			pListener = (ProgressResultListener) listener;
//		}
//
//		final HttpCommunicateResult httpHesult = new HttpCommunicateResult();
////		final AutoResetEvent set = new AutoResetEvent(false);
////		httpHesult.set = set;
//		final ProgressResultListener finalPListener = pListener;
//		fireRequestListener(null);
//		DownloadFile downloadFile = new DownloadFile(new ProgressResultListener(){
//			private boolean isDeleteFile = false;
//			@Override
//			public void result(final Object obj,final List<Error> warning) {
//				lin.util.thread.ActionExecute.execute(new Action() {
//					@Override
//					public void action() {
//						httpHesult.setResult(true,obj);
//						isDeleteFile = fireResult(httpHesult,listener, obj, warning);
//					}
////				}, new Action() {//fireResult中已经set了
//
////					@Override
////					public void action() {
////
////						httpHesult.getAutoResetEvent().set();
////					}
//				},new Action(){
//
//					@Override
//					public void action() {
//						try {
//							fireRequestResultListener(null, obj, warning);
//						}finally {
//							if(isDeleteFile && obj instanceof File){
//								((File)obj).delete();
//							}
//						}
//
//					}
//
//				});
//			}
//
//			@Override
//			public void fault(final Error error) {
//				lin.util.thread.ActionExecute.execute(new Action() {
//					@Override
//					public void action() {
//						httpHesult.setResult(false,null);
//						fireFault(httpHesult, listener, error);
//					}
////				}, new Action() {//fireFault中已经set了
////
////					@Override
////					public void action() {
////						httpHesult.getAutoResetEvent().set();
////					}
//				},new Action(){
//
//					@Override
//					public void action() {
//						fireRequestFaultListener(null, error);
//					}});
//			}
//
//			@Override
//			public void progress(long progress, long total) {
////				System.out.println("2 progress:"+progress+"\ttotal:"+total);
//				if (finalPListener != null){
//					fireProgress(httpHesult, finalPListener,progress,total);
//				}
//			}
//		});
//
//		httpHesult.request = downloadFile;
//		downloadFile.context = context;
////		downloadFile.http = this.http;
//		downloadFile.download(file);
//
//		return httpHesult;
//	}
//
//	private boolean mainThread = false;
//	public boolean isMainThread() {
//		return mainThread;
//	}
//
//	private Handler mHandler = new Handler();
//	public void setMainThread(boolean mainThread) {
//		this.mainThread = mainThread;
//	}
}