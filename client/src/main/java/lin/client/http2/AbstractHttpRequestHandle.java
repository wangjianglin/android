package lin.client.http2;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.message.AbstractHttpMessage;


/**
 * 
 * @author lin
 * @date Jun 26, 2015 11:25:34 AM
 *
 */
public abstract class AbstractHttpRequestHandle implements HttpRequestHandle {
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getParams(AbstractHttpMessage httpMessage,
			HttpPackage pack) {

		Map<String,Object> params = pack.getParams();
		if(params == null){
			return null;
		}
		Map<String,Object> textParams = new HashMap<String,Object>();
		Map<String,Object> contentParams = new HashMap<String,Object>();
		Object item = null;
		for(String key:params.keySet()){
			item = params.get(key);
			if(item instanceof ContentBody){
				contentParams.put(key, item);
			}else{
				textParams.put(key, item);
			}
		}
		@SuppressWarnings("rawtypes")
//		Map map = lin.util.json.JSONUtil.toParameters(textParams);
		Map map = lin.util.JsonUtil.toParameters(textParams);
		map.putAll(contentParams);
		return map;
	}
}
