package org.eece261.contactswap;

import android.telephony.SmsManager;

public class ContactSwapUtils {
	public static void sendSMS(String phoneNumber, String message)
    {        
    	SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);        
    }
	
/*	public static void deleteMessage(Context context, String address, long timestamp) {
		int id = 0;
		long threadId = getThreadId(context, address); 
		if (threadId > 0) {

			ContentResolver myCR = context.getContentResolver();

			Cursor cursor = myCR.query(ContentUris.withAppendedId(
					Uri.parse("content://sms/conversations/"), threadId), new String[] { "_id",
					"date", "thread_id" }, "thread_id=" + threadId + " and "
					+ "date=" + timestamp, null, "date desc");
			if (cursor != null) {
				try {
					Log.i("DELETE", "CURSOR");
					if (cursor.moveToFirst()) {
						id = cursor.getInt(0);
						Log.i("DELETE", "id=" +id);
					}
				} finally {
					cursor.close();
				}
			}

			if (id > 0) {
				Uri deleteUri;

				deleteUri = Uri.withAppendedPath(Uri.parse("content://sms/"), String.valueOf(id));
				int count = myCR.delete(deleteUri, null, null);
			}
		}
	}
	*/
	
	  /**
     * Tries to delete a message from the system database, given the thread id,
     * the timestamp of the message and the message type (sms/mms).
     */
/*    public static void deleteMessage(Context context, String address, long timestamp, String body) {
    	Log.i("Delete", "in Delete");
    	long threadId = getThreadId(context, address);
    	Log.i("Delete", "ThreadID Found = " + threadId);
    	long messageId = findMessageId(context, threadId, timestamp, body);
    	Log.i("Delete", "MID Found = " + messageId);

		if (messageId > 0) {
			Uri deleteUri;
			deleteUri = Uri.withAppendedPath(Uri.parse("content://sms/"), String.valueOf(messageId));
			
			//context.getContentResolver().delete(Uri.parse("content://sms/conversations/" + threadId),null,null);

			//context.getContentResolver().delete(deleteUri, null, null);
			//setThreadRead(context, threadId);
      }
    }
    
    public static void setThreadRead(Context context, long threadId) {
    	Log.i("Delete", "marking thread as read");
        if (threadId > 0) {
          ContentValues values = new ContentValues(1);
          values.put("read", 1);

          ContentResolver cr = context.getContentResolver();
          try {
        	  	cr.update(ContentUris.withAppendedId(Uri.withAppendedPath(Uri.parse("content://mms-sms/"), "conversations"), threadId),
                values, null, null);
          } catch (Exception e) {
            Log.v("Tag","error marking thread read");
          }
        }
      }
    
	private static long getThreadId(Context context, String address) 
	{
		Log.i("Delete", "in getThreadID");
		if (address == null)
			return 0;

		String THREAD_RECIPIENT_QUERY = "recipient";

		Uri.Builder uriBuilder = Uri.withAppendedPath(
				Uri.parse("content://mms-sms/"), "threadID").buildUpon();
		uriBuilder.appendQueryParameter(THREAD_RECIPIENT_QUERY, address);

		long threadId = 0;

		Cursor cursor = context.getContentResolver().query(uriBuilder.build(),
				new String[] { "_id" }, null, null, null);
		if (cursor != null) {
			try {
				if (cursor.moveToFirst()) {
					threadId = cursor.getLong(0);
				}
			} finally {
				cursor.close();
			}
		}
		Log.i("Delete", "returning threadID");
		return threadId;
	}


public static long findMessageId(Context context, long threadId, long timestamp, String body) {
	Log.i("Delete", "in findMessageID");
    long id = 0;
    String selection = "body = " + DatabaseUtils.sqlEscapeString(body);
    final String sortOrder = "date DESC";
    final String[] projection = new String[] { "_id", "date", "thread_id", "body" };

    if (threadId > 0) {
    	Log.i("Delete", "threadID>0");
      Cursor cursor = context.getContentResolver().query(
          ContentUris.withAppendedId(Uri.withAppendedPath(Uri.parse("content://mms-sms/"), "conversations"), threadId),
          projection,
          selection,
          null,
          sortOrder);
      if (cursor != null) {
          Log.i("Delete", "cursor!=null");
        try {
          if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
            Log.v("delete", "Message id found = " + id);
          }
        } finally {
          cursor.close();
        }
      }
    }
    Log.i("Delete", "returning message id");
    return id;
  }*/
}