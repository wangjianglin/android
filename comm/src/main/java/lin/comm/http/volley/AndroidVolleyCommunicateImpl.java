package lin.comm.http.volley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import lin.comm.http.AbstractHttpCommunicateImpl;
import lin.comm.http.HttpCommunicate;
import lin.comm.http.HttpCommunicateDownloadFile;
import lin.comm.http.HttpCommunicateRequest;

/**
 * Created by lin on 22/06/2017.
 */

public class AndroidVolleyCommunicateImpl extends AbstractHttpCommunicateImpl {

    private RequestQueue mQueue = null;//Volley.newRequestQueue(getApplicationContext());;
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
    public void newSession() {

    }

    @Override
    protected HttpCommunicateRequest getRequest() {
        return null;
    }

    @Override
    protected HttpCommunicateDownloadFile downloadRequest() {
        return null;
    }
}
