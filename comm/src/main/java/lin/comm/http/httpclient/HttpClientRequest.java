package lin.comm.http.httpclient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lin.comm.http.HttpCommunicate;
import lin.comm.http.HttpCommunicateImpl;
import lin.comm.http.HttpCommunicateRequest;
import lin.comm.http.HttpPackage;
import lin.comm.http.ResultListener;

import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;


/**
 * 
 * @author 王江林
 * @date 2013-7-16 下午12:03:00
 *
 */
public class HttpClientRequest implements HttpCommunicateRequest {

	private lin.comm.http.HttpPackage pack;
	private ResultListener listener;
	private HttpCommunicateImpl impl;
	private HttpCommunicate.Params params;
	private CookieStore mCookie;

	HttpClientRequest(CookieStore cookie){
		this.mCookie = cookie;
	}
	
	private HttpPost post = null;
	public void abort(){
		if(post != null){
			post.abort();
		}
	}
	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 50, 10,
			TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(3000),
			new ThreadPoolExecutor.CallerRunsPolicy());
	private CloseableHttpClient http;

	@Override
	public void setPackage(HttpPackage pack) {
		this.pack = pack;
	}

	@Override
	public void setImpl(HttpCommunicateImpl impl) {
		this.impl = impl;
	}

	@Override
	public void setListener(ResultListener listener) {
		this.listener = listener;
	}

	public void request(){

		ManagedHttpClientConnectionFactory connFactory = new ManagedHttpClientConnectionFactory();
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", SSLConnectionSocketFactory.getSocketFactory())
				.build();

		DnsResolver dnsResolver = new SystemDefaultDnsResolver() {

			@Override
			public InetAddress[] resolve(final String host) throws UnknownHostException {
				String destIp = null;
                if(impl.getHttpDNS() != null){
                    destIp = impl.getHttpDNS().getIpByHost(host);
                }
                if (destIp != null) {
                    return InetAddress.getAllByName(destIp);
                }else {
				return super.resolve(host);
                }
			}

		};

		RequestConfig defaultRequestConfig = RequestConfig.custom()
				.setSocketTimeout(params.getTimeout())
				.setConnectTimeout(params.getTimeout())
				.setConnectionRequestTimeout(params.getTimeout())
				.setStaleConnectionCheckEnabled(true)
				.build();

		CloseableHttpClient http = HttpClients.custom().useSystemProperties()
				.setDefaultCookieStore(mCookie)
				.setDefaultRequestConfig(defaultRequestConfig)
				.setConnectionManager(new PoolingHttpClientConnectionManager(
						socketFactoryRegistry,connFactory,dnsResolver))
//				.setSSLSocketFactory(SSLConnectionSocketFactory.getSocketFactory())
				.build();
		Runnable task = new HttpClientRequestRunnable(http,impl,pack,listener,params);
		executor.execute(task);
//		new Thread(task).start();
	}

	@Override
	public void setParams(HttpCommunicate.Params params) {
		this.params = params;
	}
}

