package lin.client.http1;

import java.io.File;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import android.content.Context;
import android.os.Handler;

import lin.util.Action;
//import lin.util.thread.AutoResetEvent;

/**
 * 
 * @author 王江林
 * @date 2013-7-29 下午9:13:22
 *
 */
@Deprecated
public class HttpCommunicateImpl{// implements HttpCommunicate{

	private Context context;
	public void init(Context context){
		this.context = context;
	}
	private String name;

	//HttpCommunicateImpl(){}
	HttpCommunicateImpl(String name, lin.client.http1.HttpCommunicate c) {
		if(c == null){
			throw new RuntimeException();
		}
		this.name = name;
		//创建HttpClientBuilder
//        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
//        //HttpClient
//        http = httpClientBuilder.build();
		CookieStore  cookie = new BasicCookieStore();
		 http = HttpClients.custom().useSystemProperties()
		            .setDefaultCookieStore(cookie)
		            .build();
	}

//	public HttpCommunicateImpl(){
//	}


	Map<String,String> defaultHeaders = new HashMap<String,String>();

	public void addHeader(String name,String value){
		defaultHeaders.put(name,value);
	}

	public void removeHeader(String name){
		defaultHeaders.remove(name);
	}
	public String getName(){
		return name;
	}

	private boolean debug = false;
	/**
	 * 通信 URL
	 */
		private URL baseUri = null;

		/**
		 * 设置通信 URL
		 * @param url
		 */
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

		public URL getCommUrl(){
			return baseUri;
		}

		public boolean isDebug() {
			return debug;
		}

		public void setDebug(boolean debug) {
			this.debug = debug;
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
	public void addHttpRequestListener(HttpRequestListener listener){
		SoftReference<HttpRequestListener> slistener = new SoftReference<HttpRequestListener>(listener);
		listeners.add(slistener);
		}
	public void removeHttpRequestListener(HttpRequestListener listener){
		for(SoftReference<HttpRequestListener> item : listeners){
			if(item.get() == listener){
				listeners.remove(item);
				break;
			}
		}
	}

	private void fireRequestResultListener(lin.client.http1.HttpPackage pack,Object obj, List<lin.client.http1.Error> warning){
		for(SoftReference<HttpRequestListener> item : listeners){
			if(item.get() != null){
				item.get().requestComplete(this, pack, obj, warning);
			}
		}
	}

	private void fireRequestFaultListener(lin.client.http1.HttpPackage pack, lin.client.http1.Error error){
		for(SoftReference<HttpRequestListener> item : listeners){
			if(item.get() != null){
				item.get().requestFault(this, pack, error);
			}
		}
	}

	private void fireRequestListener(lin.client.http1.HttpPackage pack){
		for(SoftReference<HttpRequestListener> item : listeners){
			if(item.get() != null){
				item.get().request(this, pack);
			}
		}
	}

	private CookieStore cookieStore = new BasicCookieStore();
	 CookieStore getCookieStore(){
		return cookieStore;
	}

	 public void newSession(){
		 cookieStore = new BasicCookieStore();
	 }

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
//            .setDefaultCookieStore(cookies)
//            .build();


//	private AndroidHTTPClient c = AndroidHttpClient.newInstance("");

//	public HttpCommunicateResult request(lin.client.http.TcpPackage pack,final ResultFunction result,final FaultFunction fault){
	public lin.client.http1.HttpCommunicateResult request(final lin.client.http1.HttpPackage pack,final ResultListener listener){

		this.fireRequestListener(pack);

		final lin.client.http1.HttpCommunicateResult httpHesult = new lin.client.http1.HttpCommunicateResult();
//		final AutoResetEvent set = new AutoResetEvent(false);
		HttpRequest request = new HttpRequest(this,pack, new ResultListener() {

			@Override
				public void result(final Object obj, final List<lin.client.http1.Error> warning) {
					lin.util.thread.ActionExecute.execute(new Action() {
						@Override
						public void action() {
							httpHesult.setResult(true,obj);
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
			public void fault(final lin.client.http1.Error error) {
				lin.util.thread.ActionExecute.execute(new Action() {
					@Override
					public void action() {
						httpHesult.setResult(false,null);
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
		},httpHesult,http);
		httpHesult.request = request;
//		httpHesult.set = set;
		request.request();
		return httpHesult;
	}

	private boolean fireResult(final lin.client.http1.HttpCommunicateResult httpResult,final ResultListener listener,final Object obj,final List<lin.client.http1.Error> warning){
			if(listener != null){
				if(this.mainThread && httpResult.threadId != mHandler.getLooper().getThread().getId()){
					mHandler.post(new Runnable(){

						@Override
						public void run() {
							try{
								listener.result(obj, warning);
							}finally{
//								httpResult.set.set();
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
//						httpResult.set.set();
						httpResult.getAutoResetEvent().set();
					}
					return true;
				}
			}
		return true;
	}
//	private void fireProgress(HttpCommunicateResult httpHesult,ResultListener listener,long count,long total){
//			if(listener != null){
//				listener.progress(count, total);
//			}
//		}
	private void fireFault(final lin.client.http1.HttpCommunicateResult httpResult,final ResultListener listener,final lin.client.http1.Error error){
		if(listener != null){
			if(this.mainThread && httpResult.threadId != mHandler.getLooper().getThread().getId()){
				mHandler.post(new Runnable(){

					@Override
					public void run() {
						try{
							listener.fault(error);
						}finally{
							//httpResult.set.set();
							httpResult.getAutoResetEvent().set();
						}
					}});
			}else{
				try{
					listener.fault(error);
				}finally{
					//httpResult.set.set();
					httpResult.getAutoResetEvent().set();
				}
			}
		}
	}
	private void fireProgress(final lin.client.http1.HttpCommunicateResult httpResult,final lin.client.http1.ProgressResultListener listener,final long progress,final long total){
		if(listener != null){
//			System.out.println("3 progress:"+progress+"\ttotal:"+total);
			if(this.mainThread && httpResult.threadId != mHandler.getLooper().getThread().getId()){
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

	public lin.client.http1.HttpCommunicateResult download(String file, final ResultListener listener){

		return  null;
	}

	public lin.client.http1.HttpCommunicateResult download(URL file,final ResultListener listener){
		lin.client.http1.ProgressResultListener pListener = null;
		if(listener instanceof lin.client.http1.ProgressResultListener) {
			pListener = (lin.client.http1.ProgressResultListener) listener;
		}

		final lin.client.http1.HttpCommunicateResult httpHesult = new lin.client.http1.HttpCommunicateResult();
//		final AutoResetEvent set = new AutoResetEvent(false);
//		httpHesult.set = set;
		final lin.client.http1.ProgressResultListener finalPListener = pListener;
		fireRequestListener(null);
		DownloadFile downloadFile = new DownloadFile(new lin.client.http1.ProgressResultListener(){
			private boolean isDeleteFile = false;
			@Override
			public void result(final Object obj,final List<lin.client.http1.Error> warning) {
				lin.util.thread.ActionExecute.execute(new Action() {
					@Override
					public void action() {
						httpHesult.setResult(true,obj);
						isDeleteFile = fireResult(httpHesult,listener, obj, warning);
					}
				}, new Action() {

					@Override
					public void action() {

						httpHesult.getAutoResetEvent().set();
					}
				},new Action(){

					@Override
					public void action() {
						try {
							fireRequestResultListener(null, obj, warning);
						}finally {
							if(isDeleteFile && obj instanceof File){
								((File)obj).delete();
							}
						}

					}

				});
			}

			@Override
			public void fault(final lin.client.http1.Error error) {
				lin.util.thread.ActionExecute.execute(new Action() {
					@Override
					public void action() {
						httpHesult.setResult(false,null);
						fireFault(httpHesult, listener, error);
					}
				}, new Action() {

					@Override
					public void action() {
						httpHesult.getAutoResetEvent().set();
					}
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
		});

		httpHesult.request = downloadFile;
		downloadFile.context = context;
//		downloadFile.http = this.http;
		downloadFile.download(file);

		return httpHesult;
	}

	private boolean mainThread = false;
	public boolean isMainThread() {
		return mainThread;
	}

	private Handler mHandler = new Handler();
	public void setMainThread(boolean mainThread) {
		this.mainThread = mainThread;
	}
}