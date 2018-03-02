package io.cess.comm.http.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import io.cess.comm.http.AbstractHttpCommunicateHandler;
import io.cess.comm.http.Error;
import io.cess.comm.http.HttpClientResponse;
import io.cess.comm.http.HttpClientResponseImpl;
import io.cess.comm.http.HttpMethod;
import io.cess.comm.http.HttpUtils;
import io.cess.comm.httpdns.HttpDNS;
import io.cess.comm.tcp.Session;

/**
 * Created by lin on 24/06/2017.
 */

public class AndroidVolleyRequestHandler extends AbstractHttpCommunicateHandler<AndroidVolleyCommunicateImpl> {


    AndroidVolleyRequestHandler(){
//        super(request);
//        this.mRequest = request;
    }
    @Override
    public void process(final Listener listener) {


        Request r = NewNetworkResponseRequest(this,mPack.getMethod() == HttpMethod.POST?Request.Method.POST:Request.Method.GET,
                HttpUtils.uri(mImpl, mPack), new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                //HttpUtils.fireResult(mListener,);
                HttpClientResponseImpl httpClientResponse = new HttpClientResponseImpl();
                httpClientResponse.setStatusCode(200);
                httpClientResponse.addHeaders(response.headers);
                httpClientResponse.setData(response.data);
//                Set-Cookie
                String cookie = httpClientResponse.getHeader("Set-Cookie");

                if(cookie != null && !"".equals(cookie)) {
                    mImpl.mSessionInfo.cookie = cookie;
                }

//                mPack.getRequestHandle().response(mPack,httpClientResponse, mListener);
                listener.response(httpClientResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                HttpClientResponseImpl response = new HttpClientResponseImpl();
                if(volleyError.networkResponse != null) {
                    response.setStatusCode(volleyError.networkResponse.statusCode);
                    response.addHeaders(volleyError.networkResponse.headers);
                    response.setData(volleyError.networkResponse.data);
                }else{
                    response.setStatusCode(0);
                }
                //mPack.getRequestHandle().response(mPack,response,mListener);
                listener.response(response);
            }
        });

        r.setRetryPolicy(new DefaultRetryPolicy(
                mParams.getTimeout(),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mImpl.mQueue.add(r);
    }


    public static NetworkResponseRequest NewNetworkResponseRequest(AndroidVolleyRequestHandler handler, int method, String url, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {

        HttpDNS httpDNS = handler.mImpl.getHttpDNS();
        boolean isHttpDNS = false;
        String originHostname = null;
        if(httpDNS != null) {

            try {
                URL urlObj = new URL(url);
                originHostname = urlObj.getHost();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            String dstIp = httpDNS.getIpByHost(originHostname);

            if (dstIp != null && !"".equals(dstIp)) {

                url = url.replaceFirst(originHostname, dstIp);
                isHttpDNS = true;
                // 设置HTTP请求头Host域
//                conn.setRequestProperty("Host", originHostname);
            }
        }
        NetworkResponseRequest request = new NetworkResponseRequest(method,url,listener,errorListener);
        request.mHttpDNS = isHttpDNS;
        request.mOriginHostname = originHostname;
        request.mHandler = handler;
        return request;
    }

    @Override
    public void abort() {

    }

    static class NetworkResponseRequest extends Request<NetworkResponse> {
        private Response.Listener<NetworkResponse> mListener;
        private AndroidVolleyRequestHandler mHandler;
        private boolean mHttpDNS;
        private String mOriginHostname;

        public NetworkResponseRequest(int method, String url, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
            super(method, url, errorListener);
            this.mListener = listener;
        }


//        protected void onFinish() {
//            super.onFinish();
//            this.mListener = null;
//        }

        protected void deliverResponse(NetworkResponse response) {
            if (this.mListener != null) {
                this.mListener.onResponse(response);
            }
        }

        protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> map = new HashMap<>();
//                map.put("cityname","朝阳");
            return io.cess.util.JsonUtil.toParameters(mHandler.mPack.getParams());
        }


//        @Override
//        protected Map<String, String> getPostParams() throws AuthFailureError {
//            return io.cess.util.JsonUtil.toParameters(mPack.getParams());
//        }

//        @Override
//        protected String getParamsEncoding() {
//            return super.getParamsEncoding();
//        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String,String> map = new HashMap<>();
            if(mHttpDNS){
                map.put("Host",mOriginHostname);
            }
            map.put("Cookie",mHandler.mImpl.mSessionInfo.cookie);
            for (Map.Entry<String, String> item : mHandler.mImpl.defaultHeaders().entrySet()) {
                map.put(item.getKey(),item.getValue());
            }

            for (Map.Entry<String, String> item : mHandler.mParams.headers().entrySet()){
                map.put(item.getKey(),item.getValue());
            }
            return map;
        }

        protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {

            return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
        }
    }
}


