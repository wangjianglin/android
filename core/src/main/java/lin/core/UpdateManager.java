package lin.core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import lin.client.http.*;
import lin.client.http.Error;
import lin.util.Procedure;

/**
 * 
 * @author lin
 * @date Aug 20, 2015 5:30:56 PM
 *
 */
public class UpdateManager {

	
//	private int flag = 0;//0不需要更新，1、必须更新，2、非必须更新
//	private UpdateInfo updateInfo;
	public static UpdateInfo updateInfo(Context context,String versionUrl){
		try {
//			URL url = new URL("https://www.feicuibaba.com/buyers/buyers.php?version=true&android=true");
			URL url = new URL(versionUrl);
			InputStream _in = url.openConnection().getInputStream();
			ByteArrayOutputStream _out = new ByteArrayOutputStream();
			byte[] bs = new byte[1024];
			int count = 0;
			while((count = _in.read(bs)) != -1){
				_out.write(bs, 0, count);
			}
			final String updateInfoString = _out.toString();
			UpdateInfo updateInfo = parseUpdateInfo(updateInfoString);
			int[] updateVersions = updateInfo.versions;
			PackageManager packageManager = context.getPackageManager();

			updateInfo.appVersion = packageManager.getPackageInfo(context.getPackageName(), 0).versionName;
			int[] appVersions = parseVersion(updateInfo.appVersion);
			updateInfo.appVersions = appVersions;

			if(updateVersions == null || appVersions == null){
				return null;
			}
			
	        
			int flag = 0;
	        if (updateVersions[0] > appVersions[0]) {
	            flag = 1;
	        }else if(updateVersions[0] == appVersions[0]){
	            if (updateVersions[1] > appVersions[1]) {
	                flag = 1;
	            }else if(updateVersions[1] == appVersions[1]){
	                if (updateVersions[2] > appVersions[2]) {
	                    flag = 2;
	                }
	            }
	        }
	        updateInfo.flag = flag;
	        if (flag == 0) {
	            return null;
	        }
	        final SharedPreferences preference = context.getSharedPreferences("", 0);
	        if (flag == 2 && preference.getBoolean("isupdateofversion", false) && preference.getString("isupdateofversion_version","").equals(updateInfo.version)) {
	            return null;
	        }

	        return updateInfo;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static UpdateInfo parseUpdateInfo(String info){
		UpdateInfo updateInfo = lin.util.JsonUtil.deserialize(info, UpdateInfo.class);
		updateInfo.versions = parseVersion(updateInfo.version);
		return updateInfo;
	}
	private static int[] parseVersion(String v){
		if(v == null || "".equals(v)){
			return null;
		}
		int[] r = new int[3];
		String[] vs = v.split("\\.");
		for(int n=0;n<vs.length && n<3;n++){
			r[n] = Integer.parseInt(vs[n]);
		}
		return r;
	}
	
	public static class UpdateInfo{
		private int flag;
		private String version;
		private int[] versions;
		private String appVersion;
		private int[] appVersions;
		private String message;
		public String getVersion() {
			return version;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public int getFlag() {
			return flag;
		}
		public int[] getVersions() {
			return versions;
		}
		public void setVersion(String version) {
			this.version = version;
		}
	}

	// 接受下载完成后的intent  
	static class DownloadCompleteReceiver extends BroadcastReceiver {  
          
    	private Procedure<Uri> callback;
    	private DownloadManager manager;
    	private long reference;
    	private DownloadCompleteReceiver(DownloadManager manager,long reference,Procedure<Uri> callback){
    		this.callback = callback;
    		this.manager = manager;
    		this.reference = reference;
    	}
        @Override  
        public void onReceive(Context context, Intent intent) {  
  
        	long downId = intent.getLongExtra(  
                    DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        	if(reference != downId){
        		return;
        	}
        	Uri uri =  null;
            //判断是否下载完成的广播  
            if (intent.getAction().equals(  
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

				//获取下载的文件id
//                long downId = intent.getLongExtra(  
//                        DownloadManager.EXTRA_DOWNLOAD_ID, -1);  
//                  System.out.println("ok.");
				uri = manager.getUriForDownloadedFile(downId);

				if (callback != null) {
					callback.procedure(uri);
				} else {
					callback.procedure(null);
				}
				context.unregisterReceiver(this);
			}
//                cM
//                Message message = new Message();
//                message.what = DOWNLOAD_COMPLETE;
//                message.obj = downId;
////				cMessenger.send(message);
                //自动安装apk  
//                installAPK(manager.getUriForDownloadedFile(downId));  
//                  
//                //停止服务并关闭广播  
//                UpdateService.this.stopSelf();  
  
        }  
    }
	
	public static class DownloadResult{
		private long reference;
		private BroadcastReceiver broadcastReceiver;
		private Context context;
		public void unregisterBroadcast(){
			if(broadcastReceiver != null && context != null){
				context.unregisterReceiver(broadcastReceiver);
			}
		}
		public long getReference() {
			return reference;
		}
	}

	private static boolean isComplate(Context context,String apkFilename,UpdateInfo updateInfo){
		boolean result = false;
		try {
			PackageManager pm = context.getPackageManager();
//			Log.e("archiveFilePath", filePath);
			PackageInfo info = pm.getPackageArchiveInfo(apkFilename,
					PackageManager.GET_ACTIVITIES);
			String packageName = info.packageName;
			String version = info.versionName;
			if (packageName.equals(context.getPackageName()) && version.equals(updateInfo.version)) {
				result = true;
			}
		} catch (Throwable e) {
			result = false;
			//Log.e(TAG,*****  解析未安装的 apk 出现异常 *****+e.getMessage,e);
		}
		return result;
	}

	public static DownloadResult downloadApk(Context context,String apkDownloadUrl,String apkName,UpdateInfo updateInfo,Procedure<Uri> callback){

		File file = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + "/apks");

		if (file == null) {
			file = new File(context.getCacheDir() + "/" + Environment.DIRECTORY_DOWNLOADS + "/apks");
		}
		File apkFile = new File(file.getAbsoluteFile() + "/" + apkName + updateInfo.version);
		if(apkFile.exists()){
			if(isComplate(context,apkFile.getAbsolutePath(),updateInfo)) {
				if (callback != null) {
					callback.procedure(Uri.fromFile(apkFile));
				}
				return null;
			}
			apkFile.delete();
		}
		file.deleteOnExit();
		file.mkdirs();

		HttpCommunicate.init(context);

		FileInfo downloadFileInfo = (FileInfo)HttpCommunicate.download(apkDownloadUrl,null).getResult();

		if (downloadFileInfo != null && downloadFileInfo.getFile() != null){
			File downloadFile = downloadFileInfo.getFile();
			downloadFile.renameTo(apkFile);
			if(isComplate(context,apkFile.getAbsolutePath(),updateInfo)) {
				callback.procedure(Uri.fromFile(apkFile));
			}else{
				apkFile.delete();
			}
		}

//		try {
//			URL url = new URL(apkDownloadUrl);
//			// 创建连接
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			conn.setInstanceFollowRedirects(true);
//
//			conn.connect();
//
//			// 获取文件大小
//			int length = conn.getContentLength();
//			// 创建输入流
//			InputStream is = conn.getInputStream();
//
//			FileOutputStream fos = new FileOutputStream(apkFile);
//			//int count = 0;
//			// 缓存
//			byte buf[] = new byte[4096];
//			// 写入到文件中
//
//			int numread = 0;
//
//			while ((numread = is.read(buf)) != -1) {
//				fos.write(buf, 0, numread);
//			}
//
//			fos.close();
//
//			if(isComplate(context,apkFile.getAbsolutePath(),updateInfo)) {
//				callback.procedure(Uri.fromFile(apkFile));
//			}else{
//				apkFile.delete();
//			}
//
//		} catch (Throwable e) {
////			e.printStackTrace();
//			try {
//				apkFile.delete();
//			}catch (Throwable e2) {}
//		}

		return null;
	}
	public static DownloadResult downloadApkWithDownloadManager(Context context,String apkDownloadUrl,String apkName,String version,Procedure<Uri> callback){
		
		
		 File file = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + "/apks");
		 
         if (file == null) {
             file = new File(context.getCacheDir() + "/" + Environment.DIRECTORY_DOWNLOADS + "/apks");
         }
         File apkFile = new File(file.getAbsoluteFile() + "/" + apkName + version);
         if(apkFile.exists()){
        	 if(callback != null){
        		 callback.procedure(Uri.fromFile(apkFile));
        	 }
        	 return null; 
         }
         file.deleteOnExit();
        	 file.mkdirs();
         
		DownloadManager manager = (DownloadManager) context.getSystemService(Service.DOWNLOAD_SERVICE);  
        
  
        //设置下载地址  
//        DownloadManager.Request down = new DownloadManager.Request(  
//                Uri.parse("http://gdown.baidu.com/data/wisegame/fd84b7f6746f0b18/baiduyinyue_4802.apk"));  
        DownloadManager.Request down = new DownloadManager.Request(Uri.parse(apkDownloadUrl));  
          
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以  
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE  
                | DownloadManager.Request.NETWORK_WIFI);  
          
        // 下载时，通知栏显示途中  
//        down.setNotificationVisibility(Request.VISIBILITY_VISIBLE);  
//          
//        // 显示下载界面  
//        down.setVisibleInDownloadsUi(true);  
          
        // 设置下载后文件存放的位置  
//        down.setDestinationInExternalFilesDir(context,  
//                Environment.DIRECTORY_DOWNLOADS, apkName);
        down.setDestinationUri(Uri.fromFile(apkFile));
          
        // 将下载请求放入队列  
        long reference = manager.enqueue(down);  
          
//        
        
        //注册下载广播  

		DownloadCompleteReceiver receiver = new DownloadCompleteReceiver(manager,reference,callback);  
		
        context.registerReceiver(receiver, new IntentFilter(  
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));  
//        context.registerReceiver(receiver, null);  
        DownloadResult result = new DownloadResult();
        result.context = context;
        result.broadcastReceiver = receiver;
        result.reference = reference;
        return result;
	}
	
	public static boolean queryStatus(Context context,long reference,int status){
		DownloadManager manager = (DownloadManager) context.getSystemService(Service.DOWNLOAD_SERVICE);  
		Cursor cursor = manager.query(new Query().setFilterById(reference).setFilterByStatus(status));
		return cursor.getCount() > 0;
	}
}


//
//
//package lin.core;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.app.AlertDialog.Builder;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.DialogInterface.OnClickListener;
//import android.content.pm.PackageManager;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.net.Uri;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.view.LayoutInflater;
//import android.widget.ProgressBar;
//
//
///**
// * 
// * @author lin
// * @date Aug 20, 2015 5:30:56 PM
// *
// */
//public class UpdateManager {
//	/* 下载中 */
//	private static final int DOWNLOAD = 1;
//	/* 下载结束 */
//	private static final int DOWNLOAD_FINISH = 2;
//	/* 保存解析的XML信息 */
////	HashMap<String, String> mHashMap;
//	/* 下载保存路径 */
//	private String mSavePath;
//	/* 记录进度条数量 */
//	private int progress;
//	/* 是否取消更新 */
//	private boolean cancelUpdate = false;
//
//	private Context context;
//	/* 更新进度条 */
//	private ProgressBar mProgress;
//	private Dialog mDownloadDialog;
//	
//	
//	private String versionUrl;
//	private String downloadUrl;
//	private String apkFileName;
//
//	private Handler mHandler = new Handler()
//	{
//		public void handleMessage(Message msg)
//		{
//			switch (msg.what)
//			{
//			// 正在下载
//			case DOWNLOAD:
//				// 设置进度条位置
//				mProgress.setProgress(progress);
//				break;
//			case DOWNLOAD_FINISH:
//				// 安装文件
//				installApk();
//				break;
//			default:
//				break;
//			}
//		};
//	};
//
//	public UpdateManager(Context context,String versionUrl,String downloadUrl)
//	{
//		this.context = context;
//		this.versionUrl = versionUrl;
//		this.downloadUrl = downloadUrl;
//		this.apkFileName = "";
//	}
//	
//	public void update(){
//		new Thread(new Runnable(){
//
//			@Override
//			public void run() {
//				updateImpl();
//			}});
//	}
//	private int flag = 0;//0不需要更新，1、必须更新，2、非必须更新
//	private UpdateInfo updateInfo;
//	private void updateImpl(){
//		try {
////			URL url = new URL("https://www.feicuibaba.com/buyers/buyers.php?version=true&android=true");
//			URL url = new URL(versionUrl);
//			InputStream _in = url.openConnection().getInputStream();
//			ByteArrayOutputStream _out = new ByteArrayOutputStream();
//			byte[] bs = new byte[1024];
//			int count = 0;
//			while((count = _in.read(bs)) != -1){
//				_out.write(bs, 0, count);
//			}
//			final String updateInfoString = _out.toString();
//			updateInfo = parseUpdateInfo(updateInfoString);
//			int[] updateVersions = updateInfo.versions;
//			PackageManager packageManager = context.getPackageManager();
//			int[] appVersions = parseVersion(packageManager.getPackageInfo(context.getPackageName(), 0).versionName);
//			if(updateVersions == null || appVersions == null){
//				return;
//			}
//			
//	        
//
//	        if (updateVersions[0] > appVersions[0]) {
//	            flag = 1;
//	        }else if(updateVersions[0] == appVersions[0]){
//	            if (updateVersions[1] > appVersions[1]) {
//	                flag = 1;
//	            }else if(updateVersions[1] == appVersions[1]){
//	                if (updateVersions[2] > appVersions[2]) {
//	                    flag = 2;
//	                }
//	            }
//	        }
//
//	        if (flag == 0) {
//	            return;
//	        }
//	        final SharedPreferences preference = context.getSharedPreferences("", 0);
//	        if (flag == 2 && preference.getBoolean("isupdateofversion", false) && preference.getString("isupdateofversion_version","").equals(updateInfo.version)) {
//	            return;
//	        }
//	        
////	        if (flag == 1) {
////	            updateString = "有新版本，为了正常使用，请更新！";
////	        }else{
////	            updateString = "有新版本，是否需要更新！";
////	        }
//	        
//	        mHandler.post(new Runnable(){
//
//				@Override
//				public void run() {
//			        showDownloadDialog();
//				}});
//	        
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (NameNotFoundException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	private UpdateInfo parseUpdateInfo(String info){
//		UpdateInfo updateInfo = lin.util.JsonUtil.deserialize(info, UpdateInfo.class);
//		updateInfo.versions = parseVersion(updateInfo.version);
//		return updateInfo;
//	}
//	private int[] parseVersion(String v){
//		if(v == null || "".equals(v)){
//			return null;
//		}
//		int[] r = new int[3];
//		String[] vs = v.split("\\.");
//		for(int n=0;n<vs.length && n<3;n++){
//			r[n] = Int.parseInt(vs[n]);
//		}
//		return r;
//	}
//	
//	public class UpdateInfo{
//		private String version;
//		private int[] versions;
//		private String message;
//		public String getVersion() {
//			return version;
//		}
//		public void setVersion(String version) {
//			this.version = version;
//		}
//		public String getMessage() {
//			return message;
//		}
//		public void setMessage(String message) {
//			this.message = message;
//		}
//		
//	}
//
//	/**
//	 * 检测软件更新
//	 */
////	public void checkUpdate()
////	{
////		if (isUpdate())
////		{
////			// 显示提示对话框
////			showNoticeDialog();
////		} else
////		{
////			Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_LONG).show();
////		}
////	}
//
////	/**
////	 * 检查软件是否有更新版本
////	 * 
////	 * @return
////	 */
////	private boolean isUpdate()
////	{
////		// 获取当前软件版本
////		int versionCode = getVersionCode(mContext);
////		// 把version.xml放到网络上，然后获取文件信息
////		InputStream inStream = ParseXmlService.class.getClassLoader().getResourceAsStream("version.xml");
////		// 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
////		ParseXmlService service = new ParseXmlService();
////		try
////		{
////			mHashMap = service.parseXml(inStream);
////		} catch (Exception e)
////		{
////			e.printStackTrace();
////		}
////		if (null != mHashMap)
////		{
////			int serviceCode = Int.valueOf(mHashMap.get("version"));
////			// 版本判断
////			if (serviceCode > versionCode)
////			{
////				return true;
////			}
////		}
////		return false;
////	}
//
////	/**
////	 * 获取软件版本号
////	 * 
////	 * @param context
////	 * @return
////	 */
////	private int getVersionCode(Context context)
////	{
////		int versionCode = 0;
////		try
////		{
////			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
////			versionCode = context.getPackageManager().getPackageInfo("com.szy.update", 0).versionCode;
////		} catch (NameNotFoundException e)
////		{
////			e.printStackTrace();
////		}
////		return versionCode;
////	}
//
//	/**
//	 * 显示软件更新对话框
//	 */
//	private void showNoticeDialog()
//	{
//		// 构造对话框
//		AlertDialog.Builder builder = new Builder(context);
//		builder.setTitle("R.string.soft_update_title");
//		builder.setMessage("R.string.soft_update_info");
//		// 更新
//		builder.setPositiveButton("R.string.soft_update_updatebtn", new OnClickListener()
//		{
//			@Override
//			public void onClick(DialogInterface dialog, int which)
//			{
//				dialog.dismiss();
//				// 显示下载对话框
//				showDownloadDialog();
//			}
//		});
//		// 稍后更新
//		builder.setNegativeButton("R.string.soft_update_later", new OnClickListener()
//		{
//			@Override
//			public void onClick(DialogInterface dialog, int which)
//			{
//				dialog.dismiss();
//			}
//		});
//		Dialog noticeDialog = builder.create();
//		noticeDialog.show();
//	}
//
//	/**
//	 * 显示软件下载对话框
//	 */
//	private void showDownloadDialog()
//	{
//		// 构造软件下载对话框
//		AlertDialog.Builder builder = new Builder(context);
//		//builder.setTitle(R.string.soft_updating);
//		// 给下载对话框增加进度条
//		final LayoutInflater inflater = LayoutInflater.from(context);
////		View v = inflater.inflate(R.layout.softupdate_progress, null);
////		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
////		builder.setView(v);
//		// 取消更新
//		builder.setNegativeButton("R.string.soft_update_cancel", new OnClickListener()
//		{
//			@Override
//			public void onClick(DialogInterface dialog, int which)
//			{
//				dialog.dismiss();
//				// 设置取消状态
//				cancelUpdate = true;
//			}
//		});
//		mDownloadDialog = builder.create();
//		mDownloadDialog.show();
//		// 现在文件
//		downloadApk();
//	}
//
//	/**
//	 * 下载apk文件
//	 */
//	private void downloadApk()
//	{
//		// 启动新线程下载软件
//		new downloadApkThread().start();
//	}
//
//	/**
//	 * 下载文件线程
//	 * 
//	 * @author coolszy
//	 *@date 2012-4-26
//	 *@blog http://blog.92coding.com
//	 */
//	private class downloadApkThread extends Thread
//	{
//		@Override
//		public void run()
//		{
//			try
//			{
//				// 判断SD卡是否存在，并且是否具有读写权限
//				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
//				{
//					// 获得存储卡的路径
//					String sdpath = Environment.getExternalStorageDirectory() + "/";
//					mSavePath = sdpath + "download";
//					URL url = new URL(downloadUrl);
//					// 创建连接
//					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//					conn.connect();
//					// 获取文件大小
//					int length = conn.getContentLength();
//					// 创建输入流
//					InputStream is = conn.getInputStream();
//
//					File file = new File(mSavePath);
//					// 判断文件目录是否存在
//					if (!file.exists())
//					{
//						file.mkdir();
//					}
//					File apkFile = new File(mSavePath, apkFileName);
//					FileOutputStream fos = new FileOutputStream(apkFile);
//					int count = 0;
//					// 缓存
//					byte buf[] = new byte[1024];
//					// 写入到文件中
//					do
//					{
//						int numread = is.read(buf);
//						count += numread;
//						// 计算进度条位置
//						progress = (int) (((float) count / length) * 100);
//						// 更新进度
//						mHandler.sendEmptyMessage(DOWNLOAD);
//						if (numread <= 0)
//						{
//							// 下载完成
//							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
//							break;
//						}
//						// 写入文件
//						fos.write(buf, 0, numread);
//					} while (!cancelUpdate);// 点击取消就停止下载.
//					fos.close();
//					is.close();
//				}
//			} catch (MalformedURLException e)
//			{
//				e.printStackTrace();
//			} catch (IOException e)
//			{
//				e.printStackTrace();
//			}
//			// 取消下载对话框显示
//			mDownloadDialog.dismiss();
//		}
//	};
//
//	/**
//	 * 安装APK文件
//	 */
//	private void installApk()
//	{
//		File apkfile = new File(mSavePath, apkFileName);
//		if (!apkfile.exists())
//		{
//			return;
//		}
//		// 通过Intent安装APK文件
//		Intent i = new Intent(Intent.ACTION_VIEW);
//		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
//		context.startActivity(i);
//	}
//}

