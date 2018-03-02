package io.cess.web;//package io.cess.web;
//
//import org.xwalk.core.XWalkJavascriptResult;
//import org.xwalk.core.XWalkUIClient;
//import org.xwalk.core.XWalkUIClient.InitiateBy;
//import org.xwalk.core.XWalkUIClient.JavascriptMessageType;
//import org.xwalk.core.XWalkUIClient.LoadStatus;
//import org.xwalk.core.XWalkView;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Build.VERSION;
//import android.os.Build.VERSION_CODES;
//import android.os.Bundle;
//import android.os.Message;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.webkit.ValueCallback;
//import android.webkit.WebSettings;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
///**
// * 
// * @author lin
// * @date Mar 13, 2015 10:03:02 PM
// *
// */
//public class LinXWalkActivity extends Activity{
//
//	private XWalkView webView;
//	@SuppressLint("SetJavaScriptEnabled")
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//		webView = new XWalkView(this.getApplicationContext(),this);
//		webView.setUIClient(new XWalkUIClient(webView){
//
//			@Override
//			public boolean onCreateWindowRequested(XWalkView view,
//					InitiateBy initiator, ValueCallback<XWalkView> callback) {
//				return super.onCreateWindowRequested(view, initiator, callback);
//			}
//
//			@Override
//			public void onFullscreenToggled(XWalkView view,
//					boolean enterFullscreen) {
//				super.onFullscreenToggled(view, enterFullscreen);
//			}
//
//			@Override
//			public void onIconAvailable(XWalkView view, String url,
//					Message startDownload) {
//				super.onIconAvailable(view, url, startDownload);
//			}
//
//			@Override
//			public void onJavascriptCloseWindow(XWalkView view) {
//				super.onJavascriptCloseWindow(view);
//			}
//
//			@Override
//			public boolean onJavascriptModalDialog(XWalkView view,
//					JavascriptMessageType type, String url, String message,
//					String defaultValue, XWalkJavascriptResult result) {
//				return super.onJavascriptModalDialog(view, type, url, message, defaultValue,
//						result);
//			}
//
//			@Override
//			public void onPageLoadStarted(XWalkView view, String url) {
//				super.onPageLoadStarted(view, url);
//			}
//
//			@Override
//			public void onPageLoadStopped(XWalkView view, String url,
//					LoadStatus status) {
//				super.onPageLoadStopped(view, url, status);
//				view.load("javascript:alert('ok success.');", null);
//			}
//
//			@Override
//			public void onReceivedIcon(XWalkView view, String url, Bitmap icon) {
//				super.onReceivedIcon(view, url, icon);
//			}
//
//			@Override
//			public void onReceivedTitle(XWalkView view, String title) {
//				super.onReceivedTitle(view, title);
//			}
//
//			@Override
//			public void onRequestFocus(XWalkView view) {
//				super.onRequestFocus(view);
//			}
//
//			@Override
//			public void onScaleChanged(XWalkView view, float oldScale,
//					float newScale) {
//				super.onScaleChanged(view, oldScale, newScale);
//			}
//
//			@Override
//			public void onUnhandledKeyEvent(XWalkView view, KeyEvent event) {
//				// TODO Auto-generated method stub
//				super.onUnhandledKeyEvent(view, event);
//			}
//
//			@Override
//			public void openFileChooser(XWalkView view,
//					ValueCallback<Uri> uploadFile, String acceptType,
//					String capture) {
//				super.openFileChooser(view, uploadFile, acceptType, capture);
//			}
//
//			@Override
//			public boolean shouldOverrideKeyEvent(XWalkView view, KeyEvent event) {
//				return super.shouldOverrideKeyEvent(view, event);
//			}
//			
//		});
//		webView.addJavascriptInterface(null, "");
//		
////		WebSettings webSettins = webView.getSettings();
////		webSettins.setJavaScriptEnabled(true);
////		webSettins.setUseWideViewPort(true);
////		webView.setWebViewClient(new LinWebViewClient(this,webView));
////		webView.setWebChromeClient(new LinWebChromeClient(this,webView));
//		webView.setLayoutParams(new LinearLayout.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.MATCH_PARENT,
//        1.0F));
//		this.setContentView(webView);
////		webView.setBackgroundColor(0xffffff00);
//	}
//
//	protected void loadUrl(String url){
////		webView.loadUrl(url);
////		super.get
//		webView.load(url, null);
//	}
////	@Override
////    protected void didTryLoadRuntimeView(View runtimeView) {
////        if (runtimeView != null) {
////            setContentView(runtimeView);
////            getRuntimeView().loadAppFromUrl("file:///android_asset/www/index.html");
////        } else {
////            TextView msgText = new TextView(this);
////            msgText.setText("Crosswalk failed to initialize.");
////            msgText.setTextSize(36);
////            msgText.setTextColor(Color.BLACK);
////            setContentView(msgText);
////        }
////    }
////
////    @SuppressLint("InlinedApi")
////	private void enterFullscreen() {
//////        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT &&
//////                ((getWindow().getAttributes().flags &
//////                        WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0)) {
//////            View decorView = getWindow().getDecorView();
//////            decorView.setSystemUiVisibility(
//////                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
//////                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
//////                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
//////                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
//////                    View.SYSTEM_UI_FLAG_FULLSCREEN |
//////                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//////        }
////    }
////
////    public void setIsFullscreen(boolean isFullscreen) {
////        if (isFullscreen) {
////            enterFullscreen();
////        }
////    }
//}
