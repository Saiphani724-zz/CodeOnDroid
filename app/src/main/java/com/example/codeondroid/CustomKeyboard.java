package com.example.codeondroid;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.HashMap;
import java.util.List;

public class CustomKeyboard {
    private KeyboardView mKeyboardView;
    private Activity mHostActivity;
    public int keylayouts[]={R.xml.keyboard,R.xml.keywordboard,R.xml.variablekeys};
    int kbcount,curr_layout;
    HashMap keydict ,varkeys;
    private KeyboardView.OnKeyboardActionListener mOnKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {

        public final static int CodeDelete   = -5; // Keyboard.KEYCODE_DELETE
        public final static int CodeCancel   = -3; // Keyboard.KEYCODE_CANCEL
        public final static int CodeEnter   = -4;
        public final static int CodePrev     = 55000;
        public final static int CodeAllLeft  = 55001;
        public final static int CodeLeft     = 55002;
        public final static int CodeRight    = 55003;
        public final static int CodeAllRight = 55004;
        public final static int CodeNext     = 55005;
        public final static int CodeClear    = 55006;
        @Override public void onKey(int primaryCode, int[] keyCodes) {
            // NOTE We can say '<Key android:codes="49,50" ... >' in the xml file; all codes come in keyCodes, the first in this list in primaryCode
            // Get the EditText and its Editable
            View focusCurrent = mHostActivity.getWindow().getCurrentFocus();
            if( focusCurrent==null  ) return;
             //Toast.makeText(mHostActivity.getApplicationContext(),""+primaryCode,Toast.LENGTH_SHORT).show();
            EditText edittext = (EditText) focusCurrent;
            Editable editable = edittext.getText();
            edittext.setHint("");
            int start = edittext.getSelectionStart();
            // Apply the key to the edittext
            if( primaryCode==CodeCancel ) {
                hideCustomKeyboard();
            }
            else if( primaryCode==CodeEnter ) {
                if (editable != null && start > 0) editable.insert(start, "\n");
            }
            else if( primaryCode==CodeDelete ) {
                if( editable!=null && start>0 ) editable.delete(start - 1, start);
            } else if( primaryCode==CodeClear ) {
                if( editable!=null ) editable.clear();
            } else if( primaryCode==CodeLeft ) {
                if( start>0 ) edittext.setSelection(start - 1);
            } else if( primaryCode==CodeRight ) {
                 //if (start < edittext.length()) edittext.setSelection(start + 1);
                edittext.setSelection(start+1);
            } else if( primaryCode==CodeAllLeft ) {
                if(start>=1)
                edittext.setSelection(start-1);
            } else if( primaryCode==CodeAllRight ) {
                edittext.setSelection(edittext.length());
            } else if( primaryCode==CodePrev ) {
                 //View focusNew= edittext.focusSearch(View.FOCUS_BACKWARD);
                 //if( focusNew!=null ) focusNew.requestFocus();
                edittext.setSelection(0);
            } else if( primaryCode==CodeNext ) {
                edittext.setSelection(edittext.length());
            }
            else if(primaryCode>=400)
            {
                editable.insert(start, varkeys.get(primaryCode).toString()+" ");
            }
            else if( primaryCode>=300 ) {
                editable.insert(start, keydict.get(primaryCode).toString());
                if(primaryCode==309||primaryCode==314||primaryCode==313)
                {
                    edittext.setSelection(start+1);
                }
            }
            else { // insert character
                editable.insert(start, Character.toString((char) primaryCode));
            }
            //edittext.setText(editable.toString());
        }

        @Override public void onPress(int arg0) {
        }

        @Override public void onRelease(int primaryCode) {
        }

        @Override public void onText(CharSequence text) {
        }

        @Override public void swipeDown() {
        }

        @Override public void swipeRight() {
            if (curr_layout>0)
            {
                curr_layout-=1;
                change_keyboard(keylayouts[curr_layout]);
            }
        }

        @Override public void swipeLeft() {
            if (curr_layout<kbcount-1)
            {
                curr_layout+=1;
                change_keyboard(keylayouts[curr_layout]);
            }
        }

        @Override public void swipeUp() {
        }
    };

    public CustomKeyboard(Activity host, int viewid, int layoutid) {
        mHostActivity= host;
        kbcount = keylayouts.length;
        curr_layout=0;
        keydict = new HashMap();
        varkeys = new HashMap();
        load_dict();
        mKeyboardView= (KeyboardView)mHostActivity.findViewById(viewid);
        mKeyboardView.setKeyboard(new Keyboard(mHostActivity, layoutid));
        mKeyboardView.setPreviewEnabled(false); // NOTE Do not show the preview balloons
        mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
        // Hide the standard keyboard initially
        mHostActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }
    /** Returns whether the CustomKeyboard is visible. */
    public boolean isCustomKeyboardVisible() {
        return mKeyboardView.getVisibility() == View.VISIBLE;
    }

    /** Make the CustomKeyboard visible, and hide the system keyboard for view v. */
    public void showCustomKeyboard( View v ) {
        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);
        if( v!=null ) ((InputMethodManager)mHostActivity.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /** Make the CustomKeyboard invisible. */
    public void hideCustomKeyboard() {
        mKeyboardView.setVisibility(View.GONE);
        mKeyboardView.setEnabled(false);
    }
    public void registerEditText(int resid) {
        // Find the EditText 'resid'
        EditText edittext= (EditText)mHostActivity.findViewById(resid);
        // Make the custom keyboard appear
        edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            // NOTE By setting the on focus listener, we can show the custom keyboard when the edit box gets focus, but also hide it when the edit box loses focus
            @Override public void onFocusChange(View v, boolean hasFocus) {
                if( hasFocus ) showCustomKeyboard(v); else hideCustomKeyboard();
            }
        });
        edittext.setOnClickListener(new View.OnClickListener() {
            // NOTE By setting the on click listener, we can show the custom keyboard again, by tapping on an edit box that already had focus (but that had the keyboard hidden).
            @Override public void onClick(View v) {
                showCustomKeyboard(v);
            }
        });
        // Disable standard keyboard hard way
        // NOTE There is also an easy way: 'edittext.setInputType(InputType.TYPE_NULL)' (but you will not have a cursor, and no 'edittext.setCursorVisible(true)' doesn't work )
        edittext.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                EditText edittext = (EditText) v;
                int inType = edittext.getInputType();       // Backup the input type
                edittext.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                edittext.onTouchEvent(event);               // Call native handler
                edittext.setInputType(inType);              // Restore input type

                return true; // Consume touch event
            }
        });
        // Disable spell check (hex strings look like words to Android)
        edittext.setInputType(edittext.getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }
    public void change_keyboard(int layid)
    {
        mKeyboardView.setKeyboard(new Keyboard(mHostActivity, layid));
        if(layid==R.xml.variablekeys) {
            List<Keyboard.Key> keylist = mKeyboardView.getKeyboard().getKeys();
            //keylist.get(0).label = "Num1";
            //Log.d("varkey", "" + keylist.toArray().length);
            View focusCurrent = mHostActivity.getWindow().getCurrentFocus();
            EditText edittext = (EditText) focusCurrent;
            String[] varslist =  edittext.getText().toString().split(" ");
            for(int i=0;i<varslist.length;i++)
            {
                varkeys.put(401+i,varslist[i]);
                keylist.get(i).label=varslist[i];
                if(i==keylist.toArray().length)
                {
                    break;
                }
            }
        }
    }
    public void load_dict()
    {
        keydict.put(301,"for ");
        keydict.put(302,"while ");
        keydict.put(303,"if ");
        keydict.put(304,"else ");
        keydict.put(305,"int ");
        keydict.put(306,"float ");
        keydict.put(307,"char ");
        keydict.put(308,"cin>> ");
        keydict.put(309,"( ) ");
        keydict.put(310,";");
        keydict.put(311,"cout<< ");
        keydict.put(312,"# ");
        keydict.put(313,"{\n}");
        keydict.put(314,"\" \" ");
    }

}
