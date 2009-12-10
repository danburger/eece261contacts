package org.eece261.contactswap;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.widget.LinearLayout;

public class PopUp extends Activity {
	private static final int MAX_WIDTH = 640;
	private static final double WIDTH = 0.9;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.popup);

		resizeLayout();

	}

	private void resizeLayout() {
		LinearLayout mainLL = (LinearLayout) findViewById(R.id.MainLinearLayout);

		Display d = getWindowManager().getDefaultDisplay();

		int width = d.getWidth() > MAX_WIDTH ? MAX_WIDTH
				: (int) (d.getWidth() * WIDTH);

		mainLL.setMinimumWidth(width);
		mainLL.invalidate();
	}
}
