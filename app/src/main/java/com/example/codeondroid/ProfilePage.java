package com.example.codeondroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

public class ProfilePage extends AppCompatActivity {

    ImageView img;
    Button but1,but2,butmap;
    Bitmap bitmap = null;
    StorageReference refstore;
    public Uri imguri;
    DatabaseReference reff;
    ProgressBar pbar;
    public static final int GET_FROM_GALLERY = 3;
    TextView showUsername, showEmail , showFavLang;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        this.overridePendingTransition(R.anim.zoomin,
                R.anim.zoomout);
        reff = FirebaseDatabase.getInstance().getReference().child("Users");
        img = (ImageView) findViewById(R.id.icon);
        but1 = (Button) findViewById(R.id.butProfile);
        but2 = (Button) findViewById(R.id.butUpload);
        butmap = (Button) findViewById(R.id.mapsButton);
        pbar = (ProgressBar)findViewById(R.id.pbar);

        showUsername = findViewById(R.id.showUsername);
        showEmail = findViewById(R.id.showEmail);
        showFavLang = findViewById(R.id.showFavLang);
        final FirebaseAuth fAuth = FirebaseAuth.getInstance();

        SharedPreferences sf=getSharedPreferences("myfile", Context.MODE_PRIVATE);
        showUsername.setText(sf.getString("uname","NA"));
        showFavLang.setText(sf.getString("favLang","NA"));
        showEmail.setText(sf.getString("email","NA"));
        final String uid = fAuth.getCurrentUser().getUid();

//        but1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
//            }
//        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfilePage.this,"Profile Photo",Toast.LENGTH_SHORT).show();
            }
        });
        refstore = FirebaseStorage.getInstance().getReference("Images");
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

        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uploadTask != null && uploadTask.isInProgress())
                {
                    AlertDialog.Builder next2 = new AlertDialog.Builder(ProfilePage.this);
                    next2.setTitle("Upload In Progress");
                    next2.setMessage("Be Patient\nElse\nGet a better internet connection");
                    next2.setIcon(R.drawable.uploadprogress);
                    next2.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "Ok!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

                pbar.setVisibility(View.VISIBLE);
                uploadFile(uid);
            }
        });

    }

    private String getExtension(Uri uri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile(String uid) {
        StorageReference Ref = refstore.child(uid+"."+getExtension(imguri));
        uploadTask = Ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        pbar.setVisibility(View.INVISIBLE);
                        AlertDialog.Builder next = new AlertDialog.Builder(ProfilePage.this);
                        next.setTitle("Profile Image");
                        next.setMessage("The profile image has been uploaded to database");
                        next.setIcon(R.drawable.resetcomplete);
                        next.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Success!",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                        next.show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });

    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK&&data!=null && data.getData()!=null)
        {
            imguri = data.getData();
            img.setImageURI(imguri);
        }
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//        //Detects request codes
//        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
//            Uri selectedImage = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
//                img.setImageBitmap(bitmap);
//            } catch (FileNotFoundException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    }

//    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
//        return outputStream.toByteArray();//CHANGE FOR IMAGE IN DB
//    }










}
