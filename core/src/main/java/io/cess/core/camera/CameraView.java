package io.cess.core.camera;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import io.cess.core.Nav;
import io.cess.core.R;
import io.cess.core.ResView;
import io.cess.core.annotation.Click;
import io.cess.core.annotation.ResCls;
import io.cess.core.annotation.ResId;
import io.cess.core.annotation.ViewById;

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.yixia.camera.MediaRecorderBase;
import com.yixia.camera.MediaRecorderBase.OnErrorListener;
import com.yixia.camera.MediaRecorderNative;
import com.yixia.camera.VCamera;
import com.yixia.camera.model.MediaObject;
import com.yixia.camera.model.MediaObject.MediaPart;
import com.yixia.camera.util.DeviceUtils;
import com.yixia.camera.util.FileUtils;
import com.yixia.camera.util.StringUtils;
import com.yixia.videoeditor.adapter.UtilityAdapter;

/**
 * 
 * @author lin
 * @date Jul 5, 2015 11:40:51 PM
 *
 */
@SuppressLint({ "NewApi", "MissingSuperCall", "SimpleDateFormat", "SdCardPath", "HandlerLeak" })
@ResCls(R.class)
@ResId(id="io_cess_core_camera_fragment")
public class CameraView extends ResView implements MediaRecorderBase.OnEncodeListener,OnErrorListener{

	/**
	 * 视频录制错误
	 * 
	 * @param what
	 * @param extra
	 */
	public void onVideoError(int what, int extra){
		System.out.println("what:"+what);
	}

	/**
	 * 音频录制错误
	 * 
	 * @param what
	 * @param message
	 */
	public void onAudioError(int what, String message){
		System.out.println("what:"+message);
	}
    public CameraView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 防止锁屏
		this.getActivity().getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,  
                WindowManager.LayoutParams. FLAG_FULLSCREEN); //保持屏幕高亮
	}

	//public static final String TAG = CameraView.class.getSimpleName();
    public static final String CAMERA_ID_KEY = "camera_id";
    public static final String CAMERA_FLASH_KEY = "flash_mode";
    public static final String PREVIEW_HEIGHT_KEY = "preview_height";
    /** 延迟拍摄停止 */
	private static final int HANDLE_STOP_RECORD = 1;
    
	/** 是否已经释放 */
	private volatile boolean mReleased;

//    private static final int PICTURE_SIZE_MAX_WIDTH = 1280;
//    private static final int PREVIEW_SIZE_MAX_WIDTH = 640;

//    private int mCameraID;
////    private String mFlashMode;
//    private Camera mCamera;
    
    @ViewById(id="camera_preview_view")
    //private SquareCameraPreview mPreviewView;
    private SurfaceView mSurfaceView;
    
    
    @ViewById(id="auto_flash_icon")
    private TextView autoFlashIcon;
    
    @ViewById(id="camera_preview_time")
    private TextView timeTextView;
    
    @ViewById(id="capture_image_button")
    private ImageView cameraImage;
    
    @ViewById(id="cover_bottom_view")
    private View coverBottomView;
    
    private Parameters params = null;
//    private SurfaceHolder mSurfaceHolder;

//    private int mDisplayOrientation;
//    private int mLayoutOrientation;

//    private int mPreviewHeight;

//    private CameraOrientationListener mOrientationListener;
    
//    private static int VEDIO_WIDTH = 320;
//    private static int VEDIO_HEIGHT = 240;
    @Override
//    public void onCreate(android.os.Bundle savedInstanceState) {
	protected void onInited(){
    	Nav nav = io.cess.core.Nav.getNav(this);

    	//nav.showTitle(false);
    	Object[] args = nav.getArgs();
    	if(args != null && args.length > 0 && args[0] instanceof Parameters){
    		params = (Parameters) args[0];
    	}else{
    		params = new Parameters();
    	}
    	
    	isRecording = false;
    	File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		if (DeviceUtils.isZte()) {
			if (dcim.exists()) {
				VCamera.setVideoCachePath(dcim + "/Camera/VCameraDemo/");
			} else {
				VCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/", "/sdcard-ext/") + "/Camera/VCameraDemo/");
			}
		} else {
			VCamera.setVideoCachePath(dcim + "/Camera/VCameraDemo/");
		}
		// 开启log输出,ffmpeg输出到logcat
		VCamera.setDebugMode(true);
		// 初始化拍摄SDK，必须
		VCamera.initialize(this.getActivity().getApplication());

//		try {
//			File mThemeCacheDir;
//            //获取传入参数
//            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && !isExternalStorageRemovable())
//                mThemeCacheDir = new File(getExternalCacheDir(), "Theme");
//            else
//                mThemeCacheDir = new File(getCacheDir(), "Theme");
//			ThemeHelper.prepareTheme(getApplication(), mThemeCacheDir);
//		} catch (OutOfMemoryError e) {
//			Logger.e(e);
//		} catch (Exception e) {
//			Logger.e(e);
//		}
		//解压assert里面的文件
		//startService(new Intent(this, AssertService.class));
    }
    
//    @Override
//    protected void onFirstAttachedToWindow() {
//    	
//    	UtilityAdapter.freeFilterParser();
//		UtilityAdapter.initFilterParser();
//		
//    	initMediaRecorder();
//    }
    
    @Override
    protected void onAttachedToWindow() {
    	super.onAttachedToWindow();
    	
    	ViewTreeObserver observer = mSurfaceView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
			@Override
            public void onGlobalLayout() {
                int width = mSurfaceView.getWidth();
//                mPreviewHeight = mPreviewView.getHeight();
                //mPreviewHeight = (int)(width * params.getHeight() * 1.0 / params.getWidth());
                //mPreviewView.getLayoutParams().height = mPreviewHeight;
                ViewGroup.LayoutParams p = mSurfaceView.getLayoutParams();
                p.height = width * 4 / 3;
                mSurfaceView.setTranslationY((p.height - width) / 2);
                
                ViewGroup.LayoutParams p2 = coverBottomView.getLayoutParams();
                p2.height = p.height - width;
                coverBottomView.setTranslationY((p.height) / 2);
                //mPreviewView.setLayoutParams(p);
//                mCoverHeight = (mPreviewHeight - width) / 2;
//                mCoverHeight = 1;

//                Log.d(TAG, "preview width " + width + " height " + mPreviewHeight);
//                topCoverView.getLayoutParams().height = mCoverHeight;
                //btnCoverView.getLayoutParams().height = mCoverHeight;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                	mSurfaceView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                	mSurfaceView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }
    
//    @Override
//	public void onResume() {
//		super.onResume();
//		UtilityAdapter.freeFilterParser();
//		UtilityAdapter.initFilterParser();
////
//		if (mMediaRecorder == null) {
//			initMediaRecorder();
//		} else {
////			mRecordLed.setChecked(false);
//			mMediaRecorder.prepare();
////			mProgressView.setData(mMediaObject);
//		}
//	}
    
    @Override
    protected void onDetachedFromWindow() {
    	super.onDetachedFromWindow();
    	releaseCamera();
    }
    private void releaseCamera(){
    	stopRecord();
		UtilityAdapter.freeFilterParser();
		if (!mReleased) {
			if (mMediaRecorder != null)
				mMediaRecorder.release();
		}
		mReleased = false;
    }
//    @Override
//	public void onPause() {
//		super.onPause();
//		releaseCamera();
//	}

    private MediaRecorderNative mMediaRecorder;
    private MediaObject mMediaObject;
    //private boolean mRebuild;
    /** 初始化拍摄SDK */
	private void initMediaRecorder() {
		mMediaRecorder = new MediaRecorderNative();
		//mMediaRecorder.setOnEncodeListener(this);
		//mRebuild = true;

		mMediaRecorder.setOnErrorListener(this);
		mMediaRecorder.setOnEncodeListener(this);
		File f = new File(VCamera.getVideoCachePath());
		if (!FileUtils.checkFile(f)) {
			f.mkdirs();
		}
		//if (NetworkUtils.isWifiAvailable(this)) {
		    //mMediaRecorder.setVideoEncodingBitRate(4*1024*1024);
		//mMediaRecorder.setVideoBitRate(4*1024*1024);
		    //WIFI下800码率
//		    }else{
//		    mMediaRecorder.setVideoEncodingBitRate(MediaRecorder.VIDEO_BITRATE_NORMAL);
//		    //3G、2G下600码率
//		}
		String key = String.valueOf(System.currentTimeMillis());
		mMediaObject = mMediaRecorder.setOutputDirectory(key, VCamera.getVideoCachePath() + key);
		mMediaRecorder.setSurfaceHolder(mSurfaceView.getHolder());
		mMediaRecorder.prepare();
	}
    
    
    @Click(id="change_camera")
    private void swapCameraClick(){
    	mMediaRecorder.switchCamera();
  }
    @Click(id="capture_image_button")
    private void takeCaptureClick(){
    	
    	//params.setType(Parameters.Type.Vedio);
    	
//    	if(params.getType() == Parameters.Type.Picture){
//    		takePicture();
//    	}else{
    		takeVideo();
//    	}
    	
    }
    
    private void takeVideo(){
    	if(isRecording){
    		this.stopRecord();
    		isRecording = false;
    	}else{
    		this.startRecord();
    		isRecording = true;
    	}
    }
    
    
  private Handler handler = new Handler(){

	@Override
	public void handleMessage(Message msg) {
		switch(msg.what){
		case HANDLE_STOP_RECORD:
			stopRecord();
			break;
		}
	}
	  
  };
  private volatile boolean isRecording = false;
  private volatile Date startTime = null;
  private static Format format = new SimpleDateFormat("HH:mm:ss");
  private static Calendar calendar = new GregorianCalendar();
  
  private Runnable task = new Runnable() {
	    public void run() {
	      if (isRecording) {
	        handler.postDelayed(this, 100);
	        timeTextView.setText(format.format(new Date(new Date().getTime() - startTime.getTime() - calendar.get(Calendar.ZONE_OFFSET))));
	      }
	    }
	  };
	  
    /** 开始录制 */
	private void startRecord() {
		if(isRecording){
			return;
		}
		isRecording = true;

		startTime = new Date();
		this.cameraImage.setImageDrawable(this.getContext().getResources().getDrawable(R.drawable.io_cess_core_camera_recording));
		if (mMediaRecorder != null) {
			MediaObject.MediaPart part = mMediaRecorder.startRecord();
			if (part == null) {
				return;
			}
			timeTextView.setVisibility(View.VISIBLE);
			handler.postDelayed(task, 100);
			handler.removeMessages(HANDLE_STOP_RECORD);
			if(params.getMaxDuration() > 0){
				handler.sendEmptyMessageDelayed(HANDLE_STOP_RECORD, params.getMaxDuration() - mMediaObject.getDuration() + 500);
			}
			//如果使用MediaRecorderSystem，不能在中途切换前后摄像头，否则有问题
			//if (mMediaRecorder instanceof MediaRecorderSystem) {
				//mCameraSwitch.setVisibility(View.GONE);
			//}
			//mProgressView.setData(mMediaObject);
		}

		//mRebuild = true;
		//mPressedStatus = true;
		//mRecordController.setImageResource(R.drawable.record_controller_press);
		//mBottomLayout.setBackgroundColor(mBackgroundColorPress);

//		if (mHandler != null) {
//			mHandler.removeMessages(HANDLE_INVALIDATE_PROGRESS);
//			mHandler.sendEmptyMessage(HANDLE_INVALIDATE_PROGRESS);
//
//			mHandler.removeMessages(HANDLE_STOP_RECORD);
//			mHandler.sendEmptyMessageDelayed(HANDLE_STOP_RECORD, RECORD_TIME_MAX - mMediaObject.getDuration());
//		}
//		mRecordDelete.setVisibility(View.GONE);
//		mCameraSwitch.setEnabled(false);
//		mRecordLed.setEnabled(false);
	}
	
	/** 停止录制 */
	private void stopRecord() {
		if(isRecording == false){
			return;
		}
		isRecording = false;
		this.cameraImage.setImageDrawable(this.getContext().getResources().getDrawable(R.drawable.io_cess_core_camera_recorder));
//
		if (mMediaRecorder != null && mMediaObject.getDuration() > params.getMinDuration()) {
			mMediaRecorder.stopRecord();
			
			if (mMediaObject != null) {
				MediaObject.MediaPart part = mMediaObject.getCurrentPart();
				if (part != null) {
					if (part.remove) {
//						part.remove = false;
//						mRecordDelete.setChecked(false);
//						if (mProgressView != null)
//							mProgressView.invalidate();
					}
				}
			}
			
			mMediaRecorder.startEncoding();
			
		}
//
//		mRecordDelete.setVisibility(View.VISIBLE);
//		mCameraSwitch.setEnabled(true);
//		mRecordLed.setEnabled(true);
//
//		mHandler.removeMessages(HANDLE_STOP_RECORD);
//		checkStatus();
	}
	
	@Override
	public void onEncodeStart() {
		//showProgress("", getString(R.string.record_camera_progress_message));
		showProgress("","正在生成视频，请稍候...",-1);
		
	}
	public ProgressDialog mProgressDialog;
	public ProgressDialog showProgress(String title, String message, int theme) {
		//if (mProgressDialog == null) {
			if (theme > 0)
				mProgressDialog = new ProgressDialog(this.getActivity(), theme);
			else
				mProgressDialog = new ProgressDialog(this.getActivity());
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mProgressDialog.setCanceledOnTouchOutside(false);// 不能取消
			mProgressDialog.setIndeterminate(true);// 设置进度条是否不明确
		//}

		if (!StringUtils.isEmpty(title))
			mProgressDialog.setTitle(title);
		mProgressDialog.setMessage(message);
		mProgressDialog.show();
		return mProgressDialog;
	}

	@Override
	public void onEncodeProgress(int progress) {
		//Logger.e("[MediaRecorderActivity]onEncodeProgress..." + progress);
	}

	/** 转码完成 */
	@Override
	public void onEncodeComplete() {
		//System.out.println("mMediaObject.getOutputTempVideoPath():"+mMediaObject.getOutputTempVideoPath());
		for(MediaPart part : mMediaObject.getMedaParts()){
			if(part != null){
				part.delete();
			}
		}
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
		Nav.getNav(this).pop(mMediaObject.getOutputVideoPath());
		//hideProgress();
//		Intent intent = new Intent(this, MediaPreviewActivity.class);
//		Bundle bundle = getIntent().getExtras();
//		if (bundle == null)
//			bundle = new Bundle();
//		bundle.putSerializable(CommonIntentExtra.EXTRA_MEDIA_OBJECT, mMediaObject);
//		bundle.putString("output", mMediaObject.getOutputTempVideoPath());
//		bundle.putBoolean("Rebuild", mRebuild);
//		intent.putExtras(bundle);
//		startActivity(intent);
//		mRebuild = false;
	}
	
	 public void onEncodeError(){
		 if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
		 new File(mMediaObject.getOutputVideoPath()).delete();
		 Nav.getNav(this).pop();
	 }
    
    
    
    

   
    
//    private void stopVedio(){
//        mediaRecorder.stop();
//        timeTextView.setText(format.format(new Date(new Date().getTime() - startTime.getTime() - calendar.get(Calendar.ZONE_OFFSET))));
//        mediaRecorder.release();
//        mediaRecorder = null;
//        this.cameraImage.setImageDrawable(this.getContext().getResources().getDrawable(R.drawable.camera_recorder));
//        //FormatUtil.videoRename(mRecAudioFile);
//        //Log.e(tag, "=====录制完成，已保存=====");
//        try {
//          //mCamera = Camera.open();
//          this.setupCamera();
////          Camera.Parameters parameters = camera.getParameters();
//////						parameters.setPreviewFrameRate(5); // 每秒5帧
////          parameters.setPictureFormat(ImageFormat.JPEG);// 设置照片的输出格式
////          parameters.set("jpeg-quality", 85);// 照片质量
////          camera.setParameters(parameters);
////          camera.setPreviewDisplay(surfaceHolder);
////          camera.startPreview();
////          isPreview = true;
//        } catch (Exception e) {
//          e.printStackTrace();
//        }
//    }
    
//    private void takePicture(){
//        //mOrientationListener.rememberOrientation();
//
//        // Shutter callback occurs after the image is captured. This can
//        // be used to trigger a sound to let the user know that image is taken
//        Camera.ShutterCallback shutterCallback = null;
//
//        // Raw callback occurs when the raw image data is available
//        Camera.PictureCallback raw = null;
//
//        // postView callback occurs when a scaled, fully processed
//        // postView image is available.
//        Camera.PictureCallback postView = null;
//
//        // jpeg callback occurs when the compressed image is available
//        mCamera.takePicture(shutterCallback, raw, postView, new Camera.PictureCallback() {
//			
//            /**
//             * 
//             */
//        	@Override
//        	public void onPictureTaken(byte[] data, Camera camera) {
////              int rotation = (
////              mDisplayOrientation
////                      + mOrientationListener.getRememberedNormalOrientation()
////                      + mLayoutOrientation
//        //) % 360;
//        //
//        ////Bitmap bitmap = ImageUtility.rotatePicture(getActivity(), rotation, data);
//        ////Uri uri = ImageUtility.savePicture(getActivity(), bitmap);
//        //getFragmentManager()
////              .beginTransaction()
////              .replace(
////                      R.id.fragment_container,
////                      EditSavePhotoFragment.newInstance(data, rotation, mCoverHeight, mPreviewHeight),
////                      EditSavePhotoFragment.TAG)
////              .addToBackStack(null)
////              .commit();
//        	}
//		});
//    }

//  final ImageView swapCameraBtn = (ImageView) view.findViewById(R.id.change_camera);

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        outState.putInt(CAMERA_ID_KEY, mCameraID);
//        outState.putString(CAMERA_FLASH_KEY, mFlashMode);
//        outState.putInt(PREVIEW_HEIGHT_KEY, mPreviewHeight);
//        super.onSaveInstanceState(outState);
//    }

//    private void getCamera(int cameraID) {
//        try {
//            mCamera = Camera.open(cameraID);
//            //mPreviewView.setCamera(mCamera);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * Start the camera preview
     */
//    private void startCameraPreview() {
//        determineDisplayOrientation();
//        setupCamera();
//
//        try {
//            mCamera.setPreviewDisplay(mSurfaceHolder);
//            mCamera.startPreview();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * Stop the camera preview
     */
//    private void stopCameraPreview() {
//        //mCamera.stopPreview();
//       // mPreviewView.setCamera(null);
//    }
//
//    /**
//     * Determine the current display orientation and rotate the camera preview
//     * accordingly
//     */
//    private void determineDisplayOrientation() {
//        CameraInfo cameraInfo = new CameraInfo();
//        Camera.getCameraInfo(mCameraID, cameraInfo);
//
//        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
//        int degrees = 0;
//
//        switch (rotation) {
//            case Surface.ROTATION_0: {
//                degrees = 0;
//                break;
//            }
//            case Surface.ROTATION_90: {
//                degrees = 90;
//                break;
//            }
//            case Surface.ROTATION_180: {
//                degrees = 180;
//                break;
//            }
//            case Surface.ROTATION_270: {
//                degrees = 270;
//                break;
//            }
//        }
//
//        int displayOrientation;
//
//        // Camera direction
//        if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
//            // Orientation is angle of rotation when facing the camera for
//            // the camera image to match the natural orientation of the device
//            displayOrientation = (cameraInfo.orientation + degrees) % 360;
//            displayOrientation = (360 - displayOrientation) % 360;
//        } else {
//            displayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
//        }
//
////        mDisplayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
////        mLayoutOrientation = degrees;
//        System.out.println("displayOrientation:"+displayOrientation);
//        mCamera.setDisplayOrientation(displayOrientation);
//    }

    /**
     * Setup the camera parameters
     */
//    private void setupCamera() {
//        // Never keep a global parameters
//        Camera.Parameters parameters = mCamera.getParameters();
//
//        parameters.setRotation(90);
////        Size bestPreviewSize = determineBestPreviewSize(parameters);
////        Size bestPictureSize = determineBestPictureSize(parameters);
//
////        parameters.setPreviewSize(bestPreviewSize.width, bestPreviewSize.height);
////        parameters.setPictureSize(bestPictureSize.width, bestPictureSize.height);
////        parameters.setPreviewSize(VEDIO_WIDTH,VEDIO_HEIGHT);
////        parameters.setPictureSize(VEDIO_WIDTH,VEDIO_HEIGHT);
//        
//        parameters.setPreviewSize(params.getWidth(),params.getHeight());
//        parameters.setPictureSize(params.getWidth(),params.getHeight());
//        
////        parameters.setPreviewSize(params.getHeight(),params.getWidth());
////        parameters.setPictureSize(params.getHeight(),params.getWidth());
//
//
//        // Set continuous picture focus, if it's supported
//        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
//            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//        }
//
////        final View changeCameraFlashModeBtn = getView().findViewById(R.id.flash);
////        List<String> flashModes = parameters.getSupportedFlashModes();
////        if (flashModes != null && flashModes.contains(mFlashMode)) {
////            parameters.setFlashMode(mFlashMode);
////            changeCameraFlashModeBtn.setVisibility(View.VISIBLE);
////        } else {
////            changeCameraFlashModeBtn.setVisibility(View.INVISIBLE);
////        }
//
//        // Lock in the changes
//        mCamera.setParameters(parameters);
//    }


//    private void restartPreview() {
//        stopCameraPreview();
//        mCamera.release();
//
//        getCamera(mCameraID);
//        startCameraPreview();
//    }

//    private int getFrontCameraID() {
//        PackageManager pm = getActivity().getPackageManager();
//        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
//            return CameraInfo.CAMERA_FACING_FRONT;
//        }
//
//        return getBackCameraID();
//    }

//    private int getBackCameraID() {
//        return CameraInfo.CAMERA_FACING_BACK;
//    }
//
//
//    @Override
//    protected void onDetachedFromWindow() {
//        //mOrientationListener.disable();
//
//        // stop the preview
//        stopCameraPreview();
//        mCamera.release();
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != Activity.RESULT_OK) return;
//
//        switch (requestCode) {
//            case 1:
//                Uri imageUri = data.getData();
//                break;
//
//            default:
//                super.onActivityResult(requestCode, resultCode, data);
//        }
//    }




   
}

///**
// * When orientation changes, onOrientationChanged(int) of the listener will be called
// */
//private static class CameraOrientationListener extends OrientationEventListener {
//
//    public CameraOrientationListener(Context context) {
//        super(context, SensorManager.SENSOR_DELAY_NORMAL);
//    }
//
//    @Override
//    public void onOrientationChanged(int orientation) {
//        if (orientation != ORIENTATION_UNKNOWN) {
//            normalize(orientation);
//        }
//    }
//
//    private int normalize(int degrees) {
//        if (degrees > 315 || degrees <= 45) {
//            return 0;
//        }
//
//        if (degrees > 45 && degrees <= 135) {
//            return 90;
//        }
//
//        if (degrees > 135 && degrees <= 225) {
//            return 180;
//        }
//
//        if (degrees > 225 && degrees <= 315) {
//            return 270;
//        }
//
//        throw new RuntimeException("The physics as we know them are no more. Watch out for anomalies.");
//    }
//
//    public void rememberOrientation() {
//    }
//}

//private Size determineBestPreviewSize(Camera.Parameters parameters) {
//  return determineBestSize(parameters.getSupportedPreviewSizes(), PREVIEW_SIZE_MAX_WIDTH);
//}
//
//private Size determineBestPictureSize(Camera.Parameters parameters) {
//  return determineBestSize(parameters.getSupportedPictureSizes(), PICTURE_SIZE_MAX_WIDTH);
//}
//
//private Size determineBestSize(List<Size> sizes, int widthThreshold) {
//  Size bestSize = null;
//  Size size;
//  int numOfSizes = sizes.size();
//  for (int i = 0; i < numOfSizes; i++) {
//      size = sizes.get(i);
//      boolean isDesireRatio = (size.width / 4) == (size.height / 3);
//      boolean isBetterSize = (bestSize == null) || size.width > bestSize.width;
//
//      if (isDesireRatio && isBetterSize) {
//          bestSize = size;
//      }
//  }
//
//  if (bestSize == null) {
//      Log.d(TAG, "cannot find the best camera size");
//      return sizes.get(sizes.size() - 1);
//  }
//
//  return bestSize;
//}
