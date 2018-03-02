package io.cess.comm.tcp;

public interface ProtocolParser {
	TcpPackage getPackage();

	void put(byte... bs);

	void setState(PackageState state);

	PackageState getState();

	void clear();
}
