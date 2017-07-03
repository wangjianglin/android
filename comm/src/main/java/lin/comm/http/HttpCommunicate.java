package lin.comm.http;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lin.comm.http.httpclient.HttpClientCommunicateImpl;
import lin.comm.http.httpurlconnection.HttpURLConnectionCommunicateImpl;
import lin.comm.http.volley.AndroidVolleyCommunicateImpl;
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


		private Map<String,String> mHeaders = new HashMap<>();

		public void addHeader(String name,String value){
			mHeaders.put(name,value);
		}

		public void removeHeader(String name){
			mHeaders.remove(name);
		}

		public Map<String,String> headers(){
			return new HashMap<>(mHeaders);
		}


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
	private static List<HttpRequestListener> mListeners = new ArrayList<HttpRequestListener>();
	
	private static HttpRequestListener mGlobalListner =new HttpRequestListener(){

		@Override
		public void request(HttpCommunicateImpl impl, HttpPackage pack) {
			HttpRequestListener item = null;
			for(HttpRequestListener listener : mListeners){
				if(listener == null){
					continue;
				}
				listener.request(impl, pack);
			}
		}

		@Override
		public void requestComplete(HttpCommunicateImpl impl, HttpPackage pack, Object obj, List<Error> warning) {
			HttpRequestListener item = null;
			for(HttpRequestListener listener : mListeners){
				if(listener == null){
					continue;
				}
				listener.requestComplete(impl, pack,obj,warning);
			}
		}

		@Override
		public void requestFault(HttpCommunicateImpl impl, HttpPackage pack, Error error) {
			HttpRequestListener item = null;
			for(HttpRequestListener listener : mListeners){
				if(listener == null){
					continue;
				}
				listener.requestFault(impl, pack,error);
			}
		}

	};

	public void newSession(){
		global().newSession();
	}
	//private static Map<String, HttpCommunicateImpl> impls = new HashMap<String, HttpCommunicateImpl>();
	private static Map<String,WeakReference<HttpCommunicateImpl>> mImpls = new HashMap<String, WeakReference<HttpCommunicateImpl>>();

	public static HttpCommunicateImpl get(String name) {
		return get(name,type);
	}

	public static HttpCommunicateImpl get(String name,HttpCommunicateType type) {
		WeakReference<HttpCommunicateImpl> impl = mImpls.get(name);
		if (impl != null && impl.get() != null) {
			return impl.get();
		}

		synchronized (mImpls) {
			impl = mImpls.get(name);
			if(impl != null && impl.get() == null){
				mImpls.remove(name);
			}
			if (impl == null || impl.get() == null) {
//				HttpCommunicateImpl himpl = new HttpCommunicateImpl(name,_tmp);
				HttpCommunicateImpl himpl =null;
				if(type == HttpCommunicateType.Volley){
					himpl = new AndroidVolleyCommunicateImpl(name,_tmp);
				}else if(type == HttpCommunicateType.HttpClient){
					himpl = new HttpClientCommunicateImpl(name,_tmp);
				}else{
					himpl = new HttpURLConnectionCommunicateImpl(name,_tmp);
				}
				impl = new WeakReference<HttpCommunicateImpl>(himpl);
				himpl.addHttpRequestListener(mGlobalListner);
				if(mContext != null) {
					himpl.init(mContext);
				}
				mImpls.put(name, impl);
			}
			return impl.get();
		}
	}

	public static void remove(String name) {
		synchronized (mImpls) {
			WeakReference<HttpCommunicateImpl> impl = mImpls.remove(name);
			if(impl != null){
				if(impl.get()!=null){
					impl.get().removeHttpRequestListener(mGlobalListner);
				}
			}
			
			ArrayList<String> list = new ArrayList<String>();
			for(String item : mImpls.keySet()){
				impl = mImpls.get(item);
				if(impl == null || impl.get() == null){
					list.add(item);
				}
			}
			for(String item : list){
				mImpls.remove(item);
			}
		}
	}

	private static HttpCommunicateImpl mGlobalImpl = null;

	public static HttpCommunicateImpl global(){
		if(mGlobalImpl != null){
			return mGlobalImpl;
		}
		mGlobalImpl = get("global",type);
		return mGlobalImpl;
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
		mListeners.add(listener);
	}

	public static void removeHttpRequestListener(HttpRequestListener listener) {
		global().removeHttpRequestListener(listener);
	}

	public static void removeGlobaHttpRequestListener(
			HttpRequestListener listener) {
		mListeners.remove(listener);
	}

	// private static CookieStore cookieStore = new BasicCookieStore();
//	static CookieStore getCookieStore() {
//		return global.getCookieStore();
//	}

//	public static HttpCommunicateResult request(
//			lin.client.http.packages.TcpPackage pack, final ResultListener listener) {
//		return global.request(pack, listener);
//	}

	public static <T> HttpCommunicateResult<T> request(lin.comm.http.HttpPackage<T> pack){
		return global().request(pack,null);
	}

	public static <T> HttpCommunicateResult<T> request(lin.comm.http.HttpPackage<T> pack, ResultListener<T> listener){
		return global().request(pack,listener);
	}


	public static HttpCommunicateResult<FileInfo> download(String file) {
		return global().download(file,null);
	}
	public static HttpCommunicateResult<FileInfo> download(String file,
														   ResultListener<FileInfo> listener) {
		return global().download(file,listener,null);
	}

	public static HttpCommunicateResult<FileInfo> download(String file,
												 ResultListener<FileInfo> listener,Params params) {
		return global().download(file,listener,params);
	}

	public static HttpCommunicateResult<FileInfo> download(URL file) {
		return global().download(file, null);
	}

	public static HttpCommunicateResult<FileInfo> download(URL file,
														   ResultListener<FileInfo> listener) {
		return global().download(file, listener);
	}
	public static HttpCommunicateResult<FileInfo> download(URL file,
												  ResultListener<FileInfo> listener,Params params) {
		return global().download(file, listener);
	}

	public static void addHeader(String name, String value){
		global().addHeader(name,value);
	}

	public static void removeHeader(String name){
		global().removeHeader(name);
	}

	private static Context mContext;
	public static void init(Context context){
		if(mContext != null){
			return;
		}
		HttpCommunicate.mContext = context;
		synchronized (mImpls) {
			for (WeakReference<HttpCommunicateImpl> impl : mImpls.values()){
				HttpCommunicateImpl himpl = impl.get();
				if(himpl != null){
					himpl.init(context);
				}
			}
		}
	}
}
