package lin.demo.plugin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lin.comm.http.HttpCommunicate;
import lin.core.Device;
import lin.core.Images;
import lin.core.NavActivity;
import lin.core.log.ExceptionPackage;
import lin.demo.Utils;
import lin.demo.tabbar.TabbarActivity;

/**
 * @author lin
 * @date Apr 20, 2015 12:36:16 PM
 */
public class LinWeixinPlugin {
    private static String cachePath = "/imagecache/";
    private WeixinHelper helper;
    private Context mContext;

    public LinWeixinPlugin(Context context) {
        mContext = context;
        helper = new WeixinHelper((TabbarActivity) mContext);
    }

    public void shareLink(Map<String, Object> args) {
        if (!helper.isInstallWechat()) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(mContext);
            dlg.setMessage("您还没有安装微信，请先安装微信重试！");
            dlg.setTitle("提示");
            dlg.setCancelable(true);
            dlg.setPositiveButton(android.R.string.ok, null);
            dlg.show();
            return;
        }
        String title = (String) args.get("title");
        String link = (String) args.get("link");
        String desc = (String) args.get("desc");
        String scene = (String) args.get("scene");
        if ("friend".equals(scene)) {
            helper.shareLink(title, desc, link, true);
        } else {
            helper.shareLink(title, desc, link, false);
        }
    }
}
