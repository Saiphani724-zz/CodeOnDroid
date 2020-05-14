package codeondroid.codeondroid;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class ProfilePage extends AppCompatActivity {

    ImageView img;
    Button but1,but2,butmap;
    Bitmap bitmap = null;
    StorageReference refstore;
    public Uri imguri;
    DatabaseReference reff;
    ProgressBar pbar;
    private View ambient;
    private SensorManager sensorManager;
    TextView lt;
    private Sensor lightSensor;
    private SensorEventListener lightEventListener;
    private View root;
    private float maxValue;
    public static final int GET_FROM_GALLERY = 3;
    TextView showUsername, showEmail , showFavLang;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        this.overridePendingTransition(R.anim.zoomin,
                R.anim.zoomout);
        ambient = (View)findViewById(R.id.ambient);
        lt = (TextView)findViewById(R.id.lumnsty);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lightSensor == null) {
            Toast.makeText(this, "The device has no light sensor !", Toast.LENGTH_SHORT).show();

        }
        else {
            maxValue = lightSensor.getMaximumRange() / 10;
        }
        reff = FirebaseDatabase.getInstance().getReference().child("Users");
        img = (ImageView) findViewById(R.id.icon);
        but1 = (Button) findViewById(R.id.butProfile);
        but2 = (Button) findViewById(R.id.butUpload);
        butmap = (Button) findViewById(R.id.mapsButton);
        pbar = (ProgressBar)findViewById(R.id.pbar);
        but2.setEnabled(false);
        showUsername = findViewById(R.id.showUsername);
        showEmail = findViewById(R.id.showEmail);
        showFavLang = findViewById(R.id.showFavLang);
        final FirebaseAuth fAuth = FirebaseAuth.getInstance();
        but1.setEnabled(false);
        SharedPreferences sf=getSharedPreferences("myfile", Context.MODE_PRIVATE);
        showUsername.setText(sf.getString("uname","NA"));
        showFavLang.setText(sf.getString("favLang","NA"));
        showEmail.setText(sf.getString("email","NA"));
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfilePage.this,"Profile Photo",Toast.LENGTH_SHORT).show();
            }
        });
        pbar.setVisibility(View.VISIBLE);
        Toast.makeText(ProfilePage.this,"Retreiving Profile Photo\nfrom Database",Toast.LENGTH_LONG).show();
        refstore = FirebaseStorage.getInstance().getReference("Images");
        final String uid = fAuth.getCurrentUser().getUid();
        final File file;
        lightEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
//                Toast.makeText(getApplicationContext(),"onSensorChanged",Toast.LENGTH_SHORT).show();
                float value = event.values[0];
                float finvalue=value;
                if(finvalue>650.0){
                    finvalue= (float) 650.0;
                }
                lt.setText("Luminosity : " + value + " lx");
                int newValue = (int) (255f * finvalue / maxValue);
                ambient.setBackgroundColor(Color.rgb(newValue, newValue, newValue));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
//                Toast.makeText(getApplicationContext(),"onAccuracyChanged",Toast.LENGTH_SHORT).show();
            }
        };
        try {
            file = File.createTempFile("image",".jpg");//
            StorageReference myProfilePhoto = FirebaseStorage.getInstance().getReferenceFromUrl("gs://codeondroid.appspot.com/Images").child(uid+".jpg");

            myProfilePhoto.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    pbar.setVisibility(View.INVISIBLE);
                    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    img.setImageBitmap(myBitmap);
                    but1.setEnabled(true);
                    Toast.makeText(ProfilePage.this,"Profile Image\nFound in Database",Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pbar.setVisibility(View.INVISIBLE);
                    but1.setEnabled(true);
                    Toast.makeText(ProfilePage.this,"No Profile photo uploaded",Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            Toast.makeText(ProfilePage.this,"Failed to load Profile Photo.\nCheck Internet Connection!",Toast.LENGTH_SHORT).show();
            but1.setEnabled(true);
            e.printStackTrace();
        }

//        but1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
//            }
//        });


        /*butmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePage.this,AppMap.class);
                startActivity(i);
            }
        });*/





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
                else{
                    pbar.setVisibility(View.VISIBLE);
                    uploadFile(uid);
                }

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
        final Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        AlertDialog.Builder next3 = new AlertDialog.Builder(ProfilePage.this);
                next3.setTitle("FileSelect");
                next3.setMessage("      Preferably select a landscape photo.\n\n\nSince the image is uploaded to Firebase (Firestore) as a bitmap, the orientation will change if the image is taken in Portrait mode." +
                        "\n\nHowever a portrait photo will also work normally.");
                next3.setIcon(R.drawable.landscapelock);
                next3.setNeutralButton("Will Try!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        startActivityForResult(intent,1);
                    }
                });
        next3.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK&&data!=null && data.getData()!=null)
        {
            imguri = data.getData();
            img.setImageURI(imguri);
            but2.setEnabled(true);
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

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(lightEventListener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(lightEventListener);
    }








}
