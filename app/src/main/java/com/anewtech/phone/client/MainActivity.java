package com.anewtech.phone.client;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
Handler handler;
Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runnable = new runnable() {
            public void run() {
                Intent intent = new Intent(this, SplashScreen.class);

                startActivity(intent);
            }
        };
        handler.postDelayed(runnable,6000);
    }
}
