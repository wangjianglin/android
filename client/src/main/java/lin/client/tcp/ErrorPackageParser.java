package lin.client.tcp;

@ProtocolParserType(type = 0)
public class ErrorPackageParser implements ProtocolParser {
	public Package getPackage() {
		return new ErrorPackage();
	}

	public void put(byte... bs) {
	}

	public void clear() {
	}
}
