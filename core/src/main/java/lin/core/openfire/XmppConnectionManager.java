package lin.core.openfire;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;



/**
 *
 * XMPP服务器连接工具类.
 */

public class XmppConnectionManager {
//	private static XmppConnectionManager xmppConnectionManager;

	static {
		try {
			Class.forName("org.jivesoftware.smack.ReconnectionManager");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	private XmppConnectionManager() {
//
//	}
//
//	public static XmppConnectionManager getInstance() {
//		if (xmppConnectionManager == null) {
//			xmppConnectionManager = new XmppConnectionManager();
//		}
//		return xmppConnectionManager;
//	}

	// init
	private static XMPPConnection conn;

	public static XMPPConnection getConnection(){
		return conn;
	}
	private static void connect(final String username,final String password){
		try{
//			ConnectionListener listener = new ConnectionListener() {
//
//				@Override
//				public void reconnectionSuccessful() {
//					System.out.println("11111111111111111111");
//				}
//
//				@Override
//				public void reconnectionFailed(Exception arg0) {
//					System.out.println("2222222222222222222222");
//				}
//
//				@Override
//				public void reconnectingIn(int arg0) {
//					System.out.println("33333333333333333333333");
//				}
//
//				@Override
//				public void connectionClosedOnError(Exception arg0) {
//					System.out.println("=====================");
////					conn.removeConnectionListener(this);
////					connect(username,password);
//				}
//
//				@Override
//				public void connectionClosed() {
//
//					System.out.println("********************");
////					try {
////						Thread.sleep(2000);
////					} catch (InterruptedException e) {
////					}
////					conn.removeConnectionListener(this);
////					connect(username,password);
//				}
//			};

			conn.connect();
//			conn.addConnectionListener(listener);

			if(username == null){
				conn.loginAnonymously();
			}else{
				conn.login(username, password);
			}
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	public static void init(String host,int port,String username,String password) {
		Connection.DEBUG_ENABLED = false;

		try {
			//网络断开自动重链
			Class.forName("org.jivesoftware.smack.ReconnectionManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

//		Connection.
//		Connection.addConnectionListener(new XmppConnectionManager(){});
		ProviderManager pm = ProviderManager.getInstance();
		configure(pm);
		ConnectionConfiguration config = new ConnectionConfiguration(host,port);

////		config.setReconnectionAllowed(true);
//		config.setReconnectionAllowed(true);
////		// 允许登陆成功后更新在线状态
//		config.setSendPresence(true);


		config.setReconnectionAllowed(true);
		config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
		config.setSASLAuthenticationEnabled(false);
//		config .setTruststorePath("/system/etc/security/cacerts.bks");
//		config.setTruststorePassword("changeit");
//		config.setTruststoreType("bks");


		conn = new XMPPConnection(config);

		connect(username,password);
	}


//	public void configure(ProviderManager pm) {
//		public void configure() {
//			ProviderManager.addIQProvider("query", "jabber:iq:private",new PrivateDataManager.PrivateDataIQProvider());
//			// Time
////			try {
////				ProviderManager.addIQProvider("query", "jabber:iq:time",Class.forName("org.jivesoftware.smackx.packet.Time"));
////			} catch (Exception e) {
////				e.printStackTrace();
////			}
//			// Roster Exchange
////			ProviderManager.addExtensionProvider("x", "jabber:x:roster",new RosterExchangeProvider());
//			// Message Events
//			ProviderManager.addExtensionProvider("x", "jabber:x:event",new MessageEventProvider());
//			// Chat State
//			ProviderManager.addExtensionProvider("active","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
//			ProviderManager.addExtensionProvider("composing","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
//			ProviderManager.addExtensionProvider("paused","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
//			ProviderManager.addExtensionProvider("inactive","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
//			ProviderManager.addExtensionProvider("gone","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
//			// XHTML
//			ProviderManager.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",new XHTMLExtensionProvider());
//			// Group Chat Invitations
//			ProviderManager.addExtensionProvider("x", "jabber:x:conference",new GroupChatInvitation.Provider());
//			// Service Discovery # Items //解析房间列表
//			ProviderManager.addIQProvider("query", "http://jabber.org/protocol/disco#items",new DiscoverItemsProvider());
//			// Service Discovery # Info //某一个房间的信息
//			ProviderManager.addIQProvider("query", "http://jabber.org/protocol/disco#info",new DiscoverInfoProvider());
//			// Data Forms
//			ProviderManager.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
//			// MUC User
//			ProviderManager.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",new MUCUserProvider());
//			// MUC Admin
//			ProviderManager.addIQProvider("query", "http://jabber.org/protocol/muc#admin",new MUCAdminProvider());
//			// MUC Owner
//			ProviderManager.addIQProvider("query", "http://jabber.org/protocol/muc#owner",new MUCOwnerProvider());
//			// Delayed Delivery
//			ProviderManager.addExtensionProvider("x", "jabber:x:delay",new DelayInformationProvider());
//			// Version
//			try {
//				ProviderManager.addIQProvider("query", "jabber:iq:version",Class.forName("org.jivesoftware.smackx.packet.Version"));
//			} catch (ClassNotFoundException e) {
//				// Not sure what's happening here.
//			}
//			// VCard
//			ProviderManager.addIQProvider("vCard", "vcard-temp", new VCardProvider());
//			// Offline Message Requests
//			ProviderManager.addIQProvider("offline", "http://jabber.org/protocol/offline",new OfflineMessageRequest.Provider());
//			// Offline Message Indicator
//			ProviderManager.addExtensionProvider("offline","http://jabber.org/protocol/offline",new OfflineMessageInfo.Provider());
//			// Last Activity
//			ProviderManager.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
//			// User Search
//			ProviderManager.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
//			// SharedGroupsInfo
//			ProviderManager.addIQProvider("sharedgroup","http://www.jivesoftware.org/protocol/sharedgroup",new SharedGroupsInfo.Provider());
//			// JEP-33: Extended Stanza Addressing
//			ProviderManager.addExtensionProvider("addresses","http://jabber.org/protocol/address",new MultipleAddressesProvider());
//			ProviderManager.addIQProvider("si", "http://jabber.org/protocol/si",new StreamInitiationProvider());
//			ProviderManager.addIQProvider("query", "http://jabber.org/protocol/bytestreams",new BytestreamsProvider());
//			ProviderManager.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
//			ProviderManager.addIQProvider("command", "http://jabber.org/protocol/commands",new AdHocCommandDataProvider());
//			ProviderManager.addExtensionProvider("malformed-action","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.MalformedActionError());
//			ProviderManager.addExtensionProvider("bad-locale","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.BadLocaleError());
//			ProviderManager.addExtensionProvider("bad-payload","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.BadPayloadError());
//			ProviderManager.addExtensionProvider("bad-sessionid","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.BadSessionIDError());
//			ProviderManager.addExtensionProvider("session-expired","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.SessionExpiredError());
//
//		}

	private static void configure(ProviderManager pm) {
//		public void configure() {
			pm.addIQProvider("query", "jabber:iq:private",new PrivateDataManager.PrivateDataIQProvider());
			// Time
			try {
				pm.addIQProvider("query", "jabber:iq:time",Class.forName("org.jivesoftware.smackx.packet.Time"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Roster Exchange
			pm.addExtensionProvider("x", "jabber:x:roster",new RosterExchangeProvider());
			// Message Events
			pm.addExtensionProvider("x", "jabber:x:event",new MessageEventProvider());
			// Chat State
			pm.addExtensionProvider("active","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
			pm.addExtensionProvider("composing","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
			pm.addExtensionProvider("paused","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
			pm.addExtensionProvider("inactive","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
			pm.addExtensionProvider("gone","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
			// XHTML
			pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",new XHTMLExtensionProvider());
			// Group Chat Invitations
			pm.addExtensionProvider("x", "jabber:x:conference",new GroupChatInvitation.Provider());
			// Service Discovery # Items //解析房间列表
			pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",new DiscoverItemsProvider());
			// Service Discovery # Info //某一个房间的信息
			pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",new DiscoverInfoProvider());
			// Data Forms
			pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
			// MUC User
			pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",new MUCUserProvider());
			// MUC Admin
			pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",new MUCAdminProvider());
			// MUC Owner
			pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",new MUCOwnerProvider());
			// Delayed Delivery
			pm.addExtensionProvider("x", "jabber:x:delay",new DelayInformationProvider());
			// Version
			try {
				pm.addIQProvider("query", "jabber:iq:version",Class.forName("org.jivesoftware.smackx.packet.Version"));
			} catch (ClassNotFoundException e) {
				// Not sure what's happening here.
			}
			// VCard
			pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
			// Offline Message Requests
			pm.addIQProvider("offline", "http://jabber.org/protocol/offline",new OfflineMessageRequest.Provider());
			// Offline Message Indicator
			pm.addExtensionProvider("offline","http://jabber.org/protocol/offline",new OfflineMessageInfo.Provider());
			// Last Activity
			pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
			// User Search
			pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
			// SharedGroupsInfo
			pm.addIQProvider("sharedgroup","http://www.jivesoftware.org/protocol/sharedgroup",new SharedGroupsInfo.Provider());
			// JEP-33: Extended Stanza Addressing
			pm.addExtensionProvider("addresses","http://jabber.org/protocol/address",new MultipleAddressesProvider());
			pm.addIQProvider("si", "http://jabber.org/protocol/si",new StreamInitiationProvider());
			pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams",new BytestreamsProvider());
			pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
			pm.addIQProvider("command", "http://jabber.org/protocol/commands",new AdHocCommandDataProvider());
			pm.addExtensionProvider("malformed-action","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.MalformedActionError());
			pm.addExtensionProvider("bad-locale","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.BadLocaleError());
			pm.addExtensionProvider("bad-payload","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.BadPayloadError());
			pm.addExtensionProvider("bad-sessionid","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.BadSessionIDError());
			pm.addExtensionProvider("session-expired","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.SessionExpiredError());

		}
}
