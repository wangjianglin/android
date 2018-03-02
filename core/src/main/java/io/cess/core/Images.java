package io.cess.core;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.widget.ImageView;
import android.view.View;

/**
 * 
 * @author lin
 * @date Jun 25, 2015 1:10:00 PM
 *
 * 原理：
 *
 * 1、解决防止 ImageView 在组件复用时，旧的值覆盖新的值，当新的值被设后，旧的任务取消运行
 * 2、采用 内存 + 文件 的二级缓存提高性能
 * 2、内存缓存时，采用 url + size 作为缓存 key，因为针对不同的 size 需要设计不同的参数
 * 3、在列表快速拖动时，要防止对 ImageView 不必要的设置
 *
 *
 * "http://site.com/image.png" // from Web
 * "file:///mnt/sdcard/image.png" // from SD card
 * "file:///mnt/sdcard/video.mp4" // from SD card (video thumbnail)
 * "content://media/external/images/media/13" // from content provider
 * "content://media/external/video/media/13" // from content provider (video thumbnail)
 * "assets://image.png" // from assets
 * "drawable://" + R.drawable.img // from drawables (non-9patch images)
 */


public class Images {

	private static ImageLoader imageLoader;
	private static Context mContext;

	public static void init(Context context,ImageLoaderConfiguration config){
		if(mContext != null){
			return;
		}
		mContext = context;
		ImageLoader.getInstance().init(config);

		imageLoader = ImageLoader.getInstance();

	}

	public static void cleanCache(){
		if(imageLoader != null){
			imageLoader.clearDiskCache();
			imageLoader.clearMemoryCache();
		}
	}
	public static void init(Context context){

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 或 imageScaleType(ImageScaleType.EXACTLY)
				.build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.diskCacheSize(Integer.MAX_VALUE)
//				.memoryCacheSizePercentage(20*1024*1024)
//				.memoryCacheSize(20*1024*1024)
				.defaultDisplayImageOptions(defaultOptions)
				.build();

		init(context,config);
	}
	public static interface OnImageComplete{
		void onComplete(ImageView imageView);
	}
//	public static class ImageOperation{
//		private OnImageComplete imageComplete;
//		private ImageView imageView;
//		private boolean isCompletion;
//
//		public void setOnImageComplete(OnImageComplete imageComplete){
//			this.imageComplete = imageComplete;
//			if(isCompletion && imageComplete != null){
//				imageComplete.onComplete(imageView);
//			}
//		}
//
//		private void fireComplete(){
//			if(imageComplete != null){
//				imageComplete.onComplete(imageView);
//			}
//		}
//	}

	private static String _imageUrl = "";
	public static void setImageUrl(String url){
		_imageUrl = url;
		if(_imageUrl != null){
			if(_imageUrl.endsWith("/")){
				_imageUrl = _imageUrl.substring(0,_imageUrl.length() - 1);
			}
		}
	}

	 static String getImageUrl(){
		return _imageUrl;
	}

	private static String _imageBackupUrl = "";

	public static void  setImageBackupUrl(String url){
		_imageBackupUrl = url;
		if(_imageBackupUrl != null){
			if(_imageBackupUrl.endsWith("/")){
				_imageBackupUrl = _imageBackupUrl.substring(0,_imageBackupUrl.length() - 1);
			}
		}
	}

	static String getImageBackupUrl(){
		return _imageBackupUrl;
	}

	static final String imageUrl(String url,String path) {
		if (path == null || "".equals(url)
				|| path.startsWith("http://")
				|| path.startsWith("file:///")
				|| path.startsWith("content://")
				|| path.startsWith("assets://")
				|| path.startsWith("drawable://")) {
			return path;
		}

		if(path == null){
			return path;
		}
		if (path.startsWith("/")) {
			return url + path;
		}
		return url + "/" + path;
	}

	public static Bitmap getImage(String url){
		Bitmap bitmap = imageLoader.loadImageSync(imageUrl(_imageUrl,url));
		if(bitmap == null){
			bitmap = imageLoader.loadImageSync(imageUrl(_imageBackupUrl,url));
		}
		return bitmap;
	}


	public static void setImage(ImageView image,String uri){
		setImage(image,uri,null);
	}
	public static void setImage(final ImageView image, final String uri, final OnLoadingListener listener){

//		final ImageOperation operation = new ImageOperation();
//		operation.imageView = image;
		image.setImageBitmap(null);
//		if (obj instanceof Number){
//			Drawable drawable = mContext.getResources().getDrawable(((Number) obj).intValue());
//			image.setImageDrawable(drawable);
////			operation.isCompletion = true;
////			operation.fireComplete();
//		}
//		else if(obj instanceof String) {

		ImageLoadingListener imageLoadingListener = new ImageLoadingListener() {

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {

			}

			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				if(listener != null){
					listener.onLoadingComplete(arg0, arg1, arg2);
				}
			}

			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {


				ImageLoadingListener imagebackupLoadingListener = new ImageLoadingListener() {

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {

					}

					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
						if(listener != null){
							listener.onLoadingComplete(arg0, arg1, arg2);
						}
					}

					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						if(listener != null){
							listener.onLoadingFailed(arg0, arg1);
						}
					}

					@Override
					public void onLoadingStarted(String arg0, View arg1) {

					}
				};

				imageLoader.displayImage(imageUrl(_imageBackupUrl, uri), image, imagebackupLoadingListener);
			}

			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				if(listener != null){
					listener.onLoadingStarted(arg0, arg1);
				}
			}
		};


		imageLoader.displayImage(imageUrl(_imageUrl,uri), image, imageLoadingListener);
//		}
//		image.setImageBitmap(null);
//		return operation;
	}

	public static interface OnLoadingListener{
		void onLoadingComplete(String url, View imageView, Bitmap bitmap);
		void onLoadingFailed(String url, View imageView);
		void onLoadingStarted(String url, View imageView);
	}


	public static Bitmap setImageColor(Bitmap bitmap,int color){


//		setContentView(R.layout.activity_main);
//		ma = (ImageView) findViewById(R.id.imageView00);
//		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.d1);
//		updateBitmap = Bitmap.createBitmap(bitmap.getWidth(),
//				bitmap.getHeight(), bitmap.getConfig());
		Bitmap updaeBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),bitmap.getConfig());

		Canvas canvas = new Canvas(updaeBitmap);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);////抗锯齿的画笔

		ColorMatrix cm = new ColorMatrix();
		paint.setColorFilter(new ColorMatrixColorFilter(cm));
		paint.setColor(color);
		paint.setAntiAlias(true);
//		canvas = new Canvas(updateBitmap);
//		paint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿的画笔
//		final ColorMatrix cm = new ColorMatrix();
//		paint.setColorFilter(new ColorMatrixColorFilter(cm));
//		paint.setColor(Color.BLACK);
//		paint.setAntiAlias(true);
//		final Matrix matrix = new Matrix();
//		canvas.drawBitmap(bitmap, matrix, paint);
//		ma.setImageBitmap(updateBitmap);

		Matrix matrix = new Matrix();
		canvas.drawBitmap(bitmap,matrix,paint);
		updaeBitmap.recycle();
		return updaeBitmap;
//		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				cm.set(new float[] { 160 / 128f, 0, 0, 0, 0,// 红色值
//						0, 32 / 128f, 0, 0, 0,// 绿色值
//						0, 0, 240 / 128f, 0, 0,// 蓝色值
//						0, 0, 0, 1, 0 // 透明度
//				});
//				paint.setColorFilter(new ColorMatrixColorFilter(cm));
//				canvas.drawBitmap(bitmap, matrix, paint);
//				ma.setImageBitmap(updateBitmap);
//			}
//		});
//	}
	}
}









//============================================================================================

//
//public class Images {
//
//	private static String _imageUrl = null;
//	public static void setImageUrl(String url){
//		_imageUrl = url;
//		if(_imageUrl != null){
//			if(_imageUrl.endsWith("/")){
//				_imageUrl = _imageUrl.substring(0,_imageUrl.length() - 1);
//			}
//		}
//	}
//
//	static String getImageUrl(){
//		return _imageUrl;
//	}
//
//	private static String _imageBackupUrl = null;
//
//	public static void  setImageBackupUrl(String url){
//		_imageBackupUrl = url;
//		if(_imageBackupUrl != null){
//			if(_imageBackupUrl.endsWith("/")){
//				_imageBackupUrl = _imageBackupUrl.substring(0,_imageBackupUrl.length() - 1);
//			}
//		}
//	}
//
//	static String getImageBackupUrl(){
//		return _imageBackupUrl;
//	}
//
//	static final String imageUrl(String url,String path) {
//		if (path == null || "".equals(url) || path.startsWith("http:")) {
//			return path;
//		}
//
////		if(CcnApplication.getApplication().isDebug()){
//////			httpCommUrl = Constants.HTTP_COMM_URL_DEBUG;
////		}else if(CcnApplication.getApplication().isTest()){
//////			httpCommUrl = Constants.HTTP_COMM_URL_TEST;
////		}
//		if(path == null){
//			return path;
//		}
//		if (path.startsWith("/")) {
//			return url + path;
//		}
//		return url + "/" + path;
//	}
//
//	public static void init(Context context){
//
//	}
//	public static interface OnImageComplete{
//		void onComplete(ImageView imageView);
//	}
//	public static class ImageOperation{
//		private HttpBitmapRunnable runnable;
//		private OnImageComplete imageComplete;
//		private ImageView imageView;
//		private ImageOperation(HttpBitmapRunnable runnable){
//			this.runnable = runnable;
//		}
////		public void cancel(){
//////			if(this.runnable != null){
//////				this.runnable.cancel();
//////			}
////		}
//
//		public void setOnImageComplete(OnImageComplete imageComplete){
//			this.imageComplete = imageComplete;
//			if((runnable == null || runnable.isCompletion) && imageComplete != null){
//				imageComplete.onComplete(imageView);
//			}
//		}
//	}
//	public static ImageOperation setImage(ImageView image,Object obj){
//		if(obj instanceof Int){
//			return Images.setImage(image, (Int)obj);
//		}else if(obj instanceof String){
//			return Images.setImage(image, (String)obj);
//		}
//		return null;
//	}
//	public static ImageOperation setImage(ImageView image,int resourceId){
////		lock.lock();
//		HttpBitmapRunnable runnable = cancelMap.get(image.hashCode());
//		if(runnable != null){
//			runnable.cancel();
//		}
////		lock.unlock();
//		image.setImageDrawable(image.getContext().getResources().getDrawable(resourceId));
//		ImageOperation imageOperation = new ImageOperation(null);
//		imageOperation.imageView = image;
//		return imageOperation;
//	}
//	private static Files files = null;
//	private volatile static Map<Int,HttpBitmapRunnable> cancelMap = new java.util.concurrent.ConcurrentHashMap<Int,HttpBitmapRunnable>();
//	public static ImageOperation setImage(ImageView image,String url){
//		url = imageUrl(getImageUrl(),url);
//		if(image == null){
//			ImageOperation imageOperation = new ImageOperation(null);
//			imageOperation.imageView = image;
//			return imageOperation;
//		}
//		if(files == null){
//			files = new Files(image.getContext(),"imagecache",false);
//		}
//		if(url == null || "".equals(url)){
//			image.setImageBitmap(null);
//			ImageOperation imageOperation = new ImageOperation(null);
//			imageOperation.imageView = image;
//			return imageOperation;
//		}
//		Bitmap bitmap = cache(url);
//		if(bitmap != null){
//			image.setImageBitmap(bitmap);
//			ImageOperation imageOperation = new ImageOperation(null);
//			imageOperation.imageView = image;
//			return imageOperation;
//		}
//
////    	if(files.hasCache(url)){
////    		System.out.println("=================");
////    		image.setImageBitmap(getBitmap(image,url));
////    		return new ImageOperation(null);
////    	}
//
////    	lock.lock();
//		HttpBitmapRunnable runnable = cancelMap.get(image.hashCode());
//		if(runnable != null){
////			System.out.println(runnable.url+":canceled.");
//			runnable.cancel();
//		}
//		runnable = new HttpBitmapRunnable(image,url);
//		ImageOperation r = new ImageOperation(runnable);
//		r.imageView = image;
//		runnable.operation = r;
//
//		cancelMap.put(image.hashCode(), runnable);
//		image.setImageBitmap(null);
//		executor.execute(runnable);
////		lock.unlock();
////		r.cancel();
////		image.setVisibility(View.INVISIBLE);
//		return r;
//	}
//
////	private static ReentrantLock lock = new ReentrantLock();
//
//	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 5, 3,
//			TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(3000),
//			new ThreadPoolExecutor.CallerRunsPolicy());
//
//	private static Handler handler = new Handler();
//	//	private static Files files = new File
//	private static class HttpBitmapRunnable implements Runnable{
//
//
//		private String url;
//		private ImageView image;
//		private ImageOperation operation;
//		HttpBitmapRunnable(ImageView image,String url){
//			this.url = url;
//			this.image = image;
//		}
//		private Bitmap bitmap=null;
//		private volatile boolean isCancel = false;
//		private boolean isCompletion;
//		private void cancel(){
//			isCancel = true;
//		}
//
//		private static int getImageViewFieldValue(Object object, String fieldName) {
//			int value = 0;
//			try {
//				Field field = ImageView.class.getDeclaredField(fieldName);
//				field.setAccessible(true);
//				int fieldValue = (Int) field.get(object);
//				if (fieldValue > 0 && fieldValue < Int.MAX_VALUE) {
//					value = fieldValue;
//				}
//			} catch (Exception e) {
//			}
//			return value;
//		}
//
//		private int width(){
//			final ViewGroup.LayoutParams params = image.getLayoutParams();
//			int width = 0;
//			if (params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
//				width = image.getWidth(); // Get actual image width
//			}
//			if (width <= 0 && params != null) width = params.width; // Get layout width parameter
//
//			if (width <= 0) {
//				width = getImageViewFieldValue(image, "mMaxWidth"); // Check maxWidth parameter
//			}
//			return width;
//		}
//		private int height(){
//			final ViewGroup.LayoutParams params = image.getLayoutParams();
//			int height = 0;
//			if (params != null && params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
//				height = image.getHeight(); // Get actual image width
//			}
//			if (height <= 0 && params != null) height = params.height; // Get layout width parameter
//
//			if (height <= 0) {
//				height = getImageViewFieldValue(image, "mMaxHeight"); // Check maxWidth parameter
//			}
//			return height;
//		}
//		private void getBitmap(){
//			bitmap = cache(url);
//			if(bitmap != null){
//				return;
//			}
//			try{
////	        	double imageWidth = image.getWidth() / image.getContext().getResources().getDisplayMetrics().density;
////	        	double imageHeight = image.getHeight() / image.getContext().getResources().getDisplayMetrics().density;
//
//				double imageWidth = width() / image.getContext().getResources().getDisplayMetrics().density;
//				double imageHeight = height() / image.getContext().getResources().getDisplayMetrics().density;
////	        	System.out.println("width:"+imageWidth+"\theight:"+imageHeight);
//
////	        	imageWidth = 100;
////	        	imageHeight = 100;
//				if(imageWidth == 0){
//					imageWidth = 300;
//				}
//				if(imageHeight == 0){
//					imageHeight = 300;
//				}
//				InputStream is = files.cache(url);
////	        	InputStream is = new FileInputStream(filename);
//
//				BitmapFactory.Options options = new BitmapFactory.Options();
//				options.inJustDecodeBounds = true;
//				BitmapFactory.decodeStream(is,null, options);
//				double w = options.outWidth;
//				double h = options.outHeight;
//
//				double s = (w/h)/(imageWidth/imageHeight);
//				float scale = 1;
//				if(s > 1){
//					options.inSampleSize = (int) (w / imageWidth);
//					scale = (float)(w /imageWidth);
//				}else{
//					options.inSampleSize = (int) (h / imageHeight);
//					scale = (float)(h / imageHeight);
//				}
//				options.inJustDecodeBounds = false;
//				options.inPreferredConfig = Bitmap.Config.RGB_565;
////				image = BitmapFactory.decodeStream(cr.openInputStream(mImageCaptureUri),null,options);
//
//
//
//				//解析得到图片
////	            BitmapFactory.Options options=new BitmapFactory.Options();
//				options.inJustDecodeBounds = false;
////	            options.
////	            options.inSampleSize = 10;   //width，hight设为原来的十分一
////	            Bitmap btp =BitmapFactory.decodeStream(is,null,options);
////	       2. if(!bmp.isRecycle() ){
////	                bmp.recycle()   //回收图片所占的内存
////	                system.gc()  //提醒系统及时回收
////	       }
//
//
////	            bitmap = BitmapFactory.decodeStream(is);
////	            is.reset();
//				Bitmap bitmap1 =BitmapFactory.decodeStream(files.cache(url));
//				Matrix m = new Matrix();
//				m.setScale(1/scale,1/scale);
//				bitmap = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1
//						.getHeight(), m, true);
//				//关闭数据流
//				is.close();
//				if(bitmap1 != bitmap){
//					bitmap1.recycle();
//				}
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//			if(bitmap != null){
//				putCache(url,bitmap);
//			}
//		}
//		public void run(){
//
////    		try {
////				Thread.sleep(0);
////			} catch (InterruptedException e) {
////				e.printStackTrace();
////			}
//			if(isCancel){
//				return;
//			}
//			this.getBitmap();
//			if(isCancel){
//				return;
//			}
//			handler.post(new Runnable(){
//
//				@Override
//				public void run() {
//
////		    		image.setVisibility(View.VISIBLE);
//
////					lock.lock();
//					if(isCancel){
////			    			lock.unlock();
//						return;
//					}
////			    		System.out.println(url+":set image.");
//					image.setImageBitmap(bitmap);
//
//					if(operation.imageComplete != null){
////			    			operation.imageComplete.onComplete(image);
//					}
//					isCompletion = true;
////			    		lock.unlock();
//
//				}});
//		}
//	}
//
//
//	private static Map<String,Bitmap> memCaches = new HashMap<String,Bitmap>();
//	private static Map<String,Date> memCacheDates = new HashMap<String,Date>();
//	private static int cacheMaxSize = 100;
//	private static int cacheMinSize = 50;
////	private static String[] keys = new String[101]
//
//	private static Bitmap cache(String key){
//		Bitmap bitmap = memCaches.get(key);
//		if(bitmap != null){
//			memCacheDates.put(key, new Date());
//		}
//		return bitmap;
//	}
//
//	private synchronized static void putCache(String key,Bitmap bitmap){
//		memCaches.put(key, bitmap);
//		memCacheDates.put(key, new Date());
//		if(memCaches.size() > cacheMaxSize){
//			cleanCache();
//		}
//	}
//
//	private static void cleanCache(){
//		if(memCaches.size() < cacheMaxSize){
//			return;
//		}
////		String[] keys = new HashSet<String>(memCaches.keySet()).toArray(new String[]{});
//		Set<String> keySets = memCaches.keySet();
//		String[] keys = new String[keySets.size()];
//		int n = 0;
//		for(String key : keySets){
//			keys[n++] = key;
//		}
////		String[] keys = new String[memCaches.size()];
//		Date[] keysDate = new Date[keys.length];
//		for(n=0;n<keys.length;n++){
//			keysDate[n] = memCacheDates.get(keys[n]);
//			if(keysDate[n] == null){
//				keysDate[n] = new Date(0);
//			}
//		}
//		Date tmp = null;
//		String tmpKey = null;
//		for(n=0;n<cacheMinSize;n++){
//			for(int m = keys.length-2;m>n;m--){
//				if(keysDate[m].getTime() < keysDate[m + 1].getTime()){
//					tmp = keysDate[n];
//					keysDate[n] = keysDate[m];
//					keysDate[m] = tmp;
//
//					tmpKey = keys[n];
//					keys[n] = keys[m];
//					keys[m] = tmpKey;
//				}
//			}
//		}
//		for(n=cacheMinSize;n<keys.length;n++){
//			memCaches.remove(keys[n]);
//			memCacheDates.remove(keysDate[n]);
//		}
//	}
//
//}