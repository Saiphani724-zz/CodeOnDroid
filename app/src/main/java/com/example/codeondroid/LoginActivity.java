package com.example.codeondroid;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
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
    int someError=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_left);



        b = (Button)findViewById(R.id.signin);
        prgs = (ProgressBar) findViewById(R.id.prgsBarLogin);

        TextView frgt = (TextView) findViewById(R.id.forgotpassword);

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
                        }
                    }
                });
            }
        });

        frgt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
                alertDialog.setTitle("Reset password");
                alertDialog.setMessage("Are you sure you want reset your account password?");
                final EditText input2 = new EditText(getApplicationContext());
                input2.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                input2.setHint("Enter registered Email ID");
                alertDialog.setView(input2);
                alertDialog.setIcon(R.drawable.sendemail);
                alertDialog.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(TextUtils.isEmpty(input2.getText().toString()))
                        {
                            Toast.makeText(LoginActivity.this,"Email-Id not Entered",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            prgs.setVisibility(View.VISIBLE);
                            FirebaseAuth.getInstance().sendPasswordResetEmail(input2.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                prgs.setVisibility(View.INVISIBLE);
                                                Toast.makeText(LoginActivity.this,"Password RESET initiated\nCheck your email for further instructions",Toast.LENGTH_LONG).show();
                                                AlertDialog.Builder next = new AlertDialog.Builder(LoginActivity.this);
                                                next.setTitle("Pasword RESET");
                                                next.setMessage("Password RESET initiated\nCheck your email for further instructions");
                                                next.setIcon(R.drawable.resetcomplete);
                                                // Setting Netural "Cancel" Button
                                                next.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // User pressed Cancel button. Write Logic Here
                                                        Toast.makeText(getApplicationContext(), "Check Registered Email!",
                                                                Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                                next.show();
                                            }
                                        }
                                    });
                        }
                    }

                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        Toast.makeText(LoginActivity.this, "Try Logging in again :)", Toast.LENGTH_SHORT).show();
                        // dialog.cancel();
                    }
                });
                alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User pressed Cancel button. Write Logic Here
                        Toast.makeText(LoginActivity.this, "Cancelled",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.show();
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


