package io.cess.comm.tcp;

import io.cess.comm.tcp.annotation.Command;
import io.cess.comm.tcp.annotation.RespType;

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
