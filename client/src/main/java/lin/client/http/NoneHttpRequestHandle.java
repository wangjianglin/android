package lin.client.http;

import java.util.Map;


/**
 * 
 * @author 王江林
 * @date 2013-7-16 下午12:03:17
 *
 */
public class NoneHttpRequestHandle implements HttpRequestHandle {

	@Override
	public Map<String, Object> getParams(HttpPackage pack,HttpMessage httpMessage) {
		return null;
	}

	@Override
	public void response(HttpPackage pack, byte[] bytes, ResultListener listener) {
		
	}

}
