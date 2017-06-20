package lin.comm.http.httpurlconnection;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lin.comm.http.HttpCommunicate;
import lin.comm.http.HttpCommunicateImpl;
import lin.comm.http.HttpCommunicateRequest;
import lin.comm.http.HttpPackage;
import lin.comm.http.ResultListener;


/**
 * Created by lin on 9/24/15.
 * 改用 HttpURLConnection 实现HTTP请求
 */
public class HttpURLConnectionRequest implements HttpCommunicateRequest {

    private lin.comm.http.HttpPackage pack;
    private ResultListener listener;
    private HttpCommunicateImpl impl;
    private HttpCommunicate.Params params;
    private SessionInfo sessionInfo;

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 50, 10,
            TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(3000),
            new ThreadPoolExecutor.CallerRunsPolicy());

    HttpURLConnectionRequest(SessionInfo sessionInfo){
        this.sessionInfo = sessionInfo;
    }

    @Override
    public void setPackage(HttpPackage pack) {
        this.pack = pack;
    }

    @Override
    public void setImpl(HttpCommunicateImpl impl) {
        this.impl = impl;
    }

    @Override
    public void setListener(ResultListener listener) {
        this.listener = listener;
    }

    public void request(){

        Runnable task = new HttpURLConnectionRequestRunable(sessionInfo,pack,listener,impl,params);
        executor.execute(task);
    }

    @Override
    public void setParams(HttpCommunicate.Params params) {
        this.params = params;
    }

    @Override
    public void abort() {

    }
}

