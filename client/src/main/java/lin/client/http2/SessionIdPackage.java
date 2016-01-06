package lin.client.http2;

import java.util.Map;

public class SessionIdPackage extends lin.client.http.HttpPackage{

	public SessionIdPackage(){
		super("/core/comm/sessionId.action");
	}
	
	 @Override
	public Map<String, Object> getParams() {
		return null;
	}

}
