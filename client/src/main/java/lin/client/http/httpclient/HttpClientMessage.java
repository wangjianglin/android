package lin.client.http.httpclient;

import org.apache.http.message.AbstractHttpMessage;

import lin.client.http.HttpMessage;

/**
 * Created by lin on 1/10/16.
 */
public class HttpClientMessage implements HttpMessage{
    private final AbstractHttpMessage message;

    @Override
    public void addHeader(String name, String value) {
        message.addHeader(name,value);
    }

    HttpClientMessage(AbstractHttpMessage message){
        this.message = message;
    }
}
