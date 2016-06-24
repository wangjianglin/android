package lin.web;


import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.webkit.ValueCallback;
import android.webkit.WebResourceResponse;

import java.io.InputStream;

public class LinXWalkClient extends XWalkResourceClient{

	private Activity activity = null;
	public LinXWalkClient(XWalkView webView,Activity activity) {
		super(webView);
		this.activity = activity;
	}

	@Override
	public void onLoadFinished(XWalkView view, String url) {
		
		super.onLoadFinished(view, url);
	}

	@Override
	public void onLoadStarted(XWalkView view, String url) {
//		System.out.println("onLoadStarted:"+url);
		super.onLoadStarted(view, url);
	}

	@Override
	public void onProgressChanged(XWalkView view, int progressInPercent) {
		super.onProgressChanged(view, progressInPercent);
	}

	@Override
	public void onReceivedLoadError(XWalkView view, int errorCode,
			String description, String failingUrl) {
		super.onReceivedLoadError(view, errorCode, description, failingUrl);
	}

	@Override
	public void onReceivedSslError(XWalkView view,
			ValueCallback<Boolean> callback, SslError error) {
		super.onReceivedSslError(view, callback, error);
	}

	@Override
	public WebResourceResponse shouldInterceptLoadRequest(XWalkView view,
														  String url) {
		InputStream in = WebCache.cache(this.activity, url);
		WebResourceResponse response = null;
		if(in == null){
			response = super.shouldInterceptLoadRequest(view, url);
		}else{
			response = new WebResourceResponse("images/*","utf-8",in);
		}
		return response;
	}

//	@Override
//	public XWalkWebResourceResponse shouldInterceptLoadRequest(XWalkView view, XWalkWebResourceRequest request) {
//		InputStream in = WebCache.cache(this.activity, request.getUrl().toString());
//		XWalkWebResourceResponse response = null;
//		if(in == null){
//			response = super.shouldInterceptLoadRequest(view, request);
//		}else{
//			response = this.createXWalkWebResourceResponse("images/*","utf-8",in);
//		}
//		return response;
//	}



	@SuppressLint("DefaultLocale")
	@Override
	public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
//		System.out.println("shouldOverrideUrlLoading:"+url);
		if(url == null || "".equals(url)){
			return true;
		}
		if(url.toLowerCase().startsWith("tel:")){
			String tel = url.substring(4);
//			Intent intent = new Intent("android.intent.action.CALL", 
//		             Uri.parse("tel:" + tel)); 
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
	        //通知activtity处理传入的call服务
//	        Android_2Activity.this.startActivity(intent);
			this.activity.startActivity(intent);
			return true;
		}
		return super.shouldOverrideUrlLoading(view, url);
	}


}
