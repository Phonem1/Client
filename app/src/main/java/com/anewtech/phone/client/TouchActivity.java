package com.anewtech.phone.client;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.anewtech.phone.client.toiletFeedback.FragmentLayout;

/**
 * Created by heriz on 26/12/2017.
 */

public class TouchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.touch_to_start);

        TextView touchTV = findViewById(R.id.touch_tv);
        touchTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // can use switch case to start different programs based on item clicked in menu
                Intent intent = new Intent(TouchActivity.this, FragmentLayout.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
//        finish();
    }
}
