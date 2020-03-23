package com.example.codeondroid;

import androidx.appcompat.app.AppCompatActivity;

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

public class Walkthrough extends AppCompatActivity {

    TextView textLIGHT_available, textLIGHT_reading;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightEventListener;
    private View root;
    private float maxValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);
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
//
    };





}
