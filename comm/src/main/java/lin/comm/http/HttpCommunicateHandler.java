package lin.comm.http;

import com.android.volley.Response;

/**
 * Created by lin on 28/06/2017.
 */

public interface HttpCommunicateHandler<T extends HttpCommunicateImpl> extends Aboutable {

    interface Listener{
        void response(HttpClientResponse response);
    }

    void process(Listener listener);

    void setPackage(HttpPackage pack);

    void setImpl(T impl);

    void setParams(HttpCommunicate.Params params);
}
