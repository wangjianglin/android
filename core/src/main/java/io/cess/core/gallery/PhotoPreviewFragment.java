package io.cess.core.gallery;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.cess.core.R;
import io.cess.core.annotation.ResCls;
import io.cess.core.annotation.ResId;
import io.cess.core.annotation.ViewById;
import io.cess.core.gallery.adapter.PhotoPreviewAdapter;
import io.cess.core.gallery.model.PhotoInfo;
import io.cess.core.gallery.widget.GFViewPager;


/**
 * Desction:
 * Author:pengjianbo
 * Date:2015/12/29 0029 14:43
 */
@ResCls(R.class)
@ResId(id="io_cess_core_gallery_activity_photo_preview")
public class PhotoPreviewFragment extends PhotoBaseFragment implements ViewPager.OnPageChangeListener{

    static final String PHOTO_LIST = "photo_list";

//    private RelativeLayout mTitleBar;
//    private ImageView mIvBack;
//    private TextView mTvTitle;
//    private TextView mTvIndicator;

    @ViewById(id="io_cess_core_gallery_preview_pager_pager")
    private GFViewPager mVpPager;
    private List<PhotoInfo> mPhotoList;
    private PhotoPreviewAdapter mPhotoPreviewAdapter;

    private ThemeConfig mThemeConfig;

    @Override
    protected void onCreateView() {

        mThemeConfig = GalleryFinal.getGalleryTheme();

        if ( mThemeConfig == null) {
            resultFailureDelayed(getString(R.string.io_cess_core_grallery_please_reopen_gf), true);
        } else {

            setTheme();

            mPhotoList = new ArrayList<>();//(List<PhotoInfo>) getIntent().getSerializableExtra(PHOTO_LIST);
            mPhotoPreviewAdapter = new PhotoPreviewAdapter(this.getActivity(), mPhotoList);
            mVpPager.setAdapter(mPhotoPreviewAdapter);
        }
    }

//    private void findViews() {
//        mTitleBar = (RelativeLayout) findViewById(R.id.titlebar);
//        mIvBack = (ImageView) findViewById(R.id.iv_back);
//        mTvTitle = (TextView) findViewById(R.id.tv_title);
//        mTvIndicator = (TextView) findViewById(R.id.tv_indicator);
//
//        mVpPager = (GFViewPager) findViewById(R.id.io_cess_core_gallery_preview_pager_pager);
//    }

//    private void setListener() {
//        mVpPager.addOnPageChangeListener(this);
//        mIvBack.setOnClickListener(mBackListener);
//    }

    private void setTheme() {
//        mIvBack.setImageResource(mThemeConfig.getIconBack());
//        if (mThemeConfig.getIconBack() == R.drawable.ic_gf_back) {
//            mIvBack.setColorFilter(mThemeConfig.getTitleBarIconColor());
//        }
//
//        mTitleBar.setBackgroundColor(mThemeConfig.getTitleBarBgColor());
//        mTvTitle.setTextColor(mThemeConfig.getTitleBarTextColor());
        if(mThemeConfig.getPreviewBg() != null) {
            mVpPager.setBackgroundDrawable(mThemeConfig.getPreviewBg());
        }
    }

    @Override
    protected void takeResult(PhotoInfo info) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //mTvIndicator.setText((position + 1) + "/" + mPhotoList.size());
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

//    private View.OnClickListener mBackListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            finish();
//        }
//    };

}
