package lin.web;

import lin.web.plugin.Config;
import lin.web.plugin.JavaScriptObject;

import org.xwalk.core.JavascriptInterface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.widget.LinearLayout;

/**
 * 
 * @author lin
 * @date Apr 24, 2015 7:56:13 PM
 *
 */
public class LinXWalkActivity  extends Activity{
	private LinXWalkView webView;
	private LinXWalkChromeClient chromeClient;
	
	class XWalkJavaScriptObject{
		
		private JavaScriptObject impl = null;
		public XWalkJavaScriptObject(Context context){
			impl = new JavaScriptObject(context);
		}
		@JavascriptInterface
		public String exec(String args){
			return impl.exec(args);
		}
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		Config.init(this);
		webView = new LinXWalkView(this);
		chromeClient = new LinXWalkChromeClient(webView,this);
		webView.setUIClient(chromeClient);
		webView.setResourceClient(new LinXWalkClient(webView,this));
		webView.addJavascriptInterface(new XWalkJavaScriptObject(this), "AndroidInterface");
	
//		webView.set
//	WebSettings webSettins = webView.getSettings();
//	webSettins.setJavaScriptEnabled(true);
//	webSettins.setUseWideViewPort(true);
//	webView.setWebViewClient(new LinWebViewClient(this,webView));
//	webView.setWebChromeClient(new LinWebChromeClient(this,webView));
	webView.setLayoutParams(new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT,
    1.0F));
	this.setContentView(webView);
//	this.setContentView(R.layout.activity_main);
//	webView.setBackgroundColor(0xffffff00);
	//loadUrl("http://www.baidu.com");
//	loadUrl("http://192.168.1.66/#/login");
}

protected void loadUrl(String url){
//	webView.loadUrl(url);
//	super.get
	webView.load(url, null);
}

protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//    LOG.d(TAG, "Incoming Result");
    super.onActivityResult(requestCode, resultCode, intent);
//    Log.d(TAG, "Request code = " + requestCode);
    if (webView != null && requestCode == LinXWalkChromeClient.FILECHOOSER_RESULTCODE) {
    	ValueCallback<Uri> mUploadMessage = this.chromeClient.getValueCallback();
//        Log.d(TAG, "did we get here?");
        if (null == mUploadMessage)
            return;
        Uri result = intent == null || resultCode != Activity.RESULT_OK ? null : intent.getData();
//        Log.d(TAG, "result = " + result);
//        Uri filepath = Uri.parse("file://" + FileUtils.getRealPathFromURI(result, this));
//        Log.d(TAG, "result = " + filepath);
        mUploadMessage.onReceiveValue(result);
        mUploadMessage = null;
    }
//    CordovaPlugin callback = this.activityResultCallback;
//    if(callback == null && initCallbackClass != null) {
//        // The application was restarted, but had defined an initial callback
//        // before being shut down.
//        this.activityResultCallback = appView.pluginManager.getPlugin(initCallbackClass);
//        callback = this.activityResultCallback;
//    }
//    if(callback != null) {
//        LOG.d(TAG, "We have a callback to send this result to");
//        callback.onActivityResult(requestCode, resultCode, intent);
//    }
}
//@Override
//protected void didTryLoadRuntimeView(View runtimeView) {
//    if (runtimeView != null) {
//        setContentView(runtimeView);
//        getRuntimeView().loadAppFromUrl("file:///android_asset/www/index.html");
//    } else {
//        TextView msgText = new TextView(this);
//        msgText.setText("Crosswalk failed to initialize.");
//        msgText.setTextSize(36);
//        msgText.setTextColor(Color.BLACK);
//        setContentView(msgText);
//    }
//}
//
//@SuppressLint("InlinedApi")
//private void enterFullscreen() {
////    if (VERSION.SDK_INT >= VERSION_CODES.KITKAT &&
////            ((getWindow().getAttributes().flags &
////                    WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0)) {
////        View decorView = getWindow().getDecorView();
////        decorView.setSystemUiVisibility(
////                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
////                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
////                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
////                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
////                View.SYSTEM_UI_FLAG_FULLSCREEN |
////                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
////    }
//}
//
//public void setIsFullscreen(boolean isFullscreen) {
//    if (isFullscreen) {
//        enterFullscreen();
//    }
//}
}