package jemuillot.pkg.Utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class AfterTaste extends Dialog {

	private Activity cntx;

	private LocalizedPath defaultDonateUrl;

	public AfterTaste(Activity c) {
		super(c);
		cntx = c;
	}

	public void showADClickHint() {
		Toast.makeText(cntx, R.string.afterTastePleaseClickAD,
				Toast.LENGTH_LONG).show();
	}

	public void showDonateClickHint() {
		Toast.makeText(cntx, R.string.afterTastePleaseDonate, Toast.LENGTH_LONG)
				.show();
	}

	/**
	 * Set email to null to use the default address. Default mail address =
	 * R.string.afterTasteEMailAddress You can override the value in your res
	 */
	public void feedback(final String email, final LocalizedPath homepage) {

		new AlertDialog.Builder(cntx)
				.setTitle(R.string.afterTasteFeedback)
				.setSingleChoiceItems(R.array.afterTasteFeedbackTypes, -1,
						new OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								dialog.dismiss();

								if (whichButton >= 3) {
									
									donate(homepage);

								} else {

									String[] items = cntx
											.getResources()
											.getStringArray(
													R.array.afterTasteFeedbackSubjects);

									String feedbackSelected = items[whichButton];

									String mailTo = email;
									if (mailTo == null)
										mailTo = cntx
												.getString(R.string.afterTasteEMailAddress);

									Intent returnIt = new Intent(
											Intent.ACTION_SEND);

									String[] tos = { mailTo };
									returnIt.putExtra(Intent.EXTRA_EMAIL, tos);
									returnIt.putExtra(Intent.EXTRA_SUBJECT,
											String.format(feedbackSelected,
													Packapp.getAppTitle(cntx)));
									returnIt.setType("message/rfc882");
									cntx.startActivity(Intent.createChooser(
											returnIt,
											cntx.getString(R.string.afterTasteChooseEmailClient)));
								}

							}
						}).create().show();

	}

	public void share(String downloadUrl) {
		Intent sintent = new Intent(Intent.ACTION_SEND);
		sintent.setType("text/plain");

		String appTitle = Packapp.getAppTitle(cntx);

		sintent.putExtra(Intent.EXTRA_SUBJECT, String.format(
				cntx.getString(R.string.afterTasteShareSubject), appTitle));

		sintent.putExtra(Intent.EXTRA_TEXT, String.format(
				cntx.getString(R.string.afterTasteShareContent), appTitle,
				downloadUrl));
		cntx.startActivity(Intent.createChooser(sintent,
				cntx.getString(R.string.afterTasteShare)));
	}

	public void donate(LocalizedPath localizedUrl) {

		String url = localizedUrl == null ? null : localizedUrl
				.getLocalizedPath();

		if (url == null) {
			if (defaultDonateUrl == null) {

				defaultDonateUrl = new LocalizedPath(
						cntx.getString(R.string.afterTasteDonateUrl),
						LocalizedPath.LOWERCASE_FILENAME,
						LocalizedPath.getCacheListFromUrl(cntx
								.getString(R.string.afterTasteDonateUrlLanInfo)),
						null).createLocalizedUrl();
			}

			url = defaultDonateUrl.getLocalizedPath();
		}

		if (url != null) {
			Uri uri = Uri.parse(url);

			Intent it = new Intent(Intent.ACTION_VIEW, uri);

			cntx.startActivity(it);
		}

	}

}
