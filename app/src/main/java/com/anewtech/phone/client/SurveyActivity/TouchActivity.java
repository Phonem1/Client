package com.anewtech.phone.client.SurveyActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.anewtech.phone.client.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by heriz on 26/12/2017.
 */

public class TouchActivity extends AppCompatActivity {

    HashMap<String,String> appMap;
    HashMap<String,String> infoMap;
    String filename, appCode, info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.touch_to_start);

        TextView infotv = findViewById(R.id.appInfo_tv);
        initMap();

        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("appCode") != null) {
            appCode = bundle.getString("appCode");
        }

        for(Map.Entry<String,String> entry : appMap.entrySet()){
            if(entry.getKey().equals(appCode)){
                filename = entry.getValue();
            }
        }

        for(Map.Entry<String,String> entry : infoMap.entrySet()){
            if(entry.getKey().equals(appCode)){
                info = entry.getValue();
                infotv.setHint(info);
            }
        }

        TextView touchTV = findViewById(R.id.touch_tv);
        touchTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // can use switch case to start different programs based on item clicked in menu
                Intent intent = new Intent(TouchActivity.this, FragmentLayout.class);
                intent.putExtra("asset", filename);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void initMap() {
        //name of app to their filename
        appMap = new HashMap<>();
        appMap.put("survey","survey");
        appMap.put("quiz","quiz");
        appMap.put("toilet feedback", "toilet_feedback");

        //name of app to their info
        infoMap = new HashMap<>();
        infoMap.put("survey","This is a survey test");
        infoMap.put("quiz","Are you ready to see how much you know about Singapore?");
        infoMap.put("toilet feedback","Would you like to give feedback to our public toilets?");
    }
}
