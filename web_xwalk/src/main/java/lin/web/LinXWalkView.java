package lin.web;

import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkView;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class LinXWalkView extends XWalkView{

//	public LinXWalkView(Context arg0, Activity arg1) {
//		super(arg0, arg1);
//		this.init();
//	}
//
//	public LinXWalkView(Context arg0, AttributeSet arg1) {
//		super(arg0, arg1);
//		this.init();
//	}
	public LinXWalkView(Context context) {
        this(context, null);
        this.init();
    }

    public LinXWalkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }


	private void init(){
//		this.set
		ApplicationInfo appInfo = getContext().getApplicationContext().getApplicationInfo();
        if ((appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
        	XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
        }
        XWalkPreferences.setValue("enable-javascript", true);
        XWalkPreferences.setValue("javascript-can-open-window", true);
        XWalkPreferences.setValue("allow-universal-access-from-file", true);
        XWalkPreferences.setValue("support-multiple-windows", true);





//        this.setVerticalScrollBarEnabled(false);
//        if (shouldRequestFocusOnInit()) {
            this.setFocusableInTouchMode(true);
            this.requestFocusFromTouch();
//        }
	}
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
    	if(keyCode == KeyEvent.KEYCODE_BACK){
//    		if (this.hasEnteredFullscreen()) {
//                this.leaveFullscreen();
//                return true;
//            }
//    		return this.backHistory();
    		return true;
    	}
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
	
    public boolean backHistory() {
        // Check webview first to see if there is a history
        // This is needed to support curPage#diffLink, since they are added to appView's history, but not our history url array (JQMobile behavior)
        if (super.getNavigationHistory().canGoBack()) {
            super.getNavigationHistory().navigate(XWalkNavigationHistory.Direction.BACKWARD, 1);
            return true;
        }
        return false;
    }

    static {
        XWalkPreferences.setValue("enable-javascript", true);
        XWalkPreferences.setValue("javascript-can-open-window", true);
        XWalkPreferences.setValue("allow-universal-access-from-file", true);
        XWalkPreferences.setValue("support-multiple-windows", true);
    }
}
