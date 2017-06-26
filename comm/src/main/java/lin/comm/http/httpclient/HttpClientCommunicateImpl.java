package lin.comm.http.httpclient;

import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.params.CoreConnectionPNames;

import java.net.InetAddress;
import java.net.UnknownHostException;

import lin.comm.http.AbstractHttpCommunicateImpl;
import lin.comm.http.HttpCommunicate;
import lin.comm.http.HttpCommunicateDownloadFile;
import lin.comm.http.HttpCommunicateRequest;

/**
 * Created by lin on 1/9/16.
 */
public class HttpClientCommunicateImpl extends AbstractHttpCommunicateImpl{

    private CookieStore mCookie = new BasicCookieStore();

    public HttpClientCommunicateImpl(String name, HttpCommunicate c) {
        super(name, c);
    }

    @Override
    protected HttpCommunicateRequest getRequest() {
        return new HttpClientRequest(mCookie);
    }

    @Override
    protected HttpCommunicateDownloadFile downloadRequest() {

        return new DownloadFile(mCookie,context);
    }

    @Override
    public void newSession() {
        mCookie = new BasicCookieStore();
    }
}
