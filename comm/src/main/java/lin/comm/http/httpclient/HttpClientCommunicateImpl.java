package lin.comm.http.httpclient;

import lin.comm.http.AbstractHttpCommunicateImpl;
import lin.comm.http.HttpCommunicate;
import lin.comm.http.HttpCommunicateDownloadFile;
import lin.comm.http.HttpCommunicateHandler;

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
