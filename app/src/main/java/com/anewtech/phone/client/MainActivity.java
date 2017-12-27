package com.anewtech.phone.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
