package io.cess.comm.http;

import java.util.Map;

/**
 * 
 * @author 王江林
 * @date 2013-7-16 下午12:03:04
 *
 */
public interface HttpRequestHandle {

    public void preprocess(HttpPackage pack,HttpCommunicate.Params params);

	Map<String,Object> getParams(HttpPackage pack);

    void response(HttpPackage pack,HttpClientResponse response,ResultListener listener);
}
