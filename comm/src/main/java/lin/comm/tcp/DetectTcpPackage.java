package lin.comm.tcp;

import lin.comm.tcp.annotation.Command;
import lin.comm.tcp.annotation.RespType;

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
