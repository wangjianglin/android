package lin.core;


import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.DownloadManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;


import lin.core.UpdateManager.DownloadResult;
import lin.core.UpdateManager.UpdateInfo;
import lin.util.Procedure;

/**
 * 
 * @author lin
 * @date Aug 21, 2015 3:46:07 PM
 *
 */
public class UpdateService extends Service {
  
	public static final int UPDATE = 1;
	public static final int DOWNLOAD_COMPLETE = 2;
	
	private static final int UPDATE_TIME = 10 * 60 * 1000;
	
	@SuppressLint("HandlerLeak")
	private Handler serverHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATE:
//				Log.e(TAG, "Get Message from MainActivity.");
				cMessenger = msg.replyTo;//get the messenger of client
				String[] args = (String[]) msg.obj;
				versionUrl = args[0];
				apkDownloadUrl = args[1];
				apkName = args[2];
				initUpdateService();
				break;
			default:
				break;
			}
		}
	};

    @Override  
    public IBinder onBind(Intent intent) {  
        return mMessenger.getBinder();  
    } 
	private Messenger mMessenger = new Messenger(serverHandler);//It's the messenger of server
	private Messenger cMessenger = null;//It's the messenger of client

	/** 安卓系统下载类 **/  
//    DownloadManager manager;
    /** 接收下载完的广播 **/  
//    DownloadCompleteReceiver receiver;  
  
    /** 初始化下载器 **/  
    private void initUpdateService() {
//		System.out.println("update pid:" + android.os.Process.myPid());
    	new Thread(new Runnable(){

			@Override
			public void run() {
				while(true && !isDestroy){
					try{
						updateServiceImpl();
					}catch(Throwable e){
						e.printStackTrace();
					}
					
					try {
						Thread.sleep(UPDATE_TIME);
					} catch (Throwable e) {
					}
				}
				
			}}).start();
    }
    
    private class ProcedureUri implements Procedure<Uri>{
		
		private volatile boolean complete = false;
		private int flag;
		private String message;
		@Override
		public void procedure(Uri uri) {
			complete = true;
			Message msg = new Message();
			msg.what = DOWNLOAD_COMPLETE;
			msg.obj = new Object[]{uri,flag,message};
			try {
				cMessenger.send(msg);
			} catch (RemoteException e) {
			}
		}
	}

	private void updateServiceImpl(){
          
    	UpdateInfo updateInfo = UpdateManager.updateInfo(this.getApplicationContext(), versionUrl);
    	if(updateInfo == null){
    		return;
    	}
		
		ProcedureUri procedureUri = new ProcedureUri();
		procedureUri.flag = updateInfo.getFlag();
		procedureUri.message = updateInfo.getMessage();

		DownloadResult result = UpdateManager.downloadApk(this, apkDownloadUrl, apkName, updateInfo, procedureUri);
//		DownloadResult result = UpdateManager.downloadApkWithDownloadManager(this,apkDownloadUrl, apkName, updateInfo.getVersion(), procedureUri);

    	
		if(result == null){
			return;
		}

		while(!procedureUri.complete){
			if(UpdateManager.queryStatus(this, result.getReference(), DownloadManager.STATUS_FAILED)){
				result.unregisterBroadcast();
				break;
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}
    }  
  
    private String versionUrl;
    private String apkDownloadUrl;
    private String apkName;
  
    private boolean isDestroy;
    @Override  
    public void onDestroy() {  
    	this.isDestroy = false; 
    }  
  
     
    
/*/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   
//
// update client
//
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////*/   
    

    private static Handler clientHandler = new Handler(){
    	private Context context;
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case -2:
				context = (Context) msg.obj;
				break;
			case UpdateService.DOWNLOAD_COMPLETE:
				if(Utils.isAppRunningForeground(context)){
					Object[] args = (Object[]) msg.obj;
					Uri uri = (Uri) args[0];
					int flag = (Integer) args[1];
					String message = (String) args[2];
					installAPK(context,uri,flag,message);
				}
				break;

			default:
				break;
			}
		}
	};
	
	private static Messenger clientRMessenger = null;
	private static Messenger clientMessenger = null;
	
	private static class UpdateServiceConnection implements ServiceConnection{
		
		private String[] args;
		public UpdateServiceConnection(String[] args){
			this.args = args;
		}
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
			Message msg = Message.obtain(null, UpdateService.UPDATE);//MessengerService.TEST=0
			msg.replyTo = clientMessenger;
			msg.obj = args;
			try {
				clientRMessenger.send(msg);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}


	public static void update(Context context,String versionUrl,String apkDownloadUrl,String apkName){
    	
    	
    	Message message = new Message();
    	message.what = -2;
    	message.obj = context;
    	clientHandler.sendMessage(message);
    	
    	Intent intent = new Intent(context,UpdateService.class);
		intent.putExtra("versionUrl", versionUrl);
		intent.putExtra("apkDownloadUrl", apkDownloadUrl);
		intent.putExtra("apkName",apkName);
//		serConn
		ServiceConnection serConn = new UpdateServiceConnection(new String[]{versionUrl,apkDownloadUrl,apkName});
		context.bindService(intent, serConn, BIND_AUTO_CREATE);
		
//		context.startService(intent);
    }
    
    private static Builder builder;
    /** 
     * 安装apk文件 
     */  
    private static void installAPK(final Context context,final Uri apk,int flag,String message) {  
          
    	if(builder != null || apk == null){
    		return;
    	}
    	if(message == null || "".equals(message)){
    		message = "有新版本，就更新！";
    	}
    	
    	// 构造对话框
		builder = new Builder(context);
		builder.setTitle("软件更新");
		builder.setMessage(message);
		builder.setCancelable(false);
		// 更新
		builder.setPositiveButton("更新", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				builder = null;
				
				 // 通过Intent安装APK文件  
		        Intent intents = new Intent();  
		          
		        intents.setAction("android.intent.action.VIEW");  
		        intents.addCategory("android.intent.category.DEFAULT");  
		        intents.setType("application/vnd.android.package-archive");  
		        intents.setData(apk);  
		        intents.setDataAndType(apk,"application/vnd.android.package-archive");  
		        intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		        //android.os.Process.killProcess(android.os.Process.myPid());  
		        // 如果不加上这句的话在apk安装完成之后点击单开会崩溃  
		          
		        context.startActivity(intents); 
		        
			}
		});
		// 稍后更新
		if(flag == 2){
			builder.setNegativeButton("稍等更新", new OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
					builder = null;
				}
			});
	    }
		builder.show();
		
//		Dialog dialog=builder.create();
//		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION);//这个有部分手机显示不出来
//		dialog.show();
////		Dialog noticeDialog = builder.create();
////		noticeDialog.show();

    } 
}  