package lin.client.http;

/**
 * Created by lin on 1/9/16.
 */
public interface HttpCommunicateRequest extends Aboutable{
    void setPackage(HttpPackage pack);

    void setImpl(HttpCommunicateImpl impl);

    void setListener(ResultListener listener);

    void request();

    void setParams(HttpCommunicate.Params params);
}
