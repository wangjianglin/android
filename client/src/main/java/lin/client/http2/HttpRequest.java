package lin.client.http2;

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
public class HttpRequest implements Aboutable{

	private HttpPackage pack;
	private ResultListener listener;
	private HttpCommunicateImpl impl;

	public HttpRequest(HttpCommunicateImpl impl,HttpPackage pack,ResultListener listener, HttpCommunicateResult result,HttpClient http){
		this.impl = impl;
		this.pack = pack;
		this.listener = listener;
		this.http = http;
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
	ContentType contentType= ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
	private HttpClient http;
	public void request(){
		Runnable task = new Runnable() {

			@Override
			public void run() {
					HttpResponse response;
					ByteArrayOutputStream _out = new ByteArrayOutputStream();
					try {
						//HTTP请求
						post = new HttpPost(HttpUtils.uri(impl,pack));
						for (Map.Entry<String,String> item : impl.defaultHeaders.entrySet()){
							post.addHeader(item.getKey(),item.getValue());
						}
						post.addHeader(Constants.HTTP_COMM_PROTOCOL, "");
						if(impl.isDebug()){
							post.addHeader(Constants.HTTP_COMM_PROTOCOL_DEBUG, "");
						}
						Map<String,Object> postParams = pack.getRequestHandle().getParams(post,pack);
						if(postParams != null){
							if(pack.isMultipart()){
								 MultipartEntityBuilder builder = MultipartEntityBuilder.create();
								    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
								    builder.setCharset(Charset.forName("UTF-8"));
								    if(postParams != null && postParams.size()>0){
										for(String key : postParams.keySet()){
											if(postParams.get(key) instanceof String){
												builder.addPart(key, new StringBody((String) postParams.get(key),contentType));
											}else{
												builder.addPart(key, (ContentBody) postParams.get(key));
											}
										}
									}
								    post.setEntity(builder.build());
							}else{
								List<NameValuePair> params = new
										ArrayList<NameValuePair>();
								if(postParams != null && postParams.size()>0){
									for(String key : postParams.keySet()){
										params.add(new BasicNameValuePair(key,(String)postParams.get(key)));
									}
								}
								try {
									post.setEntity(new org.apache.http.client.entity.UrlEncodedFormEntity(params,"utf-8"));
								} catch (UnsupportedEncodingException e1) {
									e1.printStackTrace();
								}
							}
						}
//						HttpConnectionParams.setSoTimeout(http.getParams(), 60000);
//						post.set
//						http.getConnectionManager().
//						http.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
						response = http.execute(post);
						HttpEntity entity = response.getEntity();
						InputStream _in = entity.getContent();
						byte bs[] = new byte[4096];
						int count = 0;
						while((count = _in.read(bs)) != -1){
							_out.write(bs, 0, count);
						}
						_in.close();
					} catch (Throwable e) {
						Error error = new Error();
						error.setCode(-2);
						error.setMessage("未知错误");
						error.setCause(e.getMessage());
						error.setStackTrace(lin.util.Utils.printStackTrace(e));
						
						HttpUtils.fireFault(listener, error);
						return;
					}
					pack.getRequestHandle().response(pack, _out.toByteArray(), listener);
			}
		};
		executor.execute(task);
//		new Thread(task).start();
	}
}
