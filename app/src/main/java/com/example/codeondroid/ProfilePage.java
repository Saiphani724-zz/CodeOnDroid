package com.example.codeondroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ProfilePage extends AppCompatActivity {

    ImageView img;
    Button but1,butmap;
    Bitmap bitmap = null;
    public static final int GET_FROM_GALLERY = 3;
    TextView showUsername, showEmail , showFavLang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        this.overridePendingTransition(R.anim.zoomin,
                R.anim.zoomout);
        img = (ImageView) findViewById(R.id.icon);
        but1 = (Button) findViewById(R.id.butProfile);
        butmap = (Button) findViewById(R.id.mapsButton);

        showUsername = findViewById(R.id.showUsername);
        showEmail = findViewById(R.id.showEmail);
        showFavLang = findViewById(R.id.showFavLang);

        SharedPreferences sf=getSharedPreferences("myfile", Context.MODE_PRIVATE);
        showUsername.setText(sf.getString("uname","NA"));
        showFavLang.setText(sf.getString("favLang","NA"));
        showEmail.setText(sf.getString("email","NA"));


        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfilePage.this,"Profile Photo",Toast.LENGTH_SHORT).show();
            }
        });

        butmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(ProfilePage.this,AppMap.class);
//                startActivity(i);
                Uri uri = Uri.parse("geo:0,0?q=10.90455131 , 76.8986295 (Google+ab2)");
                Intent in = new Intent(Intent.ACTION_VIEW, uri);
                in.setPackage("com.google.android.apps.maps");
                startActivity(in);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                img.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();//CHANGE FOR IMAGE IN DB
    }



}
