package com.example.codeondroid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class CustomKeyboard {
    private KeyboardView mKeyboardView;
    private Activity mHostActivity;
    public int keylayouts[]={R.xml.specialnumbers,R.xml.keyboard,R.xml.keywordboard,R.xml.variablekeys};
    int kbcount,curr_layout;
    int flag;
    CustomLinkedList undo_stack,redo_stack;
    HashMap keydict,varkeys,wtype,revvar;
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
            redo_stack.clearContents();
            if((primaryCode>0&&primaryCode<1000)||primaryCode==CodeClear||primaryCode==CodeEnter)
            {
                add_undo();
            }
            if( primaryCode==CodeCancel ) {
                hideCustomKeyboard();
            }
            else if( primaryCode==CodeEnter ) {
                if (editable != null && start >= 0) editable.insert(start, "\n");
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
                if(start<edittext.length())
                    edittext.setSelection(start+1);
            } else if( primaryCode==CodePrev ) {
                //View focusNew= edittext.focusSearch(View.FOCUS_BACKWARD);
                //if( focusNew!=null ) focusNew.requestFocus();
                int len =0;
                String text1 = editable.toString();
                int flag = 0;
                if(start==0)
                    len=-1;
                for(int i=start-1;i>-1;i--)
                {
                    if(i <= 0)
                    {
                        edittext.setSelection(0);
                        flag =  1;
                    }
                    if(text1.charAt(i)=='\n')
                    {
                        len = i;
                        break;
                    }
                }
                if(flag == 0)
                    edittext.setSelection(len+1);

            } else if( primaryCode==CodeNext ) {
                int len =edittext.length();
                String text1 = editable.toString();
                for(int i=start;i<text1.length();i++)
                {
                    if(text1.charAt(i)=='\n')
                    {
                        len = i;
                        break;
                    }
                }
                edittext.setSelection(len);
            }
            else if(primaryCode==-1)
            {
                if(flag==0)
                {
                    flag=1;
                }
                else
                {
                    flag=0;
                }
                mKeyboardView.setShifted((flag==1)||(flag==2));
                change_caps(flag);
                mKeyboardView.invalidateAllKeys();
                return;
            }
            else if(primaryCode==-2)
            {
                if(flag==0)
                {
                    flag=2;
                }
                else
                {
                    flag=0;
                }
                mKeyboardView.setShifted((flag==1)||(flag==2));
                change_caps(flag);
                mKeyboardView.invalidateAllKeys();
                return;
            }
            else if(primaryCode>=600)
            {
                editable.insert(start, keydict.get(primaryCode).toString());
                if(primaryCode==603)
                {
                    edittext.setSelection(start+5);
                }
                if(primaryCode==604)
                {
                    edittext.setSelection(start+6);
                }
                if(primaryCode==606)
                {
                    edittext.setSelection(start+7);
                }
                if(primaryCode==607)
                {
                    edittext.setSelection(start+4);
                }
                if(primaryCode==611)
                {
                    edittext.setSelection(start+7);
                }
                if(primaryCode==612)
                {
                    edittext.setSelection(start+5);
                }
                if(primaryCode==613)
                {
                    edittext.setSelection(start+7);
                }
                if(primaryCode==615)
                {
                    edittext.setSelection(start+7);
                }

            }
            else if(primaryCode>=500)
            {
                editable.insert(start, keydict.get(primaryCode).toString());
                if(primaryCode==507)
                {
                    edittext.setSelection(start+7);
                }

            }
            else if(primaryCode>=400)
            {
                if(varkeys.get(primaryCode)!=null && varkeys.get(primaryCode)!="")
                editable.insert(start, varkeys.get(primaryCode).toString());
            }
            else if( primaryCode>=300 ) {
                editable.insert(start, keydict.get(primaryCode).toString());
                if(primaryCode==301)
                {
                    edittext.setSelection(start+4);
                }
                if(primaryCode==309||primaryCode==314||primaryCode==313)
                {
                    edittext.setSelection(start+1);
                }
            }
            else { // insert character
                if(primaryCode==9)
                    editable.insert(start,"    ");
                else{
                    if(flag==0)
                    editable.insert(start, Character.toString((char) primaryCode));
                    else
                    {
                        if(primaryCode>=97&&primaryCode<=122)
                        {
                            editable.insert(start, Character.toString((char) (primaryCode-32)));
                        }
                        else
                        {
                            editable.insert(start, Character.toString((char) primaryCode));
                        }
                    }
                }
            }
            if(flag==1)
            {
                flag=0;
                mKeyboardView.setShifted(false);
                change_caps(flag);
                mKeyboardView.invalidateAllKeys();
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
        curr_layout=1;
        keydict = new HashMap();
        varkeys = new HashMap();
        wtype = new HashMap();
        revvar = new HashMap();
        undo_stack = new CustomLinkedList();
        redo_stack = new CustomLinkedList();
        load_dict();
        load_wtype();
        mKeyboardView= (KeyboardView)mHostActivity.findViewById(viewid);
        mKeyboardView.setKeyboard(new Keyboard(mHostActivity, layoutid));
        mKeyboardView.setPreviewEnabled(false); // NOTE Do not show the preview balloons
        mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
        // Hide the standard keyboard initially
        hideCustomKeyboard();
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
        flag=0;
        curr_layout=1;
    }
    public void registerEditText(int resid) {
        // Find the EditText 'resid'
        EditText edittext= (EditText)mHostActivity.findViewById(resid);
        // Make the custom keyboard appear
        /*edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            // NOTE By setting the on focus listener, we can show the custom keyboard when the edit box gets focus, but also hide it when the edit box loses focus
            @Override public void onFocusChange(View v, boolean hasFocus) {
                if( hasFocus ) showCustomKeyboard(v); else hideCustomKeyboard();
            }
        });*/
        edittext.setOnClickListener(new View.OnClickListener() {
            // NOTE By setting the on click listener, we can show the custom keyboard again, by tapping on an edit box that already had focus (but that had the keyboard hidden).
            @Override public void onClick(View v) {
                showCustomKeyboard(v);
            }
        });
        // Disable standard keyboard hard way
        // NOTE There is also an easy way: 'edittext.setInputType(InputType.TYPE_NULL)' (but you will not have a cursor, and no 'edittext.setCursorVisible(true)' doesn't work )
        edittext.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override public boolean onTouch(View v, MotionEvent event) {
                EditText edittext = (EditText) v;
                int inType = edittext.getInputType();       // Backup the input type
                //edittext.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                edittext.setShowSoftInputOnFocus(false);
                edittext.onTouchEvent(event);               // Call native handler
                edittext.setInputType(inType);              // Restore input type

                //return true; // Consume touch event
                InputMethodManager imm = (InputMethodManager) mHostActivity.getApplicationContext().getSystemService(
                        android.content.Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                return true;
            }
        });
        // Disable spell check (hex strings look like words to Android)
        edittext.setInputType(edittext.getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }
    public void change_keyboard(int layid)
    {
        if(layid==R.xml.keywordboard)
        {
            SharedPreferences sf= mHostActivity.getSharedPreferences("myfile2", Context.MODE_PRIVATE);
            String lang = sf.getString("selLang","NA");
            if(lang=="Java")
            {
                //mKeyboardView.setKeyboard(new Keyboard(mHostActivity,R.xml.keywordboard));
                mKeyboardView.setKeyboard(new Keyboard(mHostActivity,R.xml.javakeyboard));
                return;
            }
            if(lang=="Python3"){
                mKeyboardView.setKeyboard(new Keyboard(mHostActivity,R.xml.pythonkeyboard));
                return;
            }
        }
        mKeyboardView.setKeyboard(new Keyboard(mHostActivity, layid));
        if(layid==R.xml.variablekeys) {
            varkeys = new HashMap();
            revvar = new HashMap();
            List<Keyboard.Key> keylist = mKeyboardView.getKeyboard().getKeys();
            for(Keyboard.Key ky: keylist)
            {
                ky.label="";
            }
            //keylist.get(0).label = "Num1";
            //Log.d("varkey", "" + keylist.toArray().length);
            View focusCurrent = mHostActivity.getWindow().getCurrentFocus();
            EditText edittext = (EditText) focusCurrent;
            String[] varslist =  edittext.getText().toString().split(" |\n|;|\\(|\\)|\\{|\\}|\\[|\\+|-|\\*|=|/|,|]");
            int count=0;
            for(int i=0;i<varslist.length;i++)
            {
                if(wtype.containsKey(varslist[i])||revvar.containsKey(varslist[i]))
                {
                    continue;
                }
                if(varslist[i].equals(""))
                {
                    continue;
                }

                varkeys.put(401+count,varslist[i]);
                revvar.put(varslist[i],401+count);
                keylist.get(count).label=varslist[i];
                count++;
                if(count==keylist.toArray().length)
                {
                    break;
                }
            }
        }
    }
    public void load_dict()
    {
        keydict.put(301,"for(  ;  ;  )\n{\n    \n}");
        keydict.put(302,"while( )\n{\n    \n}");
        keydict.put(303,"if()\n{\n    \n}");
        keydict.put(304,"else\n{\n    \n}");
        keydict.put(305,"int ");
        keydict.put(306,"float ");
        keydict.put(307,"char ");
        keydict.put(308,"cin>> ");
        keydict.put(309,"()");
        keydict.put(310,";");
        keydict.put(311,"cout<< ");
        keydict.put(312,"# ");
        keydict.put(313,"{\n}");
        keydict.put(314,"\"\"");
        keydict.put(351,"==");
        keydict.put(352,"!=");
        keydict.put(353,"++");
        keydict.put(354,"--");
        keydict.put(501 ,"Scanner sc =  new Scanner(System.in);\n");
        keydict.put(502,"import ");
        keydict.put(503 ,"System");
        keydict.put(504,"public static void main(String args[])\n{\n    \n}");
        keydict.put(505,"class Myclass \n{\n    \n}");
        keydict.put(506,"new ");
        keydict.put(507,"sc.next()");
        keydict.put(508,"System.out.println();");
        keydict.put(601 ,"from ");
        keydict.put(602,"import ");
        keydict.put(603 ,"map(  )");
        keydict.put(604,"list(  )");
        keydict.put(605,"def ");
        keydict.put(606,"range(  )");
        keydict.put(607,"for  in :");
        keydict.put(608,"while");
        keydict.put(609,"if");
        keydict.put(610,"else:");
        keydict.put(611,"split(  )");
        keydict.put(612,"str(  )");
        keydict.put(613,"input(  )");
        keydict.put(614,"[]");
        keydict.put(615,"print(  )");
    }
    public void load_wtype()
    {
        String keywords[] = {"for","if","while","else","int","float","char","include","do","cout","cin","struct","class","void","public",
                "private","protected","global","static","final","using","print","input"};
        String symbols[] = {"+","-","=",";","{","}","\\","/","[","]","!","#","%","&","*","(",")",":","?","<",">","\n","\"","\'",".","_","|","\t"," ",":"};
        insert_dict(keywords,"keyword");
        insert_dict(symbols,"symbol");
    }
    public void insert_dict(String[] arr,String type)
    {
        for (String s:arr) {
            wtype.put(s,type);
        }
    }
    private void change_caps(int flag)
    {
        List<Keyboard.Key> keylist = mKeyboardView.getKeyboard().getKeys();
        if(flag==0)
        {
            keylist.get(30).label="caps";
        }
        else if(flag==1)
        {
            keylist.get(30).label="Caps";
        }
        else
        {
            keylist.get(30).label="CAPS";
        }
    }
    public void add_undo()
    {
        View focusCurrent = mHostActivity.getWindow().getCurrentFocus();
        EditText edittext = (EditText) focusCurrent;
        String content= edittext.getText().toString();
        int start = edittext.getSelectionStart();
        CustomLinknode current = undo_stack.pop();
        if (current != null) {
            int prev_start=current.getcursor();
            if(start-prev_start!=1 && start-prev_start!=0||content.length()-current.getcontent().length()!=1)
            {
                undo_stack.push(current.getcontent(),current.getcursor());
            }

        }
        else
        {
            undo_stack.push(content,start);
        }
        undo_stack.push(content, start);
        //Toast.makeText(mHostActivity.getApplicationContext(),content+" "+start,Toast.LENGTH_SHORT).show();
    }
    public void undo_action()
    {
        try{
        CustomLinknode current = undo_stack.pop();
        String content = current.getcontent();
        int cursor = current.getcursor();
        View focusCurrent = mHostActivity.getWindow().getCurrentFocus();
        EditText edittext = (EditText) focusCurrent;
        int start = edittext.getSelectionStart();
        String current_content = edittext.getText().toString();
        if(start-cursor==1&&current_content.length()-content.length()==1)
        {
            current = undo_stack.pop();
            content = current.getcontent();
            cursor = current.getcursor();
            //Toast.makeText(mHostActivity.getApplicationContext(),content.length()-current_content.length()+"",Toast.LENGTH_SHORT).show();
        }

        edittext.setText(content);
        edittext.setSelection(cursor);
        redo_stack.push(current_content,start);
        }
        catch (Exception e)
        {
           return;
        }
    }
    public void redo_action()
    {
        try{
            CustomLinknode current = redo_stack.pop();
            String content = current.getcontent();
            int cursor = current.getcursor();
            View focusCurrent = mHostActivity.getWindow().getCurrentFocus();
            EditText edittext = (EditText) focusCurrent;
            edittext.setText(content);
            edittext.setSelection(cursor);
            add_undo();
        }
        catch (Exception e)
        {
            return;
        }
    }

}
