package lin.client.tcp;


//@FunctionalInterface
public interface CommunicateListener{
	void listener(Session session,TcpPackage pack,Response pesonse);
}

