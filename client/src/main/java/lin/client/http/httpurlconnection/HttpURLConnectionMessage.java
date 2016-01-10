package lin.client.http.httpurlconnection;

import java.net.HttpURLConnection;

import lin.client.http.HttpMessage;

/**
 * Created by lin on 1/11/16.
 */
public class HttpURLConnectionMessage implements HttpMessage {

    private final HttpURLConnection conn;

    HttpURLConnectionMessage(HttpURLConnection conn){
        this.conn = conn;
    }
    @Override
    public void addHeader(String name, String value) {
        conn.setRequestProperty(name,value);
    }
}
