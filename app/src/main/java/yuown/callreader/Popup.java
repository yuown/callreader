package yuown.callreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by kirannk on 08/10/16.
 */
public class Popup extends Activity {

    public static Popup speechView;

    private String language;

    private String message;

    private TextSpeaker textSpeaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        setContentView(R.layout.activity_popup);
//        moveTaskToBack(true);

        speechView = this;
        Intent startingIntent = this.getIntent();
        language = startingIntent.getStringExtra("language");
        message = startingIntent.getStringExtra("message");

        textSpeaker = TextSpeaker.getInstance(getApplicationContext());
        textSpeaker.setActivity(this);
        textSpeaker.speakText(message, language);
    }

    public void stopSpeaking() {
        Log.d("mLog", "Stopped");
        textSpeaker.stopSpeaking();
        this.finish();
    }

    @Override
    public void onDestroy() {
        textSpeaker.stopSpeaking();
        super.onDestroy();
    }
}