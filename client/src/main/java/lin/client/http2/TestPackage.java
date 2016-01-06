package lin.client.http2;

import java.util.HashMap;
import java.util.Map;

import lin.client.http2.annotation.HttpPackageUrl;

/**
 * 
 * @author lin
 * @date Mar 8, 2015 10:24:08 PM
 *
 */
@HttpPackageUrl("/core/comm/test.action")
public class TestPackage extends lin.client.http.HttpPackage{

	
	 @Override
	public Map<String, Object> getParams() {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("data", data);
		return map;
	}

	private String data;// { get; set; }
//     public override IDictionary<string, object> GetParams()
//     {

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
         
}
