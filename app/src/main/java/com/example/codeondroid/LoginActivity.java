package com.example.codeondroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView signUp_text = findViewById(R.id.signUp_text);
        db = openOrCreateDatabase("CodeEditorDB", MODE_PRIVATE, null);
        signUp_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                finish();
            }
        });

        TextView apname=(TextView)findViewById(R.id.openAbout);

        apname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),About.class);
                startActivity(i);
            }
        });


    }
    public void verify(View v ){
        EditText uname = findViewById(R.id.uname);
        EditText pword = findViewById(R.id.pword);


        Cursor c = db.rawQuery(String.format("SELECT * FROM users WHERE username = '%s' and password='%s' ",uname.getText(),pword.getText()),null);


        if(c.getCount() == 1)
        {
            StringBuffer buffer = new StringBuffer();
            while (c.moveToNext())
            {
                String favLang = c.getString(3);
                SharedPreferences sf=getSharedPreferences("myfile", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit=sf.edit();
                edit.clear(); // remove existing entries
                edit.putString("favLang",favLang);
                edit.putString("uname" , c.getString(0) );
                edit.putString("email" , c.getString(2) );
                edit.commit();
            }
            Intent i = new Intent(getApplicationContext(), Navigationclass.class);
            startActivity(i);
//            Toast.makeText(LoginActivity.this,"Login Successfull",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(LoginActivity.this,"Enter a valid username and password",Toast.LENGTH_SHORT).show();
        }
    }
}


