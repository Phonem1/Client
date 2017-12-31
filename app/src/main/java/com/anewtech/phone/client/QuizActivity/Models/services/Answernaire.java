package com.anewtech.phone.client.QuizActivity.Models.services;

/**
 * Created by heriz on 26/12/2017.
 */
import android.support.annotation.NonNull;

public class Answernaire implements Comparable<Answernaire>{
    public int id;
    public String answerid;
    public String answer;
    public boolean sub;
    public Answernaire(int _id, String _answer, String _answerid, boolean _sub){this.id = _id; this.answer = _answer; this.answerid = _answerid; this.sub = _sub;}
    @Override
    public int compareTo(@NonNull Answernaire f) {
        if( this.id > f.id  ) {
            return 1;
        }else if (  this.id < f.id    ) {
            return -1;
        }else {
            return 0;
        }
    }
}
