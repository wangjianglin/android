package lin.client.http2;

import java.util.Map;

import org.apache.http.message.AbstractHttpMessage;


/**
 * 
 * @author 王江林
 * @date 2013-7-16 下午12:03:04
 *
 */
public interface HttpRequestHandle {
	
	Map<String,Object> getParams(AbstractHttpMessage httpMessage, HttpPackage pack);

    void response(HttpPackage pack, byte[] bytes, ResultListener listener);
}
