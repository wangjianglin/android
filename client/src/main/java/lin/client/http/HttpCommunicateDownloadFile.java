package lin.client.http;

import java.net.URL;

/**
 * Created by lin on 1/9/16.
 */
public interface HttpCommunicateDownloadFile  extends Aboutable{
//    void setPackage(HttpPackage pack);

    void setImpl(HttpCommunicateImpl impl);

    void setListener(ProgressResultListener listener);

    void download(URL url);

    void setParams(HttpCommunicate.Params params);
}
