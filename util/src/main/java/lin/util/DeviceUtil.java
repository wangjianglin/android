package lin.util;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.provider.Settings;

public class DeviceUtil {

	/**
	 * 手机设备参数信息
	 * @param ctx
	 */
	public static String collectDeviceInfo(Context ctx) {
		StringBuffer sb = new StringBuffer();
		try{
			PackageManager pm = ctx.getPackageManager();  
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);  
			if (pi != null) {  
				String versionName = pi.versionName == null ? "null" : pi.versionName;  
				String versionCode = pi.versionCode + ""; 
//				infos.put("versionName", versionName);  
//				infos.put("versionCode", versionCode);  
				sb.append("versionName=");
				sb.append(versionName);
				sb.append("\nversionCode=");
				sb.append(versionCode);
			}
		}
		catch(NameNotFoundException e){
//			Log.e(TAG, "an error occured when collect package info",e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for(Field field:fields){
			try{
				field.setAccessible(true);
				sb.append("\n");
				sb.append(field.getName());
				sb.append("=");
				sb.append(field.get(null).toString());
//				infos.put(field.getName(), field.get(null).toString());
//				Log.d(TAG,field.getName()+":"+field.get(null));
			}
			catch(Exception e){
//				Log.e(TAG, "an error occured when collect crash info",e);
			}
		}
		return sb.toString();
	}

	public static String uuid(Context context) {
		return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
	}
}
