package io.cess.comm.http;

/**
 * 
 * @author 王江林
 * @date 2013-7-16 下午12:03:42
 *
 */
public class Version {

	/**
	 * 主版本号
	 */
	private int major = 0;// { get; set; }
    /**
     * 次版本号
     */
    private int minor = 0;// { get; set; }
	public int getMajor() {
		return major;
	}
	public void setMajor(int major) {
		this.major = major;
	}
	public int getMinor() {
		return minor;
	}
	public void setMinor(int minor) {
		this.minor = minor;
	}
	public Version(int major, int minor) {
		super();
		this.major = major;
		this.minor = minor;
	}
	public Version(){}
}
