package lin.core;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.view.View;

/**
 * 
 * @author lin
 * @date Jun 25, 2015 1:10:00 PM
 *
 */


//public class Images {
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
//		if(obj instanceof Integer){
//			return Images.setImage(image, (Integer)obj);
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
//    private volatile static Map<Integer,HttpBitmapRunnable> cancelMap = new java.util.concurrent.ConcurrentHashMap<Integer,HttpBitmapRunnable>();
//	public static ImageOperation setImage(ImageView image,String url){
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
//    	if(bitmap != null){
//    		image.setImageBitmap(bitmap);
//    		ImageOperation imageOperation = new ImageOperation(null);
//    		imageOperation.imageView = image;
//    		return imageOperation;
//    	}
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
////	private static Files files = new File
//	private static class HttpBitmapRunnable implements Runnable{
//
//    	
//        private String url;
//        private ImageView image;
//        private ImageOperation operation;
//        HttpBitmapRunnable(ImageView image,String url){
//        	this.url = url;
//        	this.image = image;
//        }
//	    private Bitmap bitmap=null;
//	    private volatile boolean isCancel = false;
//	    private boolean isCompletion;
//	    private void cancel(){
//	    		isCancel = true;
//	    }
//	    
//	    private static int getImageViewFieldValue(Object object, String fieldName) {
//			int value = 0;
//			try {
//				Field field = ImageView.class.getDeclaredField(fieldName);
//				field.setAccessible(true);
//				int fieldValue = (Integer) field.get(object);
//				if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
//					value = fieldValue;
//				}
//			} catch (Exception e) {
//			}
//			return value;
//		}
//	    
//	    private int width(){
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
//	    }
//	    private int height(){
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
//	    }
//	    private void getBitmap(){
//			bitmap = cache(url);
//	    	if(bitmap != null){
//	    		return;
//	    	}
//	    	try{
////	        	double imageWidth = image.getWidth() / image.getContext().getResources().getDisplayMetrics().density;
////	        	double imageHeight = image.getHeight() / image.getContext().getResources().getDisplayMetrics().density;
//
//	        	double imageWidth = width() / image.getContext().getResources().getDisplayMetrics().density;
//	        	double imageHeight = height() / image.getContext().getResources().getDisplayMetrics().density;
////	        	System.out.println("width:"+imageWidth+"\theight:"+imageHeight);
//	        	
////	        	imageWidth = 100;
////	        	imageHeight = 100;
//	        	if(imageWidth == 0){
//	        		imageWidth = 300;
//	        	}
//	        	if(imageHeight == 0){
//	        		imageHeight = 300;
//	        	}
//	        	InputStream is = files.cache(url);
////	        	InputStream is = new FileInputStream(filename); 
//	        	
//	        	BitmapFactory.Options options = new BitmapFactory.Options();
//				options.inJustDecodeBounds = true;
//				BitmapFactory.decodeStream(is,null, options);
//				double w = options.outWidth;
//				double h = options.outHeight;
//				
//				double s = (w/h)/(imageWidth/imageHeight);
//				if(s > 1){
//					options.inSampleSize = (int) (w / imageWidth);
//				}else{
//					options.inSampleSize = (int) (h / imageHeight);
//				}
//				options.inJustDecodeBounds = false;
//				options.inPreferredConfig = Bitmap.Config.RGB_565;
////				image = BitmapFactory.decodeStream(cr.openInputStream(mImageCaptureUri),null,options);
//				
//	        	
//	        	
//	            //解析得到图片
////	            BitmapFactory.Options options=new BitmapFactory.Options();
//	            options.inJustDecodeBounds = false;
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
//	            bitmap =BitmapFactory.decodeStream(files.cache(url),null,options);
//	            //关闭数据流
//	            is.close();
//	        }catch(Exception e){
////	        	e.printStackTrace();
//	        }
//	    	if(bitmap != null){
//	    		putCache(url,bitmap);
//	    	}
//	    }
//    	public void run(){
//    		
////    		try {
////				Thread.sleep(0);
////			} catch (InterruptedException e) {
////				e.printStackTrace();
////			}
//    		if(isCancel){
//    			return;
//    		}
//    		this.getBitmap();
//    		if(isCancel){
//    			return;
//    		}
//    		handler.postDelayed(new Runnable(){
//
//				@Override
//				public void run() {
//
////		    		image.setVisibility(View.VISIBLE);
//					
////					lock.lock();
//			    		if(isCancel){
////			    			lock.unlock();
//			    			return;
//			    		}
//			    		System.out.println(url+":set image.");
//			    		image.setImageBitmap(bitmap);
//			    		
//			    		if(operation.imageComplete != null){
//			    			operation.imageComplete.onComplete(image);
//			    		}
//			    		isCompletion = true;
////			    		lock.unlock();
//					
//				}},500);
//    	}
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



public class Images {

	private static ImageLoader imageLoader;
	private static Context mContext;
	public static void init(Context context){
		mContext = context;
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.diskCacheSize(10*1024*1024)
				.defaultDisplayImageOptions(defaultOptions)
				.build();
		ImageLoader.getInstance().init(config);
		
		imageLoader = ImageLoader.getInstance();
	}
	public static interface OnImageComplete{
		void onComplete(ImageView imageView);
	}
	public static class ImageOperation{
		private OnImageComplete imageComplete;
		private ImageView imageView;
		private boolean isCompletion;
		
		public void setOnImageComplete(OnImageComplete imageComplete){
			this.imageComplete = imageComplete;
			if(isCompletion && imageComplete != null){
				imageComplete.onComplete(imageView);
			}
		}
		
		private void fireComplete(){
			if(imageComplete != null){
				imageComplete.onComplete(imageView);
			}
		}
	}

	private static String _imageUrl = null;
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

	private static String _imageBackupUrl = null;

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
		if (path == null || "".equals(url) || path.startsWith("http:")) {
			return path;
		}

//		if(CcnApplication.getApplication().isDebug()){
////			httpCommUrl = Constants.HTTP_COMM_URL_DEBUG;
//		}else if(CcnApplication.getApplication().isTest()){
////			httpCommUrl = Constants.HTTP_COMM_URL_TEST;
//		}
		if(path == null){
			return path;
		}
		if (path.startsWith("/")) {
			return url + path;
		}
		return url + "/" + path;
	}
	
	public static ImageOperation setImage(final ImageView image,final Object obj){

		final ImageOperation operation = new ImageOperation();
		operation.imageView = image;
		image.setImageBitmap(null);
		if (obj instanceof Number){
			Drawable drawable = mContext.getResources().getDrawable(((Number) obj).intValue());
			image.setImageDrawable(drawable);
			operation.isCompletion = true;
			operation.fireComplete();
		}
		else if(obj instanceof String) {

			ImageLoadingListener imageLoadingListener = new ImageLoadingListener() {

				@Override
				public void onLoadingCancelled(String arg0, View arg1) {

				}

				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					operation.isCompletion = true;
					operation.fireComplete();
				}

				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
//					operation.isCompletion = true;
//					operation.fireComplete();

					ImageLoadingListener imagebackupLoadingListener = new ImageLoadingListener() {

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {

						}

						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
							operation.isCompletion = true;
							operation.fireComplete();
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
							operation.isCompletion = true;
							operation.fireComplete();
						}

						@Override
						public void onLoadingStarted(String arg0, View arg1) {

						}
					};

					imageLoader.displayImage(imageUrl(_imageBackupUrl, (String) obj), image, imagebackupLoadingListener);
				}

				@Override
				public void onLoadingStarted(String arg0, View arg1) {

				}
			};



			imageLoader.displayImage(imageUrl(_imageUrl,(String) obj), image, imageLoadingListener);
		}
//		image.setImageBitmap(null);
		return operation;
	}
}
