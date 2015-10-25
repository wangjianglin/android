package lin.core;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;

/**
 * 
 * @author lin
 * @date Mar 12, 2015 10:08:11 PM
 *
 */
public class ImagePickerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		this.setContentView(R.layout.public_image_picker_activity);
		Intent intent = new Intent();  
        /* 开启Pictures画面Type设定为image */  
        intent.setType("image/*");  
//        intent.putExtra("aspectX", 30);
//        intent.putExtra("aspectY", 40);
//        intent.putExtra("outputX", 498);
//        intent.putExtra("outputY", 664);
//        intent.putExtra("crop", "circle");
        intent.putExtra("return-data", true); 
        /* 使用Intent.ACTION_GET_CONTENT这个Action */  
        intent.setAction(Intent.ACTION_GET_CONTENT);   
        /* 取得相片后返回本画面 */  
        startActivityForResult(Intent.createChooser(intent, "选择图片"), 1);
//        startActivityForResult(intent, 1);
	          
//    	ntent intent = new Intent(Intent.ACTION_GET_CONTENT, null);  
//   	 intent.setType("image/*");    //这个参数是确定要选择的内容为图片，
//   	intent.putExtra("crop", "circle");   //设置了参数，就会调用裁剪，如果不设置，就会跳过裁剪的过程。
//   	intent.putExtra("aspectX", 33);  //这个是裁剪时候的 裁剪框的 X 方向的比例。
//   	intent.putExtra("aspectY",43);  //同上Y 方向的比例. (注意： aspectX, aspectY ，两个值都需要为 整数，如果有一个为浮点数，就会导致比例失效。)
//   	//设置aspectX 与 aspectY 后，裁剪框会按照所指定的比例出现，放大缩小都不会更改。如果不指定，那么 裁剪框就可以随意调整了。
//   	intent.putExtra("outputX", 50);  //返回数据的时候的 X 像素大小。
//   	 intent.putExtra("outputY", 100);  //返回的时候 Y 的像素大小。
//   	//以上两个值，设置之后会按照两个值生成一个Bitmap, 两个值就是这个bitmap的横向和纵向的像素值，如果裁剪的图像和这个像素值不符合，那么空白部分以黑色填充。
//   	intent.putExtra("noFaceDetection", true); // 是否去除面部检测， 如果你需要特定的比例去裁剪图片，那么这个一定要去掉，因为它会破坏掉特定的比例。
//   	 intent.putExtra("return-data", true);  //是否要返回值。 一般都要。我第一次忘加了，总是取得空值，囧！
//   	startActivityForResult(intent, 1);
	}
	
//	private ImagePickerSelected selected;
	//498, 664
	private double imageWidth = 498;
	private double imageHeight = 664;
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (resultCode == RESULT_OK) {
        	//取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
			Uri mImageCaptureUri = data.getData();
			//返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取

			Bitmap image = null;
			if (mImageCaptureUri != null) {
				try {
					ContentResolver cr = this.getContentResolver();  
//					 BitmapFactory.Options options=new BitmapFactory.Options();
//			            options.inJustDecodeBounds = false;
//			            options.inSampleSize = 10;   //width，hight设为原来的十分一
//					image = BitmapFactory.decodeStream(cr.openInputStream(mImageCaptureUri),null,options);
					
					
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeStream(cr.openInputStream(mImageCaptureUri),null, options);
					double w = options.outWidth;
					double h = options.outHeight;
					
					double s = (w/h)/(imageWidth/imageHeight);
					if(s > 1){
						options.inSampleSize = (int) (w / imageWidth);
//						options.outWidth = (int) imageWidth;
//						options.outHeight = (int) (imageWidth * h /w);
					}else{
						options.inSampleSize = (int) (h / imageHeight);
//						options.outHeight = (int) imageHeight;
//						options.outWidth = (int) (imageHeight * w / h);
					}
					options.inJustDecodeBounds = false;
//					options.inPurgeable = true;
//					options.inInputShareable = true;
					image = BitmapFactory.decodeStream(cr.openInputStream(mImageCaptureUri),null,options);
		                
					//这个方法是根据Uri获取Bitmap图片的静态方法
//					image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				Bundle extras = data.getExtras();
				if (extras != null) {
					//这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
					image = extras.getParcelable("data");
				}
			}
			if(image != null && ImagePickerItem.selected != null){
				ImagePickerItem.selected.selected(image);
			}
        }  
//        super.onActivityResult(requestCode, resultCode, data); 
        this.finish();
    } 
	
}
