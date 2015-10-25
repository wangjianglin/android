package lin.core;

import lin.core.Images;
import lin.core.ResourceView;
import lin.core.annotation.ResourceClass;
import lin.core.annotation.ResourceId;
import lin.core.annotation.ViewById;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

/**
 * 
 * @author lin
 * @date Mar 12, 2015 5:30:19 PM
 *
 */
@ResourceClass(R.class)
@ResourceId(id="lin_core_images_picker_item")
public class ImagePickerItem extends ResourceView{

	public ImagePickerItem(Context context) {
		super(context);
		this.setBackgroundColor(0xefc0c0c0);
	}

	private static ImagePickerItem staticImageView;
	
	@ViewById(id="imagepicker_item_image_id")
	private ImageView imageView;
	
	static ImagePickerSelected selected = new ImagePickerSelected() {
		
		@Override
		public void selected(Bitmap bitmap) {
			staticImageView.imageView.setImageBitmap(bitmap);
			staticImageView._image = bitmap;
//			staticImageView.setImageDrawable(scontext.getResources().getDrawable(R.drawable.test));
		}
	};
	
	private Bitmap _image;
	public Bitmap getImage(){
		return _image;
	}
	
	@Override
	protected void onInited() {
		
//		imageView = (ImageView) this.findViewById(R.id.publish_imagepicker_item_image_id);
		rootView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(edited == false){
					return;
				}
				staticImageView = ImagePickerItem.this;
				Intent intent = new Intent(ImagePickerItem.this.getContext(),ImagePickerActivity.class);  
                /* 开启Pictures画面Type设定为image */  
                intent.setType("image/*");  
//                /* 使用Intent.ACTION_GET_CONTENT这个Action */  
                intent.setAction(Intent.ACTION_GET_CONTENT);   
                /* 取得相片后返回本画面 */  

				ImagePickerItem.this.getActivity().startActivity(intent);
//				intent.
                  				
			}
			
		});
	}

	
	@ViewById(id="images_pick_edit_and_add")
	private ImageView addImage;
	
	private boolean edited;

//	    //使图像填满
	private boolean fill;
	//
//	    //缩放
	private boolean zoom;
	//
//	    //是否显示位置标记

//	@property(readonly) NSArray * imagesForEdited;
//
//	@property(readonly) NSArray * images;
	private String imagePath;

	public boolean isEdited() {
		return edited;
	}

	public void setEdited(boolean edited) {
		this.edited = edited;
		if(addImage != null){
			addImage.setVisibility(edited?View.VISIBLE:View.INVISIBLE);
		}
	}

	public boolean isFill() {
		return fill;
	}

	public void setFill(boolean fill) {
		this.fill = fill;
	}

	public boolean isZoom() {
		return zoom;
	}

	public void setZoom(boolean zoom) {
		this.zoom = zoom;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
		Images.setImage(imageView, imagePath);
	}
}
