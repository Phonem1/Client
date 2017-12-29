package com.anewtech.phone.client;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // idle 30sec go into splash screen
        timer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
//                Intent intent = new Intent(MainActivity.this, SplashScreen.class);
//                intent.putExtra("splash", false);
//                startActivity(intent);
                finish();
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelTimer();
    }

    //These 3 are from menus, assign onClick method from xml

    public void launchSurvey(View view){
        Intent intent = new Intent(this, TouchActivity.class);
        intent.putExtra("appCode", "survey");
        startActivity(intent);
    }

    public void launchQuiz(View view){
        Intent intent = new Intent(this, TouchActivity.class);
        intent.putExtra("appCode", "quiz");
        startActivity(intent);
    }

    public void launchToiletFeedback(View view){
        Intent intent = new Intent(this, TouchActivity.class);
        intent.putExtra("appCode", "toilet feedback");
        startActivity(intent);
    }

    private void cancelTimer(){
        timer.cancel();
    }
}
