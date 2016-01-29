package lin.client.tcp;

import lin.util.thread.AutoResetEvent;


public class PackageResponse {
	private AutoResetEvent set = new AutoResetEvent();

	PackageResponse() {
	}

	private volatile ErrorTcpPackage error;
	private volatile ResponseTcpPackage pack;

	void response(ResponseTcpPackage pack) {
		this.pack = pack;
		set.set();
	}

	void setError(ErrorTcpPackage error){
		this.error = error;
		set.set();
	}

	public ResponseTcpPackage getResponse(){
		waitForEnd();
		return this.pack;
	}

	// / <summary>
	// /
	// / </summary>
	// / <param name="timeout">超时，以毫秒为单位，默认120秒</param>
	// / <returns></returns>
	public PackageResponse waitForEnd() {
		return waitForEnd(120000);
	}

	public PackageResponse waitForEnd(int timeout) {
		set.waitOne();
		return this;
	}
}
