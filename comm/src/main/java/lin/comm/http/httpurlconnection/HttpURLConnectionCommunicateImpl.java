package lin.comm.http.httpurlconnection;

import org.apache.http.impl.client.CloseableHttpClient;

import lin.comm.http.AbstractHttpCommunicateImpl;
import lin.comm.http.HttpCommunicate;
import lin.comm.http.HttpCommunicateDownloadFile;
import lin.comm.http.HttpCommunicateRequest;

/**
 * 
 * @author 王江林
 * @date 2013-7-29 下午9:13:22
 *
 */
public class HttpURLConnectionCommunicateImpl extends AbstractHttpCommunicateImpl {
	public HttpURLConnectionCommunicateImpl(String name, HttpCommunicate c) {
		super(name, c);
	}

	private SessionInfo sessionInfo = new SessionInfo();

	@Override
	protected HttpCommunicateRequest getRequest() {
		return new HttpURLConnectionRequest(sessionInfo);
	}

	@Override
	protected HttpCommunicateDownloadFile downloadRequest() {
		return new DownloadFile(context);
	}

	@Override
	public void newSession() {

	}

}