package org.eece261.contactswap;

import org.eece261.contactswap.PopUp;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver
{		
	
    @Override
    public void onReceive(Context context, Intent intent) 
    {
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();        
        SmsMessage[] msgs = null;

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
                	msgAsString = msgAsString.substring(12);
                	String Command = "";
                	String Tag = "";
                	String Data = "";
                	String Name = "";
            		String Phone = "";
            		boolean NotFound = false;
            		boolean Accept = false;
            		boolean Decline = false;
                	
                	//Get command
                	while(msgAsString.charAt(0) != ':') {
                		Command += msgAsString.charAt(0);
                		msgAsString = msgAsString.substring(1);
                	}
                	msgAsString = msgAsString.substring(1);

                	while(msgAsString.length() > 0) {
                		//Get Tag
                		Tag = "";
                    	while(msgAsString.charAt(0) != ':') {
                    		Tag += msgAsString.charAt(0);
                    		msgAsString = msgAsString.substring(1);
                    	}
                    	msgAsString = msgAsString.substring(1);
                    	
                    	if(Tag.equalsIgnoreCase("NotFound")) {
                    		NotFound = true;
                    	} else if(Tag.equalsIgnoreCase("Accept")) {
                    		Accept = true;
                    	} else if(Tag.equalsIgnoreCase("Decline")) {
                    		Decline = true;
                    	} else {
                    		Data = "";

                    		//Get Data
                        	while(msgAsString.charAt(0) != ':') {
                        		Data += msgAsString.charAt(0);
                        		msgAsString = msgAsString.substring(1);
                        	}
                        	msgAsString = msgAsString.substring(1);
                        	Data = Data.replace('$', ' ');
                        	
                        	if(Tag.equalsIgnoreCase("Name")) {
                        		Name = Data;
                        	} else if(Tag.equalsIgnoreCase("Phone")) {
                        		Phone = Data;
                        	}
                    	}
                	}
                	
                	if(Command.equalsIgnoreCase("Query")) {
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
    					
    					Name = Name.replace(' ', '$');
    					
    					String message = "";
    					if(!Phone.equalsIgnoreCase("")) {
    						message = "ContactSwap:Contact:Name:" + Name + ":Phone:" + Phone + ":";
    					} else {
    						message = "ContactSwap:Contact:Name:" + Name + ":NotFound:";
    					}
    						
                    	ContactSwapUtils.sendSMS(msgs[i].getOriginatingAddress(), message);
                    	
                	} else if(Command.equalsIgnoreCase("Contact")) {
                		if(!NotFound) {
	                		ContactHandler shSearches = new ContactHandler();
	                		shSearches.addReceived(Name, Phone);
	                	}
                	} else if(Command.equalsIgnoreCase("Friend")) {
                		final FriendHandler fhFriends = new FriendHandler();
                		if(Decline) {
                			//
                		} else if(Accept) {
                			fhFriends.addFriend(msgs[i].getOriginatingAddress());
                		} else {
                			context.startActivity(new Intent()
                										.setClass(context, PopUp.class)
                										.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                										.putExtra("Phone", msgs[i].getOriginatingAddress()));
                		}
                	}
                }
            }
        }                         
    }
}
