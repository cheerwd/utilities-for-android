package jemuillot.pkg.Utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class AfterTaste {

	private Context context;

	private LocalizedPath defaultDonateUrl;

	private String defaultEMail;

	private LocalizedPath defaultHomepage;

	private String defaultDownloadUrl;

	public AfterTaste(Context c) {
		context = c;
	}

	public void setDefaultDonateUrl(LocalizedPath url) {
		defaultDonateUrl = url;
	}

	public void setDefaultEMail(String eMail) {
		defaultEMail = eMail;
	}

	public void setDefaultHomepage(LocalizedPath homepage) {
		defaultHomepage = homepage;
	}

	public void setDefaultDownloadUrl(String url) {
		defaultDownloadUrl = url;
	}

	public boolean showRecommendedChoices() {
		new AlertDialog.Builder(context).setTitle(R.string.afterTaste)
				.setItems(R.array.afterTasteChoices, new OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						dialog.dismiss();

						switch (whichButton) {
						case 0:
							donate(null);
							break;
						case 1:
							feedback(null, null);
							break;
						case 2:
							share(null);
							break;
						}

					}
				}).create().show();

		return true;
	}

	public void showADClickHint() {
		Toast.makeText(context, R.string.afterTastePleaseClickAD,
				Toast.LENGTH_LONG).show();
	}

	public void showDonateClickHint() {
		Toast.makeText(context, R.string.afterTastePleaseDonate,
				Toast.LENGTH_LONG).show();
	}

	/**
	 * Set email to null to use the default address. Default mail address =
	 * R.string.afterTasteEMailAddress You can override the value in your res
	 */
	public void feedback(final String email, final LocalizedPath homepage) {

		new AlertDialog.Builder(context)
				.setTitle(R.string.afterTasteFeedback)
				.setItems(R.array.afterTasteFeedbackTypes,
						new OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								dialog.dismiss();

								if (whichButton >= 3) {

									if (homepage == null)
										donate(defaultHomepage);
									else
										donate(homepage);

								} else {

									String[] items = context
											.getResources()
											.getStringArray(
													R.array.afterTasteFeedbackSubjects);

									String feedbackSelected = items[whichButton];

									String mailTo = email;
									if (mailTo == null) {

										mailTo = defaultEMail;

										if (mailTo == null) {
											mailTo = context
													.getString(R.string.afterTasteEMailAddress);
										}
									}

									Intent returnIt = new Intent(
											Intent.ACTION_SEND);

									String[] tos = { mailTo };
									returnIt.putExtra(Intent.EXTRA_EMAIL, tos);
									returnIt.putExtra(
											Intent.EXTRA_SUBJECT,
											String.format(
													feedbackSelected,
													PackApp.getAppTitle(context)
															+ " v"
															+ PackApp
																	.getAppVersionName(context)));
									returnIt.setType("message/rfc882");
									context.startActivity(Intent.createChooser(
											returnIt,
											context.getString(R.string.afterTasteChooseEmailClient)));
								}

							}
						}).create().show();

	}

	public void share(String downloadUrl) {

		if (downloadUrl == null) {
			downloadUrl = defaultDownloadUrl;

			if (downloadUrl == null)
				return;
		}

		Intent sintent = new Intent(Intent.ACTION_SEND);
		sintent.setType("text/plain");

		String appTitle = PackApp.getAppTitle(context);

		sintent.putExtra(Intent.EXTRA_SUBJECT, String.format(
				context.getString(R.string.afterTasteShareSubject), appTitle));

		sintent.putExtra(Intent.EXTRA_TEXT, String.format(
				context.getString(R.string.afterTasteShareContent), appTitle,
				downloadUrl));
		context.startActivity(Intent.createChooser(sintent,
				context.getString(R.string.afterTasteShare)));
	}

	public void donate(LocalizedPath localizedUrl) {

		String url = localizedUrl == null ? null : localizedUrl
				.getLocalizedPath();

		if (url == null) {
			if (defaultDonateUrl == null) {

				defaultDonateUrl = new LocalizedPath(
						context.getString(R.string.afterTasteDonateUrl),
						LocalizedPath.LOWERCASE_FILENAME,
						LocalizedPath.getCacheListFromUrl(context
								.getString(R.string.afterTasteDonateUrlLanInfo)),
						null).createLocalizedUrl();
			}

			url = defaultDonateUrl.getLocalizedPath();
		}

		if (url != null) {
			Uri uri = Uri.parse(url);

			Intent it = new Intent(Intent.ACTION_VIEW, uri);

			context.startActivity(it);
		}

	}

	public Context getContext() {
		return context;
	}

}
