package io.cess.comm.http.okhttp;

import io.cess.comm.http.AbstractHttpCommunicateImpl;
import io.cess.comm.http.HttpCommunicate;
import io.cess.comm.http.HttpCommunicateDownloadFile;
import io.cess.comm.http.HttpCommunicateHandler;
import okhttp3.OkHttpClient;

/**
 * 
 * @author 王江林
 * @date 2013-7-29 下午9:13:22
 *
 */
public class OkHttp3CommunicateImpl extends AbstractHttpCommunicateImpl {

	public OkHttp3CommunicateImpl(String name, HttpCommunicate c) {
		super(name, c);
	}

	@Override
	protected HttpCommunicateHandler getHandler() {
		return new OkHttp3Handler();
	}

//	private SessionInfo sessionInfo = new SessionInfo();

//	@Override
//	protected HttpCommunicateRequest getRequest() {
//		return new HttpURLConnectionRequest(sessionInfo);
//	}

	@Override
	protected HttpCommunicateDownloadFile downloadRequest() {
		return new DownloadFile(mContext);
	}


}