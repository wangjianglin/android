package lin.core;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

import lin.core.annotation.Click;
import lin.core.annotation.ResCls;
import lin.core.annotation.ResId;
import lin.core.annotation.ViewById;
import lin.core.gallery.CoreConfig;
import lin.core.gallery.FunctionConfig;
import lin.core.gallery.GalleryFinal;
import lin.core.gallery.ThemeConfig;
import lin.core.gallery.UILImageLoader;
import lin.core.gallery.model.PhotoInfo;

/**
 * Created by lin on 03/03/2017.
 */
@ResCls(R.class)
@ResId(id="lin_core_images")
@BindingMethods({
        @BindingMethod(type = ImagesView.class, attribute = "images_max_select", method = "setMaxSelect"),
        @BindingMethod(type = ImagesView.class, attribute = "images_values", method = "setImages"),
        @BindingMethod(type = ImagesView.class, attribute = "images_video", method = "setVideo")
})
public class ImagesView extends ResView {

    private List<String> mImages = new ArrayList<>();
    private List<ImagesItemView> mViews = new ArrayList<>();
    private boolean mVideo = true;
    private boolean mAdd = true;
    private int mMaxSelect = 9;
    private String album = "GalleryFinal";

    public ImagesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ImagesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImagesView(Context context) {
        super(context);
    }

    @ViewById
    private GridLayout grid;

    @ViewById(id="lin_core_images_video")
    private View mVideoView;

    @ViewById(id="lin_core_images_camera")
    private View mCameraView;

    @Override
    protected void onInited() {
        this.setMaxSelect(this.getAttrs().getInt(R.styleable.images,R.styleable.images_images_max_select,this.mMaxSelect));
        this.setVideo(this.getAttrs().getBoolean(R.styleable.images,R.styleable.images_images_video,true));
        this.album = this.getAttrs().getString(R.styleable.images,R.styleable.images_images_album,album);
    }

    @Click(id="lin_core_images_camera")
    private void cameraClick(){
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setMutiSelectMaxSize(mMaxSelect-mImages.size())
                .build();

        CoreConfig coreConfig = new CoreConfig.Builder(this.getContext(), new UILImageLoader(), ThemeConfig.DEFAULT)
                .setFunctionConfig(functionConfig)
//                .setPauseOnScrollListener(pauseOnScrollListener)
//                .setNoAnimcation(mCbNoAnimation.isChecked())
                .build();
        GalleryFinal.init(coreConfig);
        GalleryFinal.openGalleryMuti(1, functionConfig, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                addPhots(resultList);
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        });
    }

    void delete(ImagesItemView item){
        grid.removeView(item);
        mViews.remove(item);
        mImages.remove(item.getImage());
        resetAdd();
        if(mImagesValueAttrChanged != null){
            mImagesValueAttrChanged.onChange();
        }
    }

    private void addPhots(List<PhotoInfo> photos){

        for (PhotoInfo photo : photos){
            ImagesItemView item = new ImagesItemView(this);
            item.setImage("file://"+photo.getPhotoPath());
            mImages.add("file://"+photo.getPhotoPath());
            mViews.add(item);
            grid.addView(item,grid.getChildCount()-1);
        }
        if(photos.size() > 0) {
            resetViews();
            resetAdd();
            if(mImagesValueAttrChanged != null){
                mImagesValueAttrChanged.onChange();
            }
        }
    }

    private void resetViews(){
        int w = this.getMeasuredWidth();
        for(int n=0;n< grid.getChildCount();n++){
            View item = grid.getChildAt(n);
            ViewGroup.LayoutParams lp = item.getLayoutParams();
            lp.width = w / 4;
            lp.height = w / 4;
            item.setLayoutParams(lp);
        }
    }

    private void reconViews(){
        for(ImagesItemView item : mViews){
            grid.removeView(item);
        }
        for(String image : mImages) {
            ImagesItemView item = new ImagesItemView(this);
            item.setImage(image);
            mViews.add(item);
            grid.addView(item, grid.getChildCount() - (isAdd()?1:0));
        }
        resetAdd();
    }

    @Override
    protected void genAttrs() {
        super.genAttrs();
        this.addAttr(R.styleable.images,R.styleable.images_images_max_select,AttrType.Int);
        this.addAttr(R.styleable.images,R.styleable.images_images_video,AttrType.Boolean);
        this.addAttr(R.styleable.images,R.styleable.images_images_album,AttrType.String);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed){
            resetViews();
        }
    }


    public boolean hasVideo(){
        return mVideo;
    }

    public void setVideo(boolean video){
        if(mVideo == video){
            return;
        }
        if(video){
            grid.addView(mVideoView,0);
        }else{
            grid.removeView(mVideoView);
        }
        mVideo = video;
    }

    public List<String> getImages() {
        return new ArrayList<String>(mImages);
    }

    public void setImages(List<String> images) {
        mImages.clear();
        mImages.addAll(images);
        reconViews();
    }

    public void setMaxSelect(int maxSelect){
        mMaxSelect = maxSelect;
        if(mMaxSelect < 1){
            mMaxSelect = 1;
        }
        resetAdd();
    }

    public int getMaxSelect(){
        return mMaxSelect;
    }

    private boolean isAdd() {
        return mAdd;
    }

    private void resetAdd() {
        boolean add = mImages.size() < mMaxSelect;
        if(add == mAdd){
            return;
        }
        this.mAdd = add;
        if(add){
            grid.addView(mCameraView);
        }else{
            grid.removeView(mCameraView);
        }
    }

    private InverseBindingListener mImagesValueAttrChanged;

    @InverseBindingAdapter(attribute = "images_values", event = "imagesValueAttrChanged")
    public static List<String> getImages(ImagesView images){
        return images.getImages();
    }

    @BindingAdapter(value = {"imagesValueAttrChanged"}, requireAll = false)
    public static void setImagesValueChanged(ImagesView view, final InverseBindingListener imagesValueAttrChanged) {
        view.mImagesValueAttrChanged = imagesValueAttrChanged;
    }
}
