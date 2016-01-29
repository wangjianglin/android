package lin.client.tcp;

public interface Communicate {


	public void close();


	public void reconnection();

	public PackageResponse send(RequestTcpPackage pack);

	// / <summary>
	// / 表示当前的连接状态，true表示连接上，false表示连接断开
	// / </summary>
	public boolean isConnected() ;
}

//public class Communicate {
//
//
//	// public static readonly Lin.Util.MapIndexProperty<byte, Type>
//	// ProtocolParsers = new Util.MapIndexProperty<byte, Type>();
//	// public static Lin.Util.MapIndexProperty<byte, Type> ProtocolParsers {
//	// get; private set; }
//	public Class<?> getProtocolParser(byte type) {
//		return null;
//	}
//
//	static {
//		// ProtocolParsers = new Util.MapIndexProperty<byte, Type>();
//	}
//
//	// public bool Connected { get; private set; }
//	// / <summary>
//	// / 服务端IP
//	// / </summary>
//	private String ip;
//	// / <summary>
//	// / 通信监听器
//	// / </summary>
//	private CommunicateListener listener;
//	// / <summary>
//	// / 商品号
//	// / </summary>
//	private int port;
//	// private Thread recvThread;
//	// / <summary>
//	// / 对象锁
//	// / </summary>
//	private Socket socket;
//
//	private ServerSocket serverSocket;
//
//	private Session clientSession;
//	private List<Session> serverSessions = new ArrayList<Session>();
//	private SessionListener sessionListener;
//
//	private boolean isServer = false;
//
//	public boolean isServer() {
//		return isServer;
//	}
//
//	// public Communicate(CommunicateListener listener, string ip, int
//	// port,ISessionListener sessionListener=null)
//	public Communicate(CommunicateListener listener, String ip, int port) {
//		this.listener = listener;
//		this.ip = ip;
//		this.port = port;
//		this.InitClient();
//	}
//
//	public Communicate(CommunicateListener listener, int port) {
//		this(listener, port, null);
//	}
//
//	public Communicate(CommunicateListener listener, int port,
//					   SessionListener sessionListener) {
//		this.listener = listener;
//		this.sessionListener = sessionListener;
//		this.port = port;
//		this.isServer = true;
//		this.InitServer();
//	}
//
//	public void close() {
//		try {
//			if (this.socket != null) {
//				this.socket.close();
//			}
//			if (this.serverSocket != null) {
//				this.serverSocket.close();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private void InitServer() {
//		autoEvent.reset();
//		Thread thread = new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				InitServerImpl();
//			}
//		});
//		thread.setDaemon(false);
//		thread.start();
//		autoEvent.waitOne();
//	}
//
//	private AutoResetEvent autoEvent = new AutoResetEvent(false);
//
//	private void InitServerImpl() {
//		try {
//			// this.socket = new Socket(this.ip,this.port);
//			// socket.Bind(new IPEndPoint(IPAddress.Parse("127.0.0.1"),
//			// this.port));
//			// socket.Listen(0);
//			this.serverSocket = new ServerSocket(this.port);
//			while (true) {
//				autoEvent.set();
//				Socket c = serverSocket.accept();
//				Session session = new Session(this, c);
//				synchronized (serverSessions) {
//					serverSessions.add(session);
//				}
//				if (sessionListener != null) {
//					sessionListener.Create(session);
//				}
//				final CommunicateRecv recv = new CommunicateRecv(this, session,
//						listener);
//				session.recv = recv;
//				Thread thread = new Thread(() -> {
//					// recv.recvData();
//					try {
//						recv.recvData();
//					} finally {
//						synchronized (serverSessions) {
//							serverSessions.remove(session);
//							if (sessionListener != null) {
//								sessionListener.Destory(session);
//							}
//						}
//					}
//				});
//				thread.setDaemon(false);
//				thread.start();
//			}
//		} catch (IOException e1) {
//
//		} finally {
//			autoEvent.set();
//		}
//	}
//
//	private void InitClient() {
//		// this.socket = new Socket(2, 1, 0);
//		try {
//			this.socket = new Socket(this.ip, this.port);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		clientSession = new Session(this, socket);
//		CommunicateRecv recv = new CommunicateRecv(this, clientSession,
//				listener);
//		clientSession.recv = recv;
//		Thread thread = new Thread(recv::recvData);
//		thread.setDaemon(false);
//		thread.start();
//	}
//
//	public void Reconnection() {
//		this.close();
//		if (isServer) {
//			this.InitServer();
//		} else {
//			this.InitClient();
//		}
//		return;
//	}
//
//	public PackageResponse send(TcpPackage pack) {
//		// EnterCriticalSection(&stCommunicateSendPackage);
//		if (isServer) {
//			for (Session session : serverSessions) {
//				session.send(pack);
//			}
//			return null;
//		} else {
//			return clientSession.send(pack);
//		}
//	}
//
//	// / <summary>
//	// / 表示当前的连接状态，true表示连接上，false表示连接断开
//	// / </summary>
//	public boolean isConnected() {
//		if (isServer) {
//			return !this.serverSocket.isClosed();
//		}
//		return this.socket.isConnected();
//	}
//}
