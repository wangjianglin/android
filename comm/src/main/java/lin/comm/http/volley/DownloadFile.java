package lin.comm.http.volley;

//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import lin.comm.http.AbstractHttpCommunicateDownloadFile;
import lin.comm.http.Error;
import lin.comm.http.FileInfo;
import lin.comm.http.HttpCommunicate;
import lin.comm.http.HttpCommunicateDownloadFile;
import lin.comm.http.HttpCommunicateImpl;
import lin.comm.http.ProgressResultListener;

/**
 * Created by lin on 9/24/15.
 */
class DownloadFile extends AbstractHttpCommunicateDownloadFile {


    DownloadFile(Context context){
        super(context);
    }


    @Override
    public HttpFileInfo getFileInfo(URL url) {
        return null;
    }




    public void abort(){
//        if(conn != null){
//            //conn.disconnect();
//        }
    }

    public void download(URL url) {

//        com.android.volley.toolbox.
//        HttpGet get = new HttpGet(url);
//        downloadImpl(url);
//        boolean isSuccess = false;
//        try{
////                    if ()
//            isSuccess = downloadImpl(url);
//        }catch (Throwable e){
//            //有问题，异常有可能是 listener 中产生的
//            e.printStackTrace();
////                    lin.client.http.Error error = new Error();
////                    error.setStackTrace(Utils.printStackTrace(e));
//            Error error = new Error(-2,"","",lin.util.Utils.printStackTrace(e));
//            listener.fault(error);
//            return;
//        }
//        //FileInfo对象
//        if(isSuccess) {
//            listener.result(new FileInfo(url.toString(),file, fileName, lastModified), null);
//        }else{
//            Error error = new Error(-3,"","","");
//            listener.fault(error);
//        }
    }


}