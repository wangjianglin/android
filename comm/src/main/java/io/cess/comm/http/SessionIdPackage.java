package io.cess.comm.http;

import io.cess.comm.http.annotation.HttpPackageMethod;
import io.cess.comm.http.annotation.HttpPackageUrl;

@HttpPackageUrl("/core/comm/sessionId.action")
@HttpPackageMethod(HttpMethod.GET)
public class SessionIdPackage extends io.cess.comm.http.HttpPackage<String>{

//	public SessionIdPackage(){
//		super("/core/comm/sessionId.action");
//	}
//
//	 @Override
//	public Map<String, Object> getParams() {
//		return null;
//	}

}
