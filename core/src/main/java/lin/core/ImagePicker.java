package lin.core;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import lin.client.http.*;
import lin.client.http.Error;
import lin.core.ResourceView;
import lin.core.annotation.Click;
import lin.core.annotation.ResourceClass;
import lin.core.annotation.ResourceId;
import lin.core.annotation.Touch;
import lin.core.annotation.ViewById;
import lin.core.fitChart.FitChart;
import lin.util.Procedure;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 
 * @author lin
 * @date Mar 11, 2015 1:33:47 AM
 *
 */
@SuppressLint({ "Recycle", "NewApi" })
@ResourceClass(R.class)
@ResourceId(id="lin_core_images_picker")
public class ImagePicker extends ResourceView{

	public static enum DotFlag{
		NONE,UP,DOWN,DOWN_LEFT,DOWN_RIGHT
	}


	@ViewById(id="lin_core_images_picker_dot_id")
	private ViewGroup dotLayout;

	private DotFlag dotFlag = DotFlag.NONE;
	private int autoFlagInterval = 0;

	public ImagePicker(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		onInitAttr(attrs);
	}

	public ImagePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		onInitAttr(attrs);
	}

	public ImagePicker(Context context) {
		super(context);
	}
	
	
	@Touch
	public void touch(){
		if(pager != null){
			pager.requestDisallowInterceptTouchEvent(true);
		}
	}
	
	@Override
	protected void onInited() {
		this.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				touch();
				return true;
			}
		});
		HttpCommunicate.init(this.getContext());

		setDotLayout();
//		this.vedio = true;
//		this.vedioUrl = "http://i.feicuibaba.com/test/2/33A26FEA-DCB8-4D0D-AC82-149CBDEFCF91.mp4";
//		this.vedioImage = "http://i.feicuibaba.com/test/2/38984ED7-67E9-4B67-A06D-B87BD475E2BB.jpg_middle.jpg";
	}

	/**
	 *
	 * @param interval 以毫秒为单位，0则不自动翻页
	 */
	public void autoFlip(int interval){
		this.autoFlagInterval = interval;
	}


	public void setDotFlag(DotFlag dotFlag){
		this.dotFlag = dotFlag;
	}

	private void setDotLayout(){
		if(dotLayout == null || this.items == null){
			return;
		}
		dotLayout.removeAllViews();
		ImageView imageView;
		LinearLayout.LayoutParams lp = null;
		for(int n=0;n<this.items.length;n++){
			imageView = new ImageView(this.getContext());

			lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			lp.setMargins(10,10,10,10);

			imageView.setLayoutParams(lp);

			imageView.setClickable(true);

			imageView.setBackgroundDrawable(this.getContext().getResources().getDrawable(R.drawable.lin_core_images_picker_circle_selector));

			dotLayout.addView(imageView);
		}
	}
	
	@Override
	public void setBackgroundColor(int color) {
		super.setBackgroundColor(color);
		resetBackground();
	}
	
	@Override
	public void setBackground(Drawable d) {
		super.setBackground(d);
		resetBackground();
	}
	
	@Override
	public void setBackgroundResource(int resid) {
		super.setBackgroundResource(resid);
		resetBackground();
	}
	private void onInitAttr(AttributeSet attrs) {
		if(attrs == null){
			return;
		}
		TypedArray attrArray = this.getContext().obtainStyledAttributes(attrs,R.styleable.lin);
		this.edited = attrArray.getBoolean(R.styleable.lin_images_picker_edit, true);
		attrArray.recycle();
	}
//		
//		ViewPager pager = (ViewPager) rootView.findViewById(R.id.public_image_picker_pager);
//		pager.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(ImagePicker.this.getContext(),ImagePickerActivity.class);  
//                /* 开启Pictures画面Type设定为image */  
////                intent.setType("image/*");  
////                /* 使用Intent.ACTION_GET_CONTENT这个Action */  
////                intent.setAction(Intent.ACTION_GET_CONTENT);   
//                /* 取得相片后返回本画面 */  
//                ImagePicker.this.getActivity().startActivityForResult(intent, 1);
//                  				
//			}
//			
//		});
//	}
	
	private void resetBackground(){
		if(items == null || this.getBackground() == null){
			return;
		}
		for(int n=0;n<items.length;n++){
			items[n].setBackground(this.getBackground());
		}
	}
	private ImagePickerItem[] items = null;
	private VideoItemView vedioView;
	@ViewById(id="images_picker_pager")
	private ViewPager pager;
	private PagerAdapter pagerAdapter = null;
	@Override
	protected void onFirstAttachedToWindow() {

		pagerAdapter = new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				int row = 0;
				if(items != null){
					row = items.length;
				}
				if(vedio){
					row += 1;
				}
				return row;
			}
//			@Override
//			public void destroyItem(View container, int position, Object object) {
//
//				if(vedio){
//					if(position == 0){
//						((ViewPager)container).removeView(vedioView);
//					}else{
//						((ViewPager)container).removeView(items[position - 1]);
//					}
//				}
//				((ViewPager)container).removeView(items[position]);
//			}
			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				if(vedio){
					if(position == 0){
						((ViewPager)container).removeView(vedioView);
					}else{
						((ViewPager)container).removeView(items[position - 1]);
					}
				}else {
					((ViewPager) container).removeView(items[position]);
				}
			}
			
//			@Override
//			public Object instantiateItem(View container, int position) {
//
//				if(vedio){
//					if(position == 0){
//						((ViewPager)container).addView(vedioView);
//						return vedioView;
//					}else{
//						((ViewPager)container).addView(items[position - 1]);
//						return items[position - 1];
//					}
//				}
//				((ViewPager)container).addView(items[position]);
//				return items[position];
//			}
			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				if(vedio){
					if(position == 0){
						((ViewPager)container).addView(vedioView);
						return vedioView;
					}else{
						((ViewPager)container).addView(items[position - 1]);
						return items[position - 1];
					}
				}else {
					((ViewPager) container).addView(items[position]);
					return items[position];
				}
			}
		};
		pager.setAdapter(pagerAdapter);
		pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			private int prePosition = 0;
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//				System.out.println("onPageScrolled("+position+","+positionOffset+","+positionOffsetPixels+")");
			}

			@Override
			public void onPageSelected(int position) {
//				System.out.println("onPageSelected("+position+")");
				if(prePosition == 0 && vedioView != null){
					vedioView.resetVedio();
				}
				prePosition = position;
			}

			@Override
			public void onPageScrollStateChanged(int state) {
//				System.out.println("onPageScrollStateChanged("+state+")");
			}
		});
		resetViews();
	}
	
	private void resetViews(){
		if(pagerAdapter == null){
			return;
		}
		if(vedioView == null && vedio){
			vedioView = new VideoItemView(this.getContext());
			vedioView.setImagePath(this.vedioImage);
		}
		if(this.imagePaths == null || this.imagePaths.length == 0){
			items = null;
			pagerAdapter.notifyDataSetChanged();
			return;
		}
		ImagePickerItem[] oldItems = items;
		items = new ImagePickerItem[this.imagePaths.length];
		int n=0;
		if(oldItems != null){
			for(;n<oldItems.length&&n<items.length;n++){
				items[n] = oldItems[n];
				items[n].setImagePath(this.imagePaths[n]);
				items[n].setEdited(this.edited);
			}
		}
		for(;n<items.length;n++){
			items[n] = new ImagePickerItem(this.getContext());
			items[n].setImagePath(this.imagePaths[n]);
			items[n].setEdited(this.edited);
		}
		pagerAdapter.notifyDataSetChanged();
		resetBackground();
		this.setEdited(this.edited);

		setDotLayout();
	}

	private boolean edited;

	private boolean fullScreen;
//	    //使图像填满
	private boolean fill;
	//
//	    //缩放
	private boolean zoom;
	//
//	    //是否显示位置标记
	private boolean showPositionLabel;
	
	private String vedioUrl;
	
	private String vedioImage;
	
	private boolean vedio;

//	@property(readonly) NSArray * imagesForEdited;
//
//	@property(readonly) NSArray * images;
	private String[] imagePaths;
	public boolean isEdited() {
		return edited;
	}

	public void setEdited(boolean edited) {
		this.edited = edited;
		if(items == null){
			return;
		}
		for(ImagePickerItem item : items){
			item.setEdited(edited);
		}
	}

	public boolean isFullScreen() {
		return fullScreen;
	}

	public void setFullScreen(boolean fullScreen) {
		this.fullScreen = fullScreen;
	}

	public boolean isFill() {
		return fill;
	}

	public void setFill(boolean fill) {
		this.fill = fill;
	}

	public boolean isZoom() {
		return zoom;
	}

	public void setZoom(boolean zoom) {
		this.zoom = zoom;
	}

	public boolean isShowPositionLabel() {
		return showPositionLabel;
	}

	public void setShowPositionLabel(boolean showPositionLabel) {
		this.showPositionLabel = showPositionLabel;
	}

	public String[] getImagePaths() {
		return imagePaths;
	}

	public void setImagePaths(String[] imagePaths) {
		this.imagePaths = imagePaths;
		this.resetViews();
	}
	
	public Bitmap[] getImages(){
		if(items == null){
			return null;
		}
		Bitmap[] images = new Bitmap[items.length];
		for(int n=0;n<items.length;n++){
			images[n] = items[n].getImage();
		}
		return images;
	}

	public String getVedioUrl() {
		return vedioUrl;
	}

	public void setVedioUrl(String vedioUrl) {
		if(this.vedioUrl == null && vedioUrl == null
				|| this.vedioUrl != null && this.vedioUrl.equals(vedioUrl)){
			return;
		}
		this.vedioUrl = vedioUrl;
		this.resetViews();
	}

	public String getVedioImage() {
		return vedioImage;
	}

	public void setVedioImage(String vedioImage) {
		if(this.vedioImage == null && vedioImage == null
				|| this.vedioImage != null && this.vedioImage.equals(vedioImage)){
			return;
		}
		this.vedioImage = vedioImage;
		this.resetViews();
	}

	public boolean isVedio() {
		return vedio;
	}

	public void setVedio(boolean vedio) {
		if(this.vedio == vedio){
			return;
		}
		this.vedio = vedio;
		this.resetViews();
	}
	
	@ResourceClass(R.class)
	@ResourceId(id="lin_core_images_picker_vedio")
	private class VideoItemView extends ResourceView implements SurfaceHolder.Callback,MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener {

		public VideoItemView(Context context) {
			super(context);
		}

		@ViewById(id = "imagepicker_item_image_id")
		private ImageView imageView;

		public void setImagePath(String imagePath) {
//			this.imagePath = imagePath;
			Images.setImage(imageView, imagePath);
		}

		@ViewById(id = "imagepicker_item_vedio_layout")
		private ViewGroup layout;

		@ViewById(id="images_pick_vedio_fit_chart")
		private FitChart fitChart;

//		@ViewById(id="imagepicker_item_vedio_id")
//		private VideoView videoView;


		@ViewById(id="imagepicker_item_vedio_id")
		private SurfaceView surfaceView;

		@ViewById(id="images_pick_edit_and_add")
		private View addView;

		@ViewById(id="images_pick_vedio_loading_text")
		private View loadingText;

		private boolean isClick;
		private Handler mHandler = new Handler();
		private Uri cacheVedioUri;
		private SurfaceHolder holder;
		private MediaPlayer player;

//		private Display currDisplay;

		@Override
		protected void onInited() {
			player = new MediaPlayer();
			player.setOnPreparedListener(this);
			player.setOnCompletionListener(this);
			holder = surfaceView.getHolder();

//			player.setDisplay(holder);
//			holder.setFixedSize(100, 100);
			holder.addCallback(this);
//			currDisplay = this.getActivity().getWindowManager().getDefaultDisplay();
			//为了可以播放视频或者使用Camera预览，我们需要指定其Buffer类型    
//			holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		@Click
		private void click() {
			if (isClick) {
				return;
			}
			isClick = true;

			if (cacheVedioUri != null){
				addView.setVisibility(View.INVISIBLE);
				surfaceView.setVisibility(View.VISIBLE);
				try {
					player.reset();
					player.setDataSource(getContext(), cacheVedioUri);
					player.prepare();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
			fitChart.setVisibility(View.VISIBLE);
			loadingText.setVisibility(View.VISIBLE);
//			surfaceView.setVisibility(View.VISIBLE);
			addView.setVisibility(View.INVISIBLE);



//			String vfile = "/storage/emulated/0/Android/data/lin.test/files/Download/videos/163537bbcd72c20f599a6c5c97512c0d.mp4";
//			try {
//
//				player.setDataSource(vfile);
//				player.prepare();
//
//				player.start();
//
//			} catch (Throwable e) {
//				e.printStackTrace();
//			}

//			player.start();
			new Thread(new Runnable() {
				@Override
				public void run() {
//					final Uri uri = downloadvideo(vedioUrl);
					final Uri uri = downloadvideoWithHttpClient(vedioUrl);
					if(uri == null){
						cacheVedioUri = Uri.parse(vedioUrl);
					}else {
						cacheVedioUri = uri;
					}
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							fitChart.setVisibility(View.GONE);
							loadingText.setVisibility(View.GONE);
							surfaceView.setVisibility(View.VISIBLE);
							try {
								player.reset();
								player.setDataSource(getContext(), cacheVedioUri);
								player.prepare();
							} catch (IOException e) {
								e.printStackTrace();
							}
//							videoView.setVideoURI(uri);
//							videoView.start();
//							videoView.requestFocus();
						}
					});
				}
			}).start();

//			if (videoView == null) {
//				videoView = new VideoView(this.getContext());
//				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//				lp.addRule(RelativeLayout.CENTER_IN_PARENT);
//				videoView.setLayoutParams(lp);
//				layout.addView(videoView);
//			}
//			videoView.setVisibility(View.VISIBLE);
//			imageView.setVisibility(View.INVISIBLE);
//			if (vedioUri != null){
//				videoView.setVideoURI(vedioUri);
//				videoView.start();
////				videoView.requestFocus();
//				return;
//			}
////			videoView.setVisibility(View.VISIBLE);
////			Uri uri = Uri.parse(vedioUrl);
//			//调用系统自带的播放器
////			    Intent intent = new Intent(Intent.ACTION_VIEW);
//////			    Log.v("URI:::::::::", uri.toString());
////			    intent.setDataAndType(uri, "video/mp4");
////			   this.getActivity(). startActivity(intent);
////			videoView.setMediaController(new MediaController(this.getContext()));
////			videoView.setVideoURI(uri);
////			videoView.start();
////			videoView.requestFocus();
//			fitChart.setVisibility(View.VISIBLE);
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					final Uri uri = downloadvideo(vedioUrl);
//					vedioUri = uri;
//					mHandler.post(new Runnable() {
//						@Override
//						public void run() {
//							fitChart.setVisibility(View.GONE);
//							videoView.setVideoURI(uri);
//							videoView.start();
//							videoView.requestFocus();
//						}
//					});
//				}
//			}).start();
		}

		public void resetVedio(){
			mHandler.post(new Runnable() {
				@Override
				public void run() {

					imageView.setVisibility(View.VISIBLE);
					surfaceView.setVisibility(View.INVISIBLE);
					fitChart.setVisibility(View.INVISIBLE);

					if (player != null) {
						player.stop();
					}
					addView.setVisibility(View.VISIBLE);
					isClick = false;
				}
			});
		}
		@Override
		public void onResume() {
			resetVedio();
		}

		public void onCompletion(MediaPlayer mp){
			addView.setVisibility(View.VISIBLE);
			isClick = false;
		}

		@Override
		public void onPrepared(MediaPlayer player){
			// 当prepare完成后，该方法触发，在这里我们播放视频    

			//首先取得video的宽和高    
			double vWidth = player.getVideoWidth();
			double vHeight = player.getVideoHeight();

			double lWidth = layout.getWidth();
			double lHeight = layout.getHeight();

			double s = (vWidth/vHeight)/(lWidth/lHeight);
			RelativeLayout.LayoutParams lp = null;
			if(s > 0){
				lp = new RelativeLayout.LayoutParams((int)lWidth,(int)(lHeight / s));
			}else{
				lp = new RelativeLayout.LayoutParams((int)(lWidth*s),(int)lHeight);
			}
			lp.addRule(RelativeLayout.CENTER_IN_PARENT);
			surfaceView.setLayoutParams(lp);
			surfaceView.setVisibility(View.VISIBLE);
			imageView.setVisibility(View.INVISIBLE);

			player.start();
//			if(vWidth > currDisplay.getWidth() || vHeight > currDisplay.getHeight()){
//				//如果video的宽或者高超出了当前屏幕的大小，则要进行缩放    
//				float wRatio = (float)vWidth/(float)currDisplay.getWidth();
//				float hRatio = (float)vHeight/(float)currDisplay.getHeight();
//
//				//选择大的一个进行缩放    
//				float ratio = Math.max(wRatio, hRatio);
//
//				vWidth = (int)Math.ceil((float)vWidth/ratio);
//				vHeight = (int)Math.ceil((float)vHeight/ratio);
//
//				//设置surfaceView的布局参数    
//				surfaceView.setLayoutParams(new LinearLayout.LayoutParams(vWidth, vHeight));
//
//				//然后开始播放视频    
//
//				player.start();
//			}
		}


		public void surfaceCreated(SurfaceHolder holder){
			// 当SurfaceView中的Surface被创建的时候被调用    
			//在这里我们指定MediaPlayer在当前的Surface中进行播放    
			player.setDisplay(holder);
			//在指定了MediaPlayer播放的容器后，我们就可以使用prepare或者prepareAsync来准备播放了    
//			player.prepareAsync();
		}
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
								   int height){

		}

		public void surfaceDestroyed(SurfaceHolder holder){

		}

		private boolean isExists(String fileName) {
			return false;
		}

		private Uri downloadvideoWithHttpClient(String vedioUrl){

			Uri uri = downloadvideoWithHttpClientImpl(Images.imageUrl(Images.getImageUrl(),vedioUrl));
			if(uri == null){
				uri = downloadvideoWithHttpClientImpl(Images.imageUrl(Images.getImageBackupUrl(),vedioUrl));
			}
			return uri;
		}

		private Uri downloadvideoWithHttpClientImpl(String vedioUrl){

			try {
				File path = this.getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + "/videos");

				if (path == null) {
					path = new File(this.getContext().getCacheDir() + "/" + Environment.DIRECTORY_DOWNLOADS + "/videos");
				}
				if(!path.exists()){
					path.mkdirs();
				}
				String md5s = lin.util.MD5.digest(vedioUrl);
				final File vedioFile = new File(path.getAbsoluteFile() + "/" + md5s + ".vcache");

				if(vedioFile.exists()){
					return Uri.fromFile(vedioFile);
				}
				HttpCommunicate.download(vedioUrl, new ProgressResultListener() {
					@Override
					public void progress(final long progress, final long total) {
//						System.out.println("=================================================");
//						System.out.println("progress:"+progress+"\ttotal:"+total);
//						System.out.println("=================================================");
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								fitChart.setValue((float) (progress * 100.0 / total));
							}
						});
					}

					@Override
					public void result(Object obj, List<Error> warning) {
//						System.out.println("=================================================");
//						System.out.println("obj:"+obj);
//						System.out.println("=================================================");
						try {

							File file = (File) obj;
							byte[] bs = new byte[1024];
							int count = 0;
							InputStream _in = new FileInputStream(file);
							OutputStream _out = new FileOutputStream(vedioFile);
							while ((count = _in.read(bs)) != -1) {
								_out.write(bs, 0, count);
							}
							_out.close();
							_in.close();
						}catch (Throwable e){}
					}

					@Override
					public void fault (Error error){
//						System.out.println("=================================================");
//						System.out.println("error:"+error.getStackTrace());
//						System.out.println("=================================================");

						}
					}

				).waitForEnd();
//				System.out.println("****************************************");


					return Uri.fromFile(vedioFile);
				}catch (Throwable e){
				e.printStackTrace();
			}
			return null;
		}
		private Uri downloadvideo(String vedioUrl){

			final ProcedureUri procedureUri = new ProcedureUri();
			File file = downloadVideoImpl(vedioUrl, procedureUri);
			if(file != null){
				return Uri.fromFile(file);
			}
			int count = 0;
			while(!procedureUri.complete){
				if(queryStatus(this.getContext(), procedureUri.reference, DownloadManager.STATUS_FAILED) || count++ > 300){
//					result.unregisterBroadcast();
					try {
						this.getContext().unregisterReceiver(procedureUri.receiver);
					}catch (Throwable e){}
					break;
				}
//				queryProgress(this.getContext(), reference);
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						fitChart.setValue(queryProgress(getContext(), procedureUri.reference) * 100);
					}
				});

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}
			return procedureUri.uri;
		}

		private File downloadVideoImpl(String vedioUrl,ProcedureUri callback) {


			String md5s = lin.util.MD5.digest(vedioUrl);
			Uri uri = Uri.parse(vedioUrl);

			File file = this.getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + "/videos");

			if (file == null) {
				file = new File(this.getContext().getCacheDir() + "/" + Environment.DIRECTORY_DOWNLOADS + "/videos");
			}
			File vedioFile = new File(file.getAbsoluteFile() + "/" + md5s + ".vcache");
			if(vedioFile.exists()){
				return vedioFile;
			}
//			if(apkFile.exists()){
//				if(callback != null){
//					callback.procedure(Uri.fromFile(apkFile));
//				}
//				return null;
//			}
//			file.deleteOnExit();
			file.mkdirs();

			DownloadManager manager = (DownloadManager) this.getContext().getSystemService(Service.DOWNLOAD_SERVICE);


			//设置下载地址
//        DownloadManager.Request down = new DownloadManager.Request(
//                Uri.parse("http://gdown.baidu.com/data/wisegame/fd84b7f6746f0b18/baiduyinyue_4802.apk"));
			DownloadManager.Request down = new DownloadManager.Request(uri);

//			ConnectivityManager connectivity = null;
//			connectivity.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE
//			NetworkInfo.State s = NetworkInfo.State.CONNECTED;
			// 设置允许使用的网络类型，这里是移动网络和wifi都可以
			down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
					| DownloadManager.Request.NETWORK_WIFI);

			// 下载时，通知栏显示途中
//        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
//
//        // 显示下载界面
//        down.setVisibleInDownloadsUi(true);

			// 设置下载后文件存放的位置
//        down.setDestinationInExternalFilesDir(context,
//                Environment.DIRECTORY_DOWNLOADS, apkName);
			down.setDestinationUri(Uri.fromFile(vedioFile));


			// 将下载请求放入队列
			long reference = manager.enqueue(down);

			callback.reference = reference;
//

			//注册下载广播

			DownloadCompleteReceiver receiver = new DownloadCompleteReceiver(manager, reference, callback);

			this.getContext().registerReceiver(receiver, new IntentFilter(
					DownloadManager.ACTION_DOWNLOAD_COMPLETE));
			callback.receiver = receiver;
//        context.registerReceiver(receiver, null);
//			DownloadResult result = new DownloadResult();
//			result.context = context;
//			result.broadcastReceiver = receiver;
//			result.reference = reference;
//			return result;
			return null;
		}
	}

	private class ProcedureUri implements Procedure<Uri>{

		private volatile boolean complete = false;
		private Uri uri;
		private BroadcastReceiver receiver;
		private long reference;
		@Override
		public void procedure(Uri uri) {
			this.uri = uri;
			complete = true;
		}
	};
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
			}
			if(callback != null){
				callback.procedure(uri);
			}else{
				callback.procedure(null);
			}
			context.unregisterReceiver(this);
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
	private static boolean queryStatus(Context context,long reference,int status){
		DownloadManager manager = (DownloadManager) context.getSystemService(Service.DOWNLOAD_SERVICE);
		Cursor cursor = manager.query(new DownloadManager.Query().setFilterById(reference).setFilterByStatus(status));

		return cursor.getCount() > 0;
	}

	private static float queryProgress(Context context,long reference){
		DownloadManager manager = (DownloadManager) context.getSystemService(Service.DOWNLOAD_SERVICE);
		Cursor cursor = manager.query(new DownloadManager.Query().setFilterById(reference));//.setFilterByStatus(status));

//		DownloadManager.COLUMN_TOTAL_SIZE_BYTES
//		return cursor.getCount() > 0;

		float progress = 0;
		if (cursor != null && cursor.moveToFirst()) {
			float far = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
			float total = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
			progress = far / total;
//			bytesAndStatus[2] = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
		}
//		System.out.println("progress:"+progress);
		return progress;
	}
}
