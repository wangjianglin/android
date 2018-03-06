package io.cess.comm.http.okhttp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.cess.comm.http.*;
import io.cess.comm.http.Error;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;


/**
 * Created by lin on 1/8/16.
 */
class OkHttp3Handler extends AbstractHttpCommunicateHandler<OkHttp3CommunicateImpl> {

    OkHttp3Handler(){
    }

    @Override
    public void process(Listener listener) {

        HttpClientResponseImpl response = null;
        try {
            response = runImpl();
        }catch (IOException e) {
            response.setStatusCode(700);
            response.setMessage(io.cess.util.Utils.printStackTrace(e));
        }catch (Throwable e) {
            io.cess.comm.http.Error error = new Error(-2,
                    "未知错误",
                    e.getMessage(),
                    io.cess.util.Utils.printStackTrace(e));

            response.setStatusCode(800);
            response.setMessage(io.cess.util.Utils.printStackTrace(e));

        }
        listener.response(response);
    }

    private HttpClientResponseImpl runImpl() throws IOException {

        HttpClientResponseImpl response = new HttpClientResponseImpl();

        Call call = Utils.newCall(mImpl,mPack,mParams,mRequestParams);

//        Call call = okHttpClient.newCall(requestBuilder.build());
        Response okResponse = call.execute();

        if(!okResponse.isSuccessful()){
            response.setStatusCode(701);
            response.setMessage(okResponse.message());
            return response;
        }

        response.setData(okResponse.body().bytes());
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
