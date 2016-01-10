package lin.client.http.httpclient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lin.client.Constants;
import lin.client.http.Aboutable;
import lin.client.http.Error;
import lin.client.http.HttpCommunicate;
import lin.client.http.HttpCommunicateImpl;
import lin.client.http.HttpCommunicateRequest;
import lin.client.http.HttpCommunicateResult;
import lin.client.http.HttpPackage;
import lin.client.http.HttpUtils;
import lin.client.http.ResultListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;


/**
 * 
 * @author 王江林
 * @date 2013-7-16 下午12:03:00
 *
 */
public class HttpClientRequest implements HttpCommunicateRequest {

	private lin.client.http.HttpPackage pack;
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
