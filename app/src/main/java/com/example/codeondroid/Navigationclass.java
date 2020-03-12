
package com.example.codeondroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class Navigationclass extends AppCompatActivity implements AdapterView.OnItemClickListener {

    TextView apname;
    LinearLayout l1;
    Button btnCC,btnCF,btnHR;

    ListView myfiles;
    String[] files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigationclass);

        apname=(TextView)findViewById(R.id.openAbout);
        btnCC=(Button)findViewById(R.id.btnCC);
        btnCF=(Button)findViewById(R.id.btnCF);
        btnHR=(Button)findViewById(R.id.btnHR);
        l1 = (LinearLayout)findViewById(R.id.ll);

        myfiles = findViewById(R.id.myfiles);

        File f = new File("" + getApplicationContext().getFilesDir());
        files = f.list();

        for (int i = 0; i < files.length; i++) {
            Log.d("TAG", "Files List: " + files[i]);
        }


        ArrayAdapter<String> ada=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                files);
        myfiles.setAdapter(ada);
        myfiles.setOnItemClickListener(this);





        registerForContextMenu(l1);


        apname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Navigationclass.this,About.class);
                startActivity(i);
            }
        });


        btnCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent l = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.codechef.com/"));
//                startActivity(l);

                SharedPreferences sf=getSharedPreferences("myfile", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit=sf.edit();
                edit.clear(); // remove existing entries
                edit.putString("url","https://www.codechef.com/");

                edit.commit();
                Intent i=new Intent(getApplicationContext(),webview.class);
                startActivity(i);
            }
        });

        btnHR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent m = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.hackerrank.com/?utm_expid=.2u09ecQTSny1HV02SEVoCg.0&utm_referrer="));
//                startActivity(m);

                SharedPreferences sf=getSharedPreferences("myfile", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit=sf.edit();
                edit.clear(); // remove existing entries
                edit.putString("url","https://www.hackerrank.com/?utm_expid=.2u09ecQTSny1HV02SEVoCg.0&utm_referrer=");

                edit.commit();
                Intent i=new Intent(getApplicationContext(),webview.class);
                startActivity(i);
            }
        });

        btnCF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent n = new Intent(Intent.ACTION_VIEW, Uri.parse("https://codeforces.com/"));
//                startActivity(n);
                SharedPreferences sf=getSharedPreferences("myfile", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit=sf.edit();
                edit.clear(); // remove existing entries
                edit.putString("url","https://www.codeforces.com/");

                edit.commit();
                Intent i=new Intent(getApplicationContext(),webview.class);
                startActivity(i);
            }
        });


    }

    //Creating a context menu to provide the user with a walkthrough of the app upon pressing anywhere on the home screen
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.walkthrough:
                Intent i=new Intent(getApplicationContext(),Walkthrough.class);
                startActivity(i);
                return true;
            default:
                return false;
        }
    }


    //Creating an options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.about:
                Intent i = new Intent(Navigationclass.this,About.class);
                startActivity(i);
                return true;
            case R.id.profile:
                Intent j = new Intent(Navigationclass.this,ProfilePage.class);
                startActivity(j);
                return true;
            case R.id.bug_report:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "prashanth.s.edu@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ReportBug");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, "ReportBug");
                startActivity(emailIntent);
                return true;
            default:
                return false;

        }
    }

    public void openEditor(View v)
    {
        startActivity(new Intent(Navigationclass.this,EditorActivity.class));
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("TAG", "onItemClick: " + files[position]);

        SharedPreferences sf=getSharedPreferences("myfile1", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=sf.edit();
        edit.clear(); // remove existing entries
        edit.putString("filename",files[position]);
        edit.commit();

        startActivity(new Intent(Navigationclass.this,EditorActivity.class));

    }

    @Override
    protected void onRestart() {
        super.onRestart();


        File f = new File("" + getApplicationContext().getFilesDir());
        files = f.list();

        ArrayAdapter<String> ada=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                files);
        myfiles.setAdapter(ada);
        myfiles.setOnItemClickListener(this);

    }

}
