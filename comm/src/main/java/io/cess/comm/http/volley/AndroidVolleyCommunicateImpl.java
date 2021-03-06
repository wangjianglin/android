package io.cess.comm.http.volley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import io.cess.comm.http.AbstractHttpCommunicateImpl;
import io.cess.comm.http.HttpCommunicate;
import io.cess.comm.http.HttpCommunicateDownloadFile;
import io.cess.comm.http.HttpCommunicateHandler;
import io.cess.comm.http.httpurlconnection.*;
import io.cess.comm.http.httpurlconnection.DownloadFile;

/**
 * @author lin
 * @date 22/06/2017.
 */

public class AndroidVolleyCommunicateImpl extends AbstractHttpCommunicateImpl {

    RequestQueue mQueue = null;//Volley.newRequestQueue(getApplicationContext());;
    volatile SessionInfo mSessionInfo = new SessionInfo();
    public AndroidVolleyCommunicateImpl(String name, HttpCommunicate c) {
        super(name, c);
//        mQueue = Volley.newRequestQueue(c.)
    }

    @Override
    public void init(Context context) {
        super.init(context);
        mQueue = Volley.newRequestQueue(context);
    }

    @Override
    protected HttpCommunicateHandler getHandler() {
        return new AndroidVolleyRequestHandler();
    }

    @Override
    public void newSession() {
        mSessionInfo = new SessionInfo();
    }

//    @Override
//    protected HttpCommunicateRequest getRequest() {
//        return new AndroidVolleyRequest(mQueue,mSessionInfo);
//    }

    @Override
    protected HttpCommunicateDownloadFile downloadRequest() {
        return new io.cess.comm.http.httpurlconnection.DownloadFile(mContext);
    }
}
