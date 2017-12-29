package com.anewtech.phone.client.SurveyActivity.Fragments;

/**
 * Created by heriz on 26/12/2017.
 */

public class CustomActivityMessageEvent {
    private int doOrDoNot;
    public CustomActivityMessageEvent(int doOrDoNot){
        this.doOrDoNot = doOrDoNot;
    }
    public int getDoOrDoNot(){
        return doOrDoNot;
    }
}
