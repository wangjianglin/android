package lin.demo;


import android.app.Application;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import lin.comm.http.HttpCommunicate;
import lin.comm.http.HttpCommunicateType;
import lin.comm.httpdns.AliHttpDNS;
import lin.comm.httpdns.HttpDNS;
import lin.core.Images;
import lin.core.LocalStorage;

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LocalStorage.init(this);
        HttpCommunicate.init(this);

        HttpCommunicate.setType(HttpCommunicateType.HttpClient);
        HttpCommunicate.setHttpDNS(new AliHttpDNS("172280"));
        HttpCommunicate.getHttpDNS().setDegradationFilter(new HttpDNS.DegradationFilter() {
            @Override
            public boolean shouldDegradeHttpDNS(String hostName) {
                return hostName == null || !hostName.endsWith(".feicuibaba.com");
            }
        });

        HttpCommunicate.getHttpDNS().setPreResolveHosts(Arrays.asList("s.feicuibaba.com"));

        try {
//            HttpCommunicate.setCommUrl(new URL("http://192.168.1.66:8080"));
            HttpCommunicate.setCommUrl(new URL("http://s.feicuibaba.com"));
//            HttpCommunicate.setCommUrl(new URL("http://120.25.147.21"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Images.init(this);
        System.out.println("app pid:" + android.os.Process.myPid());
//        Log.init(this, "http://192.168.1.66:8080/fcbb_b2b2c/exception/addLog.action", "http://192.168.1.66:8080/fcbb_b2b2c/exception/add.action", "[ccn android]");

//        Log.info("info","test");

        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {


                System.out.println("message:"+ex.getMessage());

//                ex.printStackTrace();
                printStackTrace(ex);

//                if(ex.getCause() != null) {
//                    System.out.println("cause:");
//                    ex.getCause().printStackTrace();
//                }
                Toast.makeText(DemoApplication.this,"未知异常！",1).show();
            }
            private void printStackTrace(Throwable ex){
                if(ex != null){
                    ex.printStackTrace();
                }
                if(ex.getCause() != null){
                    printStackTrace(ex.getCause());
                }
            }
        });
    }


}