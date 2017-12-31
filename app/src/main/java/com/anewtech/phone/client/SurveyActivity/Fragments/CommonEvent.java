package com.anewtech.phone.client.SurveyActivity.Fragments;

import com.anewtech.phone.client.SurveyActivity.Models.services.Questionaire;

/**
 * Created by heriz on 26/12/2017.
 */

public class CommonEvent {
    private final Questionaire message;
    private int totalQuestions;

    public CommonEvent(Questionaire message,Integer totalQuestions){
        this.message = message; this.totalQuestions = totalQuestions;
    }
    public Questionaire getMessage() {
        return message;
    }
    public int getTotalQuestions(){
        return totalQuestions;
    }
}
