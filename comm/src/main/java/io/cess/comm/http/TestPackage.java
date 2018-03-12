package io.cess.comm.http;

import io.cess.comm.http.annotation.HttpPackageMethod;
import io.cess.comm.http.annotation.HttpPackageUrl;
import io.cess.comm.http.annotation.HttpParamName;

/**
 * 
 * @author lin
 * @date Mar 8, 2015 10:24:08 PM
 *
 */
@HttpPackageMethod(HttpMethod.POST)
@HttpPackageUrl("/comm/test.action")
public class TestPackage extends HttpPackage<String>{

	@HttpParamName
	private String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
         
}
