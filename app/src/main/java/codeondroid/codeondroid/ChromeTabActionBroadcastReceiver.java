package codeondroid.codeondroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ChromeTabActionBroadcastReceiver extends BroadcastReceiver {
    public static final int ACTION_ACTION_BUTTON = 3;
    public static final String KEY_ACTION_SOURCE = "org.chromium.customtabsdemos.ACTION_SOURCE";
    @Override
    public void onReceive(Context context, Intent intent) {
        String data = intent.getDataString();

        if (data != null) {
            String toastText = getToastText(context, intent.getIntExtra(KEY_ACTION_SOURCE, -1), data);
            Intent i = new Intent(context,EditorActivity.class);
            i.putExtra("fromwebview",true);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            //Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
        }
    }
    private String getToastText(Context context, int actionSource, String message) {
        switch (actionSource) {
            case ACTION_ACTION_BUTTON:
                return "hello";
            default:
                return " not from button";
        }
    }
}
