package com.example.codeondroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.codeondroid.webview;

public class EditorActivity extends AppCompatActivity {


    final int MYREQUEST = 11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level * 100 / (float)scale;

        if(batteryPct < 25){
            Toast.makeText(getApplicationContext(), "BAttery's dying!!\nSave Your Code\n", Toast.LENGTH_LONG).show();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String str = data.getStringExtra("msg");
        EditText codebox = findViewById(R.id.codebox);

        codebox.setText(codebox.getText().toString()+'\n'+str);

    }

    public void access_clip(View view) {
//        Intent i;
//        i = new Intent(this, Clip_Activity.class);
//        startActivityForResult(i,MYREQUEST);

    }

    public void openweb(View view) {
        SharedPreferences sf=getSharedPreferences("myfile", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=sf.edit();
        edit.clear(); // remove existing entries
        edit.putString("url","https://sphere-engine.com/demo/1-online-compiler");

        edit.commit();
        Intent i=new Intent(this, webview.class);
        startActivity(i);

    }

}
