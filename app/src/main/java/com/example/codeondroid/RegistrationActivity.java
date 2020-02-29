package com.example.codeondroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codeondroid.LoginActivity;

import java.sql.SQLData;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "";
    Button submitB;
    EditText usernameET,passwordET,emailET;
    Spinner favLangS;
    String favLang = "";
    SQLiteDatabase db;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        usernameET = findViewById(R.id.username);
        passwordET = findViewById(R.id.password);
        emailET = findViewById(R.id.email);
        submitB = findViewById(R.id.submit);


        favLangS = findViewById(R.id.favLang);
        ArrayAdapter adap1=ArrayAdapter.createFromResource(this,R.array.Favourite_Lang,android.R.layout.simple_list_item_activated_1);
        favLangS.setAdapter(adap1);
        favLangS.setOnItemSelectedListener(this);

        submitB.setOnClickListener(this);

        db = openOrCreateDatabase("CodeEditorDB",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS users(username VARCHAR,password VARCHAR,email VARCHAR,favLang VARCHAR);");
    }

    @Override
    public void onClick(View v) {
        String username = usernameET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        String email = emailET.getText().toString().trim();

//        Toast.makeText(getApplicationContext(),username + " " + password,Toast.LENGTH_LONG).show();

        if(username.equals("") || password.equals("") || email.equals("") || favLang.equals("") || favLang.equals("Choose Your Favourite Language")){
            Toast.makeText(getApplicationContext(),"Enter All Fields",Toast.LENGTH_LONG).show();
            return;
        }
        Cursor c = db.rawQuery("SELECT * FROM users WHERE username ='" + username + "'", null);
//
        if(c.getCount() != 0){
            Toast.makeText(getApplicationContext(),"Username already exits",Toast.LENGTH_LONG).show();
            return;
        }
        db.execSQL("INSERT INTO users VALUES('" + username+ "','" + password + "','" + email  + "','" + favLang + "');");
        String output = "";
        c = db.rawQuery("SELECT * FROM users", null);
//        while (c.moveToNext())
//        {
//            output += "Username: " + c.getString(0) + "\n";
//            output += "Email: " + c.getString(2) + "\n";
//        }
//        showMessage("Student Details", output);

        Toast.makeText(getApplicationContext(),"Successfully Registered",Toast.LENGTH_LONG).show();
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(getApplicationContext(),"Clicked " ,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView txt = (TextView) view;
        favLang = txt.getText().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void goBack(View v){
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
    }

}



