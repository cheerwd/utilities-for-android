package jemuillot.pkg.Utilities;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;

public class SelfUpdater {

	private static final int UPDATE_ID = 1;
	private Context context;
	private String url;

	public SelfUpdater(Context c) {
		context = c;
	}

	private String getUpdateValue(String context, String key, String def) {

		int keypos = context.indexOf(key + "=");

		if (keypos < 0) {
			return def;
		}

		String ret = "";
		keypos += key.length() + 1;

		while (true) {
			char c = context.charAt(keypos);

			if (c == ';')
				return ret;

			ret += c;
			keypos++;
		}
	}

	// Reserved for DkyDrive
	@SuppressWarnings("unused")
	private String getUpdateValue_SkyDrive(String context, String key,
			String def) {

		int keypos = context.indexOf(key + "\\x3d"); // key=

		if (keypos < 0) {
			return def;
		}

		String ret = "";
		keypos += key.length() + 4;

		while (true) {
			char c = context.charAt(keypos);

			if ((c == '\\') && (context.charAt(keypos + 1) == 'x')) {
				String hexStr = context.substring(keypos + 2, keypos + 4);
				int hex = Integer.parseInt(hexStr, 16);
				c = (char) hex;

				if (c == ';')
					return ret;

				keypos += 3;
			}

			ret += c;
			keypos++;
		}
	}

	private class checkThread implements Runnable {

		public void run() {
			doCheck();
		}

	}

	public void check() {

		HandlerThread handlerThread = new HandlerThread("SelfUpdaterThread");
		handlerThread.start();

		new Handler(handlerThread.getLooper()).post(new checkThread());
	}

	public void doCheck() {

		String json = Andrutils.getHtml(url);

		try {
			JSONObject object = (JSONObject) new JSONTokener(json).nextValue();

			int version_code = object.getInt("version_code");

			int current_version_code = PackApp.getAppVersionCode(context);

			if (version_code > current_version_code) {

				String str_version_desc = object.getString("version_desc");

				String str_download = object.getString("download");

				NotificationManager nm = (NotificationManager) context
						.getSystemService(Context.NOTIFICATION_SERVICE);

				int icon = R.drawable.update_notification;

				CharSequence tickerText = context
						.getString(R.string.selfUpdaterTicket);

				long when = System.currentTimeMillis();

				Notification notification = new Notification(icon, tickerText,
						when);

				CharSequence contentTitle = PackApp.getAppTitle(context) + " "
						+ str_version_desc;
				CharSequence contentText = context
						.getString(R.string.selfUpdaterDesc);

				Intent notificationIntent = new Intent(
						"android.intent.action.VIEW", Uri.parse(str_download));

				PendingIntent contentIntent = PendingIntent.getActivity(
						context, 0, notificationIntent, 0);

				notification.defaults |= Notification.DEFAULT_SOUND
						| Notification.DEFAULT_LIGHTS;

				notification.flags |= Notification.FLAG_AUTO_CANCEL;

				notification.setLatestEventInfo(context, contentTitle,
						contentText, contentIntent);

				nm.notify(UPDATE_ID, notification);
			}
		} catch (Exception e) {
		}
	}

	public void doCheckHtml() {
		String content = Andrutils.getHtml(url);

		if (null == content)
			return;

		String str_version_code = getUpdateValue(content, "version_code", "0");

		int version_code = Integer.parseInt(str_version_code);

		int current_version_code = PackApp.getAppVersionCode(context);

		if (version_code > current_version_code) {

			String str_version_desc = getUpdateValue(content, "version_desc",
					"");
			String str_download = getUpdateValue(content, "download", "");

			NotificationManager nm = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);

			int icon = R.drawable.update_notification;

			CharSequence tickerText = context
					.getString(R.string.selfUpdaterTicket);

			long when = System.currentTimeMillis();

			Notification notification = new Notification(icon, tickerText, when);

			CharSequence contentTitle = PackApp.getAppTitle(context) + " "
					+ str_version_desc;
			CharSequence contentText = context
					.getString(R.string.selfUpdaterDesc);

			Intent notificationIntent = new Intent(
					"android.intent.action.VIEW", Uri.parse(str_download));

			PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
					notificationIntent, 0);

			notification.defaults |= Notification.DEFAULT_SOUND
					| Notification.DEFAULT_LIGHTS;

			notification.flags |= Notification.FLAG_AUTO_CANCEL;

			notification.setLatestEventInfo(context, contentTitle, contentText,
					contentIntent);

			nm.notify(UPDATE_ID, notification);
		}

	}

	public void setUrl(String url) {
		this.url = url;
	}
}
