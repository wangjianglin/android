package lin.comm.http;

/**
 * Created by lin on 24/06/2017.
 */

public abstract class AbstractHttpCommunicateRequestRunnable implements Runnable {

    protected HttpPackage mPack;
    protected ResultListener mListener;
    protected HttpCommunicateImpl mImpl;
    protected HttpCommunicate.Params mParams;

    protected AbstractHttpCommunicateRequestRunnable(AbstractHttpCommunicateRequest request){
        this.mPack = request.mPack;
        this.mListener = request.mListener;
        this.mImpl = request.mImpl;
        this.mParams = request.mParams;
    }
}
