package lin.comm.http;

import lin.comm.http.annotation.HttpPackageMethod;
import lin.comm.http.annotation.HttpPackageUrl;

@HttpPackageUrl("/core/comm/sessionId.action")
@HttpPackageMethod(HttpMethod.GET)
public class SessionIdPackage extends lin.comm.http.HttpPackage{

//	public SessionIdPackage(){
//		super("/core/comm/sessionId.action");
//	}
//
//	 @Override
//	public Map<String, Object> getParams() {
//		return null;
//	}

}
