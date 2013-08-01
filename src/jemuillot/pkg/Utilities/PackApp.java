package jemuillot.pkg.Utilities;

import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;

public class PackApp {

	private static final String STR_SYSAPP = "/system/app/";
	private static final String TAG = "PackApp";
	protected static final String PKG_BUSYBOX = "stericson.busybox";

	static public boolean moveToSystem(final Context c, boolean userRequested) {

		final ApplicationInfo ai = c.getApplicationInfo();

		final String dest = STR_SYSAPP + ai.packageName + ".apk";

		File destFile = new File(dest);

		boolean mustReboot = (destFile == null || !destFile.exists());

		Log.v(TAG, "destFile:" + destFile + ", present:" + destFile.exists()
				+ " mustReboot=" + mustReboot);

		if (mustReboot) {
			if (!userRequested)
				return false;
		}

		Log.v(TAG, "srcFile: " + ai.sourceDir);

		if (ai.sourceDir.startsWith(STR_SYSAPP)) {

			Log.v(TAG, "moveToSystem: " + ai.packageName
					+ " is already in the system path.");

			// new AlertDialog.Builder(c).setTitle("Run From System ^_^")
			// .setMessage("Run From System ^_^")
			// .setPositiveButton(R.string.utilPopStrOK, null).create()
			// .show();

			RootCommands.uninstall(ai.packageName);

			return true;
		}

		if (!RootCommands.canRunRootCommands()) {

			new AlertDialog.Builder(c).setTitle(R.string.utilPopStrError)
					.setMessage(R.string.utilCannotGetRooted)
					.setPositiveButton(R.string.utilPopStrOK, null).create()
					.show();

			return false;
		}

		if (Andrutils.isSameContent(ai.sourceDir, dest)) {

			Log.v(TAG, "App in data/app must be removed");

			new AlertDialog.Builder(c)
					.setTitle(R.string.utilPopStrMultiInstallationFound)
					.setMessage(
							R.string.utilPopStrMultiInstallationFoundExplain)
					.setOnCancelListener(new OnCancelListener() {

						@Override
						public void onCancel(DialogInterface dialog) {

							Log.v(TAG, "uninstall");
							RootCommands.uninstall(ai.packageName);

						}
					})

					.setPositiveButton(R.string.utilPopStrOK,
							new AlertDialog.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									Log.v(TAG, "uninstall");
									RootCommands.uninstall(ai.packageName);
								}
							}).create().show();

		} else {

			Log.v(TAG, "Sys Not Found or sys <> data!");

			if (mustReboot) {
				RootCommands.copy(ai.sourceDir, dest);

				if (!destFile.exists()) {
					new AlertDialog.Builder(c)
							.setTitle(R.string.utilBusyBoxNotFound)
							.setMessage(R.string.utilBusyBoxIntro)
							.setNegativeButton(R.string.utilPopStrCancel, null)
							.setNeutralButton(
									R.string.utilPopStrDownloadFromBrowser,
									new AlertDialog.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

											searchViaBrowser(c, PKG_BUSYBOX);
										}
									})
							.setPositiveButton(
									R.string.utilPopStrDownloadFromMarket,
									new AlertDialog.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

											openInMarket(c, PKG_BUSYBOX);
										}
									}).create().show();
					return false;
				}

				new AlertDialog.Builder(c)
						.setTitle(R.string.utilPopStrReboot)
						.setMessage(R.string.utilPopStrRebootReason)
						.setNegativeButton(R.string.utilPopStrNo, null)
						.setPositiveButton(R.string.utilPopStrYes,
								new AlertDialog.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										RootCommands.reboot();
									}
								}).create().show();

			} else {
				Log.v(TAG, "New version Needs copy to System!");

				new AlertDialog.Builder(c)
						.setTitle(R.string.packAppUpdateToSys)// "New version Needs copy to System.")
						.setMessage(R.string.packAppUpdateToSysExplain)
						.setOnCancelListener(new OnCancelListener() {

							@Override
							public void onCancel(DialogInterface dialog) {

								RootCommands.copy(ai.sourceDir, dest);

							}
						})

						.setPositiveButton(R.string.utilPopStrOK,
								new AlertDialog.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										RootCommands.copy(ai.sourceDir, dest);
									}
								}).create().show();
			}

		}

		return true;
	}

	public static void openInMarket(Context c, String packageName) {


		Intent viewIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("market://search?q=pname:" + packageName));

		try {
			c.startActivity(viewIntent);
		} catch (Exception e) {
			searchViaBrowser(c, packageName);			
		}

	}

	private static void searchViaBrowser(Context c, String packageName) {
		Intent viewIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://www.bing.com/search?q=" + packageName));

		try {
			c.startActivity(viewIntent);
		} catch (Exception e2) {
			// do nothing
		}
	}

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
