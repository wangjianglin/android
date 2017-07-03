package lin.core;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;

import lin.core.log.Log;
import lin.util.DeviceUtil;


/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告
 * @author Administrator
 *
 */
public class CrashHandler {

	public static enum CrashHandlerType{
		DEFAULT,EXIT,RESTART
	}
//	public static final String TAG ="CrashHandler";
	//CrashHandler 实例
	private static CrashHandler INSTANCE = new	CrashHandler();
	//context 
	private Context mContext;
	private UncaughtExceptionHandler mDefaultHandler;
//	private Map<String,String>infos =new HashMap<String, String>();
//	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	
	private CrashHandler(){};
	public static CrashHandler getInstance(){
		return INSTANCE;
	}
	private CrashListener listener;
	private CrashHandlerType handlerType = CrashHandlerType.DEFAULT;

	public void setCrashListener(CrashListener listener){
		this.listener = listener;
	}
	public void init(Context context){
		mContext = context;
		
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(){

			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				try {
					CrashHandler.this.uncaughtException(thread, ex);
				}catch (Throwable e){
					try {
						Log.crash(null,Utils.printStackTrace(e));
					}catch (Throwable e1){e1.printStackTrace();}
				}
			}});
	}

	public void setHandlerType(CrashHandlerType handlerType){
		this.handlerType = handlerType;
	}

	public CrashHandlerType getHandlerType(){
		return this.handlerType;
	}

	private void uncaughtException(Thread thread,Throwable ex){
		if(!handleException(thread,ex)&&mDefaultHandler !=null){
			mDefaultHandler.uncaughtException(thread, ex);
		}
		if (handlerType == CrashHandlerType.EXIT){
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
		if (handlerType == CrashHandlerType.RESTART){
//			Intent intent = new Intent();
//			intent.setClass(mContext, MainActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			mContext.startActivity(intent);
//			android.os.Process.killProcess(android.os.Process.myPid());
			if(Utils.isAppRunningBackground(mContext)) {
				Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				mContext.startActivity(intent);
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		}
//		else{
//			mDefaultHandler.uncaughtException(thread, ex);
//			try{
//				Thread.sleep(3000);
//			}
//			catch(InterruptedException e){
//				Log.e(TAG, "error:", e);
//			}
//			// 退出程序,注释下面的重启启动程序代码
////			android.os.Process.killProcess(android.os.Process.myPid());
////			System.exit(1);
//			// 重新启动程序，注释上面的退出程序
//			//Intent intent = new Intent();
//			//intent.setClass(mContext, MainActivity.class);
//			//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			//mContext.startActivity(intent);
//			//android.os.Process.killProcess(android.os.Process.myPid());
//		}
	}
	/**
	 * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成
	 * @param ex
	 * @return true
	 */
	private boolean handleException (Thread thread,Throwable ex) {
//		if(ex == null){
//			return false;
//		}
		//使用Toast 来显示异常信息
		
		new Thread(){
			@Override
			public void run(){
				Looper.prepare();
				Toast.makeText(mContext, "很抱歉，程序出现未知异常！", Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();
		
//		collectDeviceInfo(mContext);
//		saveCrashInfo2File(ex);
		if(listener != null){
			
			Crash crash = new Crash();
			crash.setStackTrace(Utils.printStackTrace(ex));
			crash.setDeviceInfo(DeviceUtil.collectDeviceInfo(this.mContext));
			crash.setThreadInfo(Utils.threadInfo(thread));
			listener.crash(crash);
			return true;
		}
		return false;
	}
	
	/**
	 * 保存错误信息到文件中
	 * @param ex
	 * @return 返回的文件名称，便于将文件传送到服务器
	 */
//	private String printStackTrace(Throwable ex){
//		if(ex == null){
//			return "";
//		}
//		Writer writer =new StringWriter();
//		PrintWriter printWriter = new PrintWriter(writer);
//		ex.printStackTrace(printWriter);
//		Throwable cause = ex.getCause();
//		while (cause != null ) {
//			printWriter.println();
//			cause.printStackTrace(printWriter);
//			cause = cause.getCause();
//		}
//		printWriter.close();
//		return writer.toString();
//	}
	
//	private String threadInfo(Thread thread){
//		if(thread == null){
//			return null;
//		}
//		StringBuffer sb = new StringBuffer();
//		sb.append("name:");
//		sb.append(thread.getName());
//
//		sb.append("\nid:");
//		sb.append(thread.getId());
//
//		StackTraceElement[] elements = thread.getStackTrace();
//		if(elements != null){
//			for(int n=0;n<elements.length;n++){
//				sb.append('\n');
//				sb.append(elements[n].getClassName());
//				sb.append('.');
//				sb.append(elements[n].getMethodName());
//				sb.append(" line:");
//				sb.append(elements[n].getLineNumber());
//			}
//		}
//		return sb.toString();
//	}

}
