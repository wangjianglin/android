package lin.core.openfire;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.RemoteViews;

import java.util.List;

import lin.core.R;
/**
 *
 * @author lin
 * @date Aug 31, 2015 2:09:51 PM
 *
 */
@SuppressLint("NewApi")
public class OpenfireService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

	private int iconId = 0;
	private static Thread thread;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (intent != null) {
			if (intent.hasExtra("host") && intent.hasExtra("port")) {
				final String host = intent.getStringExtra("host");
				final String title = intent.getStringExtra("title");
				final int port = intent.getIntExtra("port", intent.getIntExtra("port",5222));
				iconId = intent.getIntExtra("icon_id", R.drawable.ic_launcher);
				thread = new Thread(new Runnable() {

					@Override
					public void run() {
						openFire(host, port, title);
					}
				});
				thread.start();
			}
		}


		return super.onStartCommand(intent, flags, startId);
	}

	private void openFire(String host, int port, final String title) {
		XmppConnectionManager.init(host, port, null, null);
		XMPPConnection conn = XmppConnectionManager.getConnection();
//		conn.connect();
//		conn.loginAnonymously();


//		try {
//			conn.connect();
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//
//		try {
//			conn.loginAnonymously();
////			conn.login("test", "123456");
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}

		final MessageListener mListener = new MessageListener() {

			@Override
			public void processMessage(Chat arg0, Message arg1) {
//				System.out.println("===="+arg1.getBody()+"====");
				shwoNotification(arg1.getBody(), title);
			}
		};
		ChatManager chatmanager = conn.getChatManager();
		chatmanager.addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat arg0, boolean arg1) {
				arg0.addMessageListener(mListener);
//				System.out.println("===="+arg0.)
			}
		});
	}

	private void shwoNotification(String message, String title) {
		NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
////		Notification notification = new Notification(R.drawable.ic_launcher,"weather",System.currentTimeMillis());
//		Notification notification = new Notification();
////		if(isSendSuccess){
////			notification.setLatestEventInfo(context, "发送成功", "内容："+content, PendingIntent.getBroadcast(context, 0, new Intent(CancelNotificationAction), 0));
////		}else{
////			notification.setLatestEventInfo(context, "改善失败", "异常信息："+exceptionInfo, PendingIntent.getBroadcast(context, 0, new Intent(CancelNotificationAction), 0));
////		}
//		notification.number++;
//		notification.
//		notificationManager.notify(1, notification);


		ApplicationInfo appInfo = getApplicationContext()
				.getApplicationInfo();


//		public void startAPP(String appPackageName){
//			try{
		Intent intent = this.getPackageManager().getLaunchIntentForPackage(appInfo.packageName);
		Bundle bundle = new Bundle();
		bundle.putString("notification_message", message);
		intent.putExtras(bundle);

		Notification notif = null;
//        if (isRunningForeground(this)) {
//            notif = new Notification.Builder(this)
//                    .setSmallIcon(iconId)
//                    .setAutoCancel(true)
//                    .setTicker(message)
//                    .setDefaults(Notification.DEFAULT_VIBRATE)
//                    .setContentIntent(PendingIntent.getActivity(this.getApplicationContext(), 0, new Intent(), 0))
//                    .build();
//        } else {
		notif = new Notification.Builder(this)
				.setSmallIcon(iconId)
				.setAutoCancel(true)
				.setDefaults(Notification.DEFAULT_VIBRATE)
				.setTicker(message)
//                    .setProgress(0, 0, true)
//		         .setLargeIcon(R.drawable.ic_launcher)
				.setContentIntent(PendingIntent.getActivity(this.getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT))
				.build();
//        }

		//自定义通知栏RemoteViews
		RemoteViews remoteviews = new RemoteViews(this.getPackageName(), R.layout.notification_remote_view);
		remoteviews.setImageViewResource(R.id.notification_image, R.drawable.ic_launcher);
		remoteviews.setTextViewText(R.id.notification_title, title);
		remoteviews.setTextViewText(R.id.notification_text, message);
		notif.contentView = remoteviews;

//				startActivity(intent);
//			}catch(Exception e){
//				Toast.makeText(this, "没有安装", Toast.LENGTH_LONG).show();
//			}
//		}


		notif.flags = Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(this.getPackageName()+"message_notification", 1, notif);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		XmppConnectionManager.getConnection().disconnect();
	}

	public boolean isRunningForeground(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appProcess = am.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo app : appProcess) {
			if (app.processName.equals(context.getPackageName())) {
				if (app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					return true;
				} else {
					return false;
				}
//                if (100!=100) {
//                    return true;
//                } else {
//                    return false;
//                }
			}
		}
		return false;
	}

	public boolean isServiceRunning(Context context, String serviceName) {
		ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> services = am.getRunningServices(50);
		if (services.size() <= 0) {
			return false;
		}
		for (ActivityManager.RunningServiceInfo serviceInfo : services) {
			if (serviceInfo.service.getClassName().equals(serviceName)) {
				return true;
			}
		}
		return false;
	}
}
//
//public class OpenfireService extends Service{
//
//	@Override
//	public IBinder onBind(Intent intent) {
//		return null;
//	}
//
//	@Override
//	public void onCreate() {
//		super.onCreate();
//
//	}
//
//	private int iconId = 0;
//	private static Thread thread;
//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
//
//		if(intent != null){
//			if(intent.hasExtra("host") && intent.hasExtra("port")){
//				final String host = intent.getStringExtra("host");
//				final int port = intent.getIntExtra("port", 5222);
//				iconId = intent.getIntExtra("icon_id", R.drawable.ic_launcher);
//				thread = new Thread(new Runnable(){
//
//					@Override
//					public void run() {
//						openFire(host,port);
//					}});
//				thread.start();
//			}
//		}
//
//
//		return super.onStartCommand(intent, flags, startId);
//	}
//
//	private void openFire(String host,int port){
//		XmppConnectionManager.init(host,port,null,null);
//		XMPPConnection conn = XmppConnectionManager.getConnection();
////		conn.connect();
////		conn.loginAnonymously();
//
//
////		try {
////			conn.connect();
////		} catch (Throwable e) {
////			e.printStackTrace();
////		}
////
////		try {
////			conn.loginAnonymously();
//////			conn.login("test", "123456");
////		} catch (Throwable e) {
////			e.printStackTrace();
////		}
//
//		final MessageListener mListener = new MessageListener(){
//
//			@Override
//			public void processMessage(Chat arg0, Message arg1) {
////				System.out.println("===="+arg1.getBody()+"====");
//				shwoNotification(arg1.getBody());
//			}};
//		ChatManager chatmanager = conn.getChatManager();
//		chatmanager.addChatListener(new ChatManagerListener() {
//			@Override
//			public void chatCreated(Chat arg0, boolean arg1) {
//				arg0.addMessageListener(mListener);
////				System.out.println("===="+arg0.)
//			}
//		});
//	}
//
//	private void shwoNotification(String message){
//		NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//////		Notification notification = new Notification(R.drawable.ic_launcher,"weather",System.currentTimeMillis());
////		Notification notification = new Notification();
//////		if(isSendSuccess){
//////			notification.setLatestEventInfo(context, "发送成功", "内容："+content, PendingIntent.getBroadcast(context, 0, new Intent(CancelNotificationAction), 0));
//////		}else{
//////			notification.setLatestEventInfo(context, "改善失败", "异常信息："+exceptionInfo, PendingIntent.getBroadcast(context, 0, new Intent(CancelNotificationAction), 0));
//////		}
////		notification.number++;
////		notification.
////		notificationManager.notify(1, notification);
//
//
//		ApplicationInfo appInfo = getApplicationContext()
//				.getApplicationInfo();
//
//
////		public void startAPP(String appPackageName){
////			try{
//		Intent intent = this.getPackageManager().getLaunchIntentForPackage(appInfo.packageName);
//
//
//		Notification notif = new Notification.Builder(this)
//		         .setContentTitle("新消息")
//		         .setContentText(message)
//		         .setSmallIcon(iconId)
//				.setAutoCancel(true)
//				.setDefaults(Notification.DEFAULT_VIBRATE)
////		         .setLargeIcon(R.drawable.ic_launcher)
//				.setContentIntent(PendingIntent.getActivity(this.getApplicationContext(),1,intent,PendingIntent.FLAG_UPDATE_CURRENT))
//		         .build();
//
////				startActivity(intent);
////			}catch(Exception e){
////				Toast.makeText(this, "没有安装", Toast.LENGTH_LONG).show();
////			}
////		}
//
//
//		notif.flags = Notification.FLAG_AUTO_CANCEL;
//
//		notificationManager.notify("纯翠网", 1, notif);
//	}
//
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		XmppConnectionManager.getConnection().disconnect();
//	}
//
//}
