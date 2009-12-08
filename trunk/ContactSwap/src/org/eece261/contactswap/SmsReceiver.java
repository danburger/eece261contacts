package org.eece261.contactswap;


import java.util.ArrayList;

import org.eece261.contactswap.ContactSwap.search;
import org.eece261.contactswap.ContactSwap;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast; 

public class SmsReceiver extends BroadcastReceiver
{		
	ArrayList<search> alSearches;
	
    @Override
    public void onReceive(Context context, Intent intent) 
    {
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();        
        SmsMessage[] msgs = null;
        String str = "";            
        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];            
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                
                if(msgs[i].getMessageBody().toString().contains("ContactSwap:Response:") && ! msgs[i].getMessageBody().toString().contains("NotFound:")) {
                	str += "Contact Found!";
                	String Phone = "";
                	String Name = "";
                	int position = msgs[i].getMessageBody().toString().indexOf("Phone:") + 6;
                	while(msgs[i].getMessageBody().toString().charAt(position) != ':') {
                		Phone += msgs[i].getMessageBody().toString().charAt(position);
                		position++;
                	}
                	position = msgs[i].getMessageBody().toString().indexOf("Name:") + 5;
                	while(msgs[i].getMessageBody().toString().charAt(position) != ':') {
                		Name += msgs[i].getMessageBody().toString().charAt(position);
                		position++;
                	}
                	context.startActivity(new Intent(context, ContactSwap.class).putExtra("Name", Name).putExtra("Phone",Phone).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else if (msgs[i].getMessageBody().toString().contains("ContactSwap:Query:") ) {
                	str += "Query Received!";
                	String Name = "";
                	String Phone = "";
                	int position = msgs[i].getMessageBody().toString().indexOf("Name:") + 5;
                	while(msgs[i].getMessageBody().toString().charAt(position) != ':') {
                		Name += msgs[i].getMessageBody().toString().charAt(position);
                		position++;
                	}
                	
                	Name.replace('^', ' ');
                	
                	ContentResolver cr = context.getContentResolver();
					
					Cursor cur =  cr.query(People.CONTENT_URI,null,null,null,null);
					
					if (cur.moveToFirst()) {
				        int nameColumn = cur.getColumnIndex(People.NAME); 
				        int phoneColumn = cur.getColumnIndex(People.NUMBER);
				    
				        do {
				        	Toast.makeText(context, cur.getString(nameColumn), Toast.LENGTH_LONG).show();
				            if(cur.getString(nameColumn).equalsIgnoreCase(Name)) {
				            	Phone = cur.getString(phoneColumn);
				            	Toast.makeText(context, Phone, Toast.LENGTH_LONG).show();
				            }
				        } while (cur.moveToNext());
				    }
					
					String message = "";
					if(!Phone.equalsIgnoreCase("")) {
						message = "ContactSwap:Response:Name:" + Name + ":Phone:" + Phone + ":";
					} else {
						message = "ContactSwap:Response:Name:" + Name + ":NotFound:";
					}
						
                	sendSMS(msgs[i].getOriginatingAddress(), message);
                	
                }
            }
            //---display the new SMS message---
            if(!str.equalsIgnoreCase("")) {
            	Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            }
        }                         
    }
    
    private void sendSMS(String phoneNumber, String message)
    {        
    	SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);        
    }
}
