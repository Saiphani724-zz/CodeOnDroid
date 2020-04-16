package codeondroid.codeondroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import codeondroid.codeondroid.R;

import java.util.List;

public class Editkeys extends AppCompatActivity implements KeyboardView.OnKeyboardActionListener {
    KeyboardView mKeyboardView;
    String[] custKeyName,custKeycontent;
    int cust_key_count=18;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editkeys);
        mKeyboardView= (KeyboardView)findViewById(R.id.keySelectionBoard);
        mKeyboardView.setKeyboard(new Keyboard(getApplicationContext(), R.xml.keyboardselection));
        custKeyName = new String[cust_key_count];
        custKeycontent = new String[cust_key_count];
        load_keys();
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setOnKeyboardActionListener(this);
    }

    private void load_keys() {
        SharedPreferences sf= getSharedPreferences("keylistfile", Context.MODE_PRIVATE);
        for(int i=0;i<cust_key_count;i++)
        {
            custKeyName[i]=sf.getString("key"+i+"name","");
            custKeycontent[i]=sf.getString("key"+i+"content","");
        }
        List<Keyboard.Key> keylist = mKeyboardView.getKeyboard().getKeys();
        for(int j=0;j<cust_key_count;j++)
        {
            keylist.get(j).label=custKeyName[j];
        }
    }

    @Override
    public void onPress(final int primaryCode) {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.keyselpopup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation((View) mKeyboardView, Gravity.CENTER, 0, 0);
        final EditText keyname = popupView.findViewById(R.id.keyname);
        keyname.setText(custKeyName[primaryCode]);
        final EditText keycontent = popupView.findViewById(R.id.keycontent);
        keycontent.setText(custKeycontent[primaryCode]);
        Button ok = popupView.findViewById(R.id.savekeybutton);
        Button cancel = popupView.findViewById(R.id.cancelbutton);
        cancel.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        }));
        ok.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sf=getSharedPreferences("keylistfile", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sf.edit();
                edit.putString("key"+primaryCode+"name",keyname.getText().toString());
                edit.putString("key"+primaryCode+"content",keycontent.getText().toString());
                edit.putString("already_there","yes");
                edit.apply();
                mKeyboardView.invalidateAllKeys();
                load_keys();
                popupWindow.dismiss();
            }
        }));
    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {

    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

}
