package lin.core;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

/**
 * 
 * @author lin
 * @date Jun 30, 2015 11:41:04 AM
 *
 */
public class Utils {

	public static void tel(Activity activity,String tel){
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
        //通知activtity处理传入的call服务
//        Android_2Activity.this.startActivity(intent);
		activity.startActivity(intent);
	}
	
	/**
	 * 判断当前 app 是否在前台运行
	 * @param context
	 * @return
	 */
	public static boolean isAppRunningForeground(Context context)
	{  
	    ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);  
	    ComponentName cn = am.getRunningTasks(1).get(0).topActivity;  
	    String currentPackageName = cn.getPackageName();  
	    if(!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName()))  
	    {  
	        return true ;  
	    }  
	   
	    return false ;  
	}

	public static boolean isAppRunningBackground(Context context){
		return !isAppRunningForeground(context);
	}
	
//	public static boolean isRunningForeground(Context context)  
//	{  
//	    ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);  
//	    ComponentName cn = am.getRunningTasks(1).get(0).topActivity;  
//	    String currentPackageName = cn.getPackageName();  
//	    if(!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName()))  
//	    {  
//	        return true ;  
//	    }  
//	   
//	    return false ;  
//	}
}
