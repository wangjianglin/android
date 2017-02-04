package lin.comm.tcp;


//@FunctionalInterface
public interface CommunicateListener{
	void listener(Session session,TcpPackage pack,Response pesonse);
}

