package io.cess.comm.tcp;

public abstract class AbstractTcpPackage implements TcpPackage {
	

	private long sequeue;
	private PackageState state;

	public AbstractTcpPackage() {
		this.state = PackageState.REQUEST;
		this.sequeue = 0;
	}

	public abstract byte getType();

	public PackageState getState() {
		return state;
	}

	void setState(PackageState state) {
		this.state = state;
	}

	public long getSequeue() {
		return sequeue;
	}

	void setSequeue(long sequeue) {
		this.sequeue = sequeue;
	}


	public abstract byte[] write();// byte[] bs, int offset = 0);
}
