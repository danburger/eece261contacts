package org.eece261.contactswap;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.telephony.SmsManager;
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
    ArrayList<String> alFriends;
    ListView lvSearches;
    ListView lvResults;
    ArrayList<search> alSearches;
    
    private final ScheduledExecutorService scheduler =
        Executors.newScheduledThreadPool(1);
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	if(getIntent().hasExtra("Name") && getIntent().hasExtra("Phone")) {
    		loadSearchesList		();
    		addSearchResult(getIntent().getExtras().getString("Name"), getIntent().getExtras().getString("Phone"));
            scheduler.schedule(deleteMessages, 1, TimeUnit.SECONDS);
    		finish();
    	}
    	loadFriendsList();
    	loadSearchesList();
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
        		android.R.layout.simple_list_item_1, getSearchNames());
        
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
                addSearch(etName.getText().toString());
                queryContactsForName(etName.getText().toString());
                aaNames.notifyDataSetChanged();
                openSearchResults(etName.getText().toString());
            }
        });
    	
    	lvSearches.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				openSearchResults(getSearchNames().get(arg2));
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
        		android.R.layout.simple_list_item_1, getSearchResults(name));
        
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
                removeSearch(curname);
                startSearchManager();
            }
        });
    	
    	lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			//System.err.println("Could not listen on port: 8080.");
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				
				Log.i("CA", "Made it here");
				addContact(curname, getSearchResults(curname).get(arg2));
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
        		android.R.layout.simple_list_item_1, alFriends);
        
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
                addFriend(etName.getText().toString());
                aaFriends.notifyDataSetChanged();
            }
        });
    	
    	lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				removeFriend(arg2);
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
                    sendSMS(phoneNo, message);                
                else
                    Toast.makeText(getBaseContext(), 
                        "Please enter both phone number and message.", 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    public class search {
    	String name = "";
    	ArrayList<String> responses = new ArrayList<String>();
    }
    
    public void loadSearchesList() {
    	FileInputStream fin;
    	DataInputStream din;
    	
    	alSearches = new ArrayList<search>();
    	
    	try {
			fin = openFileInput("searchesList");
			din = new DataInputStream(fin);
			
			while(din.available() > 0) {
				search temp = new search();
				temp.name = din.readUTF();
				String current = din.readUTF();
				while(!current.equalsIgnoreCase("")) {
					temp.responses.add(current);
					current = din.readUTF();
				}
				alSearches.add(temp);
			}
			
			din.close();
			fin.close();
		} catch (FileNotFoundException e) {
			// Assume it hasn't been created yet
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void addSearchResult(String name, String phone) {
    	for(int i = 0; i < alSearches.size(); i++) {
    		if(alSearches.get(i).name.equalsIgnoreCase(name)) {
    			alSearches.get(i).responses.add(phone);
    		}
    	}
    	
    	FileOutputStream fout;
    	DataOutputStream dout;
    	
    	try {
			fout = openFileOutput("searchesList", MODE_PRIVATE);
			dout = new DataOutputStream(fout);
			
			for(int i = 0; i < alSearches.size(); i++) {
				dout.writeUTF(alSearches.get(i).name);
				for(int j = 0; j < alSearches.get(i).responses.size(); j++) {
					dout.writeUTF(alSearches.get(i).responses.get(j));
				}
				dout.writeUTF("");
			}
			
			dout.close();
			fout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private ArrayList<String> getSearchNames() {
    	loadSearchesList();
    	ArrayList<String> alResults = new ArrayList<String>();
    	for(int i = 0; i < alSearches.size(); i++) {
    		alResults.add(alSearches.get(i).name);
    	}
    	return alResults;
    }
    
    private ArrayList<String> getSearchResults(String name) {
    	loadSearchesList();
    	ArrayList<String> alResults = new ArrayList<String>();
    	for(int i = 0; i < alSearches.size(); i++) {
    		if(alSearches.get(i).name.equalsIgnoreCase(name)){
    			return alSearches.get(i).responses;
    		}
    	}
    	return alResults;
    }
    
    private void addSearch(String name) {
    	search temp = new search();
    	temp.name = name;
    	alSearches.add(temp);
    	
    	FileOutputStream fout;
    	DataOutputStream dout;
    	
    	try {
			fout = openFileOutput("searchesList", MODE_PRIVATE);
			dout = new DataOutputStream(fout);
			
			for(int i = 0; i < alSearches.size(); i++) {
				dout.writeUTF(alSearches.get(i).name);
				for(int j = 0; j < alSearches.get(i).responses.size(); j++) {
					dout.writeUTF(alSearches.get(i).responses.get(j));
				}
				dout.writeUTF("");
			}
			
			dout.close();
			fout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void removeSearch(String Name) {
    	for(int i = 0; i < alSearches.size(); i++) {
			if(alSearches.get(i).name.equalsIgnoreCase(Name)) {
				alSearches.remove(i);
			}
    	}
    	
    	FileOutputStream fout;
    	DataOutputStream dout;
    	
    	try {
    		fout = openFileOutput("searchesList", MODE_PRIVATE);
			dout = new DataOutputStream(fout);
			
			for(int i = 0; i < alSearches.size(); i++) {
				dout.writeUTF(alSearches.get(i).name);
				for(int j = 0; j < alSearches.get(i).responses.size(); j++) {
					dout.writeUTF(alSearches.get(i).responses.get(j));
				}
				dout.writeUTF("");
			}
			
			dout.close();
			fout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void loadFriendsList() {
    	FileInputStream fin;
    	DataInputStream din;
    	
    	alFriends = new ArrayList<String>();
    	
    	try {
			fin = openFileInput("friendsList");
			din = new DataInputStream(fin);
			
			while(din.available() > 0) {
				alFriends.add(din.readUTF());
			}
			
			fin.close();
		} catch (FileNotFoundException e) {
			// Assume it hasn't been created yet
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void addFriend(String friend) {
    	alFriends.add(friend);
    	
    	FileOutputStream fout;
    	DataOutputStream dout;
    	
    	try {
			fout = openFileOutput("friendsList", MODE_PRIVATE);
			dout = new DataOutputStream(fout);
			
			for(int i = 0; i < alFriends.size(); i++) {
				dout.writeUTF(alFriends.get(i));
			}
			
			fout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void removeFriend(int index) {
    	
    	alFriends.remove(index);
    	
    	FileOutputStream fout;
    	DataOutputStream dout;
    	
    	try {
    		fout = openFileOutput("friendsList", MODE_PRIVATE);
			dout = new DataOutputStream(fout);
			
			for(int i = 0; i < alFriends.size(); i++) {
				dout.writeUTF(alFriends.get(i));
			}
			
			fout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    //Sends and SMS query to all "Friends"
    private void queryContactsForName(String name)
    {
    	String message = "ContactSwap:Query:Name:" + name + ":";
    	ListIterator<String> alFriendsIterator = alFriends.listIterator();
    	while(alFriendsIterator.hasNext())
    	{
    		sendSMS(alFriendsIterator.next(),message);
    	}
    	return;
    }
    
    //---sends an SMS message to another device---
    private void sendSMS(String phoneNumber, String message)
    {        
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
 
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
            new Intent(SENT), 0);
 
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
            new Intent(DELIVERED), 0);
 
        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off", 
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));
 
        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){

			@Override
			public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered", 
                                Toast.LENGTH_SHORT).show();
                        break;                        
                }
				
			}
        }, new IntentFilter(DELIVERED));        
 
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);        
    }
    
    private final Runnable deleteMessages = new Runnable() {
    	public void run() {
        ContentResolver cr = getContentResolver();

        Uri inbox = Uri.parse( "content://sms/inbox" );
        Cursor cursor = cr.query(
            inbox,
            new String[] { "_id", "thread_id", "body" },
            null,
            null,
            null);

        if (cursor == null)
          return;

        if (!cursor.moveToFirst())
          return;

        do {
          String body = cursor.getString( 2 );
          if( body.contains( "ContactSwap:" )  && body.contains( "Query:" ) || body.contains( "Response:" ))
            continue;
          long thread_id = cursor.getLong( 1 );
          Uri thread = Uri.parse( "content://sms/conversations/" + thread_id );
          cr.delete( thread, null, null );
        } while ( cursor.moveToNext() );
      }
    };

    private Uri addContact(String contactName, String phoneNumber)
    {
    	ContentValues values = new ContentValues();

    	values.put(Contacts.People.NAME, contactName);
    	// 1 = contact is a favorite; 0 = contact is not a favorite
    	values.put(Contacts.People.STARRED, 0);

    	Uri newPersonUri = Contacts.People.createPersonInMyContactsGroup(getContentResolver(), values);

    	if (newPersonUri != null) 
    	{
			
			phoneNumber = numberConvert(phoneNumber);
			
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
 
    
	private String numberConvert(String phoneNumber) {
		if (phoneNumber.length() == 7) {
			phoneNumber = phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 7);
		}
		// Special case 2: (123) 456-7890
		else if (phoneNumber.length() == 10) {
			phoneNumber = "(" + phoneNumber.substring(0, 3) + ") "
					+ phoneNumber.substring(3, 6) + "-" + phoneNumber.substring(6,10);
		}
		else
		{
			phoneNumber = "555-5555";
		}
		return phoneNumber;
	}
}