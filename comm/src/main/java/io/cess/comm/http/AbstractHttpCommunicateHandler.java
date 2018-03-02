package io.cess.comm.http;

import com.android.volley.AuthFailureError;

import java.util.HashMap;
import java.util.Map;

import io.cess.comm.httpdns.HttpDNS;

/**
 * Created by lin on 24/06/2017.
 */

public abstract class AbstractHttpCommunicateHandler<T extends HttpCommunicateImpl> implements HttpCommunicateHandler<T> {

    protected HttpPackage mPack;
    protected T mImpl;
    protected HttpCommunicate.Params mParams;

    protected AbstractHttpCommunicateHandler(){

    }

    @Override
    public void setPackage(HttpPackage pack){
        this.mPack = pack;
    }

    @Override
    public void setImpl(T impl){
        this.mImpl = impl;
    }

    @Override
    public void setParams(HttpCommunicate.Params params){
        this.mParams = params;
    }


//    protected Map<String, String> getHeaders() {
//        Map<String,String> map = new HashMap<>();
//        HttpDNS mHttpDNS = mImpl.getHttpDNS();
//        if(mHttpDNS){
//            map.put("Host",mOriginHostname);
//        }
//        map.put("Cookie",mRunnable.mImpl.mSessionInfo.cookie);
//        for (Map.Entry<String, String> item : mRunnable.mImpl.defaultHeaders().entrySet()) {
//            map.put(item.getKey(),item.getValue());
//        }
//
//        for (Map.Entry<String, String> item : mRunnable.mParams.headers().entrySet()){
//            map.put(item.getKey(),item.getValue());
//        }
//        return map;
//    }
}
