#One method to delete SMS

# Introduction #

Note: I've read there are problems with deleting a text in the onReceive method of your broadcast receiver because the message won't be written to the db until it returns.  I figured we can get around this using another thread with a small delay.
```
private void deleteNags() {
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
      if( body.indexOf( "FRM:nagios@" ) == -1 )
        continue;
      long thread_id = cursor.getLong( 1 );
      Uri thread = Uri.parse( "content://sms/conversations/" + thread_id );
      cr.delete( thread, null, null );
    } while ( cursor.moveToNext() );
  }
```