package io.cess.comm.http.httpclient;

import io.cess.comm.http.AbstractHttpCommunicateImpl;
import io.cess.comm.http.HttpCommunicate;
import io.cess.comm.http.HttpCommunicateDownloadFile;
import io.cess.comm.http.HttpCommunicateHandler;

/**
 * Created by lin on 1/9/16.
 */
public class HttpClientCommunicateImpl extends AbstractHttpCommunicateImpl{

    public HttpClientCommunicateImpl(String name, HttpCommunicate c) {
        super(name, c);
    }

    @Override
    protected HttpCommunicateHandler getHandler() {
        return new HttpClientHandler();
    }

    @Override
    protected HttpCommunicateDownloadFile downloadRequest() {
        return new DownloadFile(mContext);
    }
}
