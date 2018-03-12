package io.cess.comm.http.httpurlconnection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.cess.comm.httpdns.HttpDNS;

/**
 * @author lin
 * @date 4/25/16.
 */
class Utils {

    private static final SSLSocketFactory mSocketFactory;

    public static HttpURLConnection open(String urlString, HttpDNS httpDNS) throws IOException {

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

            httpsConn.setSSLSocketFactory(mSocketFactory);

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
            ((HttpURLConnection) conn).setInstanceFollowRedirects(HttpURLConnection.getFollowRedirects());
            return (HttpURLConnection) conn;
        }
        return null;
    }

    static {
        SSLSocketFactory ssf = null;
        try {
            TrustManager[] tm = { new X509TrustManager(){
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[] {};
                }

                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    //Log.i(TAG, "checkClientTrusted");
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    //Log.i(TAG, "checkServerTrusted");
                }
            } };
            SSLContext sslContext = null;
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, tm, new java.security.SecureRandom());
//        // 从上述SSLContext对象中得到SSLSocketFactory对象
            ssf = sslContext.getSocketFactory();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        mSocketFactory = ssf;
    }
}
