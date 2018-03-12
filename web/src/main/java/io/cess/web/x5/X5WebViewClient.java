package io.cess.web.x5;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * @author lin
 * @date 05/02/2017.
 */

public class X5WebViewClient extends WebViewClient {
    /**
     * 防止加载网页时调起系统浏览器
     */
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    public void onReceivedHttpAuthRequest(WebView webview,
                                          com.tencent.smtt.export.external.interfaces.HttpAuthHandler httpAuthHandlerhost, String host,
                                          String realm) {
        boolean flag = httpAuthHandlerhost.useHttpAuthUsernamePassword();
    }
}
