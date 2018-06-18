package biding.animal.com.animalbiding.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

/**
 * Created by Heena.Aggarwal on 08-08-2017.
 */
public class SMSReceiver extends BroadcastReceiver
{
    final SmsManager sms = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {
         if(intent != null)
         {
             Bundle bundle = intent.getExtras();
             if(bundle != null)
             {
                 final Object[] pdusObj = (Object[]) bundle.get("pdus");
                 for(int i = 0; i < pdusObj.length; i++)
                 {
                     SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                     String sender = currentMessage.getDisplayOriginatingAddress();
                     String message = currentMessage.getDisplayMessageBody();
                    // Log.d("SmsReceiver", "sender: " + sender + "; message: " + message);
                     if(message != null) {
                         Intent myIntent = new Intent("otp");
                         myIntent.putExtra("message", message);
                         myIntent.putExtra("sender",sender);
                         LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                     }
                 }

             }
         }

    }
}
