package lin.client.http.httpclient;

import org.apache.http.HttpClientConnection;
import org.apache.http.client.CookieStore;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.BrowserCompatHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

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

//        HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
//                8 * 1024, new DefaultHttpRequestWriterFactory(), new DefaultHttpResponseParserFactory());

        HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory();
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();

        DnsResolver dnsResolver = new SystemDefaultDnsResolver() {

            @Override
            public InetAddress[] resolve(final String host) throws UnknownHostException {
                String destIp = null;
                if(HttpClientCommunicateImpl.this.getHttpDNS() != null){
                    destIp = HttpClientCommunicateImpl.this.getHttpDNS().getIpByHost(host);
                }
                if (destIp != null) {
                    return InetAddress.getAllByName(destIp);
                }else {
                    return super.resolve(host);
                }
            }

        };

//        final Registry<ConnectionSocketFactory> socketFactoryRegistry,
//        final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory,
//        final DnsResolver dnsResolver
        http = HttpClients.custom().useSystemProperties()
                .setDefaultCookieStore(cookie)
                .setConnectionManager(new PoolingHttpClientConnectionManager(
                        socketFactoryRegistry,connFactory,dnsResolver))
                .build();

    }

    @Override
    protected HttpCommunicateRequest getRequest() {
        return new HttpClientRequest(http);
    }

    @Override
    protected HttpCommunicateDownloadFile downloadRequest() {
        return new DownloadFile(http,context);
    }

    @Override
    public void newSession() {

    }
}
