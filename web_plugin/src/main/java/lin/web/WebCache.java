package lin.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.webkit.WebResourceResponse;

public class WebCache {

	@SuppressLint("DefaultLocale")
	public static WebResourceResponse cache(Context context,String url){
		String lurl = url.toLowerCase();
	    if (lurl.endsWith(".jpg") || lurl.endsWith(".gif") || lurl.endsWith(".png") || lurl.endsWith(".bmp")) {
//	    	return cacheDataAsyn(context,url);
	    	return cacheData(context,url);
	    }
	    return null;
	}
	
	public static String cacheFileName(Context context,String urlString){
		cacheData(context,urlString);
		String[] paths = lin.util.Files.externalStorages(context);
		
		for(String path : paths){
			String fileName = "images"+lin.util.MD5.digest(urlString) + ".cache";
			fileName = path + "/buyers/" + fileName;
			if((new File(fileName)).exists()){
				return fileName;
			}
		}
		return null;
	}
	
	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 3, 10,
			TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(3000),
			new ThreadPoolExecutor.CallerRunsPolicy());
	
	
	private static WebResourceResponse cacheDataAsyn(final Context context,final String urlString){
		WebResourceResponse response = null;
		try {
			final PipedOutputStream out = new PipedOutputStream();
			PipedInputStream in = new PipedInputStream(out,600 * 1024);
			response = new WebResourceResponse("image/png", "UTF-8", in);
			
			executor.execute(new Runnable(){

				@Override
				public void run() {
					InputStream _in  = cacheDateImpl(context,urlString);
					
					if(_in == null){
						try {
							out.close();
						} catch (IOException e) {
						}
						return;
					}
					
					byte[] bs = new byte[4096];
					int count = -1;
					try {
						while((count = _in.read(bs)) != -1){
							out.flush();
							out.write(bs, 0, count);
						}

						out.close();
					} catch (IOException e) {
					}
				}});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@SuppressLint("WorldWriteableFiles")
	private static WebResourceResponse cacheData(Context context,String urlString){
		InputStream _in = cacheDateImpl(context,urlString);
		if(_in != null){
			return new WebResourceResponse("image/*","UTF-8",_in);
		}
		return null;
	}
	
	private static InputStream cacheDateImpl(Context context,String urlString){
		String path = lin.util.Files.externalStorage(context);

		if(path == null){
			return null;
		}
		String fileName = "images"+lin.util.MD5.digest(urlString) + ".cache";
		
		fileName = path + "/buyers/" + fileName;
		try {
			File dir = new File(path + "/buyers/");
			if(!dir.exists()){
				dir.mkdirs();
			}
			if(!(new File(path + "/buyers/").exists())){//防止SD卡不能写数据
				return null;
			}
			InputStream _in = null;
			try{
				//_in = context.openFileInput(fileName);
				Bitmap oriBmp = BitmapFactory.decodeFile(fileName);
				if(oriBmp != null){
					_in = new FileInputStream(new File(fileName));
				}
			}catch(Throwable e){
//				e.printStackTrace();
			}
			
			if(_in != null){
				return _in;
			}
			URL url = new URL(urlString);
			_in = url.openStream();
			
			@SuppressWarnings("resource")
			FileOutputStream _out = new FileOutputStream(new File(fileName));
			byte[] bs = new byte[4096];
			int count = 0;
			while((count = _in.read(bs)) != -1){
				_out.write(bs, 0, count);
			}
			_in = new FileInputStream(new File(fileName));
			return _in;
		} catch (Throwable e) {
			new File(fileName).delete();
			}
		return null;
	}
	
//	private static String md5(String url){
//		MessageDigest md5 = null;
//		try {
//			md5 = MessageDigest.getInstance("MD5");
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}  
//	    byte[] bytes = md5.digest(url.getBytes());  
//	    StringBuffer stringBuffer = new StringBuffer();  
//	    for (byte b : bytes){  
//	        int bt = b&0xff;  
//	        if (bt < 16){  
//	            stringBuffer.append(0);  
//	        }   
//	        stringBuffer.append(Integer.toHexString(bt));  
//	    }  
//	    return stringBuffer.toString();  
//	}
}
