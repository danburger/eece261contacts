package org.eece261.contactswap;


import java.util.ArrayList;

import org.eece261.contactswap.ContactSwap.search;
import org.eece261.contactswap.ContactSwap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
                }
            }
            //---display the new SMS message---
            if(!str.equalsIgnoreCase("")) {
            	Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            }
        }                         
    }
}
