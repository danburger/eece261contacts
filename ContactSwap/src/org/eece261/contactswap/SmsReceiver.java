package org.eece261.contactswap;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver
{		
	
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
                
                String msgAsString = msgs[i].getMessageBody().toString();
                
                //Is this one of ours?
                if(msgAsString.startsWith("ContactSwap:")) {
                	//TODO rewrite parsing stuff
                }
                if(msgs[i].getMessageBody().toString().contains("ContactSwap:Contact:") && ! msgs[i].getMessageBody().toString().contains("NotFound:")) {
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
                	SearchHandler shSearches = new SearchHandler();
                	shSearches.addSearchResult(Name, Phone);
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
				            if(cur.getString(nameColumn).equalsIgnoreCase(Name)) {
				            	Phone = cur.getString(phoneColumn);
				            }
				        } while (cur.moveToNext());
				    }
					
					String message = "";
					if(!Phone.equalsIgnoreCase("")) {
						message = "ContactSwap:Contact:Name:" + Name + ":Phone:" + Phone + ":";
					} else {
						message = "ContactSwap:Contact:Name:" + Name + ":NotFound:";
					}
						
                	ContactSwapUtils.sendSMS(msgs[i].getOriginatingAddress(), message);
                	
                }
            }
        }                         
    }
}
