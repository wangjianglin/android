package lin.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

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
@ResId(id="lin_core_images_item")
public class ImagesItemView extends ResView {

    private ImagesView mImages;
    private String mImageUrl;
    public ImagesItemView(ImagesView images) {
        super(images.getContext());
        this.mImages = images;
    }



    @ViewById(id="lin_core_images_item_img")
    private ImageView imageView;


    @Click(id="lin_core_images_item_delete")
    private void cameraClick(){
        mImages.delete(this);
    }

    public void setImage(String url){
        if(mImageUrl == url){
            return;
        }
        mImageUrl = url;
        Images.setImage(imageView,url);
    }

    public String getImage(){
        return mImageUrl;
    }
}
