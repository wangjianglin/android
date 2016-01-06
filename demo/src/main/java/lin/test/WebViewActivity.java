package lin.test;


import android.os.Bundle;

public class WebViewActivity extends lin.web.LinXWalkActivity{
//public class WebViewActivity extends lin.web.LinWebViewActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_web_view);

//        super.loadUrl("http://192.168.1.66:9002/editor.html");
        //super.loadUrl("file:///android_asset/index.html?debug=true&debugJs=true&url=http://test.feicuibaba.com&channel=own" + "#/login");
        super.loadUrl("file:///android_asset/html/index.html");
    }
}
