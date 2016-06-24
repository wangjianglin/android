package lin.test;


import android.os.Bundle;

public class WebViewActivity extends lin.web.LinXWalkActivity{
//public class WebViewActivity extends lin.web.LinWebViewActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_web_view);

//        super.loadUrl("http://192.168.1.66:9002/editor.html");
        super.loadUrl("file:///android_asset/index.html?debug=false&debugJs=true&url=http://s.feicuibaba.com&channel=own");// + "#/login");
//        super.loadUrl("file:///android_asset/httpdns.html");

    }
}
