package lin.client.http2;

import java.util.Map;

import org.apache.http.message.AbstractHttpMessage;


/**
 * 
 * @author 王江林
 * @date 2013-7-16 下午12:03:17
 *
 */
public class NoneHttpRequestHandle implements HttpRequestHandle {

	@Override
	public Map<String, Object> getParams(AbstractHttpMessage httpMessage,HttpPackage pack) {
		return null;
	}

	@Override
	public void response(HttpPackage pack, byte[] bytes, ResultListener listener) {
		
	}

}
