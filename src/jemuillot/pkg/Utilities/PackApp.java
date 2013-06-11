package jemuillot.pkg.Utilities;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class PackApp {

	static public String getAppTitle(Context c) {
		PackageManager manager = c.getPackageManager();

		try {
			return (String) manager.getApplicationLabel(manager
					.getApplicationInfo(c.getPackageName(), 0));
		} catch (NameNotFoundException e) {
			return "";
		}
	}

	public static String getAppVersionName(Context c) {
		PackageManager manager = c.getPackageManager();

		try {
			PackageInfo info = manager.getPackageInfo(c.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			return "1.0";
		}
	}

	public static int getAppVersionCode(Context c) {
		PackageManager manager = c.getPackageManager();

		try {
			PackageInfo info = manager.getPackageInfo(c.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			return 0;
		}
	}

}
