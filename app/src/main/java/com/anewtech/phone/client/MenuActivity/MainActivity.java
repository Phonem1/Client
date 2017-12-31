package com.anewtech.phone.client.MenuActivity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.anewtech.phone.client.R;

public class MainActivity extends AppCompatActivity {

    MenuViewModel mViewModel;

    Handler handler;
    Runnable runSplash;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(MenuViewModel.class);

        if(mViewModel.initialSplash) {
            setContentView(R.layout.splash);
            mViewModel.initialSplash = false;
            handler = new Handler();
            handler.postDelayed(menuActivity(), 1000);
        }else{
            setLayout("menu");
        }

        // idle 30sec go into splash screen
        timer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
                setLayout("splash");
                timer.start();
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelTimer();
    }

    public Runnable menuActivity(){
        runSplash =  new Runnable() {
            @Override
            public void run() {
                setLayout("menu");
            }
        };
        return runSplash;
    }

    public void setLayout(String layout){
        switch(layout){
            case "menu":{
                setContentView(R.layout.activity_main);
                break;
            }
            case "splash":{
                setContentView(R.layout.splash);
                break;
            }
        }
    }

    private void cancelTimer(){
        timer.cancel();
    }

    //These methods are bind to view onClick

    public void stopIdle(View view){
        setLayout("menu");
    }

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

}
