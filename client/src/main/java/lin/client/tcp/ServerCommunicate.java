package lin.client.tcp;

import lin.util.thread.AutoResetEvent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lin on 1/21/16.
 */
public class ServerCommunicate extends AbstractCommunicate{


	// / <summary>
	// / 通信监听器
	// / </summary>
	private CommunicateListener listener;
	// / <summary>
	// / 商品号
	// / </summary>
	private int port;


	private ServerSocket serverSocket;

	private List<Session> serverSessions = new ArrayList<Session>();
	private SessionListener sessionListener;


	public ServerCommunicate(CommunicateListener listener, int port) {
		this(listener, port, null);
	}

	public ServerCommunicate(CommunicateListener listener, int port,
					   SessionListener sessionListener) {
		this.listener = listener;
		this.sessionListener = sessionListener;
		this.port = port;
		this.init();
	}

	public void close() {
		try {
            this.serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void init() {
		autoEvent.reset();
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
                initImpl();
			}
		});
		thread.setDaemon(false);
		thread.start();
		autoEvent.waitOne();
	}

	private AutoResetEvent autoEvent = new AutoResetEvent(false);

	private void initImpl() {
		try {

			this.serverSocket = new ServerSocket(this.port);
			while (true) {
				autoEvent.set();
				Socket c = serverSocket.accept();
				final Session session = new Session(this, c);
				synchronized (serverSessions) {
					serverSessions.add(session);
				}
				if (sessionListener != null) {
					sessionListener.Create(session);
				}
				final CommunicateRecv recv = new CommunicateRecv(this, session,
						listener);
				session.recv = recv;
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							recv.recvData();
						} finally {
							synchronized (serverSessions) {
								serverSessions.remove(session);
								if (sessionListener != null) {
									sessionListener.Destory(session);
								}
							}
						}
					}
				});
				thread.setDaemon(false);
				thread.start();
			}
		} catch (IOException e1) {

		} finally {
			autoEvent.set();
		}
	}



	public PackageResponse send(RequestTcpPackage pack) {
        for (Session session : serverSessions) {
            session.send(pack);
        }
        return null;
	}

	// / <summary>
	// / 表示当前的连接状态，true表示连接上，false表示连接断开
	// / </summary>
	public boolean isConnected() {
		return !this.serverSocket.isClosed();
	}
}

