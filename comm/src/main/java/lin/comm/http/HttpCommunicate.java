package lin.comm.http;

import android.content.Context;

import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lin.comm.http.httpclient.HttpClientCommunicateImpl;
import lin.comm.http.httpurlconnection.HttpURLConnectionCommunicateImpl;
import lin.comm.httpdns.HttpDNS;

/**
 * 
 * @author 王江林
 * @date 2013-7-16 下午12:02:48
 * 
 */
public class HttpCommunicate {

	public static class Params{
		private boolean mainThread = true;
		private boolean debug = false;
		private int timeout;

		public boolean isDebug() {
			return debug;
		}

		public void setDebug(boolean debug) {
			this.debug = debug;
		}

		public boolean isMainThread() {
			return mainThread;
		}

		public void setMainThread(boolean mainThread) {
			this.mainThread = mainThread;
		}

		public int getTimeout() {
			return timeout;
		}

		public void setTimeout(int timeout) {
			this.timeout = timeout;
		}
	}

	public static void setHttpDNS(HttpDNS httpDNS){
		global().setHttpDNS(httpDNS);
	}

	public static HttpDNS getHttpDNS(){
		return global().getHttpDNS();
	}


	private static HttpCommunicateType type = HttpCommunicateType.HttpURLConnection;

	public static void setType(HttpCommunicateType type){
		HttpCommunicate.type = type;
	}

	private HttpCommunicate(){}
	private static HttpCommunicate _tmp = new HttpCommunicate();
//	public static final HttpCommunicate global = new HttpCommunicateImpl("global");
//	
//	
//}
	//用soft引用实现
	private static List<SoftReference<HttpRequestListener>> listeners = new ArrayList<SoftReference<HttpRequestListener>>();
	
	private static HttpRequestListener globalListner =new HttpRequestListener(){

		@Override
		public void request(HttpCommunicateImpl impl, HttpPackage pack) {
			HttpRequestListener item = null;
			for(SoftReference<HttpRequestListener> listener : listeners){
				item = listener.get();
				if(item == null){
					continue;
				}
				item.request(impl, pack);
			}
		}

		@Override
		public void requestComplete(HttpCommunicateImpl impl, HttpPackage pack, Object obj, List<Error> warning) {
			HttpRequestListener item = null;
			for(SoftReference<HttpRequestListener> listener : listeners){
				item = listener.get();
				if(item == null){
					continue;
				}
				item.requestComplete(impl, pack,obj,warning);
			}
		}

		@Override
		public void requestFault(HttpCommunicateImpl impl, HttpPackage pack, Error error) {
			HttpRequestListener item = null;
			for(SoftReference<HttpRequestListener> listener : listeners){
				item = listener.get();
				if(item == null){
					continue;
				}
				item.requestFault(impl, pack,error);
			}
		}

	};

		public void newSession(){
			global().newSession();
		}
	//private static Map<String, HttpCommunicateImpl> impls = new HashMap<String, HttpCommunicateImpl>();
	//	private static Map<String,WeakReference<HttpCommunicateImpl>> impls = new HashMap<String, WeakReference<HttpCommunicateImpl>>();
		private static Map<String,SoftReference<HttpCommunicateImpl>> impls = new HashMap<String, SoftReference<HttpCommunicateImpl>>();

	public static HttpCommunicateImpl get(String name) {
		return get(name,type);
	}

	public static HttpCommunicateImpl get(String name,HttpCommunicateType type) {
		SoftReference<HttpCommunicateImpl> impl = impls.get(name);
		if (impl != null && impl.get() != null) {
			return impl.get();
		}

		synchronized (impls) {
			impl = impls.get(name);
			if(impl != null && impl.get() == null){
				impls.remove(name);
			}
			if (impl == null || impl.get() == null) {
//				HttpCommunicateImpl himpl = new HttpCommunicateImpl(name,_tmp);
				HttpCommunicateImpl himpl =null;
				if(type == HttpCommunicateType.HttpURLConnection){
					himpl = new HttpURLConnectionCommunicateImpl(name,_tmp);
				}else{
					himpl = new HttpClientCommunicateImpl(name,_tmp);
				}
				impl = new SoftReference<HttpCommunicateImpl>(himpl);
				himpl.addHttpRequestListener(globalListner);
				if(context != null) {
					himpl.init(context);
				}
				impls.put(name, impl);
			}
			return impl.get();
		}
	}

	public static void remove(String name) {
		synchronized (impls) {
			SoftReference<HttpCommunicateImpl> impl = impls.remove(name);
			if(impl != null){
				if(impl.get()!=null){
					impl.get().removeHttpRequestListener(globalListner);
				}
			}
			
			ArrayList<String> list = new ArrayList<String>();
			for(String item : impls.keySet()){
				impl = impls.get(item);
				if(impl == null || impl.get() == null){
					list.add(item);
				}
			}
			for(String item : list){
				impls.remove(item);
			}
		}
	}

//	private static HttpCommunicateImpl globalHttpURL = null;//get("Global",HttpCommunicateType.HttpURLConnection);

	private static HttpCommunicateImpl globalImpl = null;

	public static HttpCommunicateImpl global(){
		if(globalImpl != null){
			return globalImpl;
		}
		if(type == HttpCommunicateType.HttpClient){
			globalImpl = get("Global",HttpCommunicateType.HttpClient);
		}else{
			globalImpl = get("Global",HttpCommunicateType.HttpURLConnection);
		}
		return globalImpl;
	}

	/**
	 * 通信 URL
	 */
	// private static URI baseUri = null;

	/**
	 * 设置通信 URL
	 * 
	 * @param url
	 */
	public static void setCommUrl(URL url) {
		global().setCommUrl(url);
	}

	public static URL getCommUrl() {
		return global().getCommUrl();
	}

	public static boolean isDebug() {
		return global().isDebug();
	}
	
	public static void setMainThread(boolean mainThread){
		global().setMainThread(mainThread);
	}
	
	public static boolean isMainThread(){
		return global().isMainThread();
	}

	public static void setDebug(boolean debug) {
		global().setDebug(debug);
	}

	public static int getTimeout() {
		return global().getTimeout();
	}

	public static void setTimeout(int timeout) {
		global().setTimeout(timeout);
	}

	public static long getCacheSize() {
		return global().getCacheSize();
	}

	public static void setCacheSize(long cacheSize) {
		global().setCacheSize(cacheSize);
	}

//	/**
//	 * 设置代理
//	 *
//	 * @param proxy
//	 */
//	public static void setAuthenticationHandler(AuthenticationHandler proxy) {
//		global.setAuthenticationHandler(proxy);
//	}


//	/**
//	 *
//	 * @param credsProvider
//	 */
//	public static void setCredentialsProvider(CredentialsProvider credsProvider) {
//		global.setCredentialsProvider(credsProvider);
//	}

	public static void addHttpRequestListener(HttpRequestListener listener) {
		global().addHttpRequestListener(listener);
	}

	public static void addGlobalHttpRequestListener(HttpRequestListener listener) {
		listeners.add(new SoftReference<HttpRequestListener>(listener));
	}

	public static void removeHttpRequestListener(HttpRequestListener listener) {
		for(SoftReference<HttpRequestListener> item : listeners){
			if(item.get() == listener){
				listeners.remove(item);
				return;
			}
		}
	}

	public static void removeGlobaHttpRequestListener(
			HttpRequestListener listener) {
		listeners.remove(listener);
	}

	// private static CookieStore cookieStore = new BasicCookieStore();
//	static CookieStore getCookieStore() {
//		return global.getCookieStore();
//	}

//	public static HttpCommunicateResult request(
//			lin.client.http.packages.TcpPackage pack, final ResultListener listener) {
//		return global.request(pack, listener);
//	}

	public static HttpCommunicateResult<Object> request(lin.comm.http.HttpPackage pack){
//		if(listener != null){
//			return global.request(pack,listener::result,listener::fault);
//		}
		return global().request(pack,null);
	}

	public static HttpCommunicateResult<Object> request(lin.comm.http.HttpPackage pack, ResultListener listener){
//		if(listener != null){
//			return global.request(pack,listener::result,listener::fault);
//		}
		return global().request(pack,listener);
	}

//	public static HttpCommunicateResult<Object> request(lin.comm.http.HttpPackage pack, ResultListener listener, Params params){
//		return global().request(pack,listener,params);
//	}
	
//	public static HttpCommunicateResult request(lin.client.http.TcpPackage pack,ResultFunction result){
//		return global.request(pack,result,null);
//	}
//	
//	public HttpCommunicateResult request(lin.client.http.TcpPackage pack,final ResultFunction result,final FaultFunction fault){
//		return global.request(pack,result,fault);
//	}

//	public static HttpCommunicateResult upload(File file,
//			ResultListener listener) {
//		return global.upload(file, listener);
//	}


	public static HttpCommunicateResult<FileInfo> download(String file) {
		return global().download(file,null);
	}
	public static HttpCommunicateResult<FileInfo> download(String file,
														   ResultListener listener) {
		return global().download(file,listener,null);
	}

	public static HttpCommunicateResult<FileInfo> download(String file,
												 ResultListener listener,Params params) {
		return global().download(file,listener,params);
	}

	public static HttpCommunicateResult<FileInfo> download(URL file) {
		return global().download(file, null);
		//return HttpCommunicateImpl.downloadImpl(file,listener);
	}

	public static HttpCommunicateResult<FileInfo> download(URL file,
														   ResultListener listener) {
		return global().download(file, listener);
		//return HttpCommunicateImpl.downloadImpl(file,listener);
	}
	public static HttpCommunicateResult<FileInfo> download(URL file,
												  ResultListener listener,Params params) {
		return global().download(file, listener);
		//return HttpCommunicateImpl.downloadImpl(file,listener);
	}

	public static void addHeader(String name, String value){
		global().addHeader(name,value);
	}

	public static void removeHeader(String name){
		global().removeHeader(name);
	}

	private static Context context;
	public static void init(Context context){
		HttpCommunicate.context = context;
		synchronized (impls) {
			for (SoftReference<HttpCommunicateImpl> impl : impls.values()){
				HttpCommunicateImpl himpl = impl.get();
				if(himpl != null){
					himpl.init(context);
				}
			}
		}
	}
}
