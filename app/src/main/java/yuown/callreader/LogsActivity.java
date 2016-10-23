package yuown.callreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import yuown.callreader.utils.Utils;

public class LogsActivity extends AppCompatActivity {

    private TextView logsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        logsText = (TextView) findViewById(R.id.logsText);
        logsText.setMovementMethod(new ScrollingMovementMethod());
        logsText.setText(Utils.readLogs());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.clear_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clearLogs:
                Utils.clearLogs();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
