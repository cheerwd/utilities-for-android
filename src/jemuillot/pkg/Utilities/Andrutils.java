package jemuillot.pkg.Utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class Andrutils {

	public interface LocalizedStringChecker {
		
		String checkLocalizedString(Locale locale);

		String checkLocalizedString(String string);
	}

	public interface StringChecker {
		boolean check(final String string);
	}

	public LocalizedUrlHelper createLocalizedUrlHelper() {
		return new LocalizedUrlHelper();
	}

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

	public static String getLocalizedString(LocalizedStringChecker lsc) {

		Locale lc = Locale.getDefault();

		String ret = lsc.checkLocalizedString(lc);

		if (ret != null)
			return ret;

		String fullRet = lc.toString().toLowerCase(Locale.US);

		ret = lsc.checkLocalizedString(fullRet);

		if (ret != null)
			return ret;

		ret = lc.getLanguage() + "_" + lc.getCountry().toLowerCase(Locale.US);

		if (!ret.equals(fullRet)) {
			ret = lsc.checkLocalizedString(ret);

			if (ret != null)
				return ret;
		}

		ret = lc.getLanguage();

		if (!ret.equals(fullRet)) {
			ret = lsc.checkLocalizedString(ret);

			if (ret != null)
				return ret;
		}
		
		ret = lsc.checkLocalizedString("default");

		if (ret != null)
			return ret;

		return lsc.checkLocalizedString("");
	}

	public class LocalizedUrlHelper extends LocalizedStringHelper {

		@Override
		public boolean check(final String string) {
			return Andrutils.checkUrl(string);
		}

		@Override
		public String checkLocalizedString(Locale locale) {
			return null;
		}

	}

	public abstract class LocalizedStringHelper implements
			LocalizedStringChecker, StringChecker {

		protected String urlFormatted;
		protected ArrayList<String> lanInfo;

		public void setFormattedUrl(String url) {
			urlFormatted = url;
		}

		public void setLanInfoFromUrl(String url) {
			lanInfo = Andrutils.getHtmlLines(url);
		}

		public void setLanInfo(ArrayList<String> lanInfo) {
			this.lanInfo = lanInfo;
		}

		public String getLocalizedString() {

			return Andrutils.getLocalizedString(this);
		}

		public String checkLocalizedString(String string) {

			if (lanInfo == null) {
				string = String.format(urlFormatted, string);

				if (check(string))
					return string;
			} else {

				Iterator<String> it = lanInfo.iterator();

				while (it.hasNext()) {
					if (string.equals(it.next())) {
						return String.format(urlFormatted, string);
					}
				}

			}

			return null;

		}

	}

}
