package lin.client.http.httpurlconnection;

import java.net.HttpURLConnection;
import java.net.URL;

import lin.client.httpdns.HttpDNS;

/**
 * Created by lin on 4/25/16.
 */
class Utils {

    public static HttpURLConnection open(String urlString, HttpDNS httpDNS)throws Exception{
        if(httpDNS == null){
            return (HttpURLConnection) new URL(urlString).openConnection();
        }
        URL url = new URL(urlString);
//        HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();

        String originHost = url.getHost();
        String dstIp = httpDNS.getIpByHost(originHost);

        if(dstIp == null){
            return (HttpURLConnection) new URL(urlString).openConnection();
        }

        urlString = urlString.replaceFirst(originHost, dstIp);
        url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置HTTP请求头Host域
        conn.setRequestProperty("Host", originHost);
        return conn;
    }
}
