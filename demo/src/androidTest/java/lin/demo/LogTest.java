package io.cess.demo;

import android.test.AndroidTestCase;

import java.io.File;

import io.cess.comm.http.HttpCommunicate;
import io.cess.core.log.LogUploadPackage;

/**
 * @author lin
 * @date 9/25/15.
 */
public class LogTest extends AndroidTestCase {

    public void testLogUpload(){


//        LogUploadPackage uploadPackage = new LogUploadPackage("http://116.55.226.37/exception/addLog.action");
        LogUploadPackage uploadPackage = new LogUploadPackage("http://192.168.1.66:8080/fcbb_b2b2c/exception/addLog.action");

//		uploadPackage.setLog(new File(uri.getEncodedPath()));
        File file = new File("/storage/emulated/0/Android/data/com.chuncuicom.ccn/files/logs/2015-09-24.log");
        uploadPackage.setLog(file);

//		User user = LocalStorage.getItem("user",User.class);
        String pre = "ccn test";
        String uuid = "test uuid";
        if(pre != null){
            uploadPackage.setUuid("["+pre+"]"+uuid);
        }else{
            uploadPackage.setUuid(uuid);
        }

        boolean r = HttpCommunicate.request(uploadPackage, null).isSuccess();
    }
}
