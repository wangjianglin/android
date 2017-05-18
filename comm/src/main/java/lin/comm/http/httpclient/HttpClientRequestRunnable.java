package lin.comm.http.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lin.comm.http.*;

/**
 * Created by lin on 1/11/16.
 */
public class HttpClientRequestRunnable implements Runnable{
    private final ResultListener listener;
    private HttpCommunicateImpl impl;
    private HttpPackage pack;

    private HttpClient http;
    private ContentType contentType= ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
    private HttpCommunicate.Params params;

    HttpClientRequestRunnable(HttpClient http, HttpCommunicateImpl impl, HttpPackage pack, ResultListener listener, HttpCommunicate.Params params){
        this.impl = impl;
        this.pack = pack;
        this.http = http;
        this.listener = listener;
        this.params = params;
    }

    @Override
    public void run() {
        HttpResponse response;
        ByteArrayOutputStream _out = new ByteArrayOutputStream();
        try {
            //HTTP请求

//						HttpConnectionParams.setSoTimeout(http.getParams(), 60000);
//						post.set
//						http.getConnectionManager().
//						http.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);

            HttpRequestBase request = null;
            if(pack.getMethod() == HttpMethod.GET){
                request = get();
            }else{
                request = post();
            }

            //超时
            http.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, params.getTimeout());
            http.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, params.getTimeout());

            response = http.execute(request);
            HttpEntity entity = response.getEntity();
            InputStream _in = entity.getContent();
            byte bs[] = new byte[4096];
            int count = 0;
            while((count = _in.read(bs)) != -1){
                _out.write(bs, 0, count);
            }
            _in.close();
        } catch (Throwable e) {
            lin.comm.http.Error error = new lin.comm.http.Error(-2,
                    "未知错误",
                    e.getMessage(),
                    lin.util.Utils.printStackTrace(e));
//						error.setCode(-2);
//						error.setMessage("未知错误");
//						error.setCause(e.getMessage());
//						error.setStackTrace(lin.util.Utils.printStackTrace(e));

            HttpUtils.fireFault(listener, error);
            return;
        }
        pack.getRequestHandle().response(pack, _out.toByteArray(), listener);
    }

    private void addHeaders(HttpPackage pack,HttpRequestBase request){
        for (Map.Entry<String,String> item : impl.defaultHeaders().entrySet()){
            request.addHeader(item.getKey(),item.getValue());
        }
        for (Map.Entry<String,String> item : pack.getHeaders().entrySet()){
            request.removeHeaders(item.getKey());
            request.addHeader(item.getKey(),item.getValue());
        }
    }
    private HttpRequestBase get() throws Throwable {
        String url = HttpUtils.uri(impl, pack);
        url = addGetParams(url,generParams(pack.getParams()));
        HttpGet get = new HttpGet(url);

        addHeaders(this.pack,get);

        return get;
    }
    private HttpRequestBase post(){
        HttpPost post = new HttpPost(HttpUtils.uri(impl, pack));
//        for (Map.Entry<String,String> item : impl.defaultHeaders().entrySet()){
//            post.addHeader(item.getKey(),item.getValue());
//        }
        addHeaders(this.pack,post);
//        post.addHeader(Constants.HTTP_COMM_PROTOCOL, "");
//        if(impl.isDebug()){
//            post.addHeader(Constants.HTTP_COMM_PROTOCOL_DEBUG, "");
//        }
        Map<String,Object> postParams = pack.getRequestHandle().getParams(pack,new HttpClientMessage(post));
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
        return post;
    }

    private String addGetParams(String url, String params) {
//        String urlString = url.toString();
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
}
