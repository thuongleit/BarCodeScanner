package com.whooo.barscanner.di.modules;

import android.app.Activity;
import android.content.Context;

import com.whooo.barscanner.di.ActivityScope;
import com.whooo.barscanner.di.PerActivity;

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
    @ActivityScope
    Context provideContext() {
        return this.context;
    }

    @Provides
    @PerActivity
    Activity provideActivity() {
        return (Activity) this.context;
    }
}
