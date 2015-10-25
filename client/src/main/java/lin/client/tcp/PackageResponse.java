package lin.client.tcp;

import lin.util.thread.AutoResetEvent;

//class PackageResponse
public class PackageResponse {
	private AutoResetEvent set;

	PackageResponse(AutoResetEvent set) {
		this.set = set;
	}

	private volatile Package pack;

	void response(Package pack) {
		this.pack = pack;
		set.set();
	}

	// / <summary>
	// /
	// / </summary>
	// / <param name="timeout">超时，以毫秒为单位，默认120秒</param>
	// / <returns></returns>
	public Package waitForEnd() {
		return waitForEnd(120000);
	}

	public Package waitForEnd(int timeout) {
		set.waitOne();
		return this.pack;
	}
}
