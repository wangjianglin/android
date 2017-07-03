package lin.demo.plugin;

import android.app.AlertDialog;
import android.content.Context;


import java.util.Map;

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
