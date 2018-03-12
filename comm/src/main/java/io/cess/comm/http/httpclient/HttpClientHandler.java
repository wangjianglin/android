package io.cess.comm.http.httpclient;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.cess.comm.http.AbstractHttpCommunicateHandler;
import io.cess.comm.http.HttpClientResponse;
import io.cess.comm.http.HttpClientResponseImpl;
import io.cess.comm.http.HttpCommunicate;
import io.cess.comm.http.HttpCommunicateImpl;
import io.cess.comm.http.HttpMethod;
import io.cess.comm.http.HttpPackage;
import io.cess.comm.http.HttpUtils;
import io.cess.comm.http.ResultListener;
import io.cess.comm.http.httpurlconnection.HttpURLConnectionCommunicateImpl;


/**
 * @author lin
 * @date 1/11/16.
 */
public class HttpClientHandler extends AbstractHttpCommunicateHandler<HttpURLConnectionCommunicateImpl> {

    private ContentType contentType= ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);

    HttpClientHandler(){
    }

    private HttpClient genHttp(){
        		ManagedHttpClientConnectionFactory connFactory = new ManagedHttpClientConnectionFactory();
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", SSLConnectionSocketFactory.getSocketFactory())
				.build();

		DnsResolver dnsResolver = new SystemDefaultDnsResolver() {

			@Override
			public InetAddress[] resolve(final String host) throws UnknownHostException {
				String destIp = null;
                if(mImpl.getHttpDNS() != null){
                    destIp = mImpl.getHttpDNS().getIpByHost(host);
                }
                if (destIp != null) {
                    return InetAddress.getAllByName(destIp);
                }else {
				return super.resolve(host);
                }
			}

		};

		RequestConfig defaultRequestConfig = RequestConfig.custom()
				.setSocketTimeout(mParams.getTimeout())
				.setConnectTimeout(mParams.getTimeout())
				.setConnectionRequestTimeout(mParams.getTimeout())
				.setStaleConnectionCheckEnabled(true)
				.build();

		return HttpClients.custom().useSystemProperties()
//				.setDefaultCookieStore(mCookie)
				.setDefaultRequestConfig(defaultRequestConfig)
				.setConnectionManager(new PoolingHttpClientConnectionManager(
						socketFactoryRegistry,connFactory,dnsResolver))
//				.setSSLSocketFactory(SSLConnectionSocketFactory.getSocketFactory())
				.build();
    }
    @Override
    public void process(Listener listener) {
        HttpClient http = genHttp();
        ByteArrayOutputStream _out = new ByteArrayOutputStream();
        HttpClientResponseImpl httpClientResponse = new HttpClientResponseImpl();
        try {
            //HTTP请求
            HttpRequestBase request = null;
            if(mPack.getMethod() == HttpMethod.GET){
                request = get();
            }else{
                request = post();
            }

            HttpResponse response = http.execute(request);
            httpClientResponse.setStatusCode(response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            InputStream _in = entity.getContent();
            byte bs[] = new byte[4096];
            int count = 0;
            while((count = _in.read(bs)) != -1){
                _out.write(bs, 0, count);
            }
            _in.close();

            for(Header header : response.getAllHeaders()){
                httpClientResponse.addHeader(header.getName(),header.getValue());
            }
        } catch (Throwable e) {
            io.cess.comm.http.Error error = new io.cess.comm.http.Error(-2,
                    "未知错误",
                    e.getMessage(),
                    io.cess.util.Utils.printStackTrace(e));

            httpClientResponse.setStatusCode(700);
//            HttpUtils.fireFault(listener, error);
//            return;
        }
        httpClientResponse.setData(_out.toByteArray());
//        pack.getRequestHandle().response(pack,httpClientResponse, listener);
        listener.response(httpClientResponse);
    }

    private void addHeaders(HttpPackage pack,HttpRequestBase request){

        for (Map.Entry<String,String> item : mParams.headers().entrySet()){
            request.removeHeaders(item.getKey());
            request.addHeader(item.getKey(),item.getValue());
        }
    }
    private HttpRequestBase get() throws Throwable {
        String url = HttpUtils.uri(mImpl, mPack);
        url = addGetParams(url,generParams(mRequestParams));
        HttpGet get = new HttpGet(url);

        addHeaders(this.mPack,get);

        return get;
    }
    private HttpRequestBase post(){
        HttpPost post = new HttpPost(HttpUtils.uri(mImpl, mPack));

        addHeaders(this.mPack,post);

//        Map<String,Object> postParams = mPack.getRequestHandle().getParams(mPack);
        if(mRequestParams != null){
            if(mPack.isMultipart()){
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                builder.setCharset(Charset.forName("UTF-8"));
                if(mRequestParams != null && mRequestParams.size()>0){
                    for(String key : mRequestParams.keySet()){
                        if(mRequestParams.get(key) instanceof String){
                            builder.addPart(key, new StringBody((String) mRequestParams.get(key),contentType));
                        }else{
                            builder.addPart(key, (ContentBody) mRequestParams.get(key));
                        }
                    }
                }
                post.setEntity(builder.build());

            }else{
                List<NameValuePair> params = new
                        ArrayList<NameValuePair>();
                if(mRequestParams != null && mRequestParams.size()>0){
                    for(String key : mRequestParams.keySet()){
                        params.add(new BasicNameValuePair(key,(String)mRequestParams.get(key)));
                    }
                }
                try {
                    post.setEntity(new org.apache.http.client.entity.UrlEncodedFormEntity(params,"utf-8"));
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return post;
    }

    private String addGetParams(String url, String params) {
        if (url.indexOf('?') == -1) {
            url += "?" + params;
        } else {
            url += "&" + params;
        }
        return url;
    }

    private String generParams(Map<String, Object> params) throws Throwable {

        if (params == null) {
            return "";
        }
        StringBuffer sBuffer = new StringBuffer();
        for (Map.Entry<String, Object> item : params.entrySet()) {
            sBuffer.append(item.getKey());
            sBuffer.append("=");
            if (item.getValue() != null) {
                sBuffer.append(encode(item.getValue().toString()));

            }
            sBuffer.append("&");
        }
        if (sBuffer.length() > 0) {
            sBuffer.deleteCharAt(sBuffer.length() - 1);
        }
        return sBuffer.toString();
    }

    private String encode(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, "UTF-8");
    }

    @Override
    public void abort() {

    }
}
