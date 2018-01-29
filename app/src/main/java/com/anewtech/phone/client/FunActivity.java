package com.anewtech.phone.client;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.beans.LED;
import com.qihancloud.opensdk.function.beans.handmotion.AbsoluteAngleHandMotion;
import com.qihancloud.opensdk.function.beans.handmotion.NoAngleHandMotion;
import com.qihancloud.opensdk.function.beans.headmotion.AbsoluteAngleHeadMotion;
import com.qihancloud.opensdk.function.beans.headmotion.RelativeAngleHeadMotion;
import com.qihancloud.opensdk.function.beans.wheelmotion.RelativeAngleWheelMotion;
import com.qihancloud.opensdk.function.unit.HandMotionManager;
import com.qihancloud.opensdk.function.unit.HardWareManager;
import com.qihancloud.opensdk.function.unit.HeadMotionManager;
import com.qihancloud.opensdk.function.unit.WheelMotionManager;

import java.util.Random;


public class FunActivity extends TopBaseActivity{
    HardWareManager hardWareManager;
    AbsoluteAngleHandMotion absoluteAngleHandMotion;
    AbsoluteAngleHeadMotion absoluteAngleHeadMotion;
    HandMotionManager handMotionManager;
    HeadMotionManager headMotionManager;
    WheelMotionManager wheelMotionManager;
    RelativeAngleWheelMotion relativeAngleWheelMotion;
    NoAngleHandMotion noAngleHandMotion;
    Handler handler = new Handler();
    Handler reset = new Handler();
    byte delaytime = 3;
    byte randomcolour = 8;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyView(R.layout.activity_fun);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FunActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
        hardWareManager = (HardWareManager) getUnitManager(FuncConstant.HARDWARE_MANAGER);
        headMotionManager = (HeadMotionManager) getUnitManager(FuncConstant.HEADMOTION_MANAGER);
        handMotionManager = (HandMotionManager) getUnitManager(FuncConstant.HANDMOTION_MANAGER);
        //wheelMotionManager = (WheelMotionManager) getUnitManager(FuncConstant.WHEELMOTION_MANAGER);
        handler.post(runnable);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int random = new Random().nextInt(8);
            int wheel = new Random().nextInt(3);
            int horizontal = new Random().nextInt(180);
            int vertical = new Random().nextInt(23) + 7;
            int speed = new Random().nextInt(7) + 1;
            int wheelspeed = new Random().nextInt(9) + 1;
            int handangle = new Random().nextInt(270);
            int hand = new Random().nextInt(2);
            boolean light = true;

            absoluteAngleHeadMotion = new AbsoluteAngleHeadMotion(
                    AbsoluteAngleHeadMotion.ACTION_HORIZONTAL, horizontal
            );
            headMotionManager.doAbsoluteAngleMotion(absoluteAngleHeadMotion);
            absoluteAngleHeadMotion = new AbsoluteAngleHeadMotion(
                    AbsoluteAngleHeadMotion.ACTION_VERTICAL, vertical
            );
            headMotionManager.doAbsoluteAngleMotion(absoluteAngleHeadMotion);

            switch (hand) {
                case 0:
                    absoluteAngleHandMotion = new
                            AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_LEFT, speed,
                            handangle
                    );
                    handMotionManager.doAbsoluteAngleMotion(absoluteAngleHandMotion);
                    break;
                case 1:
                    absoluteAngleHandMotion = new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_RIGHT,
                            speed,
                            handangle
                    );
                    handMotionManager.doAbsoluteAngleMotion(absoluteAngleHandMotion);
                    break;
            }


           /* switch (wheel) {
                case 1:
                    relativeAngleWheelMotion = new RelativeAngleWheelMotion(RelativeAngleWheelMotion.TURN_LEFT, wheelspeed, 90);
                    wheelMotionManager.doRelativeAngleMotion(relativeAngleWheelMotion);
                    break;
                case 2:
                    relativeAngleWheelMotion = new RelativeAngleWheelMotion(RelativeAngleWheelMotion.TURN_RIGHT, wheelspeed, 90);
                    wheelMotionManager.doRelativeAngleMotion(relativeAngleWheelMotion);
                    break;
            }*/
            do{
                hardWareManager.setLED(new LED(LED.PART_ALL, LED.MODE_FLICKER_RANDOM, delaytime, randomcolour));
            }while (light == true);
        }
    };

    @Override
    protected void onMainServiceConnected() {

    }
}
