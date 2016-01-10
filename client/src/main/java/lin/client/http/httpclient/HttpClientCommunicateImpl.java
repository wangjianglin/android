package lin.client.http.httpclient;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import lin.client.http.AbstractHttpCommunicateImpl;
import lin.client.http.HttpCommunicate;
import lin.client.http.HttpCommunicateDownloadFile;
import lin.client.http.HttpCommunicateRequest;

/**
 * Created by lin on 1/9/16.
 */
public class HttpClientCommunicateImpl extends AbstractHttpCommunicateImpl{

    private CloseableHttpClient http;

    public HttpClientCommunicateImpl(String name, HttpCommunicate c) {
        super(name, c);
        CookieStore cookie = new BasicCookieStore();
        http = HttpClients.custom().useSystemProperties()
                .setDefaultCookieStore(cookie)
                .build();
    }

    @Override
    protected HttpCommunicateRequest getRequest() {
        return new HttpClientRequest(http);
    }

    @Override
    protected HttpCommunicateDownloadFile downloadRequest() {
        return new DownloadFile(http);
    }

    @Override
    public void newSession() {

    }
}
