package com.example.codeondroid;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;

public class CreateSnipetActivity extends AppCompatActivity {
    EditText code;
    float x1,x2;
    float y1, y2;
    int MIN_DISTANCE = 150;
    CustomKeyboard mCustomKeyboard1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_snipet);
        //title=findViewById(R.id.snipettitle);
        code=findViewById(R.id.snipetcode);
        mCustomKeyboard1 = new CustomKeyboard(this, R.id.snipkeyboardview, R.xml.keyboard);
        mCustomKeyboard1.registerEditText(R.id.snipetcode);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void savesnip(View view) {
        //String heading = title.getText().toString();
        final String snipcont = code.getText().toString();
        if(snipcont.length()!=0)
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            final EditText edittext = new EditText(getApplicationContext());

            edittext.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            alert.setTitle("Save snippet");
            alert.setMessage("Enter Your File Name to save");

            alert.setView(edittext);


            alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //What ever you want to do with the value
                    String filename = edittext.getText().toString()+".snip";
                    File file1 = new File(CreateSnipetActivity.this.getFilesDir() + "/snip");
                    if(!file1.exists()){
                        boolean sucess=file1.mkdir();
                    }
                    try {
                        File file = new File(CreateSnipetActivity.this.getFilesDir() + "/snip/"+filename);
                        FileWriter writer = new FileWriter(file);
                        writer.append(snipcont + " ");
                        writer.flush();
                        writer.close();
                        Toast.makeText(getApplicationContext(),"Saved your snip as " + filename, Toast.LENGTH_LONG).show();
                        setTitle(filename);
                    } catch (Exception e) {e.printStackTrace();}

                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });

            alert.show();
        }
    }
    @Override public void onBackPressed() {
        // NOTE Trap the back key: when the CustomKeyboard is still visible hide it, only when it is invisible, finish activity
        if( mCustomKeyboard1.isCustomKeyboardVisible() ) mCustomKeyboard1.hideCustomKeyboard(); else this.finish();
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
                    if (mCustomKeyboard1.curr_layout>0)
                    {
                        mCustomKeyboard1.curr_layout-=1;
                        mCustomKeyboard1.change_keyboard( mCustomKeyboard1.keylayouts[mCustomKeyboard1.curr_layout]);
                    }
                }

                // if right to left sweep event on screen
                if (x1-x2>=MIN_DISTANCE)
                {
                    //Toast.makeText(this, "Right to Left Swap Performed", Toast.LENGTH_LONG).show();
                    if (mCustomKeyboard1.curr_layout< mCustomKeyboard1.kbcount-1)
                    {
                        mCustomKeyboard1.curr_layout+=1;
                        mCustomKeyboard1.change_keyboard(mCustomKeyboard1.keylayouts[mCustomKeyboard1.curr_layout]);
                    }
                }

                break;
            }
        }
        return false;
    }


}