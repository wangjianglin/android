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

package io.cess.core.gallery;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.cess.core.Nav;
import io.cess.core.R;
import io.cess.core.ResFragment;
import io.cess.core.annotation.Click;
import io.cess.core.annotation.ListItemClick;
import io.cess.core.annotation.MenuId;
import io.cess.core.annotation.OptionsMenu;
import io.cess.core.annotation.ResCls;
import io.cess.core.annotation.ResId;
import io.cess.core.annotation.ViewById;
import io.cess.core.gallery.adapter.FolderListAdapter;
import io.cess.core.gallery.adapter.PhotoListAdapter;
import io.cess.core.gallery.model.PhotoFolderInfo;
import io.cess.core.gallery.model.PhotoInfo;
import io.cess.core.gallery.permission.AfterPermissionGranted;
import io.cess.core.gallery.permission.EasyPermissions;
import io.cess.core.gallery.utils.PhotoTools;
import io.cess.core.gallery.widget.FloatingActionButton;

import static android.app.Activity.RESULT_OK;


/**
 * Desction:图片选择器
 * Author:pengjianbo
 * Date:15/10/10 下午3:54
 */
@ResCls(R.class)
@MenuId(id="io_cess_core_gallery")
@ResId(id="io_cess_core_gallery_activity_photo_select")
@OptionsMenu
public class PhotoSelectFragment extends PhotoBaseFragment{

    protected static String mPhotoTargetFolder;

    private final int HANLDER_TAKE_PHOTO_EVENT = 1000;
    private final int HANDLER_REFRESH_LIST_EVENT = 1002;

    protected int mScreenWidth = 720;
    protected int mScreenHeight = 1280;


    @ViewById(id="io_cess_core_gallery_select_gv_photo_list")
    private GridView mGvPhotoList;

    @ViewById(id="io_cess_core_gallery_select_lv_folder_list")
    private ListView mLvFolderList;

    @ViewById(id="io_cess_core_gallery_select_ll_folder_panel")
    private View mLlFolderPanel;

    @ViewById(id="io_cess_core_gallery_select_tv_choose_count")
    private TextView mTvChooseCount;

    @ViewById(id="io_cess_core_gallery_select_tv_sub_title")
    private TextView mTvSubTitle;

    @ViewById(id="io_cess_core_gallery_select_tv_empty_view")
    private TextView mTvEmptyView;

    @ViewById(id="io_cess_core_gallery_select_tv_title")
    private TextView mTvTitle;

    private List<PhotoFolderInfo> mAllPhotoFolderList;
    private FolderListAdapter mFolderListAdapter;

    private List<PhotoInfo> mCurPhotoList;
    private PhotoListAdapter mPhotoListAdapter;

    //是否需要刷新相册
    private boolean mHasRefreshGallery = false;
    private ArrayList<PhotoInfo> mSelectPhotoList = new ArrayList<>();

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putSerializable("selectPhotoMap", mSelectPhotoList);
//    }

//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
////        mSelectPhotoList = (ArrayList<PhotoInfo>) getIntent().getSerializableExtra("selectPhotoMap");
//    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        mSelectPhotoList = (ArrayList<PhotoInfo>) getIntent().getSerializableExtra("selectPhotoMap");
//    }

    private Handler mHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if ( msg.what == HANLDER_TAKE_PHOTO_EVENT ) {
                PhotoInfo photoInfo = (PhotoInfo) msg.obj;
                takeRefreshGallery(photoInfo);
                refreshSelectCount();
            } else if ( msg.what == HANDLER_REFRESH_LIST_EVENT ){
                refreshSelectCount();
                mPhotoListAdapter.notifyDataSetChanged();
                mFolderListAdapter.notifyDataSetChanged();
                if (mAllPhotoFolderList.get(0).getPhotoList() == null ||
                        mAllPhotoFolderList.get(0).getPhotoList().size() == 0) {
                    mTvEmptyView.setText(R.string.io_cess_core_grallery_no_photo);
                }

                mGvPhotoList.setEnabled(true);
//                mLlTitle.setEnabled(true);
                //mIvTakePhoto.setEnabled(true);
            }
        }
    };

    @Override
    protected void onCreateView() {
//        super.onCreateView();
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);

//        if ( GalleryFinal.getFunctionConfig() == null || GalleryFinal.getGalleryTheme() == null) {
//            resultFailureDelayed(getString(R.string.io_cess_core_grallery_please_reopen_gf), true);
//        } else {
//            setContentView(R.layout.io_cess_core_gallery_activity_photo_select);

        DisplayMetrics dm = DeviceUtils.getScreenPix(this.getActivity());
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;

            mPhotoTargetFolder = null;

            mAllPhotoFolderList = new ArrayList<>();
            mFolderListAdapter = new FolderListAdapter(this.getActivity(), mAllPhotoFolderList, GalleryFinal.getFunctionConfig());
            mLvFolderList.setAdapter(mFolderListAdapter);

            mCurPhotoList = new ArrayList<>();
            mPhotoListAdapter = new PhotoListAdapter(this.getActivity(), mCurPhotoList, mSelectPhotoList, mScreenWidth);
            mGvPhotoList.setAdapter(mPhotoListAdapter);

            if (GalleryFinal.getFunctionConfig().isMutiSelect()) {
                mTvChooseCount.setVisibility(View.VISIBLE);
                //mFabOk.setVisibility(View.VISIBLE);
            }

            setTheme();
            mGvPhotoList.setEmptyView(mTvEmptyView);

//            if (GalleryFinal.getFunctionConfig().isCamera()) {
//                //mIvTakePhoto.setVisibility(View.VISIBLE);
//            } else {
////                mIvTakePhoto.setVisibility(View.GONE);
//            }

            refreshSelectCount();
            requestGalleryPermission();

            mGvPhotoList.setOnScrollListener(GalleryFinal.getCoreConfig().getPauseOnScrollListener());
//        }
//
//        Global.mPhotoSelectActivity = this;
    }

    private void setTheme() {

        mTvSubTitle.setTextColor(GalleryFinal.getGalleryTheme().getTitleBarTextColor());
        mTvChooseCount.setTextColor(GalleryFinal.getGalleryTheme().getTitleBarTextColor());
    }


    protected void deleteSelect(int photoId) {
        try {
            for(Iterator<PhotoInfo> iterator = mSelectPhotoList.iterator();iterator.hasNext();){
                PhotoInfo info = iterator.next();
                if (info != null && info.getPhotoId() == photoId) {
                    iterator.remove();
                    break;
                }
            }
        } catch (Exception e){}

        refreshAdapter();
    }

    private void refreshAdapter() {
        mHanlder.sendEmptyMessageDelayed(HANDLER_REFRESH_LIST_EVENT, 100);
    }

    protected void takeRefreshGallery(PhotoInfo photoInfo, boolean selected) {
        if (this.getActivity().isFinishing() || photoInfo == null) {
            return;
        }

        Message message = mHanlder.obtainMessage();
        message.obj = photoInfo;
        message.what = HANLDER_TAKE_PHOTO_EVENT;
        mSelectPhotoList.add(photoInfo);
        mHanlder.sendMessageDelayed(message, 100);
    }

    /**
     * 解决在5.0手机上刷新Gallery问题，从startActivityForResult回到Activity把数据添加到集合中然后理解跳转到下一个页面，
     * adapter的getCount与list.size不一致，所以我这里用了延迟刷新数据
     * @param photoInfo
     */
    private void takeRefreshGallery(PhotoInfo photoInfo) {
        mCurPhotoList.add(0, photoInfo);
        mPhotoListAdapter.notifyDataSetChanged();

        //添加到集合中
        List<PhotoInfo> photoInfoList = mAllPhotoFolderList.get(0).getPhotoList();
        if (photoInfoList == null) {
            photoInfoList = new ArrayList<>();
        }
        photoInfoList.add(0, photoInfo);
        mAllPhotoFolderList.get(0).setPhotoList(photoInfoList);

        if ( mFolderListAdapter.getSelectFolder() != null ) {
            PhotoFolderInfo photoFolderInfo = mFolderListAdapter.getSelectFolder();
            List<PhotoInfo> list = photoFolderInfo.getPhotoList();
            if ( list == null ) {
                list = new ArrayList<>();
            }
            list.add(0, photoInfo);
            if ( list.size() == 1 ) {
                photoFolderInfo.setCoverPhoto(photoInfo);
            }
            mFolderListAdapter.getSelectFolder().setPhotoList(list);
        } else {
            String folderA = new File(photoInfo.getPhotoPath()).getParent();
            for (int i = 1; i < mAllPhotoFolderList.size(); i++) {
                PhotoFolderInfo folderInfo = mAllPhotoFolderList.get(i);
                String folderB = null;
                if (!StringUtils.isEmpty(photoInfo.getPhotoPath())) {
                    folderB = new File(photoInfo.getPhotoPath()).getParent();
                }
                if (TextUtils.equals(folderA, folderB)) {
                    List<PhotoInfo> list = folderInfo.getPhotoList();
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(0, photoInfo);
                    folderInfo.setPhotoList(list);
                    if ( list.size() == 1 ) {
                        folderInfo.setCoverPhoto(photoInfo);
                    }
                }
            }
        }

        mFolderListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void takeResult(PhotoInfo photoInfo) {

        Message message = mHanlder.obtainMessage();
        message.obj = photoInfo;
        message.what = HANLDER_TAKE_PHOTO_EVENT;

        if ( !GalleryFinal.getFunctionConfig().isMutiSelect() ) { //单选
            mSelectPhotoList.clear();
            mSelectPhotoList.add(photoInfo);

            if ( GalleryFinal.getFunctionConfig().isEditPhoto() ) {//裁剪
                mHasRefreshGallery = true;
                toPhotoEdit();
            } else {
                ArrayList<PhotoInfo> list = new ArrayList<>();
                list.add(photoInfo);
                resultData(list);
            }

            mHanlder.sendMessageDelayed(message, 100);
        } else {//多选
            mSelectPhotoList.add(photoInfo);
            mHanlder.sendMessageDelayed(message, 100);
        }
    }

    /**
     * 执行裁剪
     */
    protected void toPhotoEdit() {
        Intent intent = new Intent(this.getActivity(), PhotoEditActivity.class);
        intent.putExtra(Constants.SELECT_MAP, mSelectPhotoList);
        startActivity(intent);
    }

    @MenuId(id="io_cess_core_gallery_ok")
    private void okClick(){
        if(mSelectPhotoList.size() > 0) {
            if (!GalleryFinal.getFunctionConfig().isEditPhoto()) {
                resultData(mSelectPhotoList);
            } else {
                toPhotoEdit();
            }
        }

        if(!Nav.pop(this)) {
            this.getActivity().finish();
        }

    }
    @MenuId(id="io_cess_core_gallery_camera")
    private void taskPhoto(){
        if (GalleryFinal.getFunctionConfig().isMutiSelect() && mSelectPhotoList.size() == GalleryFinal.getFunctionConfig().getMaxSize()) {
            toast(getString(R.string.io_cess_core_grallery_select_max_tips));
            return;
        }

        if (!DeviceUtils.existSDCard()) {
            toast(getString(R.string.io_cess_core_grallery_empty_sdcard));
            return;
        }

        takePhotoAction();
    }

    @Click(id={"io_cess_core_gallery_select_tv_sub_title","io_cess_core_gallery_select_ll_folder_panel"})
    public void albamSelClick() {
        if ( mLlFolderPanel.getVisibility() == View.VISIBLE ) {
            mLlFolderPanel.setVisibility(View.GONE);
            //mLlFolderPanel.setAnimation(AnimationUtils.loadAnimation(this, R.anim.gf_flip_horizontal_out));
        } else {
            //mLlFolderPanel.setAnimation(AnimationUtils.loadAnimation(this, R.anim.gf_flip_horizontal_in));
            mLlFolderPanel.setVisibility(View.VISIBLE);
        }

    }

    @ListItemClick(id={"io_cess_core_gallery_select_lv_folder_list"})
    private void folderItemClick(int position) {
        mLlFolderPanel.setVisibility(View.GONE);
        mCurPhotoList.clear();
        PhotoFolderInfo photoFolderInfo = mAllPhotoFolderList.get(position);
        if ( photoFolderInfo.getPhotoList() != null ) {
            mCurPhotoList.addAll(photoFolderInfo.getPhotoList());
        }
        mPhotoListAdapter.notifyDataSetChanged();

        if (position == 0) {
            mPhotoTargetFolder = null;
        } else {
            PhotoInfo photoInfo = photoFolderInfo.getCoverPhoto();
            if (photoInfo != null && !StringUtils.isEmpty(photoInfo.getPhotoPath())) {
                mPhotoTargetFolder = new File(photoInfo.getPhotoPath()).getParent();
            } else {
                mPhotoTargetFolder = null;
            }
        }
        mTvSubTitle.setText(photoFolderInfo.getFolderName());
        mFolderListAdapter.setSelectFolder(photoFolderInfo);
        mFolderListAdapter.notifyDataSetChanged();

        if (mCurPhotoList.size() == 0) {
            mTvEmptyView.setText(R.string.io_cess_core_grallery_no_photo);
        }
    }

    @ListItemClick(id={"io_cess_core_gallery_select_gv_photo_list"})
    private void photoItemClick(AdapterView<?> parent, View view, int position) {
        PhotoInfo info = mCurPhotoList.get(position);
        if (!GalleryFinal.getFunctionConfig().isMutiSelect()) {
            mSelectPhotoList.clear();
            mSelectPhotoList.add(info);
            String ext = FilenameUtils.getExtension(info.getPhotoPath());
            if (GalleryFinal.getFunctionConfig().isEditPhoto() && (ext.equalsIgnoreCase("png")
                    || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg"))) {
                toPhotoEdit();
            } else {
                ArrayList<PhotoInfo> list = new ArrayList<>();
                list.add(info);
                resultData(list);
            }
            return;
        }
        boolean checked = false;
        if (!mSelectPhotoList.contains(info)) {
            if (GalleryFinal.getFunctionConfig().isMutiSelect() && mSelectPhotoList.size() == GalleryFinal.getFunctionConfig().getMaxSize()) {
                toast(getString(R.string.io_cess_core_grallery_select_max_tips));
                return;
            } else {
                mSelectPhotoList.add(info);
                checked = true;
            }
        } else {
            try {
                for(Iterator<PhotoInfo> iterator = mSelectPhotoList.iterator();iterator.hasNext();){
                    PhotoInfo pi = iterator.next();
                    if (pi != null && TextUtils.equals(pi.getPhotoPath(), info.getPhotoPath())) {
                        iterator.remove();
                        break;
                    }
                }
            } catch (Exception e){}
            checked = false;
        }
        refreshSelectCount();

        PhotoListAdapter.PhotoViewHolder holder = (PhotoListAdapter.PhotoViewHolder) view.getTag();
        if (holder != null) {
            if (checked) {
                holder.mIvCheck.setBackgroundColor(GalleryFinal.getGalleryTheme().getCheckSelectedColor());
            } else {
                holder.mIvCheck.setBackgroundColor(GalleryFinal.getGalleryTheme().getCheckNornalColor());
            }
        } else {
            mPhotoListAdapter.notifyDataSetChanged();
        }
    }

    public void refreshSelectCount() {
        mTvChooseCount.setText(getString(R.string.io_cess_core_grallery_selected, mSelectPhotoList.size(), GalleryFinal.getFunctionConfig().getMaxSize()));
    }

//    @Override
    public void onPermissionsGranted(List<String> list) {
        getPhotos();
    }

//    @Override
    public void onPermissionsDenied(List<String> list) {
        mTvEmptyView.setText(R.string.io_cess_core_grallery_permissions_denied_tips);
        //mIvTakePhoto.setVisibility(View.GONE);
    }

    /**
     * 获取所有图片
     */
    @AfterPermissionGranted(GalleryFinal.PERMISSIONS_CODE_GALLERY)
    private void requestGalleryPermission() {
        if (EasyPermissions.hasPermissions(this.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            getPhotos();
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, getString(R.string.io_cess_core_grallery_permissions_tips_gallery),
                    GalleryFinal.PERMISSIONS_CODE_GALLERY, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void getPhotos() {
        mTvEmptyView.setText(R.string.io_cess_core_grallery_waiting);
        mGvPhotoList.setEnabled(false);
//        mLlTitle.setEnabled(false);
        //mIvTakePhoto.setEnabled(false);
        new Thread() {
            @Override
            public void run() {
                super.run();

                mAllPhotoFolderList.clear();
                List<PhotoFolderInfo> allFolderList = PhotoTools.getAllPhotoFolder(getContext(), mSelectPhotoList);
                mAllPhotoFolderList.addAll(allFolderList);

                mCurPhotoList.clear();
                if ( allFolderList.size() > 0 ) {
                    if ( allFolderList.get(0).getPhotoList() != null ) {
                        mCurPhotoList.addAll(allFolderList.get(0).getPhotoList());
                    }
                }

                refreshAdapter();
            }
        }.start();
    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ( keyCode == KeyEvent.KEYCODE_BACK ) {
//            if ( mLlFolderPanel.getVisibility() == View.VISIBLE ) {
////                mLlTitle.performClick();
//                return true;
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void onResume() {
        super.onResume();
        if ( mHasRefreshGallery) {
            mHasRefreshGallery = false;
            requestGalleryPermission();
        }
    }

//    @Override
//    public void onTrimMemory(int level) {
//        super.onTrimMemory(level);
//        if ( GalleryFinal.getCoreConfig() != null &&
//                GalleryFinal.getCoreConfig().getImageLoader() != null ) {
//            GalleryFinal.getCoreConfig().getImageLoader().clearMemoryCache();
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPhotoTargetFolder = null;
        mSelectPhotoList.clear();
        System.gc();
    }
}
