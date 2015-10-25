package lin.web.plugin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import lin.web.WebCache;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

/**
 * 
 * @author lin
 * @date Apr 21, 2015 3:54:51 PM
 *
 */
public class LinAppPlugin extends LinWebPlugin{
	
	private PackageInfo packInfo = null;
	public LinAppPlugin(Context context){
		super(context);
		try {
			PackageManager packageManager = this.getContext().getPackageManager();
			packInfo = packageManager.getPackageInfo(this.getContext().getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String bulid(){
		return packInfo.versionCode + "";
	}
	
	public String version(Map<String,Object> args){
		return packInfo.versionName;
	}
	
	public String identifier(){
		return packInfo.packageName;
	}
	
	@SuppressLint("ShowToast")
	public void info(Map<String,Object> args){
		Object text = args.get("text");
		if(text != null){
			Toast.makeText(this.getContext(), text.toString(),2000).show();
		}
	}


	public void copy(Map<String,Object> args){

//		String type = (String) args.get("type");
		String content = (String) args.get("content");
//		String sn = (String) args.get("sn");
//		if("image".equals(type)){
//			final ProgressDialog pd = ProgressDialog.show(this.getActivity(), "分享", "正在分享中，请稍后……"); 
//			saveImage(content,sn);
//		}else{
			ClipboardManager clipboard =(ClipboardManager) this.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
			clipboard.setText(content);
	
//			clipboard.getText();
//		}
	}
	
	private Handler m = new Handler();
	public void saveImage(Map<String,Object> args){
		String path = lin.util.Files.externalStorage(this.getContext());
		if(path == null){
			AlertDialog.Builder dlg = new AlertDialog.Builder(this.getActivity());
	        dlg.setMessage("没有检测到SD卡，请插入SD后重试！");
	        dlg.setTitle("提示");
	        //Don't let alerts break the back button
	        dlg.setCancelable(true);
	        dlg.setPositiveButton(android.R.string.ok,null);
	        dlg.show();
			return;
		}

		if(lin.util.Files.fileAvailableSize(path) < 4 * 1024 * 1024){
			
			AlertDialog.Builder dlg = new AlertDialog.Builder(this.getActivity());
	        dlg.setMessage("SD卡存储空间不足，请有足够多空间后重试！");
	        dlg.setTitle("提示");
	        dlg.setCancelable(true);
	        dlg.setPositiveButton(android.R.string.ok,null);
	        dlg.show();
			return;
		}
		
		final String image = (String) args.get("image");
		final String sn = (String) args.get("sn");
		final ProgressDialog pd = ProgressDialog.show(this.getActivity(), "保存", "正在保存中，请稍后……"); 
//		pd.show();
		new Thread(new Runnable(){

			@Override
			public void run() {
				saveImageImpl(image,sn,pd);
			}}).start();
		
	}
	private void saveImageImpl(String image,String sn,final ProgressDialog pd){
		String fileName = WebCache.cacheFileName(this.getActivity(), image);
//		String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		String path = lin.util.Files.externalStorage(this.getContext());
//		String path = "/storage/emulated/0";
		File dir = new File(path + "/DCIM/翡翠吧吧/");
		if(!dir.exists()){
			dir.mkdirs();
		}
		String toFileName = dir.getAbsolutePath() + "/" + lin.util.MD5.digest(image) + image.substring(image.length()-4);
		try {
			waterMark(fileName,sn,toFileName);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		m.post(new Runnable(){

			@Override
			public void run() {
				pd.dismiss();
			}});
//		String bmPath;
//		try {
//			Bitmap oriBmp = BitmapFactory.decodeFile(toFileName);
////			MediaStore.Images.Media.insertImage(this.getActivity().getContentResolver(), toFileName,"翡翠吧吧","翡翠吧吧");
//			MediaStore.Images.Media.insertImage(this.getActivity().getContentResolver(), oriBmp,"翡翠吧吧","翡翠吧吧");
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
		//this.getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+ Environment.getExternalStorageDirectory()+ "/DCIM/fcbb/"))); 
		this.getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+ toFileName))); 

//		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE); 
//		Uri uri = Uri.fromFile(new File(bmPath)); 
//		intent.setData(uri);
//		this.getActivity().sendBroadcast(intent);
//		this.getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory()))); 
	}
	
	private void waterMark(String fileName,String sn,String toFileName) throws Throwable{
//		if(new File(fileName+".sn").exists()){
//			return;
//		}
		Bitmap oriBmp = BitmapFactory.decodeFile(fileName);
		Bitmap mbmpTest = Bitmap.createBitmap(oriBmp.getWidth(),oriBmp.getHeight(), Config.ARGB_8888);
		Paint p = new Paint();
		Canvas canvasTemp = new Canvas(mbmpTest);
		canvasTemp.drawBitmap(oriBmp, 0,0, p);
		String familyName = "宋体";
		Typeface font = Typeface.create(familyName,Typeface.BOLD);
		p.setColor(Color.RED);
		p.setTypeface(font); 
		p.setTextSize(22);
		float textWidth = p.measureText(sn);
		canvasTemp.drawText(sn,oriBmp.getWidth() - 20 - textWidth,oriBmp.getHeight() - 10,p);
		
		canvasTemp.save();
		
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	     
		 mbmpTest.compress(Bitmap.CompressFormat.PNG, 100, baos);  
	     byte[] bytes = baos.toByteArray();  
	     try {
	    	 
	         FileOutputStream fileOutputStream = new FileOutputStream(toFileName);  
	         fileOutputStream.write(bytes);  
	         fileOutputStream.flush();  
	         fileOutputStream.close();  
	     } catch (Throwable e) {  
	         e.printStackTrace();  
	     }
	}
	
	public void openUrl(Map<String,Object> args){
		Uri uri = Uri.parse((String) args.get("url"));
	    this.getActivity().startActivity(new Intent(Intent.ACTION_VIEW,uri)); 
	}
}
