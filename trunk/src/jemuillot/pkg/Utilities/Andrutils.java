package jemuillot.pkg.Utilities;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

class PermissionHelper {
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public void build() {

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.permitAll().build());
	}
}

public class Andrutils {

	public static void applicationExit() {
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

	public static byte[] getBytesFromFile(File f) {
		if (f == null) {
			return null;
		}
		try {
			FileInputStream stream = new FileInputStream(f);
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = stream.read(b)) != -1)
				out.write(b, 0, n);
			stream.close();
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
		}
		return null;
	}

	public static File getFileFromBytes(byte[] b, String outputFile) {
		BufferedOutputStream stream = null;
		File file = null;
		try {
			file = new File(outputFile);
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return file;
	}

	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	public static boolean collapseStatusBar(Context c) {
		try {
			Object service = c.getSystemService("statusbar");
			Class<?> claz = Class.forName("android.app.StatusBarManager");
			Method collapse;

			try {
				collapse = claz.getMethod("collapse");
				collapse.setAccessible(true);
				collapse.invoke(service);

			} catch (Exception e) {
				try {
					collapse = claz.getMethod("collapsePanels");
					collapse.setAccessible(true);
					collapse.invoke(service);
				} catch (Exception err) {
					return false;
				}
			}

		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public static boolean backToHome(Context c) {
		Intent i = new Intent(Intent.ACTION_MAIN);

		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.addCategory(Intent.CATEGORY_HOME);

		c.startActivity(i);

		return true;
	}

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static void permitAccess() {

		if (Integer.valueOf(android.os.Build.VERSION.SDK) >= 9) {

			PermissionHelper helper = new PermissionHelper();
			helper.build();

		}

	}

	public static ArrayList<String> getHtmlLines(final String urlString) {

		permitAccess();

		if (urlString == null)
			return null;

		ArrayList<String> ret = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					((HttpURLConnection) (new URL(urlString)).openConnection())
							.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				ret.add(line);
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getHtml(String urlString) {

		permitAccess();

		try {
			StringBuffer html = new StringBuffer();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					((HttpURLConnection) (new URL(urlString)).openConnection())
							.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				html.append(line);
			}
			return html.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	protected static boolean checkUrl(String urlString) {

		permitAccess();

		try {

			new BufferedReader(new InputStreamReader(
					((HttpURLConnection) (new URL(urlString)).openConnection())
							.getInputStream()));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public interface SkippableTipListener {

		/**
		 * 
		 * @return null to make it call getTipResId
		 */
		public CharSequence getTipString();

		public int getTipResId();

		public boolean getSkippable();

		public void setSkippable(boolean isChecked);

		/**
		 * 
		 * @return true to allow close the message box
		 */
		public boolean onClose();
	}

	public static void showSkippableTip(Context context,
			final SkippableTipListener stl) {

		if (stl.getSkippable()) {
			stl.onClose();
			return;
		}

		Context c = context;

		WindowManager.LayoutParams layout = new WindowManager.LayoutParams(0,
				LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.OPAQUE);

		layout.gravity = Gravity.CENTER;

		DisplayMetrics dm = new DisplayMetrics();

		final WindowManager windowManager = (WindowManager) c
				.getSystemService(Context.WINDOW_SERVICE);

		windowManager.getDefaultDisplay().getMetrics(dm);

		layout.width = dm.widthPixels * 4 / 5;

		LayoutInflater factory = LayoutInflater.from(c);

		final View dlg = factory.inflate(R.layout.skippable_tip_dlg, null);
		windowManager.addView(dlg, layout);

		TextView tv = (TextView) dlg.findViewById(R.id.tvTipContent);

		CharSequence tips = stl.getTipString();

		if (tips == null)
			tv.setText(stl.getTipResId());
		else
			tv.setText(tips);

		CheckBox cbPassNextTime = (CheckBox) dlg
				.findViewById(R.id.cbPassNextTime);

		cbPassNextTime.setChecked(false);

		cbPassNextTime
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						stl.setSkippable(isChecked);
					}
				});

		Button cbClose = (Button) dlg.findViewById(R.id.cbClose);

		cbClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (stl.onClose())
					windowManager.removeView(dlg);
			}
		});

	}

	@SuppressWarnings("resource")
	public static boolean isSameContent(String srcFilename, String destFilename) {

		try {

			FileInputStream src = new FileInputStream(srcFilename);
			FileInputStream dst = new FileInputStream(destFilename);

			byte[] bsrc = new byte[1024];
			byte[] bdst = new byte[1024];

			int n;
			while ((n = src.read(bsrc)) != -1) {
				int m = dst.read(bdst);

				if (m == -1)
					return false;

				if (m != n)
					return false;

				if (!Arrays.equals(bsrc, bdst))
					return false;
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
