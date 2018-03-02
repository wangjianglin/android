package io.cess.core.log;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.Settings;

import io.cess.core.Crash;
import io.cess.core.CrashHandler;
import io.cess.core.CrashListener;
import io.cess.util.DeviceUtil;
import io.cess.core.Utils;

/**
 * 
 * @author lin
 * @date Aug 21, 2015 5:49:00 PM
 *
 */
public class Log {

	private String tag = null;
	public Log(String tag){
		this.tag = tag;
	}
	public void info(String info){
		info(tag,info);
	}
	
	public void error(String error){
		error(tag,error);
	}
	
	public void warning(String warning){
		warning(tag,warning);
	}
	
	public void debug(String debug){
		debug(tag,debug);
	}
	
	public void crash(String crash){
		crash(tag,crash);
	}

	public void crash(Throwable e){
		crash(tag,io.cess.util.Utils.printStackTrace(e));
	}
	
	public static void info(String tag,String info){
		log(tag,info,"INFO");
	}
	
	public static void error(String tag,String error){
		log(tag,error,"ERROR");
	}
	
	public static void warning(String tag,String warning){
		log(tag,warning,"WARNING");
	}
	
	public static void debug(String tag,String debug){
		log(tag,debug,"DEBUG");
	}

	public static void crash(String tag,Throwable e){
		crash(tag,io.cess.util.Utils.printStackTrace(e));
	}
	public static void crash(String tag,String crash){
		log(tag,crash,"CRASH");

		Crash crashObj = new Crash();
		crashObj.setStackTrace(crash);
		crashObj.setDeviceInfo(DeviceUtil.collectDeviceInfo(mContext));
		crashObj.setThreadInfo(Utils.threadInfo(Thread.currentThread()));

		Util.uploadCrash(mContext,crashObj,mExceptionUrl,mUuid,tag);
	}
	private static void log(String tag,String log,String level){
		Message message = new Message();
		message.what = LogService.LOG;
		message.obj = new String[]{log,level,tag};
		message.replyTo = clientMessenger;
		try {
			sendMessage(message);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private static void sendMessage(Message message)throws RemoteException{
		if(clientRMessenger != null && queue.isEmpty()){
			clientRMessenger.send(message);
			return;
		}
		queue.add(message);
		if(clientRMessenger != null){
			sendQueueMessage();
			return;
		}
		
		clientHandler.postDelayed(sendDelayed, 500);
	}
	private static Runnable sendDelayed = new Runnable(){

		@Override
		public void run() {
			if(clientRMessenger == null){
				clientHandler.postDelayed(sendDelayed, 500);
				return;
			}
			while(!queue.isEmpty()){
				try {
					sendQueueMessage();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}};
	private static void sendQueueMessage()throws RemoteException{
		if(clientRMessenger == null){
			return;
		}
		while(!queue.isEmpty()){
			clientRMessenger.send(queue.poll());
		}
	}
	
	private static java.util.Queue<Message> queue = new java.util.concurrent.ConcurrentLinkedQueue<Message>();
	
	private static ServiceConnection serConn = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			clientRMessenger = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			clientRMessenger = new Messenger(service);//get the object of remote service
			clientMessenger = new Messenger(clientHandler);//initial the object of local service
			sendMessage();
		}
		
		private void sendMessage() {
			Message msg = Message.obtain(null, LogService.INIT);//MessengerService.TEST=0
			msg.replyTo = clientMessenger;
			try {
				clientRMessenger.send(msg);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
//			msg = Message.obtain(null, LogService.UPLOAD_LOG_FAIL);//MessengerService.TEST=0
//			msg.replyTo = clientMessenger;
//			try {
//				clientRMessenger.send(msg);
//			} catch (RemoteException e) {
//				e.printStackTrace();
//			}
		}
	};
	private static Messenger clientRMessenger;
	private static Messenger clientMessenger;
	private static Handler clientHandler = new Handler();
//	{
//
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch(msg.what){
//			case LogService.UPLOAD_LOG:
//				uploadLog((Uri) msg.obj);
//				break;
//			}
//		}
//		
//	};
//	private static LogUpload _logUpload = null;
//	private static void uploadLog(Uri uri){
//		
//		boolean success = false;
//		if(_logUpload != null){
//			try{
//				success = _logUpload.upload(uri);
//			}catch(Throwable e){}
//		}
//		Message message = new Message();
//		message.replyTo = clientMessenger;
//		message.what = success?LogService.UPLOAD_LOG_COMPLETE:LogService.UPLOAD_LOG_FAIL;
//		
//		try {
//			clientRMessenger.send(message);
//		} catch (RemoteException e) {
//		}
//	}
	
//	public static interface LogUpload{
//		boolean upload(Uri uri);//上传成功返回true，否则返回false
//	}
//	public static void setLogUpload(LogUpload logUpload){
//		_logUpload = logUpload;
//	}

	private static boolean isInit;
	private static Context mContext;
	private static String mExceptionUrl;
	private static String mUuid;

	public static void init(Context context,String logUrl,String exceptionUrl){
		init(context,logUrl,exceptionUrl,"android");
	}
	public static synchronized void init(Context context,String logUrl,String exceptionUrl,String logPre){
		if(isInit){
			return;
		}
		mContext = context;
		mExceptionUrl = exceptionUrl;
		isInit = true;
		Intent intent = new Intent(context,LogService.class);
		
		intent.putExtra(LogService.LOG_URL, logUrl);
		intent.putExtra(LogService.LOG_PRE, logPre);
		
		context.bindService(intent, serConn, Service.BIND_AUTO_CREATE);
//		context.startService(intent);
		initLog(context,exceptionUrl,logPre);
	}
	
	private static void initLog(final Context context,final String exceptionUrl,final String pre){


		mUuid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//		Log.setLogUpload(new LogUpload(){
//
//			@Override
//			public boolean upload(Uri uri) {
//				return uploadLog(uri,uuid,pre);
//			}});
		
		CrashHandler crashHandler = CrashHandler.getInstance(); //初始化 未捕获 的异常捕获类
		crashHandler.init(context);
		crashHandler.setCrashListener(new CrashListener() { // 设置异常捕获监听

			@Override
			public void crash(Crash crashObj) {
				crashImpl(context,crashObj, exceptionUrl, mUuid, pre);
			}
		});
	}
	
	private static void crashImpl(Context context,Crash crashObj,String exceptionUrl,String uuid,String pre) {

		crash(null,crashObj.getStackTrace());
		Util.uploadCrash(context,crashObj, exceptionUrl, uuid, pre).waitForEnd();
	}
//		ExceptionPackage pack = new ExceptionPackage(exceptionUrl); // 初始化向服务器发送异常Action
//		pack.setDeviceInfo(crash.getDeviceInfo());// 获取设备信息
//		pack.setInfo(crash.getStackTrace() + "\n\nthread info:\n" + crash.getThreadInfo());// 获取异常堆栈
//		//pack.setUuid("android:"+Settings.Secure.getString(this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID));
//
//		if(pre != null){
//			pack.setUuid("["+pre+"]" + uuid);
//		}else{
//			pack.setUuid(uuid);
//		}
//
//		//写不进去，异步原因导致的
//		Log.crash(pack.getInfo());
//
//		if(isDebug(context)){
//			android.util.Log.e("CRASH",pack.getInfo());
//		}
//		HttpCommunicate.request(pack, null).waitForEnd();// 向服务器发送异常信息
//	}

//	private static boolean isDebug(Context context){
//		ApplicationInfo appInfo = context
//				.getApplicationInfo();
//		return (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
//	}

}
