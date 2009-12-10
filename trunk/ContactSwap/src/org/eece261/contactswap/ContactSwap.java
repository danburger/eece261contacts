package org.eece261.contactswap;

import java.util.ListIterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.Log; 
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
    
    String curname = "";
    
    EditText txtPhoneNo;
    EditText txtMessage;
    EditText etName;
    
    TextView tvSearchName;
    
    ListView lvFriends;
    ListView lvSearches;
    ListView lvResults;
    SearchHandler shSearches;
    FriendHandler fhFriends;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	shSearches = new SearchHandler();
    	fhFriends = new FriendHandler();
    	startMainMenu();
    }
    
    
    private void startMainMenu() {
        setContentView(R.layout.menu);
        
        btnSMSDebug = (Button) findViewById(R.id.btnSMSDebug);
        btnFriends = (Button) findViewById(R.id.btnFriends);
        btnQuit = (Button) findViewById(R.id.btnQuit);
        btnSearch = (Button) findViewById(R.id.btnSearch);
 
        btnSearch.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {                
                startSearchManager();
            }
        });
        
        btnSMSDebug.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {                
                startSMSDebug();
            }
        });
        
        btnFriends.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {                
                startFriendManagement();
            }
        });
        
        btnQuit.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {                
                finish();
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
        		android.R.layout.simple_list_item_1, shSearches.getSearchNames());
        
    	lvSearches.setAdapter(aaNames);
    	
    	btnReturn.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {                
                startMainMenu();
            }
        });
    	
    	btnSearch.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {                
            	shSearches.addSearch(etName.getText().toString());
                queryContactsForName(etName.getText().toString());
                aaNames.notifyDataSetChanged();
                openSearchResults(etName.getText().toString());
            }
        });
    	
    	lvSearches.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				openSearchResults(shSearches.getSearchNames().get(arg2));
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
        		android.R.layout.simple_list_item_1, shSearches.getSearchResults(name));
        
    	lvResults.setAdapter(aaResults);
    	
    	btnReturn.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {                
                startSearchManager();
            }
        });
    	
    	btnRemove.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {                
            	shSearches.removeSearch(curname);
                startSearchManager();
            }
        });
    	
    	lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			//System.err.println("Could not listen on port: 8080.");
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				
				final String ThisWillOnlyBeUsedHERE = shSearches.getSearchResults(curname).get(arg2);	
				new AlertDialog.Builder(ContactSwap.this).setTitle("Are you sure?")
					.setMessage("Are you sure you want to add " + curname + " with number " + ThisWillOnlyBeUsedHERE + "?")
					.setNeutralButton("No",	new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {}
					})
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							addContact(curname, ThisWillOnlyBeUsedHERE);
						}
					})
					.show();
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
    	
    	btnReturn.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {                
                startMainMenu();
            }
        });
    	
    	btnAdd.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {                
                fhFriends.addFriend(etName.getText().toString());
                aaFriends.notifyDataSetChanged();
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
        txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
        txtMessage = (EditText) findViewById(R.id.txtMessage);
 
        btnSendSMS.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {                
                String phoneNo = txtPhoneNo.getText().toString();
                String message = txtMessage.getText().toString();                 
                if (phoneNo.length()>0 && message.length()>0)                
                    ContactSwapUtils.sendSMS(phoneNo, message);                
                else
                    Toast.makeText(ContactSwap.this.getApplicationContext(), 
                        "Please enter both phone number and message.", 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    //Sends and SMS query to all "Friends"
    private void queryContactsForName(String name)
    {
    	name.replace(' ', '^');
    	
    	String message = "ContactSwap:Query:Name:" + name + ":";
    	ListIterator<String> alFriendsIterator = fhFriends.getFriends().listIterator();
    	while(alFriendsIterator.hasNext())
    	{
    		ContactSwapUtils.sendSMS(alFriendsIterator.next(),message);
    	}
    	return;
    }

    private Uri addContact(String contactName, String phoneNumber)
    {
    	ContentValues values = new ContentValues();

    	values.put(Contacts.People.NAME, contactName);
    	// 1 = contact is a favorite; 0 = contact is not a favorite
    	values.put(Contacts.People.STARRED, 0);

    	Uri newPersonUri = Contacts.People.createPersonInMyContactsGroup(getContentResolver(), values);

    	if (newPersonUri != null) 
    	{
			
			ContentValues mobileValues = new ContentValues();
			Uri mobileUri = Uri.withAppendedPath(newPersonUri, Contacts.People.Phones.CONTENT_DIRECTORY);
			mobileValues.put(Contacts.Phones.NUMBER, phoneNumber);
			mobileValues.put(Contacts.Phones.TYPE, Contacts.Phones.TYPE_MOBILE);
			Uri phoneUpdate = getContentResolver().insert(mobileUri,
					mobileValues);
			if (phoneUpdate == null) 
			{
				Log.i("CA","Failed to add new number");
			}
    	}
    	
        String addedString = "New contact added with name: " + contactName + " and number: " +phoneNumber;
       	
        Log.i("CA",addedString); 
    	
        Toast.makeText(super.getApplicationContext(), addedString, Toast.LENGTH_LONG);
    	
    	return newPersonUri;
    }
}