package lin.client.tcp;

import lin.client.tcp.annotation.Command;
import lin.client.tcp.annotation.RespType;

@Command(1)
@RespType(DetectRespTcpPackage.class)
public class DetectTcpPackage extends CommandRequestTcpPackage {

	@Override
	public void parse(byte[] bs,int offset) {
	}

	public DetectTcpPackage()
	{
	}

}
