package com.example.codeondroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class SaveCodeInFile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_code_in_file);



        try {
            File file = new File(SaveCodeInFile.this.getFilesDir(), "sample1.txt");
            FileWriter writer = new FileWriter(file);
            writer.append("Hello");
            writer.flush();
            writer.close();
//            Toast.makeText(SavecodeActivity.this, Environment.getExternalStorageState()  + "Saved your text", Toast.LENGTH_LONG).show();
        } catch (Exception e) { }



        Log.d("TAG", "saveCode: " + SaveCodeInFile.this.getFilesDir() + "/" + "sample1.txt");

        File f = new File("" + getApplicationContext().getFilesDir());

        String[] files = f.list();

        for (int i = 0; i < files.length; i++) {
            Log.d("TAG", "Files List: " + files[i]);
        }

        try {
            String yourFilePath = getApplicationContext().getFilesDir() + "/" + "sample1.txt";
            FileInputStream fin=new FileInputStream(yourFilePath);
            InputStreamReader isr = new InputStreamReader(fin);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line = "";
            while (true) {
                try {
                    if (!((line = bufferedReader.readLine()) != null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sb.append(line + "\n");
            }
            Log.d("TAG", "saveCode: " + sb.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



    }
}
