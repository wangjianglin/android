package io.cess.demo.plugin;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;


import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import io.cess.demo.R;
import io.cess.util.Utils;

/**
 * @author lin
 * @date 10/30/15.
 */
public class WeixinHelper {
    private static final String WEIXIN_PKG = "com.tencent.mm";
    private static final String FRIEND_CLZ = WEIXIN_PKG
            + ".ui.tools.ShareImgUI";
    private static final String TIMELINE_CLZ = WEIXIN_PKG
            + ".ui.tools.ShareToTimeLineUI";
    private static final String LAUNCER_CLZ = WEIXIN_PKG + ".ui.LauncherUI";
    //    private static final String APP_ID = "wx86b79000a2120cc7";
//    private static final String APP_ID = "wx0bdd918b7c28179e";
    private static final String APP_ID = "wx2eefe7a7d36bd8f6";

    private Activity activity;

    public WeixinHelper(Activity activity) {
        this.activity = activity;
    }

    public static final String TAG = "WeixinHelper";

    public boolean register() {
        Intent intent = new Intent();
        intent.setClassName("com.tencent.mm", "com.tencent.mm.plugin.base.stub.WXEntryActivity");

        intent.putExtra("_mmessage_sdkVersion", 553844737);
        intent.putExtra("_mmessage_appPackage", "io.cess.demo");
        intent.putExtra("_mmessage_content", "weixin://sendreq?appid=" + APP_ID);
        intent.putExtra("_mmessage_checksum", a("weixin://sendreq?appid=" + APP_ID, "io.cess.demo"));
        intent.setFlags(411041792);
        activity.startActivity(intent);
        return true;
    }

    public boolean shareLink(String title, String desc, String link, boolean friends) {
//		boolean isInstalled = installedApp(WEIXIN_PKG);
//		if (activity == null || uris == null || uris.length < 1 || !isInstalled) {
//			// weixin not installed
//			return false;
//		}
        Intent intent = new Intent();
//        intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI"));
        intent.setClassName("com.tencent.mm", "com.tencent.mm.plugin.base.stub.WXEntryActivity");
        intent.setAction(Intent.ACTION_SEND);

        intent.putExtra("_wxapi_command_type", 2);
        intent.putExtra("_wxapi_basereq_transaction", "webpage1430644192541");
        intent.putExtra("_wxobject_sdkVer", 0);
        intent.putExtra("_wxobject_title", title);
        intent.putExtra("_wxobject_description", desc);
//        intent.putExtra("Kdescription", desc);
        if (friends) {
            intent.putExtra("_wxapi_sendmessagetowx_req_scene", 0);
        } else {
            intent.putExtra("_wxapi_sendmessagetowx_req_scene", 1);
        }
        //localBundle.putByteArray("_wxobject_thumbdata", paramWXMediaMessage.thumbData);
//            if (paramWXMediaMessage.mediaObject != null)
//            {
//              localBundle.putString("_wxobject_identifier_", paramWXMediaMessage.mediaObject.getClass().getName());
//              paramWXMediaMessage.mediaObject.serialize(localBundle);
//            }
        Bitmap bmp = BitmapFactory.decodeResource(this.activity.getResources(), R.drawable.io_cess_web_weixin_link);
//		WXImageObject imgObj = new WXImageObject(bmp);
//
//		WXMediaMessage msg = new WXMediaMessage();
//		msg.mediaObject = imgObj;

//		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 200, 200, true);
//		bmp.recycle();
//		msg.thumbData = Util.bmpToByteArray(thumbBmp, true);  // 设置缩略图
        intent.putExtra("_wxobject_thumbdata", Utils.bmpToByteArray(bmp, true));
        intent.putExtra("_wxobject_identifier_", "com.tencent.mm.sdk.openapi.WXWebpageObject");
        intent.putExtra("_wxwebpageobject_webpageUrl", link);

        intent.putExtra("_mmessage_sdkVersion", 553844737);
        intent.putExtra("_mmessage_appPackage", "io.cess.demo");
        intent.putExtra("_mmessage_content", "weixin://sendreq?appid=" + APP_ID);
        intent.putExtra("_mmessage_checksum", a("weixin://sendreq?appid=" + APP_ID, "io.cess.demo"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private byte[] a(String paramString1, String paramString2) {
        StringBuffer localStringBuffer = new StringBuffer();
        if (paramString1 != null) {
            localStringBuffer.append(paramString1);
        }
        localStringBuffer.append(553844737);
        localStringBuffer.append(paramString2);
        localStringBuffer.append("mMcShCsTr");
        return getMessageDigest(localStringBuffer.toString().substring(1, 9).getBytes()).getBytes();
    }

    private String getMessageDigest(byte[] paramArrayOfByte) {
        char[] arrayOfChar1 = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest localMessageDigest;
            (localMessageDigest = MessageDigest.getInstance("MD5")).update(paramArrayOfByte);
            int i;
            char[] arrayOfChar2 = new char[(i = (paramArrayOfByte = localMessageDigest.digest()).length) * 2];
            int j = 0;
            for (int k = 0; k < i; k++) {
                int m = paramArrayOfByte[k];
                arrayOfChar2[(j++)] = arrayOfChar1[(m >>> 4 & 0xF)];
                arrayOfChar2[(j++)] = arrayOfChar1[(m & 0xF)];
            }
            return new String(arrayOfChar2);
        } catch (Exception localException) {
        }
        return null;
    }

    public boolean shareFriends(Uri[] uris, String text) {
        return share(uris, text, true);
    }

    public boolean shareTimeline(Uri[] uris, String text) {
        return share(uris, text, false);
    }

    public boolean isInstallWechat() {
        return installedApp(WEIXIN_PKG);
    }

    public boolean startWexin(String startLauncherName) {
        boolean isInstalled = installedApp(WEIXIN_PKG);
        if (activity == null || !isInstalled) {
            // weixin not installed
            return false;
        }
        Intent intent = null;
        if (!TextUtils.isEmpty(startLauncherName)) { // 服务端强制设置微信launcher的className
            intent = generateWeixinDefaultIntent(startLauncherName);
            try {
                activity.startActivity(intent);
                return true;
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }

        intent = scanLauncherIntent();
        try {
            activity.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return false;
        }
        return true;
    }

    private boolean installedApp(String packageName) {
        PackageInfo packageInfo = null;
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        final PackageManager packageManager = activity.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        if (packageInfos == null) {
            return false;
        }
        for (int index = 0; index < packageInfos.size(); index++) {
            packageInfo = packageInfos.get(index);
            final String name = packageInfo.packageName;
            if (packageName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 取得微信版本号
     *
     * @return
     */
    public int getWeixinVersionCode() {
        PackageInfo packageInfo = null;
        final PackageManager packageManager = activity.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        if (packageInfos == null) {
            return Integer.MIN_VALUE;
        }
        for (int index = 0; index < packageInfos.size(); index++) {
            packageInfo = packageInfos.get(index);
            final String name = packageInfo.packageName;
            if (WEIXIN_PKG.equals(name)) {
                return packageInfo.versionCode;
            }
        }
        return Integer.MIN_VALUE;
    }

    /**
     * 取得微信版本号
     *
     * @return
     */
    public String getWeixinVersionName() {
        PackageInfo packageInfo = null;
        final PackageManager packageManager = activity.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        if (packageInfos == null) {
            return null;
        }
        for (int index = 0; index < packageInfos.size(); index++) {
            packageInfo = packageInfos.get(index);
            final String name = packageInfo.packageName;
            if (WEIXIN_PKG.equals(name)) {
                return packageInfo.versionName;
            }
        }
        return null;
    }

    private boolean share(Uri[] uris, String text, boolean friends) {
        boolean isInstalled = installedApp(WEIXIN_PKG);
        if (activity == null || uris == null || uris.length < 1 || !isInstalled) {
            // weixin not installed
            return false;
        }
        Intent intent = new Intent();
        if (friends) {
            intent.setComponent(new ComponentName(WEIXIN_PKG, FRIEND_CLZ));
        } else {
            intent.setComponent(new ComponentName(WEIXIN_PKG, TIMELINE_CLZ));

            if (!TextUtils.isEmpty(text)) {
//                intent.putExtra("Kdescription", text + "<不议价>");
                intent.putExtra("Kdescription", text);
            }
        }
        if (uris.length == 1) {
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, uris[0]);
        } else {
            ArrayList<Uri> uriList = new ArrayList<Uri>();
            for (int i = 0; i < uris.length; ++i) {
                uriList.add(uris[i]);
            }
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
        }
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            activity.startActivity(intent);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * 扫描微信的launcher Intent
     *
     * @return
     */
    private Intent scanLauncherIntent() {
        PackageManager packageManager = activity.getPackageManager();

        try {
            Intent startWechatIntent = packageManager
                    .getLaunchIntentForPackage(WEIXIN_PKG);
            if (startWechatIntent != null) {
                return startWechatIntent;
            }
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }

        try {
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

            List<ResolveInfo> appList = packageManager.queryIntentActivities(
                    mainIntent, 0);
            if (appList == null) {
                Log.d(TAG, "appList is null");
                return generateWeixinDefaultIntent(LAUNCER_CLZ);
            }
            for (ResolveInfo temp : appList) {
                if (temp != null && temp.activityInfo != null
                        && WEIXIN_PKG.equals(temp.activityInfo.packageName)) {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(WEIXIN_PKG,
                            temp.activityInfo.name));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    return intent;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return generateWeixinDefaultIntent(LAUNCER_CLZ);
    }

    private Intent generateWeixinDefaultIntent(String launchrClass) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(WEIXIN_PKG, launchrClass));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }
}
