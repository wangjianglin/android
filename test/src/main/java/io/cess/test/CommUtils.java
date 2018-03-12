package io.cess.test;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.test.mock.MockContext;

import java.net.MalformedURLException;
import java.net.URL;

import io.cess.comm.http.HttpCommunicate;
import io.cess.comm.http.HttpCommunicateImpl;
import io.cess.comm.http.HttpCommunicateResult;
import io.cess.comm.http.HttpPackage;

import static junit.framework.Assert.assertTrue;


/**
 * Created by lin on 09/07/2017.
 */

public class CommUtils {

    public static <T> T request(HttpPackage<T> pack){
        return request(HttpCommunicate.global(),pack,null);
    }

    public static <T> T request(HttpPackage<T> pack,Integer errorCode){
        return request(HttpCommunicate.global(),pack,errorCode);
    }

    public static <T> T request(HttpCommunicateImpl impl,HttpPackage<T> pack){
        return request(impl,pack,null);
    }

    public static <T> T request(HttpCommunicateImpl impl,HttpPackage<T> pack,Integer errorCode){

        HttpCommunicateResult<T> result = impl.request(pack);

        result.waitForEnd();

        if(errorCode == null || errorCode == 0) {
            assertTrue("http 请求失败！\n"+error(result.getError()), result.isSuccess());
        }else{
            assertTrue("stack trace:"+error(result.getError()),result.isSuccess() == false && result.getError().getCode() == errorCode);
        }

        return result.getResult();
    }

    private static String error(io.cess.comm.http.Error error){
        if(error == null){
            return "";
        }
        return "code:"+error.getCode()+"\nmessage:"+error.getMessage()
                + "\ncause:"+error.getCause()
                + "\nstack trace:"+error.getStackTrace();
    }

}
