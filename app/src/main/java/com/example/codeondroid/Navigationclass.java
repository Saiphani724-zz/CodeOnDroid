
package com.example.codeondroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class Navigationclass extends AppCompatActivity {

    TextView apname;

    Button btnCC,btnCF,btnHR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigationclass);

        apname=(TextView)findViewById(R.id.openAbout);
        btnCC=(Button)findViewById(R.id.btnCC);
        btnCF=(Button)findViewById(R.id.btnCF);
        btnHR=(Button)findViewById(R.id.btnHR);

        apname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Navigationclass.this,About.class);
                startActivity(i);
            }
        });


        btnCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent l = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.codechef.com/"));
//                startActivity(l);

                SharedPreferences sf=getSharedPreferences("myfile", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit=sf.edit();
                edit.clear(); // remove existing entries
                edit.putString("url","https://www.codechef.com/");

                edit.commit();
                Intent i=new Intent(getApplicationContext(),webview.class);
                startActivity(i);
            }
        });

        btnHR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent m = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.hackerrank.com/?utm_expid=.2u09ecQTSny1HV02SEVoCg.0&utm_referrer="));
//                startActivity(m);

                SharedPreferences sf=getSharedPreferences("myfile", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit=sf.edit();
                edit.clear(); // remove existing entries
                edit.putString("url","https://www.hackerrank.com/?utm_expid=.2u09ecQTSny1HV02SEVoCg.0&utm_referrer=");

                edit.commit();
                Intent i=new Intent(getApplicationContext(),webview.class);
                startActivity(i);
            }
        });

        btnCF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent n = new Intent(Intent.ACTION_VIEW, Uri.parse("https://codeforces.com/"));
//                startActivity(n);
                SharedPreferences sf=getSharedPreferences("myfile", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit=sf.edit();
                edit.clear(); // remove existing entries
                edit.putString("url","https://www.codeforces.com/");

                edit.commit();
                Intent i=new Intent(getApplicationContext(),webview.class);
                startActivity(i);
            }
        });


    }

    public void openEditor(View v)
    {
        startActivity(new Intent(Navigationclass.this,EditorActivity.class));
        Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_LONG).show();
    }

//    public void getFiles(View v)
//    {
//        String path = Environment.getExternalStorageDirectory().toString() + "/Codes";
//
//        Toast.makeText(getApplicationContext(), path, Toast.LENGTH_LONG).show();
//        File directory = new File(path);
//        File[] files = directory.listFiles();
//        Log.d("Files", "Size: "+ files + " " +directory);
//
////        for (int i = 0; i < files.length; i++)
////        {
////            Log.d("Files", "FileName:" + files[i].getName());
//////            Toast.makeText(getApplicationContext(),"FileName:" + files[i].getName(),Toast.LENGTH_LONG).show();
////        }
////        Toast.makeText(getApplicationContext(),path,Toast.LENGTH_LONG).show();
//
//    }


}
