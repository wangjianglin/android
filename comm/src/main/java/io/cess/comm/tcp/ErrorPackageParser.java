package io.cess.comm.tcp;

import io.cess.comm.tcp.annotation.ProtocolParserType;
import io.cess.util.JsonUtil;

@ProtocolParserType(0)
public class ErrorPackageParser extends AbstractProtocolParser {
	public TcpPackage getPackage() {

		String s = new String(buffer,0,count);

		ErrorTcpPackage.Data data = JsonUtil.deserialize(s,ErrorTcpPackage.Data.class);

		return new ErrorTcpPackage(data);
	}
}
