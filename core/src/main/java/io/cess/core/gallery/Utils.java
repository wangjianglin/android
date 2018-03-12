//package io.cess.core.gallery;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.provider.MediaStore;
//import android.widget.Toast;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Date;
//
//import io.cess.core.R;
//import io.cess.core.gallery.model.PhotoInfo;
//
///**
// * @author lin
 //* @date 07/03/2017.
// */
//
//public class Utils {
//
//    static void resultData(Context context, ArrayList<PhotoInfo> photoList) {
//        GalleryFinal.OnHanlderResultCallback callback = GalleryFinal.getCallback();
//        int requestCode = GalleryFinal.getRequestCode();
//        if (callback != null) {
//            if ( photoList != null && photoList.size() > 0 ) {
//                callback.onHanlderSuccess(requestCode, photoList);
//            } else {
//                callback.onHanlderFailure(requestCode, context.getString(R.string.io_cess_core_grallery_photo_list_empty));
//            }
//        }
//        //finishGalleryFinalPage();
//    }
//
//    static void toast(Context context,String msg) {
//        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
//    }
//
//
//    private static boolean mTakePhotoAction;//打开相机动作
//    /**
//     * 拍照
//     */
//    protected static void takePhotoAction(Activity activity) {
//        if (!DeviceUtils.existSDCard()) {
//            String errormsg = activity.getString(R.string.io_cess_core_grallery_empty_sdcard);
//            toast(activity,errormsg);
//            if (mTakePhotoAction) {
//                resultFailure(errormsg, true);
//            }
//            return;
//        }
//
//        File takePhotoFolder = null;
////        if (StringUtils.isEmpty(mPhotoTargetFolder)) {
//            takePhotoFolder = GalleryFinal.getCoreConfig().getTakePhotoFolder();
////        } else {
////            takePhotoFolder = new File(mPhotoTargetFolder);
////        }
//        boolean suc = FileUtils.mkdirs(takePhotoFolder);
//        File toFile = new File(takePhotoFolder, "IMG" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".jpg");
//
//        //ILogger.d("create folder=" + toFile.getAbsolutePath());
//        if (suc) {
//            Uri mTakePhotoUri = Uri.fromFile(toFile);
//            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mTakePhotoUri);
//            activity.startActivityForResult(captureIntent, GalleryFinal.TAKE_REQUEST_CODE);
//        } else {
//            takePhotoFailure(activity);
//            //ILogger.e("create file failure");
//        }
//    }
//
//    static void takePhotoFailure(Context context) {
//        String errormsg = context.getString(R.string.io_cess_core_grallery_take_photo_fail);
//        if (mTakePhotoAction) {
//            resultFailure(errormsg, true);
//        } else {
//            toast(context,errormsg);
//        }
//    }
//
//    static void resultFailure(String errormsg, boolean delayFinish) {
//        GalleryFinal.OnHanlderResultCallback callback = GalleryFinal.getCallback();
//        int requestCode = GalleryFinal.getRequestCode();
//        if ( callback != null ) {
//            callback.onHanlderFailure(requestCode, errormsg);
//        }
////        if(delayFinish) {
////            finishGalleryFinalPage();
////        } else {
////            finishGalleryFinalPage();
////        }
//    }
//}
