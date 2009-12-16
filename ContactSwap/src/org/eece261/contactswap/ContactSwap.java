package org.eece261.contactswap;

import java.util.ListIterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.Contacts.People;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ContactSwap extends Activity {
	Button btnSendSMS;
	Button btnSMSDebug;
	Button btnFriends;
	Button btnQuit;
	Button btnAdd;
	Button btnReturn;
	Button btnSearch;
	Button btnRemove;
	Button btnReceived;
	Button btnSend;

	String curname = "";

	EditText txtPhoneNo;
	EditText txtMessage;
	EditText etName;

	TextView tvSearchName;
	TextView aview;
	TextView bview;

	ListView lvSend;
	ListView lvContacts;
	ListView lvFriends;
	ListView lvSearches;
	ListView lvReceivedContacts;
	ListView lvResults;
	ContactHandler chContacts; // Note: Only handles contacts internal to the
								// software
	FriendHandler fhFriends;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		chContacts = new ContactHandler();
		fhFriends = new FriendHandler();
		startMainMenu();
	}

	private void startMainMenu() {
		setContentView(R.layout.menu);

		btnSMSDebug = (Button) findViewById(R.id.btnSMSDebug);
		btnFriends = (Button) findViewById(R.id.btnFriends);
		btnQuit = (Button) findViewById(R.id.btnQuit);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSend = (Button) findViewById(R.id.btnSend);
		btnReceived = (Button) findViewById(R.id.btnReceived);

		btnSearch.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startSearchManager();
			}
		});

		btnReceived.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startReceivedContactsManager();
			}
		});

		btnSMSDebug.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startSMSDebug();
			}
		});

		btnSend.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startSendContact();
			}
		});

		btnFriends.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startFriendManagement();
			}
		});

		btnQuit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void startSendContact() {
		setContentView(R.layout.sendcontacts);

		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(People.CONTENT_URI, null, null, null, null);

		final int[] names = new int[] { android.R.id.text1, android.R.id.text2 };
		lvContacts = (ListView) findViewById(R.id.lvContacts);

		final ListAdapter adapter = new SimpleCursorAdapter(this, // Context.
				android.R.layout.two_line_list_item, cur, // Pass in the cursor
				// to bind to.
				new String[] { People.NAME, People.NUMBER }, // Array of
				// cursor
				// columns
				// to bind
				// to.
				names); // Parallel array of which template objects to bind to
		// those columns.

		lvContacts.setAdapter(adapter);

		btnReturn = (Button) findViewById(R.id.btnReturn);

		btnReturn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startMainMenu();
			}
		});

		lvContacts
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Cursor c = (Cursor) adapter.getItem(arg2);
						String name_ = c.getString(c
								.getColumnIndex(People.NAME));
						String number_ = c.getString(c
								.getColumnIndex(People.NUMBER));
						sendThisContactScreen(name_, number_);
					}
				});
	}

	private void sendThisContactScreen(final String name, final String number) {
		setContentView(R.layout.sendthiscontact);

		btnReturn = (Button) findViewById(R.id.btnReturn);
		lvFriends = (ListView) findViewById(R.id.lvFriends);

		TextView title = (TextView) findViewById(R.id.bview);
		title.setText("Send " + name + ":");

		TextView displayName = (TextView) findViewById(R.id.tvSearchName);
		displayName.setText(number);

		TextView aview = (TextView) findViewById(R.id.aview);
		aview.setText("Click a friend to send " + name + "'s number.");

		final ArrayAdapter<String> aaFriends = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, fhFriends.getFriends());

		lvFriends.setAdapter(aaFriends);

		btnReturn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startSendContact();
			}
		});

		lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				final String friendNm = fhFriends.getFriends().get(arg2);

				new AlertDialog.Builder(ContactSwap.this).setTitle(
						"Are you sure?").setMessage(
						"Are you sure you want to send " + name
								+ "'s number to " + friendNm + "?")
						.setNeutralButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										String message = "ContactSwap:Contact:Name:"
												+ name
												+ ":Phone:"
												+ number
												+ ":";
										ContactSwapUtils.sendSMS(friendNm,
												message);
										startSendContact();
									}
								}).show();
			}
		});
	}

	private void startReceivedContactsManager() {
		setContentView(R.layout.receivedcontacts);

		btnReturn = (Button) findViewById(R.id.btnReturn);
		lvReceivedContacts = (ListView) findViewById(R.id.lvReceivedContacts);

		final ArrayAdapter<String> aaNames = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, chContacts.getReceivedContacts());

		lvReceivedContacts.setAdapter(aaNames);

		btnReturn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startMainMenu();
			}
		});

		lvReceivedContacts
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						openReceivedResultsMain(chContacts.getReceivedContacts().get(
								arg2));
					}
				});

	}

	private void openReceivedResultsMain(String name) {
		setContentView(R.layout.results);

		curname = name;

		btnReturn = (Button) findViewById(R.id.btnReturn);
		btnRemove = (Button) findViewById(R.id.btnRemove);
		lvResults = (ListView) findViewById(R.id.lvResults);
		tvSearchName = (TextView) findViewById(R.id.tvSearchName);

		tvSearchName.setText(name);

		btnReturn.setText("Return to Contacts Received");

		final ArrayAdapter<String> aaResults = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, chContacts
						.getReceivedResults(name));

		lvResults.setAdapter(aaResults);

		btnReturn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startReceivedContactsManager();
			}
		});

		btnRemove.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				chContacts.removeContact(curname);
				startReceivedContactsManager();
			}
		});

		lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			// System.err.println("Could not listen on port: 8080.");
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				final String ThisWillOnlyBeUsedHERE = chContacts
						.getReceivedResults(curname).get(arg2);
				new AlertDialog.Builder(ContactSwap.this).setTitle(
						"Are you sure?").setMessage(
						"Are you sure you want to add " + curname
								+ " with number " + ThisWillOnlyBeUsedHERE
								+ "?").setNeutralButton("No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								addContact(curname, ThisWillOnlyBeUsedHERE);
								startReceivedContactsManager();
							}
						}).show();
			}
		});
	}

	private void startSearchManager() {
		setContentView(R.layout.search);

		btnReturn = (Button) findViewById(R.id.btnReturn);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		lvSearches = (ListView) findViewById(R.id.lvSearches);
		etName = (EditText) findViewById(R.id.etName);

		final ArrayAdapter<String> aaNames = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, chContacts.getSearchNames());

		lvSearches.setAdapter(aaNames);

		btnReturn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startMainMenu();
			}
		});

		btnSearch.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				chContacts.addSearch(etName.getText().toString());
				queryContactsForName(etName.getText().toString());
				aaNames.notifyDataSetChanged();
				openSearchResults(etName.getText().toString());
			}
		});

		lvSearches
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						openSearchResults(chContacts.getSearchNames().get(arg2));
					}
				});
	}

	private void openSearchResults(String name) {
		setContentView(R.layout.results);

		curname = name;

		btnReturn = (Button) findViewById(R.id.btnReturn);
		btnRemove = (Button) findViewById(R.id.btnRemove);
		lvResults = (ListView) findViewById(R.id.lvResults);
		tvSearchName = (TextView) findViewById(R.id.tvSearchName);

		tvSearchName.setText(name);

		final ArrayAdapter<String> aaResults = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, chContacts
						.getReceivedResults(name));

		lvResults.setAdapter(aaResults);

		btnReturn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startSearchManager();
			}
		});

		btnRemove.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				chContacts.removeContact(curname);
				startSearchManager();
			}
		});

		lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			// System.err.println("Could not listen on port: 8080.");
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				final String ThisWillOnlyBeUsedHERE = chContacts
						.getReceivedResults(curname).get(arg2);
				new AlertDialog.Builder(ContactSwap.this).setTitle(
						"Are you sure?").setMessage(
						"Are you sure you want to add " + curname
								+ " with number " + ThisWillOnlyBeUsedHERE
								+ "?").setNeutralButton("No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								addContact(curname, ThisWillOnlyBeUsedHERE);
							}
						}).show();
			}
		});
	}

	private void startFriendManagement() {
		setContentView(R.layout.managefriends);

		btnReturn = (Button) findViewById(R.id.btnReturn);
		btnAdd = (Button) findViewById(R.id.btnAdd);
		lvFriends = (ListView) findViewById(R.id.lvFriends);
		etName = (EditText) findViewById(R.id.etName);

		final ArrayAdapter<String> aaFriends = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, fhFriends.getFriends());

		lvFriends.setAdapter(aaFriends);

		btnReturn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startMainMenu();
			}
		});

		btnAdd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				fhFriends.addRequest(etName.getText().toString());
				ContactSwapUtils.sendSMS(etName.getText().toString(), "ContactSwap:Friend:");
				Toast.makeText(getApplicationContext(), "Friend request sent.", Toast.LENGTH_LONG);
			}
		});

		lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				fhFriends.removeFriend(arg2);
				aaFriends.notifyDataSetChanged();
			}
		});
	}

	private void startSMSDebug() {
		setContentView(R.layout.smsdebug);

		btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
		btnReturn = (Button) findViewById(R.id.btnReturn);
		txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
		txtMessage = (EditText) findViewById(R.id.txtMessage);

		btnSendSMS.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String phoneNo = txtPhoneNo.getText().toString();
				String message = txtMessage.getText().toString();
				if (phoneNo.length() > 0 && message.length() > 0)
					ContactSwapUtils.sendSMS(phoneNo, message);
				else
					Toast.makeText(ContactSwap.this.getApplicationContext(),
							"Please enter both phone number and message.",
							Toast.LENGTH_SHORT).show();
			}
		});

		btnReturn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startMainMenu();
			}
		});

	}

	// Sends and SMS query to all "Friends"
	private void queryContactsForName(String name) {
		name = name.replace(' ', '$');

		String message = "ContactSwap:Query:Name:" + name + ":";
		ListIterator<String> alFriendsIterator = fhFriends.getFriends()
				.listIterator();
		while (alFriendsIterator.hasNext()) {
			ContactSwapUtils.sendSMS(alFriendsIterator.next(), message);
		}
		return;
	}

	private Uri addContact(String contactName, String phoneNumber) {
		ContentValues values = new ContentValues();

		values.put(Contacts.People.NAME, contactName);
		// 1 = contact is a favorite; 0 = contact is not a favorite
		values.put(Contacts.People.STARRED, 0);

		Uri newPersonUri = Contacts.People.createPersonInMyContactsGroup(
				getContentResolver(), values);

		if (newPersonUri != null) {

			ContentValues mobileValues = new ContentValues();
			Uri mobileUri = Uri.withAppendedPath(newPersonUri,
					Contacts.People.Phones.CONTENT_DIRECTORY);
			mobileValues.put(Contacts.Phones.NUMBER, phoneNumber);
			mobileValues.put(Contacts.Phones.TYPE, Contacts.Phones.TYPE_MOBILE);
			Uri phoneUpdate = getContentResolver().insert(mobileUri,
					mobileValues);
			if (phoneUpdate == null) {
				Log.i("CA", "Failed to add new number");
			}
		}

		String addedString = "New contact added with name: " + contactName
				+ " and number: " + phoneNumber;

		Log.i("CA", addedString);

		Toast.makeText(super.getApplicationContext(), addedString,
				Toast.LENGTH_LONG);

		return newPersonUri;
	}
}