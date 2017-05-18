package lin.comm.http.httpclient;

import org.apache.http.client.CookieStore;
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

//    private CloseableHttpClient http;
    private CookieStore cookie = new BasicCookieStore();
    private HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory;
    private DnsResolver dnsResolver;
    private Registry<ConnectionSocketFactory> socketFactoryRegistry;

    public HttpClientCommunicateImpl(String name, HttpCommunicate c) {
        super(name, c);

//        HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
//                8 * 1024, new DefaultHttpRequestWriterFactory(), new DefaultHttpResponseParserFactory());

        connFactory = new ManagedHttpClientConnectionFactory();
        socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();

        dnsResolver = new SystemDefaultDnsResolver() {

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


    }

    @Override
    protected HttpCommunicateRequest getRequest() {
        CloseableHttpClient http = HttpClients.custom().useSystemProperties()
                .setDefaultCookieStore(cookie)
                .setConnectionManager(new PoolingHttpClientConnectionManager(
                        socketFactoryRegistry,connFactory,dnsResolver))
                .build();
        return new HttpClientRequest(http);
    }

    @Override
    protected HttpCommunicateDownloadFile downloadRequest() {
        CloseableHttpClient http = HttpClients.custom().useSystemProperties()
                .setDefaultCookieStore(cookie)
                .setConnectionManager(new PoolingHttpClientConnectionManager(
                        socketFactoryRegistry,connFactory,dnsResolver))
                .build();
        return new DownloadFile(http,context);
    }

    @Override
    public void newSession() {

    }
}
