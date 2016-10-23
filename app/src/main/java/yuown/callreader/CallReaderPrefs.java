package yuown.callreader;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import yuown.callreader.utils.Utils;

public class CallReaderPrefs extends PreferenceFragment {

    private CallReaderActivity callReaderActivity;

    public void setCallReaderActivity(CallReaderActivity callReaderActivity) {
        this.callReaderActivity = callReaderActivity;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.call_prefs);
        Preference testSpeechPreference = (Preference) findPreference("testSpeech");
        testSpeechPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Utils.writeLog("Test Speech");
                callReaderActivity.testSpeech();
                return true;
            }
        });

        Preference logsPrefs = (Preference) findPreference("logs");
        logsPrefs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Utils.writeLog("Read Logs");
                callReaderActivity.logs();
                return true;
            }
        });
    }
}
