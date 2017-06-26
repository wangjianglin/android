package lin.comm.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lin on 21/06/2017.
 */

public class HttpClientResponse {

    private int statusCode;
    private Map<String,List<String>> mHeaders = new HashMap<String,List<String>>();

    public void addHeader(String name,String value){
        List<String> list = mHeaders.get(name);
        if(list == null){
            list = new ArrayList<>();
            mHeaders.put(name,list);
        }
        list.add(value);
    }

    public void addHeader(String name,List<String> values){
        mHeaders.put(name,values);
    }

    public Map<String,List<String>> headers(){
        return new HashMap<>(mHeaders);
    }

    public String getHeader(String name){
        List<String> list = mHeaders.get(name);
        if(list == null || list.isEmpty()){
            return null;
        }
        return list.get(0);
    }

    public List<String> getHeaders(String name){
        return mHeaders.get(name);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
