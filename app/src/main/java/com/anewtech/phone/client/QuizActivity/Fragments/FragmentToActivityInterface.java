package com.anewtech.phone.client.QuizActivity.Fragments;

import com.anewtech.phone.client.QuizActivity.Models.services.Answernaire;

/**
 * Created by heriz on 26/12/2017.
 */

public interface FragmentToActivityInterface {
    public void OnItemPickedInFragment(int position, Answernaire subAnswer);
}
