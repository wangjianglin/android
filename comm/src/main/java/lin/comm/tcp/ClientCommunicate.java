package lin.comm.tcp;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by lin on 1/21/16.
 */
public class ClientCommunicate extends AbstractCommunicate{


//	public Class<?> getProtocolParser(byte type) {
//		return null;
//	}

	// / <summary>
	// / 服务端IP
	// / </summary>
	private String ip;
	// / <summary>
	// / 通信监听器
	// / </summary>
	private CommunicateListener listener;
	// / <summary>
	// / 商品号
	// / </summary>
	private int port;
	// private Thread recvThread;
	// / <summary>
	// / 对象锁
	// / </summary>
	private Socket socket;

	private Session clientSession;

	public ClientCommunicate(CommunicateListener listener, String ip, int port) {
		this.listener = listener;
		this.ip = ip;
		this.port = port;
		this.init();
	}


	public void close() {
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void init() {
		try {
			this.socket = new Socket(this.ip, this.port);
		} catch (IOException e) {
			e.printStackTrace();
		}

		clientSession = new Session(this, socket);
		final CommunicateRecv recv = new CommunicateRecv(this, clientSession,
				listener);
		clientSession.recv = recv;
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				recv.recvData();
			}
		});
		thread.setDaemon(false);
		thread.start();
	}


	public PackageResponse send(RequestTcpPackage pack) {
		return clientSession.send(pack);
	}

	// / <summary>
	// / 表示当前的连接状态，true表示连接上，false表示连接断开
	// / </summary>
	public boolean isConnected() {
		return this.socket.isConnected();
	}
}
