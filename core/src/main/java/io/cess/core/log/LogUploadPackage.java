package io.cess.core.log;

import java.io.File;

import io.cess.comm.http.HttpMethod;
import io.cess.comm.http.HttpPackage;
import io.cess.comm.http.annotation.HttpPackageMethod;
import io.cess.comm.http.annotation.HttpParamName;
/**
 * 
 * @author lin
 * @date Jun 26, 2015 12:54:49 PM
 *
 */
//@HttpPackageUrl("/exception/addLog.action")
@HttpPackageMethod(HttpMethod.POST)
public class LogUploadPackage extends HttpPackage{

	public LogUploadPackage(String url){
		super(url);
	}
	@HttpParamName
	private String uuid;
	
	@HttpParamName
	private File log;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public File getLog() {
		return log;
	}

	public void setLog(File log) {
		this.log = log;
	}

}
