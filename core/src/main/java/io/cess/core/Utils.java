package io.cess.core;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.text.TextUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

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

	public static String threadInfo(Thread thread){
		if(thread == null){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("name:");
		sb.append(thread.getName());

		sb.append("\nid:");
		sb.append(thread.getId());

		StackTraceElement[] elements = thread.getStackTrace();
		if(elements != null){
			for(int n=0;n<elements.length;n++){
				sb.append('\n');
				sb.append(elements[n].getClassName());
				sb.append('.');
				sb.append(elements[n].getMethodName());
				sb.append(" line:");
				sb.append(elements[n].getLineNumber());
			}
		}
		return sb.toString();
	}

	public static String printStackTrace(Throwable ex){
		if(ex == null){
			return "";
		}
		Writer writer =new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null ) {
			printWriter.println();
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		return writer.toString();
	}

	public static boolean isDebug(Context context){
		ApplicationInfo appInfo = context
				.getApplicationInfo();
		return (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
	}
//	public static boolean isRunningForeground(Context context)  
//	{  
//	    ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);  
//	    ComponentName cn = am.getRunningTasks(1).get(0).topActivity;  
//	    String currentPackageName = cn.getPackageName();  
//	    if(!StringUtil.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName()))
//	    {  
//	        return true ;  
//	    }  
//	   
//	    return false ;  
//	}
}
