package lin.comm.http.volley;

import com.android.volley.RequestQueue;

import lin.comm.http.AbstractHttpCommunicateRequest;

/**
 * Created by lin on 24/06/2017.
 */

public class AndroidVolleyRequest extends AbstractHttpCommunicateRequest {

    RequestQueue mQueue;
    AndroidVolleyRequest(RequestQueue queue){
        this.mQueue = queue;
    }

    @Override
    protected Runnable getTask() {
        return new AndroidVolleyRequestRunnable(this);
    }
}
