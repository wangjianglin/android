package lin.web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import lin.web.plugin.JavaScriptObject;
import lin.web.with.LinWebChromeClient;
import lin.web.with.LinWebView;
import lin.web.with.LinWebViewClient;
import lin.web.with.LinWebViewConsole;
import lin.web.x5.X5WebView;

/**
 * 
 * @author lin
 * @date Mar 13, 2015 10:03:02 PM
 *
 */
public class LinWebViewActivity extends Activity{

	private X5WebView webView;
	private TextView textView;
	private ScrollView scrollView;
	@SuppressLint({ "SetJavaScriptEnabled", "SdCardPath" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.lin_web_view);
//		webView = new LinWebView(this);
		webView = (X5WebView) this.findViewById(R.id.lin_web_view_view);
		System.out.println(webView.getView().getClass().getName());



		webView.requestFocus(View.FOCUS_DOWN);

		scrollView = (ScrollView) this.findViewById(R.id.lin_web_view_scroll_view);
		textView = (TextView) this.findViewById(R.id.lin_web_view_text_view);
//		scrollView =
//		webView.setBackgroundColor(0xffffff00);
//		webView.getWebChromeClient().setLinWebViewConsole(new LinWebViewConsole(){
//
//			@Override
//			public void consoleMessage(ConsoleMessage message) {
//				if(consoleEnable){
//					textView.append(message.sourceId()+":"+message.lineNumber()+"\n" +message.message() + "\n");
//				}
//			}
//		});
	}
	
	/**
//     * 清除WebView缓存
//     */ 
//    public void clearCache(){ 
//           try{
//        //清理Webview缓存数据库 
//        try { 
//            deleteDatabase("webview.db");  
//            deleteDatabase("webviewCache.db"); 
//        } catch (Exception e) { 
//            e.printStackTrace(); 
//        } 
//           
//        //WebView 缓存文件 
//        File appCacheDir = new File(getFilesDir().getAbsolutePath()+APP_CACAHE_DIRNAME); 
////        Log.e(TAG, "appCacheDir path="+appCacheDir.getAbsolutePath()); 
//           
//        File webviewCacheDir = new File(getCacheDir().getAbsolutePath()+"/webviewCache"); 
////        Log.e(TAG, "webviewCacheDir path="+webviewCacheDir.getAbsolutePath()); 
//           
//        //删除webview 缓存目录 
//        if(webviewCacheDir.exists()){ 
//            deleteFile(webviewCacheDir); 
//        } 
//        //删除webview 缓存 缓存目录 
//        if(appCacheDir.exists()){ 
//            deleteFile(appCacheDir); 
//        } 
//           }catch(Throwable e){
//        	   e.printStackTrace();
//           }
//    }
//    /**
//     * 递归删除 文件/文件夹
//     * 
//     * @param file
//     */ 
//    private void deleteFile(File file) { 
//   
////        Log.i(TAG, "delete file path=" + file.getAbsolutePath()); 
//           
//        if (file.exists()) { 
//            if (file.isFile()) { 
//                file.delete(); 
//            } else if (file.isDirectory()) { 
//                File files[] = file.listFiles(); 
//                for (int i = 0; i < files.length; i++) { 
//                    deleteFile(files[i]); 
//                } 
//            } 
//            file.delete(); 
//        } 
//    }
//    private final String APP_CACAHE_DIRNAME = "APP_CACAHE_DIRNAME";

	private boolean consoleEnable = false;
	
	public boolean isConsoleEnable() {
		return consoleEnable;
	}
	public void setConsoleEnable(boolean consoleEnable) {
		this.consoleEnable = consoleEnable;
	}

	private boolean isShow = false;
	public void showConsole(){
		scrollView.setLayoutParams(new LinearLayout.LayoutParams(
	            ViewGroup.LayoutParams.MATCH_PARENT,
	            600,
	        1.0F));
		this.webView.requestLayout();
		scrollView.requestLayout();
		textView.requestLayout();
		isShow = true;
	}
	public boolean consoleStatue(){
		return isShow;
	}
	public void hideConsole(){
		scrollView.setLayoutParams(new LinearLayout.LayoutParams(
	            ViewGroup.LayoutParams.MATCH_PARENT,
	            0,
	        1.0F));
		isShow = false;
	}
	protected void loadUrl(String url){
		webView.loadUrl(url);//webView.loadDataWithBaseURL();
//		webView.loadDataWithBaseURL("file:///android_asset","","text/html","utf-8","");
	}
	
//	public void back(){
//		webView.canGoBack();
//	}
	
//	public LinWebView getWebView(){
//		return webView;
//	}
}
