package jemuillot.pkg.Utilities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class AfterTasteFeedbackSelector extends Activity implements
		View.OnClickListener {

	private RadioGroup mRadioGroup;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.aftertaste_feedbackcat);
		mRadioGroup = (RadioGroup) findViewById(R.id.aftertTasteCategory);

		Button okButton = (Button) findViewById(R.id.afterTasteSelect);
		okButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		int selected = mRadioGroup.getCheckedRadioButtonId();
		
		getIntent().putExtra("Selected", selected);
	}

}
