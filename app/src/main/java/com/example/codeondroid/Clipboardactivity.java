package com.example.codeondroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class Clipboardactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clipboardactivity);
        ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        String text = "";
        text = getClipboardDataForHoney(getApplicationContext());
        TextView clipoutput = findViewById(R.id.cliptext);
        clipoutput.setText(text);
    }
    private static String getClipboardDataForHoney(Context mContext) {
        ClipboardManager clipboard = (ClipboardManager) mContext
                .getSystemService(Context.CLIPBOARD_SERVICE);//get Clipboard manager
        ClipData abc = clipboard.getPrimaryClip();//Get Primary clip
        Toast.makeText(mContext,abc.getItemCount()+"",Toast.LENGTH_SHORT).show();
        ClipData.Item item = abc.getItemAt(0);//Get item from clip data

        return item.getText().toString();
    }

}
