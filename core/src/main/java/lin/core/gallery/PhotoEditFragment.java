/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package lin.core.gallery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import lin.core.R;
import lin.core.annotation.ResCls;
import lin.core.annotation.ResId;
import lin.core.annotation.ViewById;
import lin.core.gallery.adapter.PhotoEditListAdapter;
import lin.core.gallery.model.PhotoInfo;
import lin.core.gallery.model.PhotoTempModel;
import lin.core.gallery.utils.RecycleViewBitmapUtils;
import lin.core.gallery.utils.Utils;
import lin.core.gallery.widget.FloatingActionButton;
import lin.core.gallery.widget.HorizontalListView;
import lin.core.gallery.widget.crop.CropImageFragment;
import lin.core.gallery.widget.crop.CropImageView;
import lin.core.gallery.widget.zoonview.PhotoView;


/**
 * Desction:图片裁剪
 * Author:pengjianbo
 * Date:15/10/10 下午5:40
 */
@ResCls(R.class)
@ResId(id="lin_core_gallery_activity_photo_edit")
public class PhotoEditFragment extends CropImageFragment implements AdapterView.OnItemClickListener, View.OnClickListener {


    private final int CROP_SUC = 1;//裁剪成功
    private final int CROP_FAIL = 2;//裁剪失败
    private final int UPDATE_PATH = 3;//更新path


    @ViewById(id="lin_core_gallery_edit_crop_photo")
    private CropImageView mIvCropPhoto;

    @ViewById(id="lin_core_gallery_edit_source_photo")
    private PhotoView mIvSourcePhoto;

    @ViewById(id="lin_core_gallery_edit_empty_view")
    private TextView mTvEmptyView;

    @ViewById(id="lin_core_gallery_edit_lv_gallery")
    private HorizontalListView mLvGallery;

    @ViewById(id="lin_core_gallery_edit_gallery")
    private LinearLayout mLlGallery;

    private ArrayList<PhotoInfo> mPhotoList;
    private PhotoEditListAdapter mPhotoEditListAdapter;
    private int mSelectIndex = 0;
    private boolean mCropState;
    private ProgressDialog mProgressDialog;
    private boolean mRotating;

    private ArrayList<PhotoInfo> mSelectPhotoList;
    private LinkedHashMap<Integer, PhotoTempModel> mPhotoTempMap;
    private File mEditPhotoCacheFile;

    private Drawable mDefaultDrawable;


    private boolean mCropPhotoAction;//裁剪图片动作
    private boolean mEditPhotoAction;//编辑图片动作

//    @Override
//    public void onViewStateRestored(@Nullable Bundle outState) {
//        super.onViewStateRestored(outState);
////    }
//
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("selectPhotoMap", mSelectPhotoList);
        outState.putSerializable("editPhotoCacheFile", mEditPhotoCacheFile);
        outState.putSerializable("photoTempMap", mPhotoTempMap);

        outState.putInt("selectIndex", mSelectIndex);
        outState.putBoolean("cropState", mCropState);
        outState.putBoolean("rotating", mRotating);

        outState.putBoolean("takePhotoAction", mTakePhotoAction);
        outState.putBoolean("cropPhotoAction", mCropPhotoAction);
        outState.putBoolean("editPhotoAction", mEditPhotoAction);

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mSelectPhotoList = new ArrayList<>();//(ArrayList<PhotoInfo>) getIntent().getSerializableExtra("selectPhotoMap");
        mEditPhotoCacheFile = (File) savedInstanceState.getSerializable("editPhotoCacheFile");
        mPhotoTempMap = new LinkedHashMap<>((HashMap<Integer, PhotoTempModel>) this.getActivity().getIntent().getSerializableExtra("results"));

        mSelectIndex = savedInstanceState.getInt("selectIndex");
        mCropState = savedInstanceState.getBoolean("cropState");
        mRotating = savedInstanceState.getBoolean("rotating");

        mTakePhotoAction = savedInstanceState.getBoolean("takePhotoAction");
        mCropPhotoAction = savedInstanceState.getBoolean("cropPhotoAction");
        mEditPhotoAction = savedInstanceState.getBoolean("editPhotoAction");
    }

    private android.os.Handler mHanlder = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if ( msg.what == CROP_SUC ) {
                String path = (String) msg.obj;
                PhotoInfo photoInfo = mPhotoList.get(mSelectIndex);
                try {
                    Iterator<Map.Entry<Integer, PhotoTempModel>> entries = mPhotoTempMap.entrySet().iterator();
                    while (entries.hasNext()) {
                        Map.Entry<Integer, PhotoTempModel> entry = entries.next();
                        if (entry.getKey() == photoInfo.getPhotoId()) {
                            PhotoTempModel tempModel = entry.getValue();
                            tempModel.setSourcePath(path);
                            tempModel.setOrientation(0);
                        }
                    }
                } catch (Exception e) {
                }
                toast(getString(R.string.lin_core_grallery_crop_suc));

                Message message = mHanlder.obtainMessage();
                message.what = UPDATE_PATH;
                message.obj = path;
                mHanlder.sendMessage(message);

            } else if ( msg.what == CROP_FAIL ) {
                toast(getString(R.string.lin_core_grallery_crop_fail));
            } else if ( msg.what == UPDATE_PATH ) {
                if (mPhotoList.get(mSelectIndex) != null) {
                    PhotoInfo photoInfo = mPhotoList.get(mSelectIndex);
                    String path = (String) msg.obj;
                    //photoInfo.setThumbPath(path);
                    try {
                        for(Iterator<PhotoInfo> iterator = mSelectPhotoList.iterator();iterator.hasNext();){
                            PhotoInfo info = iterator.next();
                            if (info != null && info.getPhotoId() == photoInfo.getPhotoId()) {
                                info.setPhotoPath(path);
                            }
                        }
                    } catch (Exception e) {
                    }
                    photoInfo.setPhotoPath(path);

                    loadImage(photoInfo);
                    mPhotoEditListAdapter.notifyDataSetChanged();
                }

                if (GalleryFinal.getFunctionConfig().isForceCrop() && !GalleryFinal.getFunctionConfig().isForceCropEdit()) {
                    resultAction();
                }
            }
            corpPageState(false);
            mCropState = false;
            //mTvTitle.setText(R.string.lin_core_grallery_photo_edit);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ( GalleryFinal.getFunctionConfig() == null || GalleryFinal.getGalleryTheme() == null) {
            resultFailureDelayed(getString(R.string.lin_core_grallery_please_reopen_gf), true);
        } else {
            //setContentView(R.layout.lin_core_gallery_activity_photo_edit);
            mDefaultDrawable = getResources().getDrawable(R.drawable.ic_gf_default_photo);

            mSelectPhotoList = new ArrayList<> ();//(ArrayList<PhotoInfo>) getIntent().getSerializableExtra(SELECT_MAP);
            mTakePhotoAction = false;//this.getIntent().getBooleanExtra(TAKE_PHOTO_ACTION, false);
            mCropPhotoAction = false;//this.getIntent().getBooleanExtra(CROP_PHOTO_ACTION, false);
            mEditPhotoAction = false;//this.getIntent().getBooleanExtra(EDIT_PHOTO_ACTION, false);

            if (mSelectPhotoList == null) {
                mSelectPhotoList = new ArrayList<>();
            }
            mPhotoTempMap = new LinkedHashMap<>();
            mPhotoList = new ArrayList<>(mSelectPhotoList);

            mEditPhotoCacheFile = GalleryFinal.getCoreConfig().getEditPhotoCacheFolder();

            if (mPhotoList == null) {
                mPhotoList = new ArrayList<>();
            }

            for (PhotoInfo info : mPhotoList) {
                mPhotoTempMap.put(info.getPhotoId(), new PhotoTempModel(info.getPhotoPath()));
            }

            findViews();
            setListener();
            setTheme();

            mPhotoEditListAdapter = new PhotoEditListAdapter(this, mPhotoList, mScreenWidth);
            mLvGallery.setAdapter(mPhotoEditListAdapter);

            try {
                File nomediaFile = new File(mEditPhotoCacheFile, ".nomedia");
                if (!nomediaFile.exists()) {
                    nomediaFile.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

//            if (GalleryFinal.getFunctionConfig().isCamera()) {
//                mIvTakePhoto.setVisibility(View.VISIBLE);
//            }
//
//            if (GalleryFinal.getFunctionConfig().isCrop()) {
//                mIvCrop.setVisibility(View.VISIBLE);
//            }
//
//            if (GalleryFinal.getFunctionConfig().isRotate()) {
//                mIvRotate.setVisibility(View.VISIBLE);
//            }

            if (!GalleryFinal.getFunctionConfig().isMutiSelect()) {
                mLlGallery.setVisibility(View.GONE);
            }

            initCrop(mIvCropPhoto, GalleryFinal.getFunctionConfig().isCropSquare(), GalleryFinal.getFunctionConfig().getCropWidth(), GalleryFinal.getFunctionConfig().getCropHeight());
            if (mPhotoList.size() > 0 && !mTakePhotoAction) {
                loadImage(mPhotoList.get(0));
            }

            if (mTakePhotoAction) {
                //打开相机
                takePhotoAction();
            }
            if (mCropPhotoAction) {
//                mIvCrop.performClick();
//                if ( !GalleryFinal.getFunctionConfig().isRotate() && !GalleryFinal.getFunctionConfig().isCamera()) {
//                    mIvCrop.setVisibility(View.GONE);
//                }
            } else {
                //判断是否强制裁剪
                hasForceCrop();
            }

            if(GalleryFinal.getFunctionConfig().isEnablePreview()){
                //mIvPreView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setTheme() {
//        mIvBack.setImageResource(GalleryFinal.getGalleryTheme().getIconBack());
//        if (GalleryFinal.getGalleryTheme().getIconBack() == R.drawable.ic_gf_back) {
//            mIvBack.setColorFilter(GalleryFinal.getGalleryTheme().getTitleBarIconColor());
//        }
//
//        mIvTakePhoto.setImageResource(GalleryFinal.getGalleryTheme().getIconCamera());
//        if (GalleryFinal.getGalleryTheme().getIconCamera() == R.drawable.ic_gf_camera) {
//            mIvTakePhoto.setColorFilter(GalleryFinal.getGalleryTheme().getTitleBarIconColor());
//        }
//
//        mIvCrop.setImageResource(GalleryFinal.getGalleryTheme().getIconCrop());
//        if (GalleryFinal.getGalleryTheme().getIconCrop() == R.drawable.ic_gf_crop) {
//            mIvCrop.setColorFilter(GalleryFinal.getGalleryTheme().getTitleBarIconColor());
//        }
//
//        mIvPreView.setImageResource(GalleryFinal.getGalleryTheme().getIconPreview());
//        if (GalleryFinal.getGalleryTheme().getIconPreview() == R.drawable.ic_gf_preview) {
//            mIvPreView.setColorFilter(GalleryFinal.getGalleryTheme().getTitleBarIconColor());
//        }
//
//        mIvRotate.setImageResource(GalleryFinal.getGalleryTheme().getIconRotate());
//        if (GalleryFinal.getGalleryTheme().getIconRotate() == R.drawable.ic_gf_rotate) {
//            mIvRotate.setColorFilter(GalleryFinal.getGalleryTheme().getTitleBarIconColor());
//        }

        if ( GalleryFinal.getGalleryTheme().getEditPhotoBgTexture() != null ) {
            mIvSourcePhoto.setBackgroundDrawable(GalleryFinal.getGalleryTheme().getEditPhotoBgTexture());
            mIvCropPhoto.setBackgroundDrawable(GalleryFinal.getGalleryTheme().getEditPhotoBgTexture());
        }

//        mFabCrop.setIcon(GalleryFinal.getGalleryTheme().getIconFab());
//        mTitlebar.setBackgroundColor(GalleryFinal.getGalleryTheme().getTitleBarBgColor());
//        mTvTitle.setTextColor(GalleryFinal.getGalleryTheme().getTitleBarTextColor());
//        mFabCrop.setColorPressed(GalleryFinal.getGalleryTheme().getFabPressedColor());
//        mFabCrop.setColorNormal(GalleryFinal.getGalleryTheme().getFabNornalColor());
    }

    private void findViews() {
//        mIvTakePhoto = (ImageView) findViewById(R.id.iv_take_photo);
//        mIvCropPhoto = (CropImageView) findViewById(R.id.lin_core_gallery_edit_crop_photo);
//        mIvSourcePhoto = (PhotoView) findViewById(R.id.lin_core_gallery_edit_source_photo);
//        mLvGallery = (HorizontalListView) findViewById(R.id.lin_core_gallery_edit_lv_gallery);
//        mLlGallery = (LinearLayout) findViewById(R.id.lin_core_gallery_edit_gallery);
//        mTvEmptyView = (TextView) findViewById(R.id.lin_core_gallery_edit_empty_view);
////        mFabCrop = (FloatingActionButton) findViewById(R.id.fab_crop);
//        mIvCrop = (ImageView) findViewById(R.id.iv_crop);
//        mIvRotate = (ImageView) findViewById(R.id.iv_rotate);
//        mTvTitle = (TextView) findViewById(R.id.tv_title);
//        mTitlebar = (LinearLayout) findViewById(R.id.titlebar);
//        mIvPreView = (ImageView) findViewById(R.id.iv_preview);
    }

    private void setListener() {
//        mIvTakePhoto.setOnClickListener(this);
//        mIvBack.setOnClickListener(this);
        mLvGallery.setOnItemClickListener(this);
//        mFabCrop.setOnClickListener(this);
//        mIvCrop.setOnClickListener(this);
//        mIvRotate.setOnClickListener(this);
//        mIvPreView.setOnClickListener(this);
    }

    @Override
    protected void takeResult(PhotoInfo info) {
        if (!GalleryFinal.getFunctionConfig().isMutiSelect()) {
            mPhotoList.clear();
            mSelectPhotoList.clear();
        }
        mPhotoList.add(0, info);
        mSelectPhotoList.add(info);
        mPhotoTempMap.put(info.getPhotoId(), new PhotoTempModel(info.getPhotoPath()));
        if (!GalleryFinal.getFunctionConfig().isEditPhoto() && mTakePhotoAction) {
            resultAction();
        } else {
            if (GalleryFinal.getFunctionConfig().isEnablePreview()) {
//                mIvPreView.setVisibility(View.VISIBLE);
            }
            mPhotoEditListAdapter.notifyDataSetChanged();

            PhotoSelectActivity activity = (PhotoSelectActivity) ActivityManager.getActivityManager().getActivity(PhotoSelectActivity.class.getName());
            if (activity != null) {
                activity.takeRefreshGallery(info, true);
            }
            loadImage(info);

            hasForceCrop();
        }
    }

    private void loadImage(PhotoInfo photo) {
        mTvEmptyView.setVisibility(View.GONE);
        mIvSourcePhoto.setVisibility(View.VISIBLE);
        mIvCropPhoto.setVisibility(View.GONE);

        String path = "";
        if (photo != null) {
            path = photo.getPhotoPath();
        }
        if (GalleryFinal.getFunctionConfig().isCrop()) {
            setSourceUri(Uri.fromFile(new File(path)));
        }

        GalleryFinal.getCoreConfig().getImageLoader().displayImage(this.getActivity(), path, mIvSourcePhoto, mDefaultDrawable, mScreenWidth, mScreenHeight);
    }

    public void deleteIndex(int position, PhotoInfo dPhoto) {
        if (dPhoto != null) {
            PhotoSelectActivity activity = (PhotoSelectActivity) ActivityManager.getActivityManager().getActivity(PhotoSelectActivity.class.getName());
            if (activity != null) {
                activity.deleteSelect(dPhoto.getPhotoId());
            }

            try {
                for(Iterator<PhotoInfo> iterator = mSelectPhotoList.iterator();iterator.hasNext();){
                    PhotoInfo info = iterator.next();
                    if (info != null && info.getPhotoId() == dPhoto.getPhotoId()) {
                        iterator.remove();
                        break;
                    }
                }
            } catch (Exception e){}
        }

        if (mPhotoList.size() == 0) {
            mSelectIndex = 0;
            mTvEmptyView.setText(R.string.lin_core_grallery_no_photo);
            mTvEmptyView.setVisibility(View.VISIBLE);
            mIvSourcePhoto.setVisibility(View.GONE);
            mIvCropPhoto.setVisibility(View.GONE);
//            mIvPreView.setVisibility(View.GONE);
        } else {
            if (position == 0) {
                mSelectIndex = 0;
            } else if (position == mPhotoList.size()) {
                mSelectIndex = position - 1;
            } else {
                mSelectIndex = position;
            }

            PhotoInfo photoInfo = mPhotoList.get(mSelectIndex);
            loadImage(photoInfo);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mSelectIndex = i;
        PhotoInfo photoInfo = mPhotoList.get(i);
        loadImage(photoInfo);
    }

//    @Override
    public void setCropSaveSuccess(final File file) {
        Message message = mHanlder.obtainMessage();
        message.what = CROP_SUC;
        message.obj = file.getAbsolutePath();
        mHanlder.sendMessage(message);
    }

//    @Override
    public void setCropSaveException(Throwable throwable) {
        mHanlder.sendEmptyMessage(CROP_FAIL);
    }

//    @Override
    public void onClick(View v) {
        int id = v.getId();
//        if (id == R.id.fab_crop) {
//            if (mPhotoList.size() == 0) {
//                return;
//            }
//            if (mCropState) {
//                System.gc();
//                PhotoInfo photoInfo = mPhotoList.get(mSelectIndex);
//                try {
//                    String ext = FilenameUtils.getExtension(photoInfo.getPhotoPath());
//                    File toFile;
//                    if (GalleryFinal.getFunctionConfig().isCropReplaceSource()) {
//                        toFile = new File(photoInfo.getPhotoPath());
//                    } else {
//                        toFile = new File(mEditPhotoCacheFile, Utils.getFileName(photoInfo.getPhotoPath()) + "_crop." + ext);
//                    }
//
//                    FileUtils.mkdirs(toFile.getParentFile());
//                    onSaveClicked(toFile);//保存裁剪
//                } catch (Exception e) {
//                    //ILogger.e(e);
//                }
//            } else { //完成选择
//                resultAction();
//            }
//        } else
//        if (id == R.id.iv_crop) {
//
//            if (mPhotoList.size() > 0) {
//                PhotoInfo photoInfo = mPhotoList.get(mSelectIndex);
//                String ext = FilenameUtils.getExtension(photoInfo.getPhotoPath());
//                if (StringUtils.isEmpty(ext) || !(ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg"))) {
//                    toast(getString(R.string.lin_core_grallery_edit_letoff_photo_format));
//                } else {
//                    if (mCropState) {
//                        setCropEnabled(false);
//
//                        corpPageState(false);
//
//                        mTvTitle.setText(R.string.lin_core_grallery_photo_edit);
//                    } else {
//                        corpPageState(true);
//                        setCropEnabled(true);
//
//                        mTvTitle.setText(R.string.lin_core_grallery_photo_crop);
//                    }
//                    mCropState = !mCropState;
//                }
//            }
//        } else if (id == R.id.iv_rotate) {
//            rotatePhoto();
//        } else if (id == R.id.iv_take_photo) {
//            if (GalleryFinal.getFunctionConfig().isMutiSelect() && GalleryFinal.getFunctionConfig().getMaxSize() == mSelectPhotoList.size()) {
//                toast(getString(R.string.lin_core_grallery_select_max_tips));
//            } else {
//                takePhotoAction();
//            }
//        } else if (id == R.id.iv_back) {
//            if (mCropState && !(mCropPhotoAction && !GalleryFinal.getFunctionConfig().isRotate() && !GalleryFinal.getFunctionConfig().isCamera())) {
//                if ((GalleryFinal.getFunctionConfig().isForceCrop() && GalleryFinal.getFunctionConfig().isForceCropEdit())) {
//                    mIvCrop.performClick();
//                    return;
//                }
//            }
//            finish();
//        } else if (id == R.id.iv_preview) {
//            Intent intent = new Intent(this, PhotoPreviewActivity.class);
//            intent.putExtra(PhotoPreviewActivity.PHOTO_LIST, mSelectPhotoList);
//            startActivity(intent);
//        }
    }

    private void resultAction() {
        resultData(mSelectPhotoList);
    }

    private void hasForceCrop() {
        if(GalleryFinal.getFunctionConfig().isForceCrop()) {
            //mIvCrop.performClick();//进入裁剪状态
            if(!GalleryFinal.getFunctionConfig().isForceCropEdit()) {//强制裁剪后是否可以编辑
                //mIvCrop.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 图片旋转
     */
    private void rotatePhoto() {
        if (mPhotoList.size() > 0 && mPhotoList.get(mSelectIndex) != null && !mRotating) {
            final PhotoInfo photoInfo = mPhotoList.get(mSelectIndex);
            final String ext = FilenameUtils.getExtension(photoInfo.getPhotoPath());
            if (StringUtils.isEmpty(ext) || !(ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg"))) {
                toast(getString(R.string.lin_core_grallery_edit_letoff_photo_format));
                return;
            }
            mRotating = true;
            if (photoInfo != null) {
                final PhotoTempModel photoTempModel = mPhotoTempMap.get(photoInfo.getPhotoId());
                final String path = photoTempModel.getSourcePath();

                File file;
                if (GalleryFinal.getFunctionConfig().isRotateReplaceSource()) { //裁剪覆盖源文件
                    file = new File(path);
                } else {
                    file = new File(mEditPhotoCacheFile, Utils.getFileName(path) + "_rotate." + ext);
                }

                final File rotateFile = file;
                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        mTvEmptyView.setVisibility(View.VISIBLE);
                        mProgressDialog = ProgressDialog.show(getContext(), "", getString(R.string.lin_core_grallery_waiting), true, false);
                    }

                    @Override
                    protected Bitmap doInBackground(Void... params) {
                        int orientation;
                        if ( GalleryFinal.getFunctionConfig().isRotateReplaceSource() ) {
                            orientation = 90;
                        } else {
                            orientation = photoTempModel.getOrientation() + 90;
                        }
                        Bitmap bitmap = Utils.rotateBitmap(path, orientation, mScreenWidth, mScreenHeight);
                        if (bitmap != null) {
                            Bitmap.CompressFormat format;
                            if ( ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg") ) {
                                format = Bitmap.CompressFormat.JPEG;
                            } else {
                                format = Bitmap.CompressFormat.PNG;
                            }
                            Utils.saveBitmap(bitmap, format, rotateFile);
                        }
                        return bitmap;
                    }

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        super.onPostExecute(bitmap);
                        if (mProgressDialog != null) {
                            mProgressDialog.dismiss();
                            mProgressDialog = null;
                        }
                        if (bitmap != null) {
                            bitmap.recycle();
                            
                            mTvEmptyView.setVisibility(View.GONE);

                            if ( !GalleryFinal.getFunctionConfig().isRotateReplaceSource() ) {
                                int orientation = photoTempModel.getOrientation() + 90;
                                if (orientation == 360) {
                                    orientation = 0;
                                }
                                photoTempModel.setOrientation(orientation);
                            }

                            Message message = mHanlder.obtainMessage();
                            message.what = UPDATE_PATH;
                            message.obj = rotateFile.getAbsolutePath();
                            mHanlder.sendMessage(message);
                        } else {
                            mTvEmptyView.setText(R.string.lin_core_grallery_no_photo);
                        }
                        loadImage(photoInfo);
                        mRotating = false;
                    }
                }.execute();
            }
        }
    }

    private void corpPageState(boolean crop) {
        if (crop) {
            mIvSourcePhoto.setVisibility(View.GONE);
            mIvCropPhoto.setVisibility(View.VISIBLE);
            mLlGallery.setVisibility(View.GONE);
            if (GalleryFinal.getFunctionConfig().isCrop()) {
                //mIvCrop.setVisibility(View.VISIBLE);
            }
            if (GalleryFinal.getFunctionConfig().isRotate()) {
                //mIvRotate.setVisibility(View.GONE);
            }

            if (GalleryFinal.getFunctionConfig().isCamera()) {
                //mIvTakePhoto.setVisibility(View.GONE);
            }
        } else {
            mIvSourcePhoto.setVisibility(View.VISIBLE);
            mIvCropPhoto.setVisibility(View.GONE);
            if (GalleryFinal.getFunctionConfig().isCrop()) {
                //mIvCrop.setVisibility(View.VISIBLE);
            }
            if (GalleryFinal.getFunctionConfig().isRotate()) {
                //mIvRotate.setVisibility(View.VISIBLE);
            }

            if (GalleryFinal.getFunctionConfig().isCamera()) {
                //mIvTakePhoto.setVisibility(View.VISIBLE);
            }

            if (GalleryFinal.getFunctionConfig().isMutiSelect()) {
                mLlGallery.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RecycleViewBitmapUtils.recycleImageView(mIvCropPhoto);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (mCropState && !(mCropPhotoAction && !GalleryFinal.getFunctionConfig().isRotate() && !GalleryFinal.getFunctionConfig().isCamera())) {
//                if ((GalleryFinal.getFunctionConfig().isForceCrop() && GalleryFinal.getFunctionConfig().isForceCropEdit())) {
//                    //mIvCrop.performClick();
//                    return true;
//                }
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }



//    //=======================================================================================
//
//    private int aspectX;
//    private int aspectY;
//
//    // Output image
//    private int maxX;
//    private int maxY;
//    private int exifRotation;
//
//    private Uri sourceUri;
//
//    private boolean isSaving;
//
//    private int sampleSize;
//    private RotateBitmap rotateBitmap;
//    private CropImageView imageView;
//    private HighlightView cropView;
//
//    private final Handler handler = new Handler();
//    private boolean cropEnabled;
//
//    public void initCrop(CropImageView imageView, boolean square, int maxX, int maxY) {
//        if (square) {
//            this.aspectX = 1;
//            this.aspectY = 1;
//        }
//        this.maxX = maxX;
//        this.maxY = maxY;
//        this.imageView = imageView;
//
//        imageView.context = this.getContext();
//        imageView.setRecycler(new ImageViewTouchBase.Recycler() {
//            @Override
//            public void recycle(Bitmap b) {
//                b.recycle();
//                System.gc();
//            }
//        });
//    }
//
//
//    public void setSourceUri(Uri sourceUri) {
//        if(rotateBitmap != null) {
//            rotateBitmap.recycle();
//            rotateBitmap = null;
//        }
//        this.sourceUri = sourceUri;
//        this.isSaving = false;
//        this.sampleSize = 0;
//        this.rotateBitmap = null;
//        this.cropView = null;
//        this.imageView.clear();
//        if (sourceUri != null) {
//            exifRotation = CropUtil.getExifRotation(CropUtil.getFromMediaUri(this, getContentResolver(), sourceUri));
//
//            InputStream is = null;
//            try {
//                sampleSize = calculateBitmapSampleSize(sourceUri);
//                is = getContentResolver().openInputStream(sourceUri);
//                BitmapFactory.Options option = new BitmapFactory.Options();
//                option.inSampleSize = sampleSize;
//                rotateBitmap = new RotateBitmap(BitmapFactory.decodeStream(is, null, option), exifRotation);
//            } catch (IOException e) {
//                //ILogger.e(e);
////                setCropSaveException(e);
//            } catch (OutOfMemoryError e) {
//                //ILogger.e(e);
////                setCropSaveException(e);
//            } finally {
//                CropUtil.closeSilently(is);
//            }
//        }
//
//        //if (rotateBitmap == null) {
//        //    finish();
//        //    return;
//        //}
//        if (rotateBitmap != null) {
//            startCrop();
//        }
//    }
//
//    private void startCrop() {
//        if (this.getActivity().isFinishing()) {
//            return;
//        }
//        imageView.setImageRotateBitmapResetBase(rotateBitmap, true);
//        CropUtil.startBackgroundJob(this, null, getResources().getString(R.string.lin_core_grallery_waiting),
//                new Runnable() {
//                    public void run() {
//                        final CountDownLatch latch = new CountDownLatch(1);
//                        handler.post(new Runnable() {
//                            public void run() {
//                                if (imageView.getScale() == 1F) {
//                                    imageView.center();
//                                }
//                                latch.countDown();
//                            }
//                        });
//                        try {
//                            latch.await();
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
//                        new PhotoEditFragment.Cropper().crop();
//                    }
//                }, handler
//        );
//    }
//
//    private int calculateBitmapSampleSize(Uri bitmapUri) throws IOException {
//        InputStream is = null;
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        try {
//            is = this.getContext().getContentResolver().openInputStream(bitmapUri);
//            BitmapFactory.decodeStream(is, null, options); // Just get image size
//        } finally {
//            CropUtil.closeSilently(is);
//        }
//
//        int maxSize = getMaxImageSize();
//        int sampleSize = 1;
//        while (options.outHeight / sampleSize > maxSize || options.outWidth / sampleSize > maxSize) {
//            sampleSize = sampleSize << 1;
//        }
//        return sampleSize;
//    }
//
//    private int getMaxImageSize() {
//        int textureLimit = getMaxTextureSize();
//        if (textureLimit == 0) {
//            return SIZE_DEFAULT;
//        } else {
//            return Math.min(textureLimit, SIZE_LIMIT);
//        }
//    }
//
//    private class Cropper {
//
//        private void makeDefault() {
//            if (rotateBitmap == null) {
//                return;
//            }
//
//            HighlightView hv = new HighlightView(imageView, GalleryFinal.getGalleryTheme().getCropControlColor());
//            final int width = rotateBitmap.getWidth();
//            final int height = rotateBitmap.getHeight();
//
//            Rect imageRect = new Rect(0, 0, width, height);
//
//            // Make the default size about 4/5 of the width or height
//            int cropWidth = Math.min(width, height) * 4 / 5;
//            @SuppressWarnings("SuspiciousNameCombination")
//            int cropHeight = cropWidth;
//
//            if (aspectX != 0 && aspectY != 0) {
//                if (aspectX > aspectY) {
//                    cropHeight = cropWidth * aspectY / aspectX;
//                } else {
//                    cropWidth = cropHeight * aspectX / aspectY;
//                }
//            }
//
//            int x = (width - cropWidth) / 2;
//            int y = (height - cropHeight) / 2;
//
//            RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
//            hv.setup(imageView.getUnrotatedMatrix(), imageRect, cropRect, aspectX != 0 && aspectY != 0);
//            imageView.add(hv);
//        }
//
//        public void crop() {
//            handler.post(new Runnable() {
//                public void run() {
//                    makeDefault();
//                    imageView.invalidate();
//                    if (imageView.highlightViews.size() == 1) {
//                        cropView = imageView.highlightViews.get(0);
//                        cropView.setFocus(true);
//                    }
//                }
//            });
//        }
//    }
}
