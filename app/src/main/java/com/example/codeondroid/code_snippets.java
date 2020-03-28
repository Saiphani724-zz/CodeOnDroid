package com.example.codeondroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class code_snippets extends AppCompatActivity implements CustomAdapterCommunicator {
    String files[];
    ArrayList<Snipetcontaier> arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_snippets);
        this.overridePendingTransition(R.anim.bounce,
                R.anim.bounce);
        arr = new ArrayList<Snipetcontaier>();
        load_snipets();
        //l1.setOnItemClickListener(this);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.addnewsnip:
                create_new_snip();
                return true;
            default:
                return false;

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.snipet_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    /*public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i=new Intent();
        i.putExtra("snip",arr.get(position).getContent());
        setResult(RESULT_OK,i);
        finish();

    }*/
    private void create_new_snip() {
        SharedPreferences sf=getSharedPreferences("mysendsnipfile", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=sf.edit();
        edit.clear(); // remove existing entries
        edit.putString("fname","NA");
        edit.commit();
        Intent i = new Intent(this,CreateSnipetActivity.class);
        startActivity(i);
    }

    @Override
    public void customsetresult(String snip) {
        Intent i=new Intent();
        i.putExtra("snip",snip);
        setResult(RESULT_OK,i);
        //Toast.makeText(this,snip,Toast.LENGTH_SHORT).show();
        this.finish();
    }

    @Override
    public void loadondelete() {
        load_snipets();
    }

    @Override
    public void customstartactivity(String Filename) {
        SharedPreferences sf=getSharedPreferences("mysendsnipfile", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=sf.edit();
        edit.clear(); // remove existing entries
        edit.putString("fname",Filename);
        edit.commit();
        Intent i = new Intent(this,CreateSnipetActivity.class);
        startActivity(i);
    }

    public void load_snipets()
    {
        arr.clear();
        Snipetcontaier obj1= new Snipetcontaier("Input Testcase","int t;\ncin>>t;");
        Snipetcontaier obj2= new Snipetcontaier("output variable","cout<<var;");
        arr.add(obj1);
        arr.add(obj2);
        File f = new File("" + getFilesDir()+"/snip");
        FilenameFilter fileFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.contains(".");
            }
        };
        files = f.list(fileFilter);
        if(!f.exists()){
            f.mkdir();
        }
        assert files != null;
        for(String fname:files)
        {
            try {
                String yourFilePath = getApplicationContext().getFilesDir() + "/snip/" + fname;
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
                //Log.d("TAG", "onCreate: " + sb.toString()  + "\n") ;
                Snipetcontaier temp = new Snipetcontaier(fname.substring(0,fname.length()-5),sb.toString());
                arr.add(temp);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        ListView l1= (ListView)findViewById(R.id.sniplist);
        l1.setAdapter(new MyCustomAdapter(arr, getApplicationContext(),this));
    }

    @Override
    protected void onRestart() {
        load_snipets();
        super.onRestart();
    }
}
