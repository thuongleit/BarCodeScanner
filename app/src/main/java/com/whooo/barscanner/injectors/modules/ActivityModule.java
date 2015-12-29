package com.whooo.barscanner.injectors.modules;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.whooo.barscanner.Constant;
import com.whooo.barscanner.injectors.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by thuongle on 12/23/15.
 */
@Module
public class ActivityModule {
    private final Context context;

    public ActivityModule(Context context) {
        this.context = context;
    }

    @Provides
    @PerActivity
    Context provideContext() {
        return this.context;
    }

    @Provides
    @PerActivity
    Activity provideActivity() {
        return (Activity) this.context;
    }

    @Provides
    @PerActivity
    SharedPreferences provideSharedPreferences() {
        return context.getSharedPreferences(Constant.PREFERENCE_KEY, Context.MODE_PRIVATE);
    }
}
