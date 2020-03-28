package com.example.codeondroid;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codeondroid.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLData;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "";
    Button submitB;
    EditText usernameET,passwordET,emailET;
    Spinner favLangS;
    String favLang = "";

    DatabaseReference reff;
    long  uno = 0;
//    SQLiteDatabase db;
    Users user;
    ProgressBar prgsBar;
    FirebaseAuth fAuth;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        this.overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);

        usernameET = findViewById(R.id.username);
        passwordET = findViewById(R.id.password);
        emailET = findViewById(R.id.email);
        submitB = findViewById(R.id.submit);

        user = new Users();

        prgsBar = (ProgressBar)findViewById(R.id.prgsBar);

        reff = FirebaseDatabase.getInstance().getReference().child("Users");
        fAuth = FirebaseAuth.getInstance();



//        reff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists())
//                {
//                    uno = dataSnapshot.getChildrenCount();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


        favLangS = findViewById(R.id.favLang);
        ArrayAdapter adap1=ArrayAdapter.createFromResource(this,R.array.Favourite_Lang,android.R.layout.simple_list_item_activated_1);
        favLangS.setAdapter(adap1);
        favLangS.setOnItemSelectedListener(this);

        submitB.setOnClickListener(this);

//        db = openOrCreateDatabase("CodeEditorDB",MODE_PRIVATE,null);
//        db.execSQL("CREATE TABLE IF NOT EXISTS users(username VARCHAR,password VARCHAR,email VARCHAR,favLang VARCHAR);");
    }








    @Override
    public void onClick(View v) {
        final String username = usernameET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        String email = emailET.getText().toString().trim();




        if(TextUtils.isEmpty(username)&&TextUtils.isEmpty(password)&&TextUtils.isEmpty(favLang))
        {
            Toast.makeText(getApplicationContext(),"All fields are empty",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(username))
        {
            usernameET.setError("Username Not Entered");
            return;
        }

        if(TextUtils.isEmpty(email))
        {
            emailET.setError("EmailID Not Entered");
            return;
        }

        if(TextUtils.isEmpty(password))
        {
            passwordET.setError("Password Not Entered");
            return;
        }

        if(favLang.equals("") || favLang.equals("Choose Your Favourite Language"))
        {
            Toast.makeText(getApplicationContext(),"Favorite Language not selected",Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length() < 6)
        {
            passwordET.setError("Firebase authentication\n requires password of length greater than 6");
            return;
        }

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setFavlang(favLang);

        prgsBar.setVisibility(View.VISIBLE);

        //Authorising user

        fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    prgsBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Successfully Registered",Toast.LENGTH_LONG).show();
                    String userId =fAuth.getCurrentUser().getUid();
                    reff.child(userId).setValue(user);
                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                }
                else {
                    prgsBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Error!\n"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });

//        String userid = fAuth.getCurrentUser().getUid();

        // FireBase pushing the values into firebase










//        if(username.equals("") || password.equals("") || email.equals("") || favLang.equals("") || favLang.equals("Choose Your Favourite Language")){
//            Toast.makeText(getApplicationContext(),"Enter All Fields",Toast.LENGTH_LONG).show();
//            return;
//        }




//        Cursor c = db.rawQuery("SELECT * FROM users WHERE username ='" + username + "'", null);
//        if(c.getCount() != 0){
//            Toast.makeText(getApplicationContext(),"Username already exits",Toast.LENGTH_LONG).show();
//            return;
//        }
//        db.execSQL("INSERT INTO users VALUES('" + username+ "','" + password + "','" + email  + "','" + favLang + "');");
//        String output = "";
//        c = db.rawQuery("SELECT * FROM users", null);
//        while (c.moveToNext())
//        {
//            output += "Username: " + c.getString(0) + "\n";
//            output += "Email: " + c.getString(2) + "\n";
//        }
//        showMessage("Student Details", output);







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



