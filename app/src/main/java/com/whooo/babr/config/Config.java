package com.whooo.babr.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.whooo.babr.di.ApplicationScope;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by thuongle on 1/30/16.
 */
@Singleton
public class Config {

    private static final String PREF_FILE_NAME = "babr_cfg";
    private static final String PREF_KEY_NEED_SHOW_TUTOR = "PREF_KEY_NEED_SHOW_TUTOR";
    private static final String PREF_KEY_IS_FIRST_RUN = "PREF_KEY_IS_FIRST_RUN";
    private static final String PREF_KEY_IS_DONT_SHOW = "PREF_KEY_IS_DONT_SHOW";
    private static final String PREF_NAME_HISTORY = "PREF_NAME_HISTORY";
    private static final String PREF_KEY_SIGN_IN_TYPE = "PREF_KEY_SIGN_IN_TYPE";
    private static final String PREF_KEY_SIGN_IN_USER = "PREF_KEY_SIGN_IN_USER";
    private static final String PREF_KEY_USER_ID = "PREF_KEY_USER_ID";

    private final SharedPreferences mSharedPreferences;

    @Inject
    public Config(@ApplicationScope Context context) {
        mSharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void putUserId(String userId) {
        mSharedPreferences.edit().putString(PREF_KEY_USER_ID, userId).apply();
    }

    public String getUserId() {
        return mSharedPreferences.getString(PREF_KEY_USER_ID, "");
    }

    public boolean isFirstRun() {
        return mSharedPreferences.getBoolean(PREF_KEY_IS_FIRST_RUN, true);
    }

    public void putIsFirstRun(boolean isFirstRun) {
        mSharedPreferences.edit().putBoolean(PREF_KEY_IS_FIRST_RUN, isFirstRun).apply();
    }

    public void putIsDontShow(boolean isDontShow) {
        mSharedPreferences.edit().putBoolean(PREF_KEY_IS_DONT_SHOW, isDontShow).apply();

    }

    public boolean isIsDontShow() {
        return mSharedPreferences.getBoolean(PREF_KEY_IS_DONT_SHOW, false);
    }


    public void putIsSignIn(boolean isSignIn) {
        mSharedPreferences.edit().putBoolean(PREF_KEY_SIGN_IN_USER, isSignIn).apply();
    }

    public boolean getIsSignIn() {
        return mSharedPreferences.getBoolean(PREF_KEY_SIGN_IN_USER, false);

    }

    public void putSignInType(int type) {
        mSharedPreferences.edit().putInt(PREF_KEY_SIGN_IN_TYPE, type).apply();

    }

    public int getSignInType() {
        return mSharedPreferences.getInt(PREF_KEY_SIGN_IN_TYPE, -1);
    }

}
