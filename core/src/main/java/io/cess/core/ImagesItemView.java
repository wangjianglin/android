package io.cess.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.util.List;

import io.cess.core.annotation.Click;
import io.cess.core.annotation.ResCls;
import io.cess.core.annotation.ResId;
import io.cess.core.annotation.ViewById;
import io.cess.core.gallery.CoreConfig;
import io.cess.core.gallery.FunctionConfig;
import io.cess.core.gallery.GalleryFinal;
import io.cess.core.gallery.ThemeConfig;
import io.cess.core.gallery.UILImageLoader;
import io.cess.core.gallery.model.PhotoInfo;

/**
 * @author lin
 * @date 03/03/2017.
 */
@ResCls(R.class)
@ResId(id="io_cess_core_images_item")
public class ImagesItemView extends ResView {

    private ImagesView mImages;
    private String mImageUrl;
    public ImagesItemView(ImagesView images) {
        super(images.getContext());
        this.mImages = images;
    }



    @ViewById(id="io_cess_core_images_item_img")
    private ImageView imageView;


    @Click(id="io_cess_core_images_item_delete")
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
