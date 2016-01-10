package lin.client.http;

import java.util.Map;

import lin.client.http.annotation.HttpPackageMethod;
import lin.client.http.annotation.HttpPackageUrl;

@HttpPackageUrl("/core/comm/sessionId.action")
@HttpPackageMethod(HttpMethod.GET)
public class SessionIdPackage extends lin.client.http.HttpPackage{

//	public SessionIdPackage(){
//		super("/core/comm/sessionId.action");
//	}
//
//	 @Override
//	public Map<String, Object> getParams() {
//		return null;
//	}

}
