
package com.example.codeondroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Navigationclass extends AppCompatActivity implements AllFiles.OnFragmentInteractionListener, PythonFiles.OnFragmentInteractionListener  {


    LinearLayout l1;
    Button btnCC,btnCF,btnHR;
    DatabaseReference reff;

    SharedPreferences sf;

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigationclass);
        this.overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_left);
        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("All Files"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.python_icon));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.cpp_icon));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.java_icon));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.c_icon));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager() , tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        final FirebaseAuth fAuth = FirebaseAuth.getInstance();

        String email = fAuth.getCurrentUser().getEmail();
        reff = FirebaseDatabase.getInstance().getReference().child("Users");

        final String uid = fAuth.getCurrentUser().getUid();
//        Toast.makeText(getApplicationContext(),uid,Toast.LENGTH_LONG).show();

       reff.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               String username = dataSnapshot.child(uid).child("username").getValue(String.class);
               String emailsend  = dataSnapshot.child(uid).child("email").getValue(String.class);
                String favlang  = dataSnapshot.child(uid).child("favlang").getValue(String.class);


                SharedPreferences sf=getSharedPreferences("myfile", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit=sf.edit();
                edit.clear(); // remove existing entries
                edit.putString("favLang",favlang);
                edit.putString("uname" , username );
                edit.putString("email" , emailsend );
//                Toast.makeText(getApplicationContext(),"Welcome "+username,Toast.LENGTH_LONG).show();
                edit.commit();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });



        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        btnCC=(Button)findViewById(R.id.btnCC);
        btnCF=(Button)findViewById(R.id.btnCF);
        btnHR=(Button)findViewById(R.id.btnHR);
        l1 = (LinearLayout)findViewById(R.id.ll);





        registerForContextMenu(l1);




        btnCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent l = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.codechef.com/"));
//                startActivity(l);

//                SharedPreferences sf=getSharedPreferences("myfileweb", Context.MODE_PRIVATE);
//                SharedPreferences.Editor edit=sf.edit();
//                edit.clear(); // remove existing entries
//                edit.putString("url","https://www.codechef.com/");

//                edit.commit();
//
//                CustomTab.openCustomTab();
                openCustomTab("https://www.codechef.com/");

            }
        });

        btnHR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent m = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.hackerrank.com/?utm_expid=.2u09ecQTSny1HV02SEVoCg.0&utm_referrer="));
//                startActivity(m);

//                SharedPreferences sf=getSharedPreferences("myfileweb", Context.MODE_PRIVATE);
//                SharedPreferences.Editor edit=sf.edit();
//                edit.clear(); // remove existing entries
//                edit.putString("url","https://www.hackerrank.com/?utm_expid=.2u09ecQTSny1HV02SEVoCg.0&utm_referrer=");
//
//                edit.commit();
//                Intent i=new Intent(getApplicationContext(),webview.class);
//                startActivity(i);

                openCustomTab("https://www.hackerrank.com/?utm_expid=.2u09ecQTSny1HV02SEVoCg.0&utm_referrer=");


            }
        });

        btnCF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent n = new Intent(Intent.ACTION_VIEW, Uri.parse("https://codeforces.com/"));
//                startActivity(n);
//                SharedPreferences sf=getSharedPreferences("myfileweb", Context.MODE_PRIVATE);
////                SharedPreferences.Editor edit=sf.edit();
////                edit.clear(); // remove existing entries
////                edit.putString("url","https://www.codeforces.com/");
////
////                edit.commit();
////                Intent i=new Intent(getApplicationContext(),webview.class);
////                startActivity(i);
                openCustomTab("https://www.codeforces.com/");
            }
        });

    }

    void openCustomTab(String url)
    {
        CustomTabsIntent.Builder cusTab = new CustomTabsIntent.Builder();
        cusTab.setToolbarColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
        cusTab.addDefaultShareMenuItem();
        cusTab.setStartAnimations(this, R.anim.fadein, R.anim.fadeout);
        cusTab.setExitAnimations(this, R.anim.fadeout, R.anim.fadein);
        CustomTabsIntent customTabsIntent = cusTab.build();
        customTabsIntent.launchUrl(this,Uri.parse(url));
    }


    //Creating a context menu to provide the user with a walkthrough of the app upon pressing anywhere on the home screen
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();

        if(v.getId()==R.id.ll)
        {
            inflater.inflate(R.menu.context_menu,menu);
        }
        if(v.getId()==R.id.myfiles)
        {
            inflater.inflate(R.menu.list_menu,menu);
        }

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
//            case R.id.share:
//                String myFilePath = getApplicationContext().getFilesDir() + "/" + sf.getString("filename","NA");;
//                Toast.makeText(this, "" + myFilePath, Toast.LENGTH_SHORT).show();
//                Intent intentShareFile = new Intent(Intent.ACTION_SEND);
//                File fileWithinMyDir = new File(myFilePath);
//
//                if(fileWithinMyDir.exists()) {
//                    intentShareFile.setType("application/pdf");
//                    intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+myFilePath));
//
//                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
//                            "Sharing File...");
//                    intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
//
//                    startActivity(Intent.createChooser(intentShareFile, "Share File"));
//                }
//                return  true;
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

            case  R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Welcome.class));
                finish();
                return true;
            default:
                return false;

        }
    }

    public void openEditor(View v)
    {
        SharedPreferences sf=getSharedPreferences("myfile1", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=sf.edit();
        edit.clear(); // remove existing entries
        edit.putString("filename","NA");
        edit.commit();
        startActivity(new Intent(Navigationclass.this,EditorActivity.class));
    }



    @Override
    protected void onRestart() {
        super.onRestart();


//        Fragment frg = null;
//        frg = getSupportFragmentManager().findFragmentById(R.id.all_files);
//        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.detach(frg);
//        ft.attach(frg);
//        ft.commit();

    }
}
