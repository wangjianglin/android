package io.cess.comm.http.okhttp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.cess.comm.http.FileParamInfo;
import io.cess.comm.http.HttpClientResponseImpl;
import io.cess.comm.http.HttpCommunicate;
import io.cess.comm.http.HttpCommunicateImpl;
import io.cess.comm.http.HttpMethod;
import io.cess.comm.http.HttpPackage;
import io.cess.comm.http.HttpUtils;
import io.cess.comm.httpdns.HttpDNS;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Created by lin on 4/25/16.
 */
class Utils {

    public static Request.Builder get(HttpCommunicateImpl impl, HttpPackage pack,Map<String,Object> requestParams) throws UnsupportedEncodingException {
        String url = HttpUtils.uri(impl,pack);
        url = HttpUtils.urlAddQueryString(url,HttpUtils.generQueryString(requestParams));
        return new Request.Builder()
                .url(url);
    }

    public static Request.Builder post(HttpCommunicateImpl impl, HttpPackage pack,Map<String,Object> requestParams) throws UnsupportedEncodingException {

        RequestBody requestBody = null;
        if(pack.isMultipart()){
            MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
            for(Map.Entry<String,Object> item : requestParams.entrySet()){
                Object value = item.getValue();
                if(value instanceof FileParamInfo){
                    FileParamInfo fileParamInfo = (FileParamInfo) value;
                    multipartBodyBuilder.addFormDataPart(item.getKey(),
                            fileParamInfo.getFileName(),
                            fileRequestBody(fileParamInfo)
                    );
                }else{
                    multipartBodyBuilder.addFormDataPart(item.getKey(),HttpUtils.encode(item.getValue().toString()));
                }
            }
            requestBody = multipartBodyBuilder.build();
        }else{
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            for(Map.Entry<String,String> item : HttpUtils.queryMap(requestParams).entrySet()){
                bodyBuilder.add(item.getKey(),item.getValue());
            }
            requestBody = bodyBuilder.build();
        }

        return new Request.Builder()
                .url(HttpUtils.uri(impl, pack))
                .post(requestBody);
    }

    private static RequestBody fileRequestBody(final FileParamInfo fileInfo){

        return new RequestBody() {
            @Override public MediaType contentType() {
                return MediaType.parse(fileInfo.getMimeType());
            }

            @Override public long contentLength() {
                return fileInfo.getLength();
            }

            @Override public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(fileInfo.getFile());
                    sink.writeAll(source);
                } finally {
                    Util.closeQuietly(source);
                }
            }
        };
    }

    public static Call newCall(HttpCommunicateImpl impl,
                                 HttpPackage pack,
                                 HttpCommunicate.Params params,
                                 Map<String,Object> requestParams) throws UnsupportedEncodingException {


        Request.Builder requestBuilder = null;

        if(pack.getMethod() == HttpMethod.POST){
            requestBuilder = Utils.post(impl,pack,requestParams);
        }else{
            requestBuilder = Utils.get(impl,pack,requestParams);
        }

        for(Map.Entry<String,String> item : params.headers().entrySet()){
            requestBuilder.addHeader(item.getKey(),item.getValue());
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(params.getTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(params.getTimeout(),TimeUnit.MILLISECONDS)
                .writeTimeout(params.getTimeout(),TimeUnit.MILLISECONDS)
                .build();
        return okHttpClient.newCall(requestBuilder.build());
    }
}
