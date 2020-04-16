package codeondroid.codeondroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import codeondroid.codeondroid.R;
public class Welcome extends AppCompatActivity {

    public static int SPLASH_TIME_OUT=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();
        this.overridePendingTransition(R.anim.zoomin,
                R.anim.zoomout);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent LetsCode = new Intent(Welcome.this,LoginActivity.class);
                startActivity(LetsCode);
                finish();
            }
        },SPLASH_TIME_OUT);

    }
}
