package org.eece261.contactswap;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PopUp extends Activity {
	private static final int MAX_WIDTH = 640;
	private static final double WIDTH = 0.9;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final String Friend = (String) getIntent().getExtras().get("Phone");

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.popup);
		
		TextView tvMessage = (TextView) findViewById(R.id.txtRequest);
		Button btnAccept = (Button) findViewById(R.id.btnAccept);
		Button btnDecline = (Button) findViewById(R.id.btnDecline);
		
		if(Friend != null) {
			tvMessage.setText("Would you like to be friends with " + Friend + "?");
		} else {
			tvMessage.setText("Would you like to be friends with anonymous?");
		}

		resizeLayout();
		
		btnAccept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FriendHandler fhFriends = new FriendHandler();
				fhFriends.addFriend(Friend);
				ContactSwapUtils.sendSMS(Friend, "ContactSwap:Friend:Accept:");
				finish();
			}
		});
		
		btnDecline.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FriendHandler fhFriends = new FriendHandler();
				fhFriends.addFriend(Friend);
				ContactSwapUtils.sendSMS(Friend, "ContactSwap:Friend:Decline:");
				finish();
			}
		});

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
