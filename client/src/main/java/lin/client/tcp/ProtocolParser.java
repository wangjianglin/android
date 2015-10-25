package lin.client.tcp;

public interface ProtocolParser {
	Package getPackage();

	void put(byte... bs);

	void clear();
}
