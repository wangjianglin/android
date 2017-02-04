package lin.core.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.provider.Settings;
import lin.comm.http.HttpCommunicate;
import lin.core.Device;
import lin.core.LocalStorage;
import lin.util.JsonUtil;

/**
 * 
 * @author lin
 * @date Aug 23, 2015 10:51:32 AM
 *
 */
public class LogService extends Service{

	public static final int INIT = 1;
	public static final int LOG = 2;
	public static final int UPLOAD_LOG = 3;
	public static final int UPLOAD_LOG_COMPLETE = 4;
	public static final int UPLOAD_LOG_FAIL = 5;
	private static final long LOG_FILE_SIZE = 512 * 1024;
	private static final long LOG_FILE_UPLOAD_TIME = 20 * 60 * 10;
	private static final String LOG_FILE_UPLOAD_SITUATION = "_LOG_SERVICE_LOG_FILE_UPLOAD_SITUATION_LOCAL_STROAGE_ITEM_";

	public static final String START_LOG_ACTION = "lin.core.log.LogService.__start_log_action__";
	public static final String EXCEPTION_URL = "exception_url";
	public static final String LOG_URL = "log_url";
	public static final String LOG_PRE = "log_pre";

	private boolean isInitLog = false;
	@Override
	public IBinder onBind(Intent intent) {
		if(!isInitLog){
			isInitLog = true;
			if(intent.hasExtra(LOG_URL)){
				logUrl = intent.getStringExtra(LOG_URL);
			}else{
				logUrl = "";
			}
			if(intent.hasExtra(LOG_PRE)){
				logPre = intent.getStringExtra(LOG_PRE);
			}else{
				logPre = "";
			}
			System.out.println("service pid:" + android.os.Process.myPid());
			uploadLogFile(this.getApplicationContext());
		}
		return mMessenger.getBinder();
	}

	
	@Override
	public void onCreate() {
		super.onCreate();
		
	}


	private String logUrl = null;
	private String logPre = null;
//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
////		System.out.println("service cmd pid:" + android.os.Process.myPid());
////		if(intent != null && START_LOG_ACTION.equals(intent.getAction())){
//////			if(intent.hasExtra(EXCEPTION_URL)){
//////				exceptionUrl = intent.getStringExtra(EXCEPTION_URL);
//////			}else{
//////				exceptionUrl = "";
//////			}
////			if(intent.hasExtra(LOG_URL)){
////				logUrl = intent.getStringExtra(LOG_URL);
////			}else{
////				logUrl = "";
////			}
////			if(intent.hasExtra(LOG_PRE)){
////				logPre = intent.getStringExtra(LOG_PRE);
////			}else{
////				logPre = "";
////			}
////			uploadLogFile(this.getApplicationContext());
////		}
//		return super.onStartCommand(intent, flags, startId);
//	}




	@SuppressLint("HandlerLeak")
	private Handler serverHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case INIT:
				cMessenger = msg.replyTo;//get the messenger of client
				break;
			case LOG:
				String[] args = (String[]) msg.obj;
				log(getApplication(),args[0],args[1],args[2]);
				break;
//			case UPLOAD_LOG_COMPLETE:
//				logUploadSuccess = true;
//			case UPLOAD_LOG_FAIL:
//				if(set != null){
//					set.set();
//				}
//				break;
			default:
				break;
			}
		}
	};
	
	private static synchronized void log(Context context,String log,String level,String tag){
		
		Date date = new Date();
		createLogFile(context,date);
		
		if(tag == null || "".equals(tag)){
			_out.println("["+level + logFormat1.format(date) + log);
		}else{
			_out.println("["+level + logFormat2.format(date) + tag + "]" + log);
		}
	}

	@SuppressLint("SimpleDateFormat")
	private static Format format = new SimpleDateFormat("yyyy-MM-dd");
	@SuppressLint("SimpleDateFormat")
	private static Format logFormat1 = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss.SSS]");
	@SuppressLint("SimpleDateFormat")
	private static Format logFormat2 = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss.SSS][");
	private static File logFile = null;
	private static PrintStream _out;
	private static String logFileName = null;
	private static void createLogFile(Context context,Date date){
		String fileName = format.format(date);
		
		File file = context.getExternalFilesDir("/logs");
		 
        if (file == null) {
            file = new File(context.getCacheDir() + "/logs");
        }
        if(!file.exists()){
       	 file.mkdirs();
        }
        
		if(logFile != null && fileName.equals(logFileName) && logFile.length() < LOG_FILE_SIZE){
			return;
		}
		
		int count = 1;
		logFile = new File(file.getAbsoluteFile() + "/" + fileName + ".log");
		while(logFile.exists() && logFile.length() >= LOG_FILE_SIZE){
			logFile = new File(file.getAbsoluteFile() + "/" + fileName + "-" + count + ".log");
			count++;
		}
         logFileName = fileName;
         
         try {
			_out = new PrintStream(new FileOutputStream(logFile,true));
		} catch (FileNotFoundException e) {
		}
         if(logFile.length() == 0){
        	 _out.println("device info:");
        	 _out.println(Device.collectDeviceInfo(context));
        	 _out.println();
         }
	}
	
	
	
	private Messenger mMessenger = new Messenger(serverHandler);//It's the messenger of server
	private Messenger cMessenger = null;//It's the messenger of client
	
	private static Thread thread = null;
//	private AutoResetEvent set = null;
	private boolean logUploadSuccess = false;
	private synchronized void uploadLogFile(final Context context){
		if(thread != null){
			return;
		}
//		set = new AutoResetEvent();
		thread = new Thread(new Runnable(){

			@Override
			public void run() {
				while(true){
					try{
						uploadLogFileImpl(context);
					}catch(Throwable e){
						e.printStackTrace();
					}
					try {
						Thread.sleep(LOG_FILE_UPLOAD_TIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}});
		thread.start();
	}
	
	private void uploadLogFileImpl(Context context) {
		if (cMessenger == null) {
			return;
		}

		File path = context.getExternalFilesDir("/logs");

		if (path != null && path.exists()) {
			uploadLogFileImpl2(context,path);
		}
		path = new File(context.getCacheDir() + "/logs");
		if(!path.exists()){
			uploadLogFileImpl2(context,path);
		}
	}

	private void uploadLogFileImpl2(Context context,File path){
       
        String fileName = format.format(new Date());
        File[] files = path.listFiles();
//        Message message = null;
        
        LocalStorage.init(context);
        
        @SuppressWarnings("unchecked")
		List<String> list = (List<String>) LocalStorage.getItem(LOG_FILE_UPLOAD_SITUATION,new JsonUtil.GeneralType(List.class, String.class));
        if(list == null){
        	list = new ArrayList<String>();
        }

		final String uuid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        List<String> newList = new ArrayList<String>();
		if(files != null) {
			for (File file : files) {
				//过滤掉已经上传过的
				if (file == null || (file.getName().contains(fileName) && file.length() < LOG_FILE_SIZE)) {
					continue;
				}
				if (!list.contains(file.getAbsolutePath())) {
//        		newList.add(file.getAbsolutePath());
//        		continue;
					logUploadSuccess = uploadLog(file, uuid, logPre, logUrl);
					if (logUploadSuccess) {
						list.add(file.getAbsolutePath());
						newList.add(file.getAbsolutePath());
					} else {
						continue;
					}
				}else{
					newList.add(file.getAbsolutePath());
				}
				if (file.getName().contains(fileName)) {//不删除当天的日志
					file.delete();
				}

				LocalStorage.setItem(LOG_FILE_UPLOAD_SITUATION, list);
//        	message = new Message();
//        	message.what = UPLOAD_LOG;
//        	message.obj = Uri.fromFile(file);
//        	set.reset();
//        	logUploadSuccess = false;
//        	try {
//				cMessenger.send(message);
//			} catch (RemoteException e) {
//				e.printStackTrace();
//			}
//        	set.waitOne();

			}
		}
        LocalStorage.setItem(LOG_FILE_UPLOAD_SITUATION,newList);
	}
	
	
	

	
	private static boolean uploadLog(File file,String uuid,String pre,String logUrl){
		
		LogUploadPackage uploadPackage = new LogUploadPackage(logUrl);
		
//		uploadPackage.setLog(new File(uri.getEncodedPath()));
		uploadPackage.setLog(file);
		
//		User user = LocalStorage.getItem("user",User.class);
		if(pre != null){
			uploadPackage.setUuid("["+pre+"]"+uuid);
		}else{
			uploadPackage.setUuid(uuid);
		}
		
		return HttpCommunicate.request(uploadPackage, null).isSuccess();
	}

}
