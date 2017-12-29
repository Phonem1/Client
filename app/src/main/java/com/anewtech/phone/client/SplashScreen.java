package com.anewtech.phone.client;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    ImageView splash;
    private boolean initialSplash = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

//        Bundle bundle = getIntent().getExtras();
//        if(bundle != null){
//            initialSplash = bundle.getBoolean("splash");
//        }

        splash= findViewById(R.id.splashImage);
        splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initialSplash = false;
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                Log.e("xxx","SplashScreen: initialSplash = "+initialSplash);
                startActivity(intent);
            }
        });

        setInitialSplash();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        finish();
    }

    public void setInitialSplash(){
        Log.e("xxx","SplashScreen: initialSplash = "+initialSplash);
        if(initialSplash){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    splash.performClick();
                    initialSplash = false;
                }
            },500);
        }
    }
}

