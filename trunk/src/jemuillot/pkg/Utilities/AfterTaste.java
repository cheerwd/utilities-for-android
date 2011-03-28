package jemuillot.pkg.Utilities;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

public class AfterTaste {

	public static final int FEEDBACK_SUGGESTION = 0;
	public static final int FEEDBACK_BUGREPORT = 1;
	public static final int FEEDBACK_SAYHELLO = 2;

	private Activity cntx;

	public AfterTaste(Activity c) {
		cntx = c;
	}

	public void showADClickHint() {
		Toast.makeText(cntx, R.string.afterTastePleaseClickAD, Toast.LENGTH_LONG).show();
	}

	/**
	 * Set email to null to use the default address. Default mail address =
	 * R.string.afterTasteEMailAddress You can override the value in your res
	 */
	public void feedback(String email) {
		// Starting this will FC, I will try to figure it out these days...
		// cntx.startActivity(new Intent(cntx,
		// AfterTasteFeedbackSelector.class));

		if (email == null)
			email = cntx.getString(R.string.afterTasteEMailAddress);

		Intent returnIt = new Intent(Intent.ACTION_SEND);
		String[] tos = { email };
		returnIt.putExtra(Intent.EXTRA_EMAIL, tos);
		returnIt.putExtra(Intent.EXTRA_SUBJECT, String.format(cntx
				.getString(R.string.afterTasteFeedbackSubject), Packapp.getAppTitle(cntx)));
		returnIt.setType("message/rfc882");
		cntx.startActivity(Intent.createChooser(returnIt, cntx
				.getString(R.string.afterTasteChooseEmailClient)));
	}

	public void share(String downloadUrl) {
		Intent sintent = new Intent(Intent.ACTION_SEND);
		sintent.setType("text/plain");

		String appTitle = Packapp.getAppTitle(cntx);

		sintent.putExtra(Intent.EXTRA_SUBJECT, String.format(cntx
				.getString(R.string.afterTasteShareSubject), appTitle));

		sintent.putExtra(Intent.EXTRA_TEXT, String.format(cntx
				.getString(R.string.afterTasteShareContent), appTitle,
				downloadUrl));
		cntx.startActivity(Intent.createChooser(sintent, cntx
				.getString(R.string.afterTasteShare)));
	}
}
