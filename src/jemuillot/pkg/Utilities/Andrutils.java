package jemuillot.pkg.Utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class Andrutils {
	
	public static ArrayList<String> getHtmlLines(String urlString) {
		
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
		try {
	
			 new BufferedReader(new InputStreamReader(
					((HttpURLConnection) (new URL(urlString)).openConnection())
							.getInputStream()));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	

	public String getLocalizedString() {

		Locale lc = Locale.getDefault();

		String fullRet = lc.toString().toLowerCase(Locale.US);

		String ret = checkLocalizedString(fullRet);

		if (ret != null)
			return ret;

		ret = lc.getLanguage() + "_" + lc.getCountry().toLowerCase(Locale.US);

		if (!ret.equals(fullRet)) {
			ret = checkLocalizedString(ret);

			if (ret != null)
				return ret;
		}

		ret = lc.getLanguage();

		if (!ret.equals(fullRet)) {
			ret = checkLocalizedString(ret);

			if (ret != null)
				return ret;
		}

		ret = checkLocalizedString("default");

		if (ret != null)
			return ret;

		return checkLocalizedString("");
	}

	protected String checkLocalizedString(String string) {
		return string;
	}

}
