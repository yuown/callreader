package yuown.callreader;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

import yuown.callreader.utils.Utils;

/**
 * Created by kirannk on 14/10/16.
 */
class TextSpeaker implements TextToSpeech.OnInitListener {

    private AudioManager audioManager;

    private TextToSpeech mTts;

    private boolean inProgress = false;

    private int initStatus = -1;

    private Context context;

    private static TextSpeaker instance = null;

    private String message;

    private String language;

    private TextSpeaker(Context context) {
        this.context = context;
        mTts = new TextToSpeech(context, this);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public static TextSpeaker getInstance(Context context) {
        if(null == instance) {
            instance = new TextSpeaker(context);
        }
        return instance;
    }

    @Override
    public void onInit(int status) {
        initStatus = status;
        speakOut(message, language);
    }

    public void testSpeech(final String testField, final String language) {
        if(inProgress) {
            if (!mTts.isSpeaking()) {
                inProgress = false;
            }
        }
        if (!inProgress) {
            inProgress = true;
            String message = testField;
            if (message.trim().length() == 0) {
                message = "Testing Text to Speech Engine";
            }
            speakOut(message, language);
        } else {
            inProgress = false;
            stopSpeaking();
        }
    }

    public void speakText(String message, String language) {
        Utils.showMessage(context, message);
        if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
            speakOut(message, language);
        }
    }

    public void stopSpeaking() {
        if (mTts != null) {
            if (mTts.isSpeaking()) {
                mTts.stop();
            }
        }
    }

    private void speakOut(String message, String language) {
        decideLanguage(language);
        this.message = message;
        this.language = language;
        if (initStatus == TextToSpeech.SUCCESS) {
            mTts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            Utils.showMessage(context, "Couldn't Speak! - S: " + initStatus);
        }
    }

    private void decideLanguage(String language) {
        try {
            mTts.setLanguage(Locale.forLanguageTag(language));
        } catch (Exception e) {
            mTts.setLanguage(Locale.ENGLISH);
        }
    }
}