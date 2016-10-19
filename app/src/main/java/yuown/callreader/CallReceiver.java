package yuown.callreader;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

import yuown.callreader.utils.Utils;

/**
 * Created by kirannk on 05/10/16.
 */
public class CallReceiver extends BroadcastReceiver {

    private static boolean incomingFlag = false;

    private static String incoming_number = null;

    private SharedPreferences sharedPref;

    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        boolean isActiveCall = Utils.isCallActive(context);
        boolean ignoreOnCall = sharedPref.getBoolean("ignoreOnCall", true);

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        switch (tm.getCallState()) {
            case TelephonyManager.CALL_STATE_RINGING:
                if(!isActiveCall || !ignoreOnCall) {
                    incomingFlag = true;
                    incoming_number = intent.getStringExtra("incoming_number");

                    Intent t = new Intent();
                    t.setClass(context, Popup.class);
                    t.putExtra("message", generateMessage(context, incoming_number));
                    t.putExtra("language", sharedPref.getString("language", ""));
                    t.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    t.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    t.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    t.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(t);
                }
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                if (incomingFlag) {
                    Popup.speechView.stopSpeaking();
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                if (incomingFlag) {
                    Popup.speechView.stopSpeaking();
                }
                break;
        }
    }

    private String generateMessage(Context context, String incoming_number) {
        String contactName = Utils.readContact(context, incoming_number);
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(sharedPref.getString("message", ""));
        if (null == contactName) {
            messageBuilder.append(incoming_number);
        } else {
            messageBuilder.append(contactName);
        }
        return messageBuilder.toString();
    }
}