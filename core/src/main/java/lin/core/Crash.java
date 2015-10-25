package lin.core;

import java.util.Date;

public class Crash {

	private String stackTrace;
	private String deviceInfo;
	private Date date = new Date();
	private String threadInfo;
	public String getStackTrace() {
		return stackTrace;
	}
	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}
	public String getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void setThreadInfo(String threadInfo) {
		this.threadInfo = threadInfo;
	}
	public String getThreadInfo() {
		return threadInfo;
	}
}
