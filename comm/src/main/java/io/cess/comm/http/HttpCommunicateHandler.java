package io.cess.comm.http;

import com.android.volley.Response;

import java.util.Map;

/**
 * @author lin
 * @date 28/06/2017.
 */

public interface HttpCommunicateHandler<T extends HttpCommunicateImpl> extends Aboutable {

    interface Listener{
        void response(HttpClientResponse response);
    }

    void process(Listener listener);

    void setPackage(HttpPackage pack);

    void setImpl(T impl);

    void setParams(HttpCommunicate.Params params);

    void setRequestParams(Map<String,Object> requestParams);
}
