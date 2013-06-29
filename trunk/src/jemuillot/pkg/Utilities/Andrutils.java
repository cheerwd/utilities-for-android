package jemuillot.pkg.Utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;

public class Andrutils {


	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static void permitAccess() {
		
		if (Integer.valueOf(android.os.Build.VERSION.SDK) >= 9)
		{
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
			.permitAll().build());
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

}
