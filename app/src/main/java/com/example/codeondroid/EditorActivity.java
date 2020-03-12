package com.example.codeondroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;



public class EditorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    CustomKeyboard mCustomKeyboard;
    float x1,x2;
    float y1, y2;
    final int MYREQUEST = 11;

    EditText codebox, inputbox;
    String code;
    TextView outputbox;
    Spinner Lang;

    String langs[] = {"Cpp14", "C", "Java", "Python3"};
    int langPos = 1;
    int MIN_DISTANCE = 150;
    EditText filename;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


//        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//        Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);
        mCustomKeyboard = new CustomKeyboard(this, R.id.keyboardview, R.xml.keyboard);
        mCustomKeyboard.registerEditText(R.id.codebox);
//        mCustomKeyboard.registerEditText(R.id.outputbox);
        mCustomKeyboard.registerEditText(R.id.inputbox);

        filename = findViewById(R.id.filename);
        codebox = findViewById(R.id.codebox);
        inputbox = findViewById(R.id.inputbox);
        outputbox = findViewById(R.id.outputbox);

        Lang = findViewById(R.id.Lang);
        ArrayAdapter<String> adap1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, langs);

        Lang.setAdapter(adap1);
        Lang.setOnItemSelectedListener(this);

        SharedPreferences sf=getSharedPreferences("myfile", Context.MODE_PRIVATE);
        String lang = sf.getString("favLang","NA");
        for(int i = 0;i < langs.length; i++)
        {
            if(langs[i].equals((lang)))
            {
                langPos = i;
            }
        }

        int spinnerPosition = adap1.getPosition(lang);
        Lang.setSelection(spinnerPosition);
//        Toast.makeText(getApplicationContext(),langPos + "\t" + lang, Toast.LENGTH_LONG).show();
//
//        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
//        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
//
//        float batteryPct = level * 100 / (float)scale;
//
//        if(batteryPct < 25){
//            Toast.makeText(getApplicationContext(), "BAttery's dying!!\nSave Your Code\n", Toast.LENGTH_LONG).show();
//        }

        SharedPreferences sf1=getSharedPreferences("myfile1", Context.MODE_PRIVATE);
        String openfilename = sf1.getString("filename","NA");
        if(!openfilename.equals("NA")){
            Log.d("TAG", "onCreate: " + openfilename);


            try {
                String yourFilePath = getApplicationContext().getFilesDir() + "/" + openfilename;
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
                Log.d("TAG", "onCreate: " + sb.toString()  + "\n") ;
                filename.setText(openfilename);
                codebox.setText(sb.toString());


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        else{
            Log.d("TAG", "onCreate: " + "Emptyyyyyyyyyyyyyyyyyyyyyyyy");
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String str = data.getStringExtra("msg");
        EditText codebox = findViewById(R.id.codebox);

        codebox.setText(codebox.getText().toString()+'\n'+str);

    }

    public void access_clip(View view) {
//        Intent i;
//        i = new Intent(this, Clip_Activity.class);
//        startActivityForResult(i,MYREQUEST);

    }

    public void compileCode(View view) {

        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest sr = new StringRequest(Request.Method.POST,"https://ide.geeksforgeeks.org/main.php/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Volley", "onResponse: " + response);
                try {
                    JSONObject res = new JSONObject(response);
                    String output = res.getString("output");
                    outputbox.setText(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                queue.getCache().clear();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley", "onErrorResponse: " + error);
            }
        }){
            @Override
            protected Map<String,String> getParams(){

                Log.d("code", "getParams: " + code);
                Map<String,String> params = new HashMap<String, String>();
                params.put("lang" , langs[langPos]);
                params.put("code" , codebox.getText().toString());
                params.put("input" , inputbox.getText().toString());
                params.put("save" , "false");
//                Toast.makeText(getApplicationContext(),langPos + "", Toast.LENGTH_LONG).show();

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }

    @Override public void onBackPressed() {
        // NOTE Trap the back key: when the CustomKeyboard is still visible hide it, only when it is invisible, finish activity
        if( mCustomKeyboard.isCustomKeyboardVisible() ) mCustomKeyboard.hideCustomKeyboard(); else this.finish();
    }
    @Override
    public boolean onTouchEvent(MotionEvent touchevent)
    {
        switch (touchevent.getAction())
        {
            // when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN:
            {
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                x2 = touchevent.getX();
                y2 = touchevent.getY();
                View focusCurrent = getWindow().getCurrentFocus();
                if (x2-x1>=MIN_DISTANCE)
                {
                    //Toast.makeText(this, "Left to Right Swap Performed", Toast.LENGTH_LONG).show();
                    if (mCustomKeyboard.curr_layout>0)
                    {
                        mCustomKeyboard.curr_layout-=1;
                        mCustomKeyboard.change_keyboard( mCustomKeyboard.keylayouts[mCustomKeyboard.curr_layout]);
                    }
                }

                // if right to left sweep event on screen
                if (x1-x2>=MIN_DISTANCE)
                {
                    //Toast.makeText(this, "Right to Left Swap Performed", Toast.LENGTH_LONG).show();
                    if (mCustomKeyboard.curr_layout< mCustomKeyboard.kbcount-1)
                    {
                        mCustomKeyboard.curr_layout+=1;
                        mCustomKeyboard.change_keyboard(mCustomKeyboard.keylayouts[mCustomKeyboard.curr_layout]);
                    }
                }

                break;
            }
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("TAG", "onItemSelected: " + position + " " + id);
        langPos = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

//        Toast.makeText(getApplicationContext(),langPos + "", Toast.LENGTH_LONG).show();
        Log.d("TAG", "onNothingSelected: ");
    }

    public void saveCode(View view) {


        try {
            File file = new File(EditorActivity.this.getFilesDir(), "" + filename.getText().toString());
            FileWriter writer = new FileWriter(file);
            writer.append(codebox.getText().toString());
            writer.flush();
            writer.close();
            Toast.makeText(getApplicationContext(),"Save your code as " + filename.getText().toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {

        }


    }
}
