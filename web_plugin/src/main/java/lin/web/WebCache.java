package lin.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.webkit.WebResourceResponse;

import lin.util.ThreadPool;
import lin.util.thread.AutoResetEvent;

public class WebCache {

	@SuppressLint("DefaultLocale")
	public static InputStream cache(Context context,String url){
		String lurl = url.toLowerCase();
	    if (lurl.endsWith(".jpg") || lurl.endsWith(".gif") || lurl.endsWith(".png") || lurl.endsWith(".bmp")) {
			return asynCacheFile(context, url);
//			return cacheDataAsyn2(context, url);
//	    	return cacheData(context,url);
	    }
	    return null;
	}

	/**
	 * 先会判断是否有缓存，没有则会缓存文件
	 * @param context
	 * @param urlString
	 * @return
	 */
	public static String cacheFileName(Context context,String urlString) {

		String fileName = existCacheFileName(context,urlString);
		if(fileName != null){
			return fileName;
		}

		return cacheFileImplReturnFileName(context,urlString);

//		cacheData(context,urlString);
//
//		return existCacheFileName(context,urlString);
//		String[] paths = lin.util.Files.externalStorages(context);

//		for(String path : paths){
//			String fileName = "images"+lin.util.MD5.digest(urlString) + ".cache";
//			fileName = path + "/buyers/" + fileName;
	}

	/**
	 * 先会判断是否有缓存，没有则不会缓存文件
	 * @param context
	 * @param urlString
	 * @return
	 */
	public static String existCacheFileName(Context context,String urlString){
		File path = context.getExternalCacheDir();
		String fileNameMD5 = "images" + lin.util.MD5.digest(urlString) + ".cache";

		String fileName = path.getAbsolutePath() + "/imagecache/" + fileNameMD5;

		File file = new File(fileName);
		if(file.exists()){
			file.setLastModified(new Date().getTime());
			return fileName;
		}

		path = context.getCacheDir();

		fileName = path.getAbsolutePath() + "/imagecache/" + fileNameMD5;

		file = new File(fileName);
		if(file.exists()){
			file.setLastModified(new Date().getTime());
			return fileName;
		}

		return null;
	}

	private static  InputStream asynCacheFile2(final Context context,final String urlString) {
		String cFileName = existCacheFileName(context, urlString);
		if (cFileName != null) {
			try {
				//return new WebResourceResponse("images/*", "UTF-8", new FileInputStream(cFileName));
				return new FileInputStream(cFileName);
			} catch (FileNotFoundException e) {

			}
		}
		executor.execute(new Runnable() {
			@Override
			public void run() {
				cacheFileImplReturnFileName(context, urlString);
			}
		});
		return null;
	}

//	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 12, 10,
//			TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(3000),
//			new ThreadPoolExecutor.CallerRunsPolicy());
	private static ThreadPool executor = new ThreadPool(6,6,3000);
	
	private static class Datac{private int count=0;private int preOffset=0;}
	private static InputStream asynCacheFile(final Context context,final String urlString){

//		String cFileName = existCacheFileName(context, urlString);
//		if(cFileName != null){
//			try {
//				return  new FileInputStream(cFileName);
//			} catch (FileNotFoundException e) {
//
//			}
//		}



		final byte[] bs = new byte[4096];

		final AutoResetEvent inSet = new AutoResetEvent();//通知in
		final AutoResetEvent outSet = new AutoResetEvent();//通知out

		final Datac d = new Datac();

		InputStream in = new InputStream() {
			@Override
			public int read() throws IOException {
				inSet.waitOne();
				return 0;
			}
			public int available() throws IOException {
				inSet.waitOne();
				return d.count;
			}

			public int read(byte[] buffer) throws IOException {
				return read(buffer, 0, buffer.length);
			}

			@Override
			public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
				//super.read(buffer, byteOffset, byteCount);
//					synchronized (context){
//						System.out.println("offset:"+byteOffset+"\tbyte count:"+byteCount+"\tcount:"+d.count);
//					}
				inSet.waitOne();
				if(d.count == -1){
					return -1;
				}
				int n = 0;
				for(;n<byteCount&n<d.count;n++){
					buffer[byteOffset + n] = bs[d.preOffset + n];
				}

				if(n + d.preOffset == d.count) {
					d.preOffset = 0;
					inSet.reset();
					outSet.set();
				}else{
					d.preOffset += n;
				}
				return n;
			}
		};

		final OutputStream out = new OutputStream() {
			@Override
			public void write(int oneByte) throws IOException {

			}

			public void write(byte[] buffer, int offset, int count) throws IOException {
				d.count = count;
				inSet.set();
				outSet.waitOne();
				outSet.reset();
			}

			@Override
			public void close() throws IOException {
				d.count = -1;
				inSet.set();
			}
		};
//			response = new WebResourceResponse("images/*", "UTF-8", in);

		executor.execute(new Runnable(){

			@Override
			public void run() {

				InputStream _in = cacheFileImplCheckAndReturnInputStream(context, urlString);
//					InputStream _in = cacheFileImplReturnInputStream(context, urlString);

				if(_in == null){
					try {
						out.close();
					} catch (IOException e) {
					}
					return;
				}


				int count = -1;
				try {
					while((count = _in.read(bs)) != -1){
						out.write(bs, 0, count);
					}
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}});

		return in;
	}


//	@SuppressLint("WorldWriteableFiles")
//	private static WebResourceResponse cacheFileImpl(Context context,String urlString){
//		InputStream _in = cacheFileImplReturnInputStream(context, urlString);
//		if(_in != null){
//			return new WebResourceResponse("image/*","UTF-8",_in);
//		}
//		return null;
//	}


	private static InputStream cacheFileImplReturnInputStream(Context context,String urlString){
		try{
			return new FileInputStream(cacheFileImplReturnFileName(context,urlString));
		}catch (Throwable e){

		}
		return null;
	}

	private static InputStream cacheFileImplCheckAndReturnInputStream(Context context,String urlString){
		try{
			return new FileInputStream(cacheFileImplCheckAndReturnFileName(context,urlString));
		}catch (Throwable e){

		}
		return null;
	}

	private static String cacheFileImplCheckAndReturnFileName(Context context,String urlString){
		String fileName = existCacheFileName(context, urlString);
		if(fileName != null){
			return fileName;
		}
		return cacheFileImplReturnFileName(context,urlString);
	}
	private static String cacheFileImplReturnFileName(Context context,String urlString){

		boolean isExternal = true;
		File path = context.getExternalCacheDir();
		if (path == null) {
			isExternal = false;
			path = context.getCacheDir();
		}

		if (path == null) {
			return null;
		}
		String fileNameMD5 = "images" + lin.util.MD5.digest(urlString) + ".cache";

		String fileName = path.getAbsolutePath() + "/imagecache/" + fileNameMD5;

		try {
			writeFileToLocal(context, path, fileName, urlString);
			return fileName;
		}catch (Throwable e) {
			try {
				new File(fileName).delete();
			}catch (Throwable e2){}
		}

		if(!isExternal){
			return null;
		}
		try{
			path = context.getCacheDir();

			fileName = path.getAbsolutePath() + "/imagecache/" + fileNameMD5;

			writeFileToLocal(context, path, fileName, urlString);

			return fileName;
		}catch (Throwable e){
			try {
				new File(fileName).delete();
			}catch (Throwable e2){}
		}
		return null;
	}

	private static void writeFileToLocal(Context context,File path,String fileName,String urlString)throws Throwable{
//		try {
		File dir = new File(path + "/imagecache/");
		if(!dir.exists()){
			dir.mkdirs();
		}
		if(!(new File(path + "/imagecache/").exists())){//防止SD卡不能写数据
//				if(isExternal){
//					path = context.getCacheDir();
//				}else{
//
//				}
			return;
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
			return;
		}
		URL url = new URL(urlString);
		_in = url.openStream();

		FileOutputStream _out = new FileOutputStream(new File(fileName));

		addCacheFileNumber(dir);

		byte[] bs = new byte[4096];
		int count = 0;
		while((count = _in.read(bs)) != -1){
			_out.write(bs, 0, count);
		}

		_out.close();
	}


	private static int preSize = 0;
	private static File prePath = null;
	private static Object lock = new Object();

	private static void clearCacheFiles(File path){
		File[] files = path.listFiles();
		File tmp = null;
		int m = 0;
		for(int n=0;n<files.length * 2 / 3;n++){

			for(m = 0;m<files.length-n-1;m++){
				if(files[m].lastModified() < files[m+1].lastModified()){
					tmp = files[m];
					files[m] = files[m+1];
					files[m+1] = tmp;
				}
			}
			synchronized (lock) {
				if(prePath == null || !path.getAbsoluteFile().equals(prePath)){
					return;
				}
				if(files[m].delete()) {
					preSize--;
				}
			}
			try{
				Thread.sleep(10);
			}catch (Throwable e){}
		}
	}

	private static void addCacheFileNumber(final File path){
		if(path == null || !path.exists()){
			return;
		}
		synchronized (lock) {
			if (prePath != null && prePath.getAbsoluteFile().equals(path.getAbsoluteFile())) {
				preSize++;
				if (preSize > 60000) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							try{
								clearCacheFiles(path);
							}catch (Throwable e){}
						}
					}).start();

				}
				return;
			}
			prePath = path;
			preSize = prePath.listFiles().length;
		}

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
