package lin.client.tcp;

public abstract class Package {
	

	public long sequeue;// { get; internal set; }
	public PackageState state;// { get;internal set; }

	public Package() {
		this.state = PackageState.NONE;
		this.sequeue = 0;
	}

	// public abstract byte Type { get; }

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
