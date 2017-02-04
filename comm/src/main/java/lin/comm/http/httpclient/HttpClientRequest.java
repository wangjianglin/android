package lin.comm.http.httpclient;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lin.comm.http.HttpCommunicate;
import lin.comm.http.HttpCommunicateImpl;
import lin.comm.http.HttpCommunicateRequest;
import lin.comm.http.HttpPackage;
import lin.comm.http.ResultListener;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;


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

	HttpClientRequest(HttpClient http){
		this.http = http;
	}
	
//	public HttpClientRequest(HttpCommunicateImpl impl,lin.client.http.HttpPackage pack,ResultListener listener, HttpCommunicateResult result,HttpClient http){
//		this.impl = impl;
//		this.pack = pack;
//		this.listener = listener;
//		this.http = http;
//	}
	
	private HttpPost post = null;
	public void abort(){
		if(post != null){
			post.abort();
		}
	}
	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 50, 10,
			TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(3000),
			new ThreadPoolExecutor.CallerRunsPolicy());
	private HttpClient http;

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
		Runnable task = new HttpClientRequestRunnable(http,impl,pack,listener);
		executor.execute(task);
//		new Thread(task).start();
	}

	@Override
	public void setParams(HttpCommunicate.Params params) {

	}
}
