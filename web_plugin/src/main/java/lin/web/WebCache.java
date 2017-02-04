package lin.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import lin.comm.http.FileInfo;
import lin.comm.http.HttpCommunicate;
import lin.comm.httpdns.HttpDNS;
import lin.util.ThreadPool;
import lin.util.thread.AutoResetEvent;

public class WebCache {

	private static String backHost;
	private static String cachePath = "/imagecache/";

	public static void setCachePath(String path){
		cachePath = "/"+path+"/";
	}

	private static HttpDNS _httpDNS;

	public static void setHttpDNS(HttpDNS httpDNS){
		_httpDNS = httpDNS;
	}

//	private static HttpDNS httpDNS;

	public static void setBackHost(String host){
		backHost = host;
	}
	private static final int HTTP_TIMEOUT = 8000;
	@SuppressLint("DefaultLocale")
	public static InputStream cache(Context context,String url){
		if(url == null){
			return null;
		}
		String lurl = url.toLowerCase();
	    if ((lurl.startsWith("http://") || lurl.startsWith("https://"))
				&& (lurl.endsWith(".jpg") || lurl.endsWith(".gif") || lurl.endsWith(".png") || lurl.endsWith(".bmp"))) {

			return asynCacheFile(context, url);

//			return cacheDataAsyn2(context, url);
//	    	return cacheData(context,url);
	    }
	    return null;
	}

	private static String getBackUrl(String url){
		try {
			URL u = new URL(url);
			return url.replaceFirst(u.getHost(),backHost);
		}catch (Throwable e){}
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
	public static String existCacheFileName(Context context,String urlString) {
		String fileName = existCacheFileNameimpl(context,urlString);
		if(fileName == null){
			fileName = existCacheFileNameimpl(context,getBackUrl(urlString));
		}
		return fileName;
//		String backUrl = getBackUrl(url);
//		if(backUrl != null) {
//			String filename = existCacheFileName(context,backUrl);
//			if(filename != null){
//				try {
//					return new FileInputStream(filename);
//				} catch (FileNotFoundException e) {
//				}
//			}
//		}
	}

	private static String existCacheFileNameimpl(Context context,String urlString){
		if(urlString == null){
			return null;
		}
		File path = context.getExternalCacheDir();
		String fileNameMD5 = "images" + lin.util.MD5.digest(urlString) + ".cache";

		String fileName = path.getAbsolutePath() + cachePath + fileNameMD5;

		File file = new File(fileName);
		if(file.exists()){
			file.setLastModified(new Date().getTime());
			return fileName;
		}

		path = context.getCacheDir();

		fileName = path.getAbsolutePath() + cachePath + fileNameMD5;

		file = new File(fileName);
		if(file.exists()){
			file.setLastModified(new Date().getTime());
			return fileName;
		}

		return null;
	}

//	private static  InputStream asynCacheFile2(final Context context,final String urlString) {
//		String cFileName = existCacheFileName(context, urlString);
//		if (cFileName != null) {
//			try {
//				//return new WebResourceResponse("images/*", "UTF-8", new FileInputStream(cFileName));
//				return new FileInputStream(cFileName);
//			} catch (FileNotFoundException e) {
//
//			}
//		}
//		executor.execute(new Runnable() {
//			@Override
//			public void run() {
//				cacheFileImplReturnFileName(context, urlString);
//			}
//		});
//		return null;
//	}

	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 6, 10,
			TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(3000),
			new ThreadPoolExecutor.CallerRunsPolicy());
//	private static ThreadPool executor = new ThreadPool(6,6,3000);
	
	private static class Datac{private int count=0;private int preOffset=0;}
	private static InputStream asynCacheFile(final Context context,final String urlString) {
		InputStream _in = asynCacheFileImpl(context,urlString);
		if(_in == null){
			_in = asynCacheFileImpl(context,getBackUrl(urlString));
		}
		return _in;
	}

	private static InputStream asynCacheFileImpl(final Context context,final String urlString){

		if(urlString == null || "".equals(urlString)){
			return null;
		}

		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final AutoResetEvent inSet = new AutoResetEvent();//通知in

		final Datac d = new Datac();

		InputStream in = new InputStream() {
			private byte[] bs = null;
			@Override
			public int read() throws IOException {
				inSet.waitOne();
				return 0;
			}

			public int available() throws IOException {
				inSet.waitOne();
				bs = out.toByteArray();
				return out.size();
			}

			public int read(byte[] buffer) throws IOException {
				return read(buffer, 0, buffer.length);
			}

			@Override
			public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {

				inSet.waitOne();
				if(d.count == -1 || d.preOffset >= out.size()){
					return -1;
				}
				int n = 0;

				for(;n < byteCount && n + d.preOffset < bs.length; n++){
					buffer[byteOffset + n] = bs[d.preOffset + n];
				}

				d.preOffset += n;

				return n;
			}
		};

//		final OutputStream out = new OutputStream() {
//			@Override
//			public void write(int oneByte) throws IOException {
//
//			}
//
//			public void write(byte[] buffer, int offset, int count) throws IOException {
//				d.count = count;
//				System.out.println("out thread id:"+Thread.currentThread().getId());
//				inSet.set();
//				outSet.waitOne();
//				outSet.reset();
//			}
//
//			@Override
//			public void close() throws IOException {
//				d.count = -1;
//				inSet.set();
//			}
//		};
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
					inSet.set();
					return;
				}


				int count = -1;
				byte[] bs = new byte[4096];
				try {
					while((count = _in.read(bs)) != -1){
						out.write(bs, 0, count);
					}
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				inSet.set();
			}
		});

		return in;
	}
	//有可能死锁的
//	private static InputStream asynCacheFileImpl(final Context context,final String urlString){
//
//		if(urlString == null || "".equals(urlString)){
//			return null;
//		}
//
//
//		final byte[] bs = new byte[4096];
//
//		final AutoResetEvent inSet = new AutoResetEvent();//通知in
//		final AutoResetEvent outSet = new AutoResetEvent();//通知out
//
//		final Datac d = new Datac();
//
//		InputStream in = new InputStream() {
//			@Override
//			public int read() throws IOException {
//				inSet.waitOne();
//				return 0;
//			}
//
//			public int available() throws IOException {
//				inSet.waitOne();
//				return d.count;
//			}
//
//			public int read(byte[] buffer) throws IOException {
//				return read(buffer, 0, buffer.length);
//			}
//
//			@Override
//			public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
//				inSet.waitOne();
//				if(d.count == -1){
//					return -1;
//				}
//				int n = 0;
//				for(;n<byteCount&n+d.preOffset<d.count;n++){
//					buffer[byteOffset + n] = bs[d.preOffset + n];
//				}
//
//				if(n + d.preOffset == d.count) {
//					d.preOffset = 0;
//					inSet.reset();
//					outSet.set();
//				}else{
//					d.preOffset += n;
//				}
//				return n;
//			}
//		};
//
//		final OutputStream out = new OutputStream() {
//			@Override
//			public void write(int oneByte) throws IOException {
//
//			}
//
//			public void write(byte[] buffer, int offset, int count) throws IOException {
//				d.count = count;
//				System.out.println("out thread id:"+Thread.currentThread().getId());
//				inSet.set();
//				outSet.waitOne();
//				outSet.reset();
//			}
//
//			@Override
//			public void close() throws IOException {
//				d.count = -1;
//				inSet.set();
//			}
//		};
//
//		executor.execute(new Runnable(){
//			@Override
//			public void run() {
//
//				InputStream _in = cacheFileImplCheckAndReturnInputStream(context, urlString);
//
//				if(_in == null){
//					try {
//						out.close();
//					} catch (IOException e) {
//					}
//					return;
//				}
//
//
//				int count = -1;
//				try {
//					while((count = _in.read(bs)) != -1){
//						out.write(bs, 0, count);
//					}
//					out.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//
//		return in;
//	}


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

		if(fileName != null){//检查图片文件的完整性
			try{
				//_in = context.openFileInput(fileName);
				Bitmap oriBmp = BitmapFactory.decodeFile(fileName);
				if(oriBmp != null){
					return fileName;
				}
			}catch(Throwable e){
			}
		}
		fileName =  cacheFileImplReturnFileName(context,urlString);

		if(fileName != null){//检查图片文件的完整性
			try{
				//_in = context.openFileInput(fileName);
				Bitmap oriBmp = BitmapFactory.decodeFile(fileName);
				if(oriBmp != null){
					return fileName;
				}
			}catch(Throwable e){
			}
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

		String fileName = path.getAbsolutePath() + cachePath + fileNameMD5;

		try {
			if(!writeFileToLocal(context, path, fileName, urlString)){
				return null;
			}
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

			fileName = path.getAbsolutePath() + cachePath + fileNameMD5;

			if(!writeFileToLocal(context, path, fileName, urlString)){
				return null;
			}

			return fileName;
		}catch (Throwable e){
			try {
				new File(fileName).delete();
			}catch (Throwable e2){}
		}
		return null;
	}

	private static boolean writeFileToLocal(Context context,File path,String fileName,String urlString)throws Throwable{

		HttpCommunicate.init(context);

		File dir = new File(path + cachePath);
		if(!dir.exists()){
			dir.mkdirs();
		}
		if(!(new File(path + cachePath).exists())){//防止SD卡不能写数据
			return false;
		}

		try{
			Bitmap oriBmp = BitmapFactory.decodeFile(fileName);
			if(oriBmp != null){
				return true;
			}
		}catch(Throwable e){
//				e.printStackTrace();
		}


//		FileInfo fileInfo = (FileInfo)HttpCommunicate.download(urlString,null).getResult();
//
//		fileInfo.getFile().renameTo(new File(fileName));

		DownloadImage downloadImage = new DownloadImage(urlString,fileName);

		if(!downloadImage.download()){
			return false;
		}

		try{
			Bitmap oriBmp = BitmapFactory.decodeFile(fileName);
			if(oriBmp == null){
				return false;
			}
		}catch(Throwable e){
			return false;
		}

		addCacheFileNumber(dir);

		return true;
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
				Thread.sleep(1000);
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


	private static class DownloadImage {

		private Map<String, Boolean> urls = new HashMap<String, Boolean>();
		private String urlString;
		private String fileName;

		DownloadImage(String url, String fileName) {
			this.urlString = url;
			this.fileName = fileName;
		}

		boolean download() {
			try {
				return downloadImpl(urlString);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return false;
		}

		private boolean downloadImpl(String urlString) throws Throwable {

			HttpURLConnection conn = open(urlString, _httpDNS);

			conn.setInstanceFollowRedirects(true);
			conn.setReadTimeout(HTTP_TIMEOUT);

			int statusCode = conn.getResponseCode();
			while (statusCode == HttpURLConnection.HTTP_MOVED_TEMP
					|| statusCode == HttpURLConnection.HTTP_MOVED_PERM) {

				urls.put(urlString, new Boolean(true));
				String newUrlString = conn.getHeaderField("Location");
				if (new Boolean(true).equals(urls.get(newUrlString))) {
					return false;
				}
				return downloadImpl(newUrlString);

			}

			InputStream _in = conn.getInputStream();

			FileOutputStream _out = new FileOutputStream(new File(fileName));

			byte[] bs = new byte[4096];
			int count = 0;
			while ((count = _in.read(bs)) != -1) {
				_out.write(bs, 0, count);
			}

			_out.close();
			return true;

		}

		private static HttpURLConnection open(String urlString, HttpDNS httpDNS) throws Exception {
			if (httpDNS == null) {
				return (HttpURLConnection) new URL(urlString).openConnection();
			}
			URL url = new URL(urlString);

			String originHost = url.getHost();
			String dstIp = httpDNS.getIpByHost(originHost);

			if (dstIp == null) {
				return (HttpURLConnection) new URL(urlString).openConnection();
			}

			urlString = urlString.replaceFirst(originHost, dstIp);
			url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置HTTP请求头Host域
			conn.setRequestProperty("Host", originHost);
			return conn;
		}
	}
}


