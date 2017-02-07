package lin.demo;


import android.os.Bundle;

import lin.core.ViewActivity;

//public class WebViewActivity extends ViewActivity{
//
//}
//public class WebViewActivity extends lin.web.LinXWalkActivity{
public class WebViewActivity extends lin.web.LinWebViewActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_web_view);

//        super.loadUrl("http://www.baidu.com");
//        super.loadUrl("file:///android_asset/android-index.html");
        super.loadUrl("file:///android_asset/index.html?debug=false&debugJs=true&url=http://s.feicuibaba.com&channel=own");// + "#/login");
//        super.loadUrl("file:///android_asset/httpdns.html");

    }
}
