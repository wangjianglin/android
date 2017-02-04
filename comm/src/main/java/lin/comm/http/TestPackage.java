package lin.comm.http;

import lin.comm.http.annotation.HttpPackageMethod;
import lin.comm.http.annotation.HttpPackageUrl;
import lin.comm.http.annotation.HttpParamName;

/**
 * 
 * @author lin
 * @date Mar 8, 2015 10:24:08 PM
 *
 */
@HttpPackageMethod(HttpMethod.GET)
@HttpPackageUrl("/core/comm/test.action")
public class TestPackage extends HttpPackage{

	
//	 @Override
//	public Map<String, Object> getParams() {
//		Map<String,Object> map = new HashMap<String, Object>();
//		map.put("data", data);
//		return map;
//	}

	@HttpParamName
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