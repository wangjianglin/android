package lin.test;


import android.app.Application;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

import lin.client.http.HttpCommunicate;
import lin.client.http.HttpCommunicateType;
import lin.core.Images;
import lin.core.log.Log;

public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        HttpCommunicate.init(this);

        HttpCommunicate.setType(HttpCommunicateType.HttpClient);

        try {
            HttpCommunicate.setCommUrl(new URL("http://192.168.1.66:8080"));
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
                Toast.makeText(TestApplication.this,"未知异常！",1).show();
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