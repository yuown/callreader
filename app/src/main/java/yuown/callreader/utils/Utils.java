package yuown.callreader.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    private static final String fullName = "CallReader.log";
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static File file = null;
    private static FileWriter fileWriter = null;
    private static BufferedReader fileReader = null;

    public static void showMessage(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static boolean isCallActive(Context context){
        AudioManager manager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        return manager.getMode() == AudioManager.MODE_IN_CALL;
    }

    public static String readContact(Context context, String phoneNumber) {
        ContentResolver contentResolver = context.getContentResolver();
        String contactName = null;
        Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = contentResolver.query(CONTENT_URI, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            break;
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return contactName;
    }

    public static void writeLog(String line) {
        if(null == file) {
            file = new File(Environment.getExternalStorageDirectory(), fullName);
        }
        if(null == fileWriter) {
            try {
                fileWriter = new FileWriter(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        line = sdf.format(new Date()) + " - " + line;
        try {
            fileWriter.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readLogs() {
        if(null == file) {
            file = new File(Environment.getExternalStorageDirectory(), fullName);
        }
        if(null == fileReader) {
            try {
                fileReader = new BufferedReader(new FileReader(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        String line = "";
        try {
            while((line = fileReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
                stringBuilder.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static void clearLogs() {
        if(null == file) {
            file = new File(Environment.getExternalStorageDirectory(), fullName);
        }
        try {
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.write("");
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}