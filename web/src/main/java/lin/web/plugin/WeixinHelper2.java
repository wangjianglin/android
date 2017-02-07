package lin.web.plugin;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
//import com.alipay.mobile.common.logging.LogCatLog;
//import com.alipay.mobile.framework.AlipayApplication;

public class WeixinHelper2 {

	private static final String WEIXIN_PKG = "com.tencent.mm";
	private static final String FRIEND_CLZ = WEIXIN_PKG
			+ ".ui.tools.ShareImgUI";
	private static final String TIMELINE_CLZ = WEIXIN_PKG
			+ ".ui.tools.ShareToTimeLineUI";
	private static final String LAUNCER_CLZ = WEIXIN_PKG + ".ui.LauncherUI";

	private Activity activity;

	public WeixinHelper2(Activity activity) {
		this.activity = activity;
	}

	public static final String TAG = "WeixinHelper";

	public boolean shareFriends(Uri uri, String text) {
		return share(uri, text, true);
	}

	public boolean shareTimeline(Uri uri, String text) {
		return share(uri, text, false);
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
				// LogCatLog.printStackTraceAndMore(e);
			}
		}

		intent = scanLauncherIntent();
		try {
			activity.startActivity(intent);
		} catch (Exception e) {
//			LogCatLog.printStackTraceAndMore(e);
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

	/***
	 * 取得微信版本号
	 * 
	 * @param packageName
	 * @return
	 */
	public int getWeixinVersionCode() {
		PackageInfo packageInfo = null;
		final PackageManager packageManager = activity.getPackageManager();
		List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
		if (packageInfos == null) {
			return Integer.MAX_VALUE;
		}
		for (int index = 0; index < packageInfos.size(); index++) {
			packageInfo = packageInfos.get(index);
			final String name = packageInfo.packageName;
			if (WEIXIN_PKG.equals(name)) {
				return packageInfo.versionCode;
			}
		}
		return Integer.MAX_VALUE;
	}

	private boolean share(Uri uri, String text, boolean friends) {
		boolean isInstalled = installedApp(WEIXIN_PKG);
		if (activity == null || uri == null || !isInstalled) {
			// weixin not installed
			return false;
		}
		Intent intent = new Intent();
		if (friends) {
			intent.setComponent(new ComponentName(WEIXIN_PKG, FRIEND_CLZ));
		} else {
			intent.setComponent(new ComponentName(WEIXIN_PKG, TIMELINE_CLZ));

			if (!TextUtils.isEmpty(text)) {
				intent.putExtra("Kdescription", text);
			}
		}
		intent.setAction(Intent.ACTION_SEND);
		intent.setType("images/*");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		try {
			activity.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/***
	 * 扫描微信的launcher Intent
	 * 
	 * @return
	 */
	private Intent scanLauncherIntent() {
//		PackageManager packageManager = AlipayApplication.getInstance()
//				.getApplicationContext().getPackageManager();
		PackageManager packageManager = activity.getPackageManager();
		try {
			Intent startWechatIntent = packageManager
					.getLaunchIntentForPackage(WEIXIN_PKG);
			if (startWechatIntent != null) {
				return startWechatIntent;
			}
		} catch (Exception e) {
//			LogCatLog.printStackTraceAndMore(e);
		}

		try {
			Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
			mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

			List<ResolveInfo> appList = packageManager.queryIntentActivities(
					mainIntent, 0);
			if (appList == null) {
//				LogCatLog.d(TAG, "appList is null");
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
//			LogCatLog.printStackTraceAndMore(e);
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