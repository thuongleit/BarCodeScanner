package com.whooo.barscanner.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.parse.ParseUser;
import com.whooo.barscanner.di.ApplicationScope;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by thuongle on 1/30/16.
 */
@Singleton
public class Config {

    private static final String PREF_FILE_NAME = "barscanner_cfg";
    private static final String PREF_KEY_NEED_SHOW_TUTOR = "PREF_KEY_NEED_SHOW_TUTOR";
    private static final String PREF_KEY_SIGN_IN_USER = "PREF_KEY_SIGN_IN_USER";
    private static final String PREF_KEY_IS_FIRST_RUN = "PREF_KEY_IS_FIRST_RUN";

    private final SharedPreferences mSharedPreferences;

    @Inject
    public Config(@ApplicationScope Context context) {
        mSharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public boolean isFirstRun() {
        return mSharedPreferences.getBoolean(PREF_KEY_IS_FIRST_RUN, true);
    }

    public void putIsFirstRun(boolean isFirstRun) {
        mSharedPreferences.edit().putBoolean(PREF_KEY_IS_FIRST_RUN, isFirstRun).apply();
    }

    public boolean isUserLogin() {
        return (getCurrentUser() != null);
    }

    public ParseUser getCurrentUser(){
        return ParseUser.getCurrentUser();
    }
}
