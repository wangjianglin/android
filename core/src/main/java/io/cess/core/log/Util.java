package io.cess.core.log;

import android.content.Context;

import io.cess.comm.http.HttpCommunicate;
import io.cess.comm.http.HttpCommunicateResult;
import io.cess.core.Crash;
import io.cess.core.Utils;

/**
 * @author lin
 * @date 16/06/2017.
 */

public class Util {

    public static HttpCommunicateResult<Object> uploadCrash(Context context, Crash crash, String exceptionUrl, String uuid, String pre) {
        ExceptionPackage pack = new ExceptionPackage(exceptionUrl); // 初始化向服务器发送异常Action
        pack.setDeviceInfo(crash.getDeviceInfo());// 获取设备信息
        pack.setInfo(crash.getStackTrace() + "\n\nthread info:\n" + crash.getThreadInfo());// 获取异常堆栈
        //pack.setUuid("android:"+Settings.Secure.getString(this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID));

        if(pre != null){
            pack.setUuid("["+pre+"]" + uuid);
        }else{
            pack.setUuid(uuid);
        }

        //写不进去，异步原因导致的

        if(Utils.isDebug(context)){
            android.util.Log.e("CRASH",pack.getInfo());
        }
        return HttpCommunicate.request(pack, null);// 向服务器发送异常信息
    }
}
