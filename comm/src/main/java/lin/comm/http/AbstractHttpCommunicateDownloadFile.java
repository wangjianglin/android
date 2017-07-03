package lin.comm.http;

import android.content.Context;


/**
 * Created by lin on 9/24/15.
 */
public abstract class AbstractHttpCommunicateDownloadFile implements HttpCommunicateDownloadFile {

    protected static final int DOWNLOAD_SIZE = 800 * 1024;

    protected ProgressResultListener mListener;

    protected HttpCommunicateImpl mImpl;
    protected HttpCommunicate.Params mParams;
    protected Context mContext;

    protected AbstractHttpCommunicateDownloadFile(Context context){
        this.mContext = context;
    }

    @Override
    public void setImpl(HttpCommunicateImpl impl) {
        this.mImpl = impl;
    }

    @Override
    public void setListener(ProgressResultListener listener) {
        this.mListener = listener;
    }


    @Override
    public void setParams(HttpCommunicate.Params params) {
        this.mParams = params;
    }
}