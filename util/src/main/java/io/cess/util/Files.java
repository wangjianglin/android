package io.cess.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.StatFs;
import android.os.storage.StorageManager;

/**
 * 
 * @author lin
 * @date Jun 30, 2015 2:48:47 PM
 *
 */
public class Files {

	/**
	 * 判断指定的路径是否具有写的权限
	 * @param path
	 * @return
	 */
	public static boolean canWrite(String path){
		File file = new File(path +"/testcanwritefile-tmp."+(new Date().getTime()));
		for(long n = 0;file.exists();){
			file = new File(path +"/testcanwritefile-tmp-233449DBCE453503485ABCDdeef46."+(new Date().getTime())+n);
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
//			e.printStackTrace();
		}
		boolean r = new File(file.getAbsolutePath()).exists();
		file.delete();
		return r;
	}
	
	/**
	 * 得到所有 扩展 路径
	 * @param context
	 * @return
	 */
	public static String[] externalStorages(Context context){
		StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
		// 获取sdcard的路径：外置和内置
		List<String> list = new ArrayList<String>();
		try {
			String[] paths = (String[]) sm.getClass().getMethod("getVolumePaths").invoke(sm);
			 for(int i = 0; i < paths.length; i++)
	            {
	                String status = (String) sm.getClass().getMethod("getVolumeState", String.class).invoke(sm, paths[i]);
//	                System.out.println(paths[i]+" can write:"+canWrite(paths[i]));
	                if(status.equals(android.os.Environment.MEDIA_MOUNTED) && canWrite(paths[i]))
	                {
	                	list.add(paths[i]);
	                }
	            }
		} catch (Throwable e) {
		}
		
		return list.toArray(new String[]{});
	}
	/**
	 * 得到可用的扩展路径，外置优先
	 * @param context
	 * @return
	 */
	public static String externalStorage(Context context){
		StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
		// 获取sdcard的路径：外置和内置
//		List<String> list = new ArrayList<String>();
		String path = null;
		long size = 0;
		try {
			String[] paths = (String[]) sm.getClass().getMethod("getVolumePaths").invoke(sm);
			 for(int i = 0; i < paths.length; i++)
	            {
	                String status = (String) sm.getClass().getMethod("getVolumeState", String.class).invoke(sm, paths[i]);
//	                System.out.println(paths[i]+" can write:"+canWrite(paths[i]));
	                if(status.equals(android.os.Environment.MEDIA_MOUNTED) && canWrite(paths[i]))
	                {
//	                	list.add(paths[i]);
	                	if(path == null){
	                		path = paths[i];
	                		size = fileAvailableSize(new File(paths[i]));
	                		continue;
	                	}
	                	long tmpSize = fileAvailableSize(new File(paths[i]));
	                	if(tmpSize > 50 * 1024 * 1024 || tmpSize > size){
	                		path = paths[i];
	                		size = tmpSize;
	                	}
	                }
	            }
		} catch (Throwable e) {
		}
		
//			 System.out.println("path:"+path);
		return path;
		
		
//		if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
//			AlertDialog.Builder dlg = new AlertDialog.Builder(this.getActivity());
//	        dlg.setMessage("没有检测到SD卡，请插入SD后重试！");
//	        dlg.setTitle("提示");
//	        //Don't let alerts break the back button
//	        dlg.setCancelable(true);
//	       
//	        dlg.show();
//			return;
//		}
//		File path = Environment.getExternalStorageDirectory(); //取得sdcard文件路径
//
//		StatFs stat = new StatFs(path.getPath()); 
////		long blockSize = stat.getBlockSize(); 
//
//
//		long availableBlocks = stat.getAvailableBlocks();
//
//		long blocSize = stat.getBlockSize();
//
//		if(availableBlocks * blocSize < 1 * 1024 * 1024){
//			
//			AlertDialog.Builder dlg = new AlertDialog.Builder(this.getActivity());
//	        dlg.setMessage("SD卡存储空间不足，请有足够多空间后重试！");
//	        dlg.setTitle("提示");
//	        //Don't let alerts break the back button
//	        dlg.setCancelable(true);
//	       
//	        dlg.show();
//			return;
//		}
//
//		//(availableBlocks * blockSize)/1024      KIB 单位
//
//		//(availableBlocks * blockSize)/1024 /1024  MIB单位
//		final ProgressDialog pd = ProgressDialog.show(this.getActivity(), "分享", "正在分享中，请稍后……"); 
//		new Thread(new Runnable(){
//
//			@Override
//			public void run() {
//				shareImpl(args,pd);
//			}}).start();
//		
//	}
	}
	
	/**
	 * 得到文件夹可用大小
	 * @param path
	 * @return
	 */
	public static long fileAvailableSize(String path){
		return fileAvailableSize(new File(path));
	}
	
	/**
	 * 得到文件夹可用大小
	 * @param path
	 * @return
	 */
	@SuppressLint("NewApi")
	public static long fileAvailableSize(File path){
//		File path = Environment.getExternalStorageDirectory(); //取得sdcard文件路径
		//
				StatFs stat = new StatFs(path.getPath()); 
////				long blockSize = stat.getBlockSize(); 
		//
		//
//				@SuppressWarnings("deprecation")
//				long availableBlocks = stat.getAvailableBlocks();
				if(Build.VERSION.SDK_INT >= 19){
				long availableBlocks = stat.getAvailableBlocksLong();
		
				long blocSize = stat.getBlockSizeLong();
		//
			return availableBlocks * blocSize;
				}
				@SuppressWarnings("deprecation")
				long availableBlocks = stat.getAvailableBlocks();
				
				@SuppressWarnings("deprecation")
				long blocSize = stat.getBlockSize();
		//
			return availableBlocks * blocSize;
	}

	
	public Files(Context context,String cachePath){
		this(context,cachePath,false);
	}
	
	public Files(Context context,String cachePath,boolean isSD){
		this.cachePath = cachePath;
		if(this.cachePath == null || "".equals(this.cachePath)){
			this.cachePath = "/";
		}
		if(!this.cachePath.startsWith("/")){
			this.cachePath = "/" + this.cachePath;
		}
		if(!this.cachePath.endsWith("/")){
			this.cachePath = this.cachePath + "/";
		}
		this.context = context;
		if(isSD){
			String[] sds = externalStorages(this.context);
			if(sds == null || sds.length == 0){
				isSD = false;
			}
		}
		this.isSD = isSD;
	}

	private Context context;
	private String cachePath;
	private boolean isSD;
	public String getCachePath(){
		return cachePath;
	}
	
	@SuppressLint("DefaultLocale")
	public InputStream cache(String url){
		String fileName = cacheFile(url);
		if(isSD){
			fileName = cacheSDDataImpl(context,url);
		}
		fileName = cacheInternalDataImpl(context,url);
		if(fileName == null){
			try{
			URL uurl = new URL(url);
			HttpURLConnection conn=(HttpURLConnection)uurl.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(12000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //使用缓存
            conn.setUseCaches(true);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
//            InputStream is = conn.getInputStream();
//		InputStream _in = url.openStream();
           return conn.getInputStream();
			}catch(Throwable e){
				
			}
		}
		try {
			return new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
		}
		return null;
	}
	
	public String cacheFile(String url){
		if(isSD){
			return cacheSDDataImpl(context,url);
		}
		return cacheInternalDataImpl(context,url);
	}
	
	public boolean hasCache(String url){
		if(isSD){
			String cacheFileName = this.cachePath + "cache_"+io.cess.util.MD5.digest(url) + ".cache";;
			String fileName = isCacheFileName(cacheFileName);
			return new File(fileName).exists();
		}
		
		String path = context.getCacheDir().getAbsolutePath();
		path = path + this.cachePath;
		String fileName = "cache_"+io.cess.util.MD5.digest(url) + ".cache";
		
		fileName = path + fileName;
		return new File(fileName).exists();
//		try {
//			File dir = new File(path);
//			if(!dir.exists()){
//				dir.mkdirs();
//			}
	}

	private String cacheInternalDataImpl(Context context,String urlString){

		String path = context.getCacheDir().getAbsolutePath();
		path = path + this.cachePath;
		String fileName = "cache_"+io.cess.util.MD5.digest(urlString) + ".cache";
		
		fileName = path + fileName;
		try {
			File dir = new File(path);
			if(!dir.exists()){
				dir.mkdirs();
			}
			if(!(new File(path).exists())){//防止SD卡不能写数据
				return null;
			}
			File file = new File(fileName);
			if(file.exists()){
				return fileName;
			}
			URL url = new URL(urlString);
			InputStream _in = url.openStream();
			
			@SuppressWarnings("resource")
			FileOutputStream _out = new FileOutputStream(file);
			byte[] bs = new byte[4096];
			int count = 0;
			while((count = _in.read(bs)) != -1){
				_out.write(bs, 0, count);
			}
			return fileName;
		} catch (Throwable e) {
			new File(fileName).delete();
			}
		return null;
	}
	private String cacheSDDataImpl(Context context,String urlString){
		String cacheFileName = this.cachePath + "cache_"+io.cess.util.MD5.digest(urlString) + ".cache";;
		String fileName = isCacheFileName(cacheFileName);
		if(fileName != null){
			return fileName;
		}
		
		String path = io.cess.util.Files.externalStorage(context);
		
		if(path == null){
			return null;
		}
		
		fileName = path + cacheFileName;
		try {
//			File dir = new File(path);
//			if(!dir.exists()){
//				dir.mkdirs();
//			}
//			if(!(new File(path).exists())){//防止SD卡不能写数据
//				return null;
//			}
			File file = new File(fileName);
			if(file.exists()){
				return fileName;
			}
			if(!file.getParentFile().exists()){
				file.mkdirs();
				file = new File(fileName);
			}
			URL url = new URL(urlString);
			
			 HttpURLConnection conn=(HttpURLConnection)url.openConnection();
	            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
	            conn.setConnectTimeout(12000);
	            //连接设置获得数据流
	            conn.setDoInput(true);
	            //使用缓存
	            conn.setUseCaches(true);
	            //这句可有可无，没有影响
	            //conn.connect();
	            //得到数据流
//	            InputStream is = conn.getInputStream();
//			InputStream _in = url.openStream();
	           InputStream _in = conn.getInputStream();
			
			@SuppressWarnings("resource")
			FileOutputStream _out = new FileOutputStream(file);
			byte[] bs = new byte[4096];
			int count = 0;
			while((count = _in.read(bs)) != -1){
				_out.write(bs, 0, count);
			}
			return fileName;
		} catch (Throwable e) {
			new File(fileName).delete();
			}
		return null;
	}

	private String isCacheFileName(String cacheFileName){

		String[] paths = io.cess.util.Files.externalStorages(this.context);
		String fileName = null;
		for(String path : paths){
			fileName = path + cacheFileName;
			if((new File(fileName)).exists()){
				return fileName;
			}
		}
		return null;
	}
}
