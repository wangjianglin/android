package lin.demo.http;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import lin.comm.http.Error;
import lin.comm.http.ExceptionPackage;
import lin.comm.http.FileInfo;
import lin.comm.http.HttpCommunicate;
import lin.comm.http.HttpCommunicateImpl;
import lin.comm.http.ResultListener;
import lin.comm.http.SessionIdPackage;
import lin.comm.http.TestPackage;
import lin.comm.httpdns.AliHttpDNS;
import lin.core.LayoutInflaterFactory;
import lin.core.ViewActivity;
import lin.core.annotation.Click;
import lin.demo.R;
import lin.demo.databinding.ActivityFormBinding;
import lin.demo.form.FormData;
import lin.demo.form.FormView;

public class HttpActivity extends ViewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ptr_default);
        super.onCreate(savedInstanceState);
//        ActivityFormBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_form);
//        setContentView(R.layout.activity_form);

        setContentView(R.layout.activity_http);
    }

    @Click(R.id.activity_http_button1)
    private void c1(){

//        ExceptionPackage pack = new ExceptionPackage();
//        pack.setWarning(true);
        TestPackage pack = new TestPackage();

        pack.setData("测试数据！");

//        ExceptionPackage pack = new ExceptionPackage();

        HttpCommunicateImpl impl = HttpCommunicate.get("impl");

        impl.setDebug(true);
        impl.setMainThread(true);

        impl.setHttpDNS(new AliHttpDNS(this,"172280"){
            @Override
            public String getIpByHost(String hostName) {
//                return super.getIpByHost(hostName);
                return "120.76.68.177";
            }
        });

        try {
            impl.setCommUrl(new URL("https://s.feicuibaba.com"));
//            HttpCommunicate.setCommUrl(new URL("http://192.168.0.100:8080/fcbb_b2b2c"));
//            HttpCommunicate.setCommUrl(new URL("http://192.168.1.66:8080/fcbb_b2b2c"));
//            HttpCommunicate. setCommUrl(new URL("http://120.76.68.177/"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        impl.request(pack, new ResultListener<String>() {
            @Override
            public void result(String obj, List<Error> warning) {
                Toast.makeText(getApplicationContext(),"obj:"+obj,Toast.LENGTH_LONG).show();
                System.out.println();
            }

            @Override
            public void fault(Error error) {
                Toast.makeText(getApplicationContext(),error.getStackTrace(),Toast.LENGTH_LONG).show();
                System.out.println();
            }
        });


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                testSesssionId(HttpCommunicate.global());
//
//                System.out.println();
//                System.out.println();
//
//                HttpCommunicateImpl impl2 = HttpCommunicate.get("test");
//                impl2.setCommUrl(HttpCommunicate.getCommUrl());
//                testSesssionId(impl2);
//            }
//        }).start();
    }//https://www.feicuibaba.com/proxy/proxy-channel.apk.php?channel=

    @Click(R.id.activity_http_button2)
    private void c2(){
        HttpCommunicateImpl impl = HttpCommunicate.get("impl");

        impl.setDebug(true);
        impl.setMainThread(true);

        impl.setHttpDNS(new AliHttpDNS(this,"172280"){
            @Override
            public String getIpByHost(String hostName) {
//                return super.getIpByHost(hostName);
                return "120.76.68.177";
            }
        });

        try {
            HttpCommunicate.setCommUrl(new URL("https://s.feicuibaba.com"));
//            HttpCommunicate.setCommUrl(new URL("http://192.168.0.100:8080/fcbb_b2b2c"));
//            HttpCommunicate.setCommUrl(new URL("http://192.168.1.66:8080/fcbb_b2b2c"));
//            HttpCommunicate. setCommUrl(new URL("http://120.76.68.177/"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        impl.download("https://www.feicuibaba.com/proxy/proxy-channel.apk.php?channel=own", new ResultListener<FileInfo>() {
            @Override
            public void result(FileInfo obj, List<Error> warning) {
                Toast.makeText(getApplicationContext(),obj.getFile().getAbsolutePath(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void fault(Error error) {
                Toast.makeText(getApplicationContext(),"错误",Toast.LENGTH_LONG).show();
                System.out.println(error.getStackTrace());
            }
        });
    }

    private void testSesssionId(HttpCommunicateImpl impl){

        for(int n=0;n<10;n++) {
            SessionIdPackage sessionIdPackage = new SessionIdPackage();

            impl.request(sessionIdPackage, new ResultListener<String>() {
                @Override
                public void result(String obj, List<Error> warning) {

//                    Log.d("session id",obj);
                    System.out.println("session id:" + obj);
                }

                @Override
                public void fault(Error error) {
//                    Log.d("session id",error.getStackTrace());
                    System.out.println("session error.");
                    System.out.println(error.getStackTrace());
                }
            }).waitForEnd();

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
