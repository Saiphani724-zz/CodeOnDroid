package com.example.codeondroid;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

//    SQLiteDatabase db;
    Button b;
    ProgressBar prgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




        b = (Button)findViewById(R.id.signin);
        prgs = (ProgressBar) findViewById(R.id.prgsBarLogin);

        TextView signUp_text = findViewById(R.id.signUp_text);
//        db = openOrCreateDatabase("CodeEditorDB", MODE_PRIVATE, null);
        signUp_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                finish();
            }
        });
        final FirebaseAuth fAuth = FirebaseAuth.getInstance();

        //User already registered
        if(fAuth.getCurrentUser()!=null)
        {
            // make the channel. The method has been discussed before.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                makeNotificationChannel("CHANNEL_1", "Example channel", NotificationManager.IMPORTANCE_HIGH);
            }

            NotificationCompat.Builder notification =
                    new NotificationCompat.Builder(getApplicationContext(), "CHANNEL_1");

            notification
                    .setSmallIcon(R.drawable.ic_settings_ethernet_black_24dp)
                    .setContentTitle("Logged In!")
                    .setContentText("Welcome! User is aldready logged in.")
                    .setNumber(3); // this shows a number in the notification dots

            NotificationManager notificationManager =
                    (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

            assert notificationManager != null;
            notificationManager.notify(1, notification.build());
            startActivity(new Intent(getApplicationContext(), Navigationclass.class));
            finish();
        }

        TextView apname=(TextView)findViewById(R.id.openAbout);

        apname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),About.class);
                startActivity(i);
            }
        });

        final EditText emailET = findViewById(R.id.email);
        final EditText pwordET = findViewById(R.id.pword);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailET.getText().toString().trim();
                String pword = pwordET.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    emailET.setError("Enter Registered Email Id");
                    return;
                }
                if(TextUtils.isEmpty(pword))
                {
                    pwordET.setError("Enter Password");
                    return;
                }
                if(pword.length()<6)
                {
                    pwordET.setError("Firebase authentication\n requires password of length greater than 6");
                    return;
                }
                prgs.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(email,pword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            prgs.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(),"Valid Credentials",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Navigationclass.class));
                            finish();
                        }
                        else
                        {
                            prgs.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(),"Error!\n"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                });
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    void makeNotificationChannel(String id, String name, int importance)
    {

        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setShowBadge(true); // set false to disable badges, Oreo exclusive

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }




//    public void verify(View v ){
//        EditText uname = findViewById(R.id.email);
//        EditText pword = findViewById(R.id.pword);
//
//
//        Cursor c = db.rawQuery(String.format("SELECT * FROM users WHERE username = '%s' and password='%s' ",uname.getText(),pword.getText()),null);
//
//
//        if(c.getCount() == 1)
//        {
//            StringBuffer buffer = new StringBuffer();
//            while (c.moveToNext())
//            {
//                String favLang = c.getString(3);
//                SharedPreferences sf=getSharedPreferences("myfile", Context.MODE_PRIVATE);
//                SharedPreferences.Editor edit=sf.edit();
//                edit.clear(); // remove existing entries
//                edit.putString("favLang",favLang);
//                edit.putString("uname" , c.getString(0) );
//                edit.putString("email" , c.getString(2) );
//                edit.commit();
//            }
//            Intent i = new Intent(getApplicationContext(), Navigationclass.class);
//            startActivity(i);
////            Toast.makeText(LoginActivity.this,"Login Successfull",Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            Toast.makeText(LoginActivity.this,"Enter a valid username and password",Toast.LENGTH_SHORT).show();
//        }
//    }



}


