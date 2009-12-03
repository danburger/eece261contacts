package org.eece261.contactswap;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ContactSwap extends Activity {
    Button btnSendSMS;
    Button btnSMSDebug;
    Button btnFriends;
    Button btnQuit;
    Button btnAdd;
    Button btnReturn;
    
    EditText txtPhoneNo;
    EditText txtMessage;
    EditText etName;
    
    ListView lvFriends;
    ArrayList<String> alFriends;
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	loadFriendsList();
    	startMainMenu();
    }
    
    private void startMainMenu() {
        setContentView(R.layout.menu);
        
        btnSMSDebug = (Button) findViewById(R.id.btnSMSDebug);
        btnFriends = (Button) findViewById(R.id.btnFriends);
        btnQuit = (Button) findViewById(R.id.btnQuit);
 
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
    
    private void removeFriend(String friend) {
    	Iterator<String> sit = alFriends.iterator();
    	
    	while(sit.hasNext()) {
    		String current = sit.next();
    		if(current.equalsIgnoreCase(friend)) {
    			alFriends.remove(current);
    		}
    	}
    	
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
}