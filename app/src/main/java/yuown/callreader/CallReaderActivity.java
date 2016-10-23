package yuown.callreader;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import yuown.callreader.utils.Utils;

public class CallReaderActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    private TextSpeaker textSpeaker;

    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener;

    private CallReaderPrefs callReaderPrefs;

    private PackageManager packageManager;

    private ComponentName callReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS}, 1);
        }

        callReaderPrefs = new CallReaderPrefs();
        callReaderPrefs.setCallReaderActivity(this);
        getFragmentManager().beginTransaction().replace(android.R.id.content, callReaderPrefs).commit();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        textSpeaker = TextSpeaker.getInstance(getApplicationContext());
        packageManager = getPackageManager();
        callReceiver = new ComponentName(this, CallReceiver.class);

        sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if(key.equals("broadcast")) {
                    boolean enabled = sharedPreferences.getBoolean(key, true);
                    int state = PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
                    if(!enabled) {
                        state = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
                    }
                    Utils.writeLog("Broadcast enable: " + enabled);
                    packageManager.setComponentEnabledSetting(callReceiver, state, PackageManager.DONT_KILL_APP);
                }
            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }

    protected final void testSpeech() {
        textSpeaker.testSpeech(sharedPreferences.getString("message", ""), sharedPreferences.getString("language", ""));
    }

    public void logs() {
        Intent t = new Intent();
        t.setClass(getApplicationContext(), LogsActivity.class);
        startActivity(t);
    }
}