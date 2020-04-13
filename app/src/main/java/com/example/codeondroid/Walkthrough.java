package com.example.codeondroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.Toast;

public class Walkthrough extends AppCompatActivity implements Recycleviewcommunicator {

    TextView textLIGHT_available, textLIGHT_reading;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightEventListener;
    private View root;
    private float maxValue;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);
        this.overridePendingTransition(R.anim.zoomin,
                R.anim.zoomout);
//
//        root = findViewById(R.id.root);
//        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
//
//        if (lightSensor == null) {
//            Toast.makeText(this, "The device has no light sensor !", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//
//        // max value for light sensor
//        maxValue = lightSensor.getMaximumRange();
//
//        lightEventListener = new SensorEventListener() {
//            @Override
//            public void onSensorChanged(SensorEvent sensorEvent) {
//                float value = sensorEvent.values[0];
//                getSupportActionBar().setTitle("Luminosity : " + value + " lx");
//
//                // between 0 and 255
//                int newValue = (int) (255f * value / maxValue);
//                root.setBackgroundColor(Color.rgb(newValue, newValue, newValue));
//            }
//
//            @Override
//            public void onAccuracyChanged(Sensor sensor, int i) {
//
//            }
//        };

        String [] features = {
                "Programming specific keyboard",
                "Users Custom keys",
                "Code Snipets",
                "Easy switch between browser and editor",
                "Undo and redo",
                "Google maps api"
        };


        mRecyclerView =  findViewById(R.id.features);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FeaturesCardViewAdapter(features , this);
        mRecyclerView.setAdapter(mAdapter);

        String [] uicomponents ={
                "Activity transition animations",
                "Dark/ light mode",
                "Firebase authentication",
                "Forgot password email reset",
                "Persistent error display in edit text",
                "Firebase realtime database",
                "Custom tabs",
                "Button disable until download progress completed",
                "Firebase bucket storage for profile photo with realtime database mapping",
                "Device file explorer access, Android temp file creation and upload",
                "Intro Splash screen",
                "Authentication key maintenance for one-time login",
                "Top menu bug report Gmail intent",
                "Ambient light sensor (suggest dark/light mode)"
        };


        mRecyclerView =  findViewById(R.id.ui);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FeaturesCardViewAdapter(uicomponents , this);
        mRecyclerView.setAdapter(mAdapter);




    };

    @Override
    public void load_files() {

    }

    @Override
    public void share_files(String fname) {

    }


}
