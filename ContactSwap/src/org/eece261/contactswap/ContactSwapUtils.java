package org.eece261.contactswap;

import android.telephony.SmsManager;

public class ContactSwapUtils {
	public static void sendSMS(String phoneNumber, String message)
    {        
    	SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);        
    }

}
