package lin.comm.http.volley;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import lin.comm.http.AbstractHttpCommunicateRequest;
import lin.comm.http.AbstractHttpCommunicateRequestRunnable;
import lin.comm.http.HttpCommunicateRequest;
import lin.comm.http.HttpUtils;

/**
 * Created by lin on 24/06/2017.
 */

public class AndroidVolleyRequestRunnable extends AbstractHttpCommunicateRequestRunnable {

    private HttpCommunicateRequest mRequest;
    private RequestQueue mQueue;

    AndroidVolleyRequestRunnable(AndroidVolleyRequest request){
        super(request);
        this.mRequest = request;
        this.mQueue = request.mQueue;
    }
    @Override
    public void run() {

        Request r = new StringRequest(HttpUtils.uri(mImpl, mPack),null,null);


        mQueue.add(r);
    }
}
