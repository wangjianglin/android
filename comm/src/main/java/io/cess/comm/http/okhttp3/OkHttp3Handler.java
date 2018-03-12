package io.cess.comm.http.okhttp3;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.cess.comm.http.*;
import io.cess.comm.http.Error;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * @author lin
 * @date 1/8/16.
 */
class OkHttp3Handler extends AbstractHttpCommunicateHandler<OkHttp3CommunicateImpl> {

    OkHttp3Handler(){
    }

    @Override
    public void process(Listener listener) {

        HttpClientResponseImpl response = new HttpClientResponseImpl();
        try {
            runImpl(response);
        }catch (IOException e) {
            response.setStatusCode(701);
            response.setMessage("读取网络数据错误");
            response.setStackTrace(io.cess.util.Utils.printStackTrace(e));
        }catch (Throwable e) {
            response.setStatusCode(700);
            response.setMessage("未知错误");
            response.setStackTrace(io.cess.util.Utils.printStackTrace(e));

        }
        listener.response(response);
    }

    private HttpClientResponseImpl runImpl(HttpClientResponseImpl response) throws IOException {


        Request.Builder requestBuilder = null;

        if(mPack.getMethod() == HttpMethod.POST){
            requestBuilder = Utils.post(mImpl,mPack,mRequestParams);
        }else{
            requestBuilder = Utils.get(mImpl,mPack,mRequestParams);
        }

        for(Map.Entry<String,String> item : mParams.headers().entrySet()){
            requestBuilder.addHeader(item.getKey(),item.getValue());
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(mParams.getTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(mParams.getTimeout(),TimeUnit.MILLISECONDS)
                .writeTimeout(mParams.getTimeout(),TimeUnit.MILLISECONDS)
                .dns(new OkHttp3Dns(mImpl.getHttpDNS()))
                .build();
        Call call = okHttpClient.newCall(requestBuilder.build());

        Response okResponse = call.execute();

//        if(!okResponse.isSuccessful()){
//            response.setStatusCode(701);
//            response.setMessage(okResponse.message());
//            return response;
//        }

        if(okResponse.body() != null) {
            response.setData(okResponse.body().bytes());
        }
        response.setStatusCode(okResponse.code());

        Headers headers = okResponse.headers();
        if(headers != null){
            for(String name : headers.names()){
                response.addHeader(name,headers.values(name));
            }
        }

        return response;
    }

    @Override
    public void abort() {

    }
}
