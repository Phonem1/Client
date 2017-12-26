package com.anewtech.phone.client.toiletFeedback.Models.reports;

import java.util.Date;

/**
 * Created by heriz on 26/12/2017.
 */

public class SurveyAnswerReport {
    public String questionid;
    public String question;
    public String answerid;
    public String answer;
    public Date createdat;
    public Date updatedat;
    public SurveyAnswerReport(){}
    public SurveyAnswerReport(String _questionid, String _question, String _answerid, String _answer){
        questionid = _questionid;
        question = _question;
        answerid = _answerid;
        answer = _answer;
    }

    @Override
    public String toString() {
        return "SurveyAnswerReport{" +
                "questionid='" + questionid + '\'' +
                ", question='" + question + '\'' +
                ", answerid='" + answerid + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}

