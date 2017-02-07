package lin.web.with;

import java.lang.reflect.Method;

import lin.web.plugin.Config;
import lin.web.plugin.JavaScriptObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

/**
 * 
 * @author lin
 * @date Mar 13, 2015 9:54:32 PM
 *
 */
@SuppressLint("NewApi")
public class LinWebView extends WebView{

	public LinWebView(Context context, AttributeSet attrs, int defStyle,
			boolean privateBrowsing) {
		super(context, attrs, defStyle, privateBrowsing);
		this.init();
//		this.set

	}

	public LinWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.init();
	}

	public LinWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init();
	}

	public LinWebView(Context context) {
		super(context);
		this.init();
	}

	private void init(){
		Config.init(getActivity());
//		this.addJavascriptInterface(new JavaScriptInterface(this.getContext()), "AndroidInterface");
		ApplicationInfo appInfo = getContext().getApplicationContext().getApplicationInfo();
		if ((appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
			if (Build.VERSION.SDK_INT >= 19) {
				try {
					Method m = WebView.class.getDeclaredMethod("setWebContentsDebuggingEnabled", boolean.class);
					m.invoke(null, true);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}

		WebSettings webSettins = this.getSettings();
		webSettins.setJavaScriptEnabled(true);
		webSettins.setUseWideViewPort(false);
		webSettins.setAllowFileAccess(true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			webSettins.setAllowFileAccessFromFileURLs(true);
			webSettins.setAllowUniversalAccessFromFileURLs(true);
		}
//		webSettins.setAllowUniversalAccessFromFileURLs(true);
		webSettins.setCacheMode(WebSettings.LOAD_NO_CACHE);

		webSettins.setRenderPriority(WebSettings.RenderPriority.HIGH);
//        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
		// 开启 DOM storage API 功能
		webSettins.setDomStorageEnabled(true);
		//开启 database storage API 功能
		webSettins.setDatabaseEnabled(true);
		webSettins.setAppCacheEnabled(true);
		webSettins.setAllowContentAccess(true);
		if (Build.VERSION.SDK_INT < 19/*Build.VERSION_CODES.KITKAT*/) {
			webSettins.setDatabasePath("/data/data/" + this.getContext().getPackageName() + "/databases/");
		}
//		this.setWebViewClient(new LinWebViewClient(this.getActivity(), this));
//		this.setWebChromeClient(new LinWebChromeClient(this.getActivity(), this));

		final JavaScriptObject javaScriptObject = new JavaScriptObject(this.getContext());
		this.addJavascriptInterface(new Object(){

			@JavascriptInterface
			public String exec(String args){
				return  javaScriptObject.exec(args);
			}
		}, "AndroidInterface");

////			if ( 0 != ( getApplcationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE ) ) {
//				WebView.setWebContentsDebuggingEnabled(true);
////			}
//		}
//		this.getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//        this.getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	private LinWebChromeClient chromeClient;
	
	public void setWebChromeClient(LinWebChromeClient client) {
        super.setWebChromeClient(client);
        this.chromeClient = client;
    }

    public LinWebChromeClient getWebChromeClient() {
        return chromeClient;
    }
	
    private LinWebViewClient client;
//    @Override
    public void setWebViewClient(LinWebViewClient client) {
    	super.setWebViewClient(client);
    	this.client = client;
    }
    public LinWebViewClient getWebViewClient(){
    	return this.client;
    }
    
	private Activity getActivity(){
		return (Activity)this.getContext();
	}
	
//	class JavaScriptInterface{
//		
//		
//	}

	
	
	/**
     *  判断是否有响应KeyDown事件，
     *  用解决百度输入法，在按返回键关闭键盘时，程序会接收到KeyUp事件，但接收到KeyDown事件
     */
    private boolean isKeyDown = false;
    /*
     * onKeyDown
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
    	isKeyDown = true;
//        if(keyDownCodes.contains(keyCode))
//        {
//            if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
//                    // only override default behavior is event bound
//                    LOG.d(TAG, "Down Key Hit");
//                    this.loadUrl("javascript:cordova.fireDocumentEvent('volumedownbutton');");
//                    return true;
//            }
//            // If volumeup key
//            else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
//                    LOG.d(TAG, "Up Key Hit");
//                    this.loadUrl("javascript:cordova.fireDocumentEvent('volumeupbutton');");
//                    return true;
//            }
//            else
//            {
//                return super.onKeyDown(keyCode, event);
//            }
//        }
//        else if(keyCode == KeyEvent.KEYCODE_BACK)
//        {
//            return !(this.startOfHistory()) || this.bound;
//        }
//        else if(keyCode == KeyEvent.KEYCODE_MENU)
//        {
//            //How did we get here?  Is there a childView?
//            View childView = this.getFocusedChild();
//            if(childView != null)
//            {
//                //Make sure we close the keyboard if it's present
//                InputMethodManager imm = (InputMethodManager) cordova.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(childView.getWindowToken(), 0);
//                cordova.getActivity().openOptionsMenu();
//                return true;
//            } else {
//                return super.onKeyDown(keyCode, event);
//            }
//        }
        
        return super.onKeyDown(keyCode, event);
    }
    

//    @Override
//    public boolean onKeyDown(int keyCode,KeyEvent event){
//    	if (keyCode == KeyEvent.KEYCODE_BACK) {
//            InputMethodManager imm = (InputMethodManager)this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
//            boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开  
//            System.out.println("status:"+isOpen);
//    	}
//    	return super.onKeyDown(keyCode, event);
//    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
    	if(isKeyDown == false){
    		isKeyDown = false;
    		return true;
    	}
    	boolean result = this.onKeyUpImpl(keyCode, event);
    	isKeyDown = false;
    	return result;
    }
    
    private boolean onKeyUpImpl(int keyCode,KeyEvent event){
        // If back key
//        if (keyCode == KeyEvent.KEYCODE_BACK && isKeyDown) {
//            // A custom view is currently displayed  (e.g. playing a video)
//            if(mCustomView != null) {
//                this.hideCustomView();
//            } else {
//                // The webview is currently displayed
//                // If back key is bound, then send event to JavaScript
//                if (this.bound) {
//                    this.loadUrl("javascript:cordova.fireDocumentEvent('backbutton');");
//                    return true;
//                } else {
//                    // If not bound
//                    // Go to previous page in webview if it is possible to go back
//                    if (this.backHistory()) {
//                        return true;
//                    }
//                    // If not, then invoke default behavior
//                    else {
//                        //this.activityState = ACTIVITY_EXITING;
//                    	//return false;
//                    	// If they hit back button when app is initializing, app should exit instead of hang until initialization (CB2-458)
//                    	this.cordova.getActivity().finish();
//                    }
//                }
//            }
//        }
        // Legacy
//        else if (keyCode == KeyEvent.KEYCODE_MENU) {
//            if (this.lastMenuEventTime < event.getEventTime()) {
//                this.loadUrl("javascript:cordova.fireDocumentEvent('menubutton');");
//            }
//            this.lastMenuEventTime = event.getEventTime();
//            return super.onKeyUp(keyCode, event);
//        }
//        // If search key
//        else if (keyCode == KeyEvent.KEYCODE_SEARCH) {
//            this.loadUrl("javascript:cordova.fireDocumentEvent('searchbutton');");
//            return true;
//        }
//        else if(keyUpCodes.contains(keyCode))
//        {
//            //What the hell should this do?
//            return super.onKeyUp(keyCode, event);
//        }

        //Does webkit change this behavior?
        return super.onKeyUp(keyCode, event);
    }
}
