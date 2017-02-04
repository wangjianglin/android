package lin.comm.http;

import android.content.Context;

import java.net.URL;
import java.util.Map;

import lin.comm.httpdns.HttpDNS;

/**
 * Created by lin on 1/9/16.
 */
public interface HttpCommunicateImpl{
    void init(Context context);

    void setHttpDNS(HttpDNS httpDNS);
    HttpDNS getHttpDNS();

    long getCacheSize();

    void setCacheSize(long cacheSize);

    int getTimeout();

    void setTimeout(int timeout);

    void addHeader(String name, String value);

    void removeHeader(String name);

    String getName();

    void setCommUrl(URL url);

    URL getCommUrl();

    boolean isDebug();

    void setDebug(boolean debug);

    void addHttpRequestListener(HttpRequestListener listener);

    void removeHttpRequestListener(HttpRequestListener listener);

    HttpCommunicateResult<Object> request(HttpPackage pack, ResultListener listener);
    HttpCommunicateResult<Object> request(HttpPackage pack);

    //	public HttpCommunicateResult request(lin.client.http.TcpPackage pack,final ResultFunction result,final FaultFunction fault){
    HttpCommunicateResult<Object> request(HttpPackage pack, ResultListener listener, HttpCommunicate.Params params);

    HttpCommunicateResult<FileInfo> download(String file);
    HttpCommunicateResult<FileInfo> download(String file, ResultListener listener);

    HttpCommunicateResult<FileInfo> download(String file, ResultListener listener, HttpCommunicate.Params params);

    HttpCommunicateResult<FileInfo> download(URL file);
    HttpCommunicateResult<FileInfo> download(URL file, ResultListener listener);

    HttpCommunicateResult<FileInfo> download(URL file, ResultListener listener, HttpCommunicate.Params params);

    boolean isMainThread();

    void setMainThread(boolean mainThread);

    void newSession();

    Map<String,String> defaultHeaders();

//    void setType(HttpCommunicateType type);
}
