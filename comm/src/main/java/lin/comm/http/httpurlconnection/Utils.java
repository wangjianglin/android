package lin.comm.http.httpurlconnection;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import lin.comm.httpdns.HttpDNS;

/**
 * Created by lin on 4/25/16.
 */
class Utils {

    public static HttpURLConnection open(String urlString, HttpDNS httpDNS)throws Exception{

        URLConnection conn = null;
        URL url = new URL(urlString);
        final String originHostname = url.getHost();
        if(httpDNS == null){
            conn = url.openConnection();
        }else {

            String dstIp = httpDNS.getIpByHost(originHostname);

            if (dstIp == null) {
                conn = new URL(urlString).openConnection();
            }else {

                urlString = urlString.replaceFirst(originHostname, dstIp);
                url = new URL(urlString);
                conn = url.openConnection();

                // 设置HTTP请求头Host域
                conn.setRequestProperty("Host", originHostname);
            }

        }

        if(conn instanceof HttpsURLConnection){

            HttpsURLConnection httpsConn = (HttpsURLConnection) conn;

            httpsConn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {

                    if(originHostname == null || "".equals(originHostname)){
                        return HttpsURLConnection.getDefaultHostnameVerifier().verify(hostname, session);
                    }
                    return HttpsURLConnection.getDefaultHostnameVerifier().verify(originHostname, session);
                }
            });
        }
        if(conn instanceof HttpURLConnection){
            return (HttpURLConnection) conn;
        }
        return null;
    }
}
