package jemuillot.pkg.Utilities.Compat;

import java.lang.reflect.Method;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

class AppWidgetManagerCompat3 implements
		AppWidgetManagerCompat.AppWidgetManagerInterfaces {

	private static final String TAG = "AppWidgetManagerCompat";

	protected Context context;

	protected AppWidgetManager awm;

	public AppWidgetManagerCompat3(Context context) {
		this.context = context;
		awm = AppWidgetManager.getInstance(context);
	}

	@Override
	public boolean bindAppWidgetIdIfAllowed(int appWidgetId,
			ComponentName provider) {

		Method m = null;

		Method[] methods = AppWidgetManager.class.getMethods();

		Log.v(TAG, methods.toString());

		try {
			m = AppWidgetManager.class.getMethod("bindAppWidgetIdIfAllowed",
					new Class[] { Integer.TYPE, ComponentName.class });
		} catch (NoSuchMethodException e) {

			try {
				m = AppWidgetManager.class.getMethod("bindAppWidgetId",
						new Class[] { Integer.TYPE, ComponentName.class });
			} catch (NoSuchMethodException e2) {

				Log.v(TAG, "bindAppWidgetId Doesn't Exist");
			}
			if (m != null) {
				try {
					m.invoke(awm, appWidgetId, provider);
				} catch (Exception e3) {
					Log.v(TAG, "bindAppWidgetId Failed: " + e3.toString());

				}

				return true;
			}

		}
		if (m != null) {
			try {
				return (Boolean) m.invoke(awm, appWidgetId, provider);
			} catch (Exception e) {
				Log.v(TAG, "bindAppWidgetIdIfAllowed Failed");
			}
		}

		return true;
	}

	@Override
	public Bundle getAppWidgetOptions(int appWidgetId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateAppWidgetOptions(int appWidgetId, Bundle options) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean supportUserBind() {
		return false;
	}

	@Override
	public AppWidgetManager getUnresolved() {
		return awm;
	}

}

class AppWidgetManagerCompat16 extends AppWidgetManagerCompat3 {

	@Override
	public boolean supportUserBind() {
		return true;
	}

	public AppWidgetManagerCompat16(Context context) {
		super(context);
	}

	@Override
	public boolean bindAppWidgetIdIfAllowed(int appWidgetId,
			ComponentName provider) {

		return awm.bindAppWidgetIdIfAllowed(appWidgetId, provider);
	}

	@Override
	public Bundle getAppWidgetOptions(int appWidgetId) {
		return awm.getAppWidgetOptions(appWidgetId);
	}

	@Override
	public void updateAppWidgetOptions(int appWidgetId, Bundle options) {
		awm.updateAppWidgetOptions(appWidgetId, options);
	}

}

public class AppWidgetManagerCompat {

	public interface AppWidgetManagerInterfaces {

		public AppWidgetManager getUnresolved();

		public boolean supportUserBind();

		public boolean bindAppWidgetIdIfAllowed(int appWidgetId,
				ComponentName provider);

		public Bundle getAppWidgetOptions(int appWidgetId);

		void updateAppWidgetOptions(int appWidgetId, Bundle options);
	}

	public AppWidgetManagerInterfaces compat;

	@SuppressWarnings("deprecation")
	public AppWidgetManagerCompat(Context c) {

		int sdkv = Integer.parseInt(Build.VERSION.SDK);

		if (sdkv >= 16)
			compat = new AppWidgetManagerCompat16(c);
		else
			compat = new AppWidgetManagerCompat3(c);

	}

}
