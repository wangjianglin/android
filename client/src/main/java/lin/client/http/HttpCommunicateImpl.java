package lin.client.http;

import android.content.Context;

import java.net.URL;
import java.util.Map;

/**
 * Created by lin on 1/9/16.
 */
public interface HttpCommunicateImpl{
    void init(Context context);

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

    HttpCommunicateResult request(HttpPackage pack, ResultListener listener);

    //	public HttpCommunicateResult request(lin.client.http.TcpPackage pack,final ResultFunction result,final FaultFunction fault){
    HttpCommunicateResult request(HttpPackage pack, ResultListener listener, HttpCommunicate.Params params);

    HttpCommunicateResult download(String file, ResultListener listener);

    HttpCommunicateResult download(String file, ResultListener listener, HttpCommunicate.Params params);

    HttpCommunicateResult download(URL file, ResultListener listener);

    HttpCommunicateResult download(URL file, ResultListener listener, HttpCommunicate.Params params);

    boolean isMainThread();

    void setMainThread(boolean mainThread);

    void newSession();

    Map<String,String> defaultHeaders();

//    void setType(HttpCommunicateType type);
}
