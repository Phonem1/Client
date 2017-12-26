package com.anewtech.phone.client.toiletFeedback.Models.data;

import android.arch.lifecycle.ViewModel;

import java.util.List;

/**
 * Created by heriz on 26/12/2017.
 */

public class SurveyDataModel extends ViewModel {
    public String version;
    public String title;
    public String datetime;
    public String uploadedby;
    public String access;
    public Template template;
    public List<Question> layersquestions;
    public List<Answer> layersanswers;
    public List<FinalMessage> layerslastmessages;
}
