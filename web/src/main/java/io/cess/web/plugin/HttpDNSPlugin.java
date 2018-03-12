package io.cess.web.plugin;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import io.cess.comm.Constants;
import io.cess.comm.http.HttpCommunicate;
import io.cess.comm.http.HttpCommunicateImpl;
import io.cess.comm.http.HttpCommunicateResult;
import io.cess.comm.http.HttpMethod;
import io.cess.comm.http.HttpPackage;

/**
 * @author lin
 * @date 5/1/16.
 */
public class HttpDNSPlugin extends LinWebPlugin {

    private HttpCommunicateImpl impl;

    public HttpDNSPlugin(Context context) {
        super(context);
        HttpCommunicate.init(context);
        impl = HttpCommunicate.get("web http");

        impl.setTimeout(20 * 1000);
        impl.addHeader(Constants.HTTP_COMM_PROTOCOL, "");
        impl.addHeader(Constants.HTTP_COMM_PROTOCOL_DEBUG, "");

    }

    public Object http(Map<String,Object> args){

        Map<String,Object> config = (Map<String, Object>) args.get("config");
        Map<String,String> headers = (Map<String, String>) config.get("headers");


        Object methodStr = config.get("method");
        Map<String,Object> params = (Map<String, Object>) config.get("params");
        String url = (String) config.get("url");

        String host = (String) args.get("host");
        String destIp = (String) args.get("destIp");

        if(headers == null){
            headers = new HashMap<String,String>();
        }
        if(destIp != null && !"".equals(destIp) && !io.cess.comm.Utils.detectIfProxyExist(this.getContext())){
            headers.put("Host",host);
            url = url.replaceFirst(host,destIp);
        }

        HttpMethod method = HttpMethod.POST;
        if("get".equals(methodStr)){
            method = HttpMethod.GET;
        }
        HttpRequestPackage pack = new HttpRequestPackage(url,method);

        pack.params = params;
        pack.headers = headers;

        HttpCommunicateResult<Object> result = impl.request(pack);

        result.waitForEnd();
        if(result.isSuccess()){
            return result.getResult();
        }

        return result.getError();
    }

    private class HttpRequestPackage extends io.cess.comm.http.HttpPackage{

        HttpRequestPackage(String url, HttpMethod method){
            super(url,method);
            this.setRequestHandle(HttpPackage.NORMAL);
        }

        private Map<String,Object> params;
        private Map<String,String> headers;

        @Override
        public Map<String, Object> getParams() {
            return params;
        }

        @Override
        public Map<String, String> getHeaders() {
            return headers;
        }
    }
}
