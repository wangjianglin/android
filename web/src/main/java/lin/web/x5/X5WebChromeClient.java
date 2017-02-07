package lin.web.x5;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by lin on 05/02/2017.
 */

public class X5WebChromeClient extends WebChromeClient {
    @Override
    public boolean onJsConfirm(WebView arg0, String arg1, String arg2, JsResult arg3) {
        return super.onJsConfirm(arg0, arg1, arg2, arg3);
    }

    View myVideoView;
    View myNormalView;
    IX5WebChromeClient.CustomViewCallback callback;

    ///////////////////////////////////////////////////////////
    //
    /**
     * 全屏播放配置
     */
    @Override
    public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
//			FrameLayout normalView = (FrameLayout) ((Activity) getContext()).findViewById(R.id.web_filechooser);
//			ViewGroup viewGroup = (ViewGroup) normalView.getParent();
//			viewGroup.removeView(normalView);
//			viewGroup.addView(view);
//			myVideoView = view;
//			myNormalView = normalView;
//			callback = customViewCallback;
    }

    @Override
    public void onHideCustomView() {
        if (callback != null) {
            callback.onCustomViewHidden();
            callback = null;
        }
        if (myVideoView != null) {
            ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
            viewGroup.removeView(myVideoView);
            viewGroup.addView(myNormalView);
        }
    }

    @Override
    public boolean onShowFileChooser(WebView arg0,
                                     ValueCallback<Uri[]> arg1, FileChooserParams arg2) {
        // TODO Auto-generated method stub
        Log.e("app", "onShowFileChooser");
        return super.onShowFileChooser(arg0, arg1, arg2);
    }

    @Override
    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String captureType) {
        Log.e("app", "openFileChooser");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        try
//        {
//            ((Activity) (this.getContext())).startActivityForResult(Intent.createChooser(intent, "choose files"),
//                    1);
//        }
//        catch (android.content.ActivityNotFoundException ex)
//        {
//
//        }

        super.openFileChooser(uploadFile, acceptType, captureType);
    }
    /**
     * webview 的窗口转移
     */
    @Override
    public boolean onCreateWindow(WebView arg0, boolean arg1, boolean arg2, Message msg) {
        // TODO Auto-generated method stub
//        if (X5WebView.isSmallWebViewDisplayed == true) {
//
//            WebView.WebViewTransport webViewTransport = (WebView.WebViewTransport) msg.obj;
//            WebView webView = new WebView(X5WebView.this.getContext()) {
//
//                protected void onDraw(Canvas canvas) {
//                    super.onDraw(canvas);
//                    Paint paint = new Paint();
//                    paint.setColor(Color.GREEN);
//                    paint.setTextSize(15);
//                    canvas.drawText("新建窗口", 10, 10, paint);
//                };
//            };
//            webView.setWebViewClient(new WebViewClient() {
//                public boolean shouldOverrideUrlLoading(WebView arg0, String arg1) {
//                    arg0.loadUrl(arg1);
//                    return true;
//                };
//            });
//            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(400, 600);
//            lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
//            X5WebView.this.addView(webView, lp);
//            webViewTransport.setWebView(webView);
//            msg.sendToTarget();
//        }
        return true;
    }

    @Override
    public boolean onJsAlert(WebView arg0, String arg1, String arg2, JsResult arg3) {
        /**
         * 这里写入你自定义的window alert
         */
        // AlertDialog.Builder builder = new Builder(getContext());
        // builder.setTitle("X5内核");
        // builder.setPositiveButton("确定", new
        // DialogInterface.OnClickListener() {
        //
        // @Override
        // public void onClick(DialogInterface dialog, int which) {
        // // TODO Auto-generated method stub
        // dialog.dismiss();
        // }
        // });
        // builder.show();
        // arg3.confirm();
        // return true;
        Log.i("yuanhaizhou", "setX5webview = null");
        return super.onJsAlert(null, "www.baidu.com", "aa", arg3);
    }

    /**
     * 对应js 的通知弹框 ，可以用来实现js 和 android之间的通信
     */
    @Override
    public boolean onJsPrompt(WebView arg0, String arg1, String arg2, String arg3, JsPromptResult arg4) {
        // 在这里可以判定js传过来的数据，用于调起android native 方法
        if (this.isMsgPrompt(arg1)) {
            if (this.onJsPrompt(arg2, arg3)) {
                return true;
            } else {
                return false;
            }
        }
        return super.onJsPrompt(arg0, arg1, arg2, arg3, arg4);
    }

    @Override
    public void onReceivedTitle(WebView arg0, final String arg1) {
        super.onReceivedTitle(arg0, arg1);
        Log.i("yuanhaizhou", "webpage title is " + arg1);

    }

    /**
     * 判定当前的prompt消息是否为用于调用native方法的消息
     *
     * @param msg
     *            消息名称
     * @return true 属于prompt消息方法的调用
     */
    private boolean isMsgPrompt(String msg) {
        if (msg != null && msg.startsWith(SecurityJsBridgeBundle.PROMPT_START_OFFSET)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 当webchromeClient收到 web的prompt请求后进行拦截判断，用于调起本地android方法
     *
     * @param methodName
     *            方法名称
     * @param blockName
     *            区块名称
     * @return true ：调用成功 ； false ：调用失败
     */
    private boolean onJsPrompt(String methodName, String blockName) {
        String tag = SecurityJsBridgeBundle.BLOCK + blockName + "-" + SecurityJsBridgeBundle.METHOD + methodName;

//        if (this.mJsBridges != null && this.mJsBridges.containsKey(tag)) {
//            ((SecurityJsBridgeBundle) this.mJsBridges.get(tag)).onCallMethod();
//            return true;
//        } else {
            return false;
//        }
    }
}
