package com.anewtech.phone.client.QuizActivity.Services;

import com.anewtech.phone.client.QuizActivity.Models.reports.SurveyAnswerReport;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by heriz on 26/12/2017.
 */

public class SurveyReportService {
    private FirebaseFirestore db;
    private String COLLECTION_NAME = "surveyfeedbacks";

    public SurveyReportService(){
        db = FirebaseFirestore.getInstance();
    }

    public void reportToFirebase(SurveyAnswerReport msar){
        if ( msar != null){
            timeStamp(msar);
            db.collection(COLLECTION_NAME).add(msar);
        }
    }

    private void timeStamp(SurveyAnswerReport msar){
        if ( msar != null ) {
            Date currentTime = Calendar.getInstance().getTime();
            msar.createdat =  currentTime;
            msar.updatedat = currentTime;
        }

    }
}
