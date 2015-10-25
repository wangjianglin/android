package lin.client.tcp;

/// <summary>
/// 请求错误响应包
/// </summary>
public class ErrorPackage extends Package {
	@Override
	public final byte getType() {
		return 0;
	}

	// private byte _type;
	@Override
	public final byte[] write() {
		return new byte[0];
	}
}
