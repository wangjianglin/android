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


import android.os.Bundle;
import android.support.v7.app.ActionBar;

import io.cess.core.R;
import io.cess.core.ViewActivity;
import io.cess.core.annotation.ResCls;
import io.cess.core.annotation.ViewCls;
import io.cess.core.gallery.model.PhotoInfo;


/**
 * Desction:图片选择器
 * Author:pengjianbo
 * Date:15/10/10 下午3:54
 */

@ResCls(R.class)
@ViewCls(PhotoSelectFragment.class)
public class PhotoSelectActivity extends ViewActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = this.getSupportActionBar();

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    protected void takeRefreshGallery(PhotoInfo photoInfo, boolean selected){}

    protected void deleteSelect(int photoId){}

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
//@ResCls(R.class)
//@MenuId(id="io_cess_core_gallery")
//public class PhotoSelectActivity extends PhotoBaseActivity{
//
//    private final int HANLDER_TAKE_PHOTO_EVENT = 1000;
//    private final int HANDLER_REFRESH_LIST_EVENT = 1002;
//
//    @ViewById(id="io_cess_core_gallery_select_gv_photo_list")
//    private GridView mGvPhotoList;
//
//    @ViewById(id="io_cess_core_gallery_select_lv_folder_list")
//    private ListView mLvFolderList;
//
//    @ViewById(id="io_cess_core_gallery_select_ll_folder_panel")
//    private View mLlFolderPanel;
//
//    @ViewById(id="io_cess_core_gallery_select_tv_choose_count")
//    private TextView mTvChooseCount;
//
//    @ViewById(id="io_cess_core_gallery_select_tv_sub_title")
//    private TextView mTvSubTitle;
//
//    @ViewById(id="io_cess_core_gallery_select_tv_empty_view")
//    private TextView mTvEmptyView;
//
//    @ViewById(id="io_cess_core_gallery_select_tv_title")
//    private TextView mTvTitle;
//
//    private List<PhotoFolderInfo> mAllPhotoFolderList;
//    private FolderListAdapter mFolderListAdapter;
//
//    private List<PhotoInfo> mCurPhotoList;
//    private PhotoListAdapter mPhotoListAdapter;
//
//    //是否需要刷新相册
//    private boolean mHasRefreshGallery = false;
//    private ArrayList<PhotoInfo> mSelectPhotoList = new ArrayList<>();
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putSerializable("selectPhotoMap", mSelectPhotoList);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        mSelectPhotoList = (ArrayList<PhotoInfo>) getIntent().getSerializableExtra("selectPhotoMap");
//    }
//
//    private Handler mHanlder = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if ( msg.what == HANLDER_TAKE_PHOTO_EVENT ) {
//                PhotoInfo photoInfo = (PhotoInfo) msg.obj;
//                takeRefreshGallery(photoInfo);
//                refreshSelectCount();
//            } else if ( msg.what == HANDLER_REFRESH_LIST_EVENT ){
//                refreshSelectCount();
//                mPhotoListAdapter.notifyDataSetChanged();
//                mFolderListAdapter.notifyDataSetChanged();
//                if (mAllPhotoFolderList.get(0).getPhotoList() == null ||
//                        mAllPhotoFolderList.get(0).getPhotoList().size() == 0) {
//                    mTvEmptyView.setText(R.string.io_cess_core_grallery_no_photo);
//                }
//
//                mGvPhotoList.setEnabled(true);
////                mLlTitle.setEnabled(true);
//                //mIvTakePhoto.setEnabled(true);
//            }
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if ( GalleryFinal.getFunctionConfig() == null || GalleryFinal.getGalleryTheme() == null) {
//            resultFailureDelayed(getString(R.string.io_cess_core_grallery_please_reopen_gf), true);
//        } else {
//            setContentView(R.layout.io_cess_core_gallery_activity_photo_select);
//            mPhotoTargetFolder = null;
//
//            mAllPhotoFolderList = new ArrayList<>();
//            mFolderListAdapter = new FolderListAdapter(this, mAllPhotoFolderList, GalleryFinal.getFunctionConfig());
//            mLvFolderList.setAdapter(mFolderListAdapter);
//
//            mCurPhotoList = new ArrayList<>();
//            mPhotoListAdapter = new PhotoListAdapter(this, mCurPhotoList, mSelectPhotoList, mScreenWidth);
//            mGvPhotoList.setAdapter(mPhotoListAdapter);
//
//            if (GalleryFinal.getFunctionConfig().isMutiSelect()) {
//                mTvChooseCount.setVisibility(View.VISIBLE);
//                //mFabOk.setVisibility(View.VISIBLE);
//            }
//
//            setTheme();
//            mGvPhotoList.setEmptyView(mTvEmptyView);
//
////            if (GalleryFinal.getFunctionConfig().isCamera()) {
////                //mIvTakePhoto.setVisibility(View.VISIBLE);
////            } else {
//////                mIvTakePhoto.setVisibility(View.GONE);
////            }
//
//            refreshSelectCount();
//            requestGalleryPermission();
//
//            mGvPhotoList.setOnScrollListener(GalleryFinal.getCoreConfig().getPauseOnScrollListener());
//        }
//
//        Global.mPhotoSelectActivity = this;
//    }
//
//    private void setTheme() {
////        mIvBack.setImageResource(GalleryFinal.getGalleryTheme().getIconBack());
////        if (GalleryFinal.getGalleryTheme().getIconBack() == R.drawable.ic_gf_back) {
////            mIvBack.setColorFilter(GalleryFinal.getGalleryTheme().getTitleBarIconColor());
////        }
//
////        mIvFolderArrow.setImageResource(GalleryFinal.getGalleryTheme().getIconFolderArrow());
////        if (GalleryFinal.getGalleryTheme().getIconFolderArrow() == R.drawable.ic_gf_triangle_arrow) {
////            mIvFolderArrow.setColorFilter(GalleryFinal.getGalleryTheme().getTitleBarIconColor());
////        }
//
////        mIvClear.setImageResource(GalleryFinal.getGalleryTheme().getIconClear());
////        if (GalleryFinal.getGalleryTheme().getIconClear() == R.drawable.ic_gf_clear) {
////            mIvClear.setColorFilter(GalleryFinal.getGalleryTheme().getTitleBarIconColor());
////        }
//
////        mIvPreView.setImageResource(GalleryFinal.getGalleryTheme().getIconPreview());
////        if (GalleryFinal.getGalleryTheme().getIconPreview() == R.drawable.ic_gf_preview) {
////            mIvPreView.setColorFilter(GalleryFinal.getGalleryTheme().getTitleBarIconColor());
////        }
//
////        mIvTakePhoto.setImageResource(GalleryFinal.getGalleryTheme().getIconCamera());
////        if (GalleryFinal.getGalleryTheme().getIconCamera() == R.drawable.ic_gf_camera) {
////            mIvTakePhoto.setColorFilter(GalleryFinal.getGalleryTheme().getTitleBarIconColor());
////        }
//        //mFabOk.setIcon(GalleryFinal.getGalleryTheme().getIconFab());
//
////        mTitlebar.setBackgroundColor(GalleryFinal.getGalleryTheme().getTitleBarBgColor());
//        mTvSubTitle.setTextColor(GalleryFinal.getGalleryTheme().getTitleBarTextColor());
////        mTvTitle.setTextColor(GalleryFinal.getGalleryTheme().getTitleBarTextColor());
//        mTvChooseCount.setTextColor(GalleryFinal.getGalleryTheme().getTitleBarTextColor());
////        mFabOk.setColorPressed(GalleryFinal.getGalleryTheme().getFabPressedColor());
////        mFabOk.setColorNormal(GalleryFinal.getGalleryTheme().getFabNornalColor());
//    }
//
////    private void findViews() {
//////        mGvPhotoList = (GridView) findViewById(R.id.gv_photo_list);
//////        mLvFolderList = (ListView) findViewById(R.id.lv_folder_list);
//////        mTvSubTitle = (TextView) findViewById(R.id.tv_sub_title);
//////        mLlFolderPanel = (View) findViewById(R.id.ll_folder_panel);
//////        mIvTakePhoto = (ImageView) findViewById(R.id.iv_take_photo);
//////        mTvChooseCount = (TextView) findViewById(R.id.tv_choose_count);
//////        mIvBack = (ImageView) findViewById(R.id.iv_back);
//////        mFabOk = (FloatingActionButton) findViewById(R.id.fab_ok);
//////        mTvEmptyView = (TextView) findViewById(R.id.tv_empty_view);
////////        mLlTitle = (LinearLayout) findViewById(R.id.ll_title);
//////        mIvClear = (ImageView) findViewById(R.id.iv_clear);
//////        mTitlebar = (RelativeLayout) findViewById(R.id.titlebar);
//////        mTvTitle = (TextView) findViewById(R.id.tv_title);
//////        mIvFolderArrow = (ImageView) findViewById(R.id.iv_folder_arrow);
//////        mIvPreView = (ImageView) findViewById(R.id.iv_preview);
////    }
//
////    private void setListener() {
//////        mLlTitle.setOnClickListener(this);
//////        mTvSubTitle.setOnClickListener(this);
//////        mIvTakePhoto.setOnClickListener(this);
//////        mIvBack.setOnClickListener(this);
//////        mIvFolderArrow.setOnClickListener(this);
//////
//////        mLvFolderList.setOnItemClickListener(this);
//////        mGvPhotoList.setOnItemClickListener(this);
//////        mFabOk.setOnClickListener(this);
//////        mIvClear.setOnClickListener(this);
//////        mIvPreView.setOnClickListener(this);
////    }
//
//    protected void deleteSelect(int photoId) {
//        try {
//            for(Iterator<PhotoInfo> iterator = mSelectPhotoList.iterator();iterator.hasNext();){
//                PhotoInfo info = iterator.next();
//                if (info != null && info.getPhotoId() == photoId) {
//                    iterator.remove();
//                    break;
//                }
//            }
//        } catch (Exception e){}
//
//        refreshAdapter();
//    }
//
//    private void refreshAdapter() {
//        mHanlder.sendEmptyMessageDelayed(HANDLER_REFRESH_LIST_EVENT, 100);
//    }
//
//    protected void takeRefreshGallery(PhotoInfo photoInfo, boolean selected) {
//        if (isFinishing() || photoInfo == null) {
//            return;
//        }
//
//        Message message = mHanlder.obtainMessage();
//        message.obj = photoInfo;
//        message.what = HANLDER_TAKE_PHOTO_EVENT;
//        mSelectPhotoList.add(photoInfo);
//        mHanlder.sendMessageDelayed(message, 100);
//    }
//
//    /**
//     * 解决在5.0手机上刷新Gallery问题，从startActivityForResult回到Activity把数据添加到集合中然后理解跳转到下一个页面，
//     * adapter的getCount与list.size不一致，所以我这里用了延迟刷新数据
//     * @param photoInfo
//     */
//    private void takeRefreshGallery(PhotoInfo photoInfo) {
//        mCurPhotoList.add(0, photoInfo);
//        mPhotoListAdapter.notifyDataSetChanged();
//
//        //添加到集合中
//        List<PhotoInfo> photoInfoList = mAllPhotoFolderList.get(0).getPhotoList();
//        if (photoInfoList == null) {
//            photoInfoList = new ArrayList<>();
//        }
//        photoInfoList.add(0, photoInfo);
//        mAllPhotoFolderList.get(0).setPhotoList(photoInfoList);
//
//        if ( mFolderListAdapter.getSelectFolder() != null ) {
//            PhotoFolderInfo photoFolderInfo = mFolderListAdapter.getSelectFolder();
//            List<PhotoInfo> list = photoFolderInfo.getPhotoList();
//            if ( list == null ) {
//                list = new ArrayList<>();
//            }
//            list.add(0, photoInfo);
//            if ( list.size() == 1 ) {
//                photoFolderInfo.setCoverPhoto(photoInfo);
//            }
//            mFolderListAdapter.getSelectFolder().setPhotoList(list);
//        } else {
//            String folderA = new File(photoInfo.getPhotoPath()).getParent();
//            for (int i = 1; i < mAllPhotoFolderList.size(); i++) {
//                PhotoFolderInfo folderInfo = mAllPhotoFolderList.get(i);
//                String folderB = null;
//                if (!StringUtils.isEmpty(photoInfo.getPhotoPath())) {
//                    folderB = new File(photoInfo.getPhotoPath()).getParent();
//                }
//                if (StringUtil.equals(folderA, folderB)) {
//                    List<PhotoInfo> list = folderInfo.getPhotoList();
//                    if (list == null) {
//                        list = new ArrayList<>();
//                    }
//                    list.add(0, photoInfo);
//                    folderInfo.setPhotoList(list);
//                    if ( list.size() == 1 ) {
//                        folderInfo.setCoverPhoto(photoInfo);
//                    }
//                }
//            }
//        }
//
//        mFolderListAdapter.notifyDataSetChanged();
//    }
//
//    @Override
//    protected void takeResult(PhotoInfo photoInfo) {
//
//        Message message = mHanlder.obtainMessage();
//        message.obj = photoInfo;
//        message.what = HANLDER_TAKE_PHOTO_EVENT;
//
//        if ( !GalleryFinal.getFunctionConfig().isMutiSelect() ) { //单选
//            mSelectPhotoList.clear();
//            mSelectPhotoList.add(photoInfo);
//
//            if ( GalleryFinal.getFunctionConfig().isEditPhoto() ) {//裁剪
//                mHasRefreshGallery = true;
//                toPhotoEdit();
//            } else {
//                ArrayList<PhotoInfo> list = new ArrayList<>();
//                list.add(photoInfo);
//                resultData(list);
//            }
//
//            mHanlder.sendMessageDelayed(message, 100);
//        } else {//多选
//            mSelectPhotoList.add(photoInfo);
//            mHanlder.sendMessageDelayed(message, 100);
//        }
//    }
//
//    /**
//     * 执行裁剪
//     */
//    protected void toPhotoEdit() {
//        Intent intent = new Intent(this, PhotoEditActivity.class);
//        intent.putExtra(PhotoEditActivity.SELECT_MAP, mSelectPhotoList);
//        startActivity(intent);
//    }
//
////    mTvSubTitle.setOnClickListener(this);
////        mIvTakePhoto.setOnClickListener(this);
////        mIvBack.setOnClickListener(this);
////        mIvFolderArrow.setOnClickListener(this);
////
////        mLvFolderList.setOnItemClickListener(this);
////        mGvPhotoList.setOnItemClickListener(this);
////        mFabOk.setOnClickListener(this);
////        mIvClear.setOnClickListener(this);
////        mIvPreView.setOnClickListener(this);
//    @MenuId(id="io_cess_core_gallery_ok")
//    private void okClick(){
//        if(mSelectPhotoList.size() > 0) {
//            if (!GalleryFinal.getFunctionConfig().isEditPhoto()) {
//                resultData(mSelectPhotoList);
//            } else {
//                toPhotoEdit();
//            }
//        }
//    }
//    @MenuId(id="io_cess_core_gallery_camera")
//    private void taskPhoto(){
//        if (GalleryFinal.getFunctionConfig().isMutiSelect() && mSelectPhotoList.size() == GalleryFinal.getFunctionConfig().getMaxSize()) {
//            toast(getString(R.string.io_cess_core_grallery_select_max_tips));
//            return;
//        }
//
//        if (!DeviceUtils.existSDCard()) {
//            toast(getString(R.string.io_cess_core_grallery_empty_sdcard));
//            return;
//        }
//
//        takePhotoAction();
//    }
//    @Click(id={"io_cess_core_gallery_select_tv_sub_title","io_cess_core_gallery_select_iv_clear","io_cess_core_gallery_select_iv_preview"})
//    //@Override
//    public void onClick(View v) {
//        int id = v.getId();
////        if ( id == R.id.ll_title || id == R.id.iv_folder_arrow) {
//        if ( id == R.id.io_cess_core_gallery_select_tv_sub_title) {
//            if ( mLlFolderPanel.getVisibility() == View.VISIBLE ) {
//                mLlFolderPanel.setVisibility(View.GONE);
//                //mLlFolderPanel.setAnimation(AnimationUtils.loadAnimation(this, R.anim.gf_flip_horizontal_out));
//            } else {
//                //mLlFolderPanel.setAnimation(AnimationUtils.loadAnimation(this, R.anim.gf_flip_horizontal_in));
//                mLlFolderPanel.setVisibility(View.VISIBLE);
//            }
////        } else if ( id == R.id.iv_take_photo ) {
////            //判断是否达到多选最大数量
////            if (GalleryFinal.getFunctionConfig().isMutiSelect() && mSelectPhotoList.size() == GalleryFinal.getFunctionConfig().getMaxSize()) {
////                toast(getString(R.string.select_max_tips));
////                return;
////            }
////
////            if (!DeviceUtils.existSDCard()) {
////                toast(getString(R.string.empty_sdcard));
////                return;
////            }
////
////            takePhotoAction();
////        } else if ( id == R.id.iv_back ) {
////            if ( mLlFolderPanel.getVisibility() == View.VISIBLE ) {
//////                mLlTitle.performClick();
////            } else {
////                finish();
////            }
////        } else if ( id == R.id.fab_ok ) {
////            if(mSelectPhotoList.size() > 0) {
////                if (!GalleryFinal.getFunctionConfig().isEditPhoto()) {
////                    resultData(mSelectPhotoList);
////                } else {
////                    toPhotoEdit();
////                }
////            }
////        } else if ( id == R.id.iv_clear ) {
////            mSelectPhotoList.clear();
////            mPhotoListAdapter.notifyDataSetChanged();
////            refreshSelectCount();
//        } else if ( id == R.id.iv_preview ) {
//            Intent intent = new Intent(this, PhotoPreviewActivity.class);
//            intent.putExtra(PhotoPreviewActivity.PHOTO_LIST, mSelectPhotoList);
//            startActivity(intent);
//        }
//    }
//
//    @ListItemClick(id={"io_cess_core_gallery_select_lv_folder_list","io_cess_core_gallery_select_gv_photo_list"})
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        int parentId = parent.getId();
//        if ( parentId == R.id.io_cess_core_gallery_select_lv_folder_list ) {
//            folderItemClick(position);
//        } else {
//            photoItemClick(view, position);
//        }
//    }
//    private void folderItemClick(int position) {
//        mLlFolderPanel.setVisibility(View.GONE);
//        mCurPhotoList.clear();
//        PhotoFolderInfo photoFolderInfo = mAllPhotoFolderList.get(position);
//        if ( photoFolderInfo.getPhotoList() != null ) {
//            mCurPhotoList.addAll(photoFolderInfo.getPhotoList());
//        }
//        mPhotoListAdapter.notifyDataSetChanged();
//
//        if (position == 0) {
//            mPhotoTargetFolder = null;
//        } else {
//            PhotoInfo photoInfo = photoFolderInfo.getCoverPhoto();
//            if (photoInfo != null && !StringUtils.isEmpty(photoInfo.getPhotoPath())) {
//                mPhotoTargetFolder = new File(photoInfo.getPhotoPath()).getParent();
//            } else {
//                mPhotoTargetFolder = null;
//            }
//        }
//        mTvSubTitle.setText(photoFolderInfo.getFolderName());
//        mFolderListAdapter.setSelectFolder(photoFolderInfo);
//        mFolderListAdapter.notifyDataSetChanged();
//
//        if (mCurPhotoList.size() == 0) {
//            mTvEmptyView.setText(R.string.io_cess_core_grallery_no_photo);
//        }
//    }
//
//    private void photoItemClick(View view, int position) {
//        PhotoInfo info = mCurPhotoList.get(position);
//        if (!GalleryFinal.getFunctionConfig().isMutiSelect()) {
//            mSelectPhotoList.clear();
//            mSelectPhotoList.add(info);
//            String ext = FilenameUtils.getExtension(info.getPhotoPath());
//            if (GalleryFinal.getFunctionConfig().isEditPhoto() && (ext.equalsIgnoreCase("png")
//                    || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg"))) {
//                toPhotoEdit();
//            } else {
//                ArrayList<PhotoInfo> list = new ArrayList<>();
//                list.add(info);
//                resultData(list);
//            }
//            return;
//        }
//        boolean checked = false;
//        if (!mSelectPhotoList.contains(info)) {
//            if (GalleryFinal.getFunctionConfig().isMutiSelect() && mSelectPhotoList.size() == GalleryFinal.getFunctionConfig().getMaxSize()) {
//                toast(getString(R.string.io_cess_core_grallery_select_max_tips));
//                return;
//            } else {
//                mSelectPhotoList.add(info);
//                checked = true;
//            }
//        } else {
//            try {
//                for(Iterator<PhotoInfo> iterator = mSelectPhotoList.iterator();iterator.hasNext();){
//                    PhotoInfo pi = iterator.next();
//                    if (pi != null && StringUtil.equals(pi.getPhotoPath(), info.getPhotoPath())) {
//                        iterator.remove();
//                        break;
//                    }
//                }
//            } catch (Exception e){}
//            checked = false;
//        }
//        refreshSelectCount();
//
//        PhotoListAdapter.PhotoViewHolder holder = (PhotoListAdapter.PhotoViewHolder) view.getTag();
//        if (holder != null) {
//            if (checked) {
//                holder.mIvCheck.setBackgroundColor(GalleryFinal.getGalleryTheme().getCheckSelectedColor());
//            } else {
//                holder.mIvCheck.setBackgroundColor(GalleryFinal.getGalleryTheme().getCheckNornalColor());
//            }
//        } else {
//            mPhotoListAdapter.notifyDataSetChanged();
//        }
//    }
//
//    public void refreshSelectCount() {
//        mTvChooseCount.setText(getString(R.string.io_cess_core_grallery_selected, mSelectPhotoList.size(), GalleryFinal.getFunctionConfig().getMaxSize()));
////        if ( mSelectPhotoList.size() > 0 && GalleryFinal.getFunctionConfig().isMutiSelect() ) {
////            mIvClear.setVisibility(View.VISIBLE);
////        } else {
////            mIvClear.setVisibility(View.GONE);
////        }
//
////        if(GalleryFinal.getFunctionConfig().isEnablePreview()){
////            mIvPreView.setVisibility(View.VISIBLE);
////        } else {
////            mIvPreView.setVisibility(View.GONE);
////        }
//    }
//
//    @Override
//    public void onPermissionsGranted(List<String> list) {
//        getPhotos();
//    }
//
//    @Override
//    public void onPermissionsDenied(List<String> list) {
//        mTvEmptyView.setText(R.string.io_cess_core_grallery_permissions_denied_tips);
//        //mIvTakePhoto.setVisibility(View.GONE);
//    }
//
//    /**
//     * 获取所有图片
//     */
//    @AfterPermissionGranted(GalleryFinal.PERMISSIONS_CODE_GALLERY)
//    private void requestGalleryPermission() {
//        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//            getPhotos();
//        } else {
//            // Ask for one permission
//            EasyPermissions.requestPermissions(this, getString(R.string.io_cess_core_grallery_permissions_tips_gallery),
//                    GalleryFinal.PERMISSIONS_CODE_GALLERY, Manifest.permission.READ_EXTERNAL_STORAGE);
//        }
//    }
//
//    private void getPhotos() {
//        mTvEmptyView.setText(R.string.io_cess_core_grallery_waiting);
//        mGvPhotoList.setEnabled(false);
////        mLlTitle.setEnabled(false);
//        //mIvTakePhoto.setEnabled(false);
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//
//                mAllPhotoFolderList.clear();
//                List<PhotoFolderInfo> allFolderList = PhotoTools.getAllPhotoFolder(PhotoSelectActivity.this, mSelectPhotoList);
//                mAllPhotoFolderList.addAll(allFolderList);
//
//                mCurPhotoList.clear();
//                if ( allFolderList.size() > 0 ) {
//                    if ( allFolderList.get(0).getPhotoList() != null ) {
//                        mCurPhotoList.addAll(allFolderList.get(0).getPhotoList());
//                    }
//                }
//
//                refreshAdapter();
//            }
//        }.start();
//    }
//
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
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if ( mHasRefreshGallery) {
//            mHasRefreshGallery = false;
//            requestGalleryPermission();
//        }
//    }
//
//    @Override
//    public void onTrimMemory(int level) {
//        super.onTrimMemory(level);
//        if ( GalleryFinal.getCoreConfig() != null &&
//                GalleryFinal.getCoreConfig().getImageLoader() != null ) {
//            GalleryFinal.getCoreConfig().getImageLoader().clearMemoryCache();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mPhotoTargetFolder = null;
//        mSelectPhotoList.clear();
//        System.gc();
//    }
//}
