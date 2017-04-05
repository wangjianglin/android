package lin.core.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lin.core.R;
import lin.core.ResFragment;
import lin.core.ViewActivity;
import lin.core.gallery.model.PhotoInfo;
import lin.core.gallery.permission.EasyPermissions;
import lin.core.gallery.utils.*;

import static android.app.Activity.RESULT_OK;

/**
 * Created by lin on 07/03/2017.
 */

public abstract class PhotoBaseFragment extends ResFragment implements EasyPermissions.PermissionCallbacks {
//}
//public abstract class PhotoBaseActivity extends ViewActivity implements EasyPermissions.PermissionCallbacks{

    protected static String mPhotoTargetFolder;

    protected Uri mTakePhotoUri;
    private MediaScanner mMediaScanner;

    protected int mScreenWidth = 720;
    protected int mScreenHeight = 1280;

    protected boolean mTakePhotoAction;//打开相机动作


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("takePhotoUri", mTakePhotoUri);
        outState.putString("photoTargetFolder", mPhotoTargetFolder);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
//    }
//
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null) {
            mTakePhotoUri = savedInstanceState.getParcelable("takePhotoUri");
            mPhotoTargetFolder = savedInstanceState.getString("photoTargetFolder");
        }
    }

    protected Handler mFinishHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            finishGalleryFinalPage();
        }
    };

    @Override
    protected void onCreateView() {
    //        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        super.onCreate(savedInstanceState);
//        ActivityManager.getActivityManager().addActivity(this);
        mMediaScanner = new MediaScanner(this.getContext());
        DisplayMetrics dm = DeviceUtils.getScreenPix(this.getActivity());
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaScanner != null) {
            mMediaScanner.unScanFile();
        }
//        ActivityManager.getActivityManager().finishActivity(this);
    }

    public void toast(String msg) {
        Toast.makeText(this.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 拍照
     */
    protected void takePhotoAction() {
        if (!DeviceUtils.existSDCard()) {
            String errormsg = getString(R.string.lin_core_grallery_empty_sdcard);
            toast(errormsg);
            if (mTakePhotoAction) {
                resultFailure(errormsg, true);
            }
            return;
        }

        File takePhotoFolder = null;
        if (StringUtils.isEmpty(mPhotoTargetFolder)) {
            takePhotoFolder = GalleryFinal.getCoreConfig().getTakePhotoFolder();
        } else {
            takePhotoFolder = new File(mPhotoTargetFolder);
        }
        boolean suc = FileUtils.mkdirs(takePhotoFolder);
        File toFile = new File(takePhotoFolder, "IMG" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".jpg");

        //ILogger.d("create folder=" + toFile.getAbsolutePath());
        if (suc) {
            mTakePhotoUri = Uri.fromFile(toFile);
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mTakePhotoUri);
            startActivityForResult(captureIntent, GalleryFinal.TAKE_REQUEST_CODE);
        } else {
            takePhotoFailure();
            //ILogger.e("create file failure");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( requestCode == GalleryFinal.TAKE_REQUEST_CODE ) {
            if (resultCode == RESULT_OK && mTakePhotoUri != null) {
                final String path = mTakePhotoUri.getPath();
                if (new File(path).exists()) {
                    final PhotoInfo info = new PhotoInfo();
                    info.setPhotoId(lin.core.gallery.utils.Utils.getRandom(10000, 99999));
                    info.setPhotoPath(path);
                    updateGallery(path);
                    takeResult(info);
                } else {
                    takePhotoFailure();
                }
            } else {
                takePhotoFailure();
            }
        }
    }

    private void takePhotoFailure() {
        String errormsg = getString(R.string.lin_core_grallery_take_photo_fail);
        if (mTakePhotoAction) {
            resultFailure(errormsg, true);
        } else {
            toast(errormsg);
        }
    }

    /**
     * 更新相册
     */
    private void updateGallery(String filePath) {
        if (mMediaScanner != null) {
            mMediaScanner.scanFile(filePath, "image/jpeg");
        }
    }

    protected void resultData(ArrayList<PhotoInfo> photoList) {
        GalleryFinal.OnHanlderResultCallback callback = GalleryFinal.getCallback();
        int requestCode = GalleryFinal.getRequestCode();
        if (callback != null) {
            if ( photoList != null && photoList.size() > 0 ) {
                callback.onHanlderSuccess(requestCode, photoList);
            } else {
                callback.onHanlderFailure(requestCode, getString(R.string.lin_core_grallery_photo_list_empty));
            }
        }
//        finishGalleryFinalPage();
    }

    protected void resultFailureDelayed(String errormsg, boolean delayFinish) {
        GalleryFinal.OnHanlderResultCallback callback = GalleryFinal.getCallback();
        int requestCode = GalleryFinal.getRequestCode();
        if ( callback != null ) {
            callback.onHanlderFailure(requestCode, errormsg);
        }
        if(delayFinish) {
            mFinishHanlder.sendEmptyMessageDelayed(0, 500);
        } else {
//            finishGalleryFinalPage();
        }
    }

    protected void resultFailure(String errormsg, boolean delayFinish) {
        GalleryFinal.OnHanlderResultCallback callback = GalleryFinal.getCallback();
        int requestCode = GalleryFinal.getRequestCode();
        if ( callback != null ) {
            callback.onHanlderFailure(requestCode, errormsg);
        }
//        if(delayFinish) {
//            finishGalleryFinalPage();
//        } else {
//            finishGalleryFinalPage();
//        }
    }

//    private void finishGalleryFinalPage() {
////        ActivityManager.getActivityManager().finishActivity(PhotoEditActivity.class);
////        ActivityManager.getActivityManager().finishActivity(PhotoSelectActivity.class);
////        Global.mPhotoSelectActivity = null;
////        System.gc();
//    }

    protected abstract void takeResult(PhotoInfo info);

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(List<String> list) {
    }

    @Override
    public void onPermissionsDenied(List<String> list) {
    }
}
