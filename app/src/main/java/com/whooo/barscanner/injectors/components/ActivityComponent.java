package com.whooo.barscanner.injectors.components;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.whooo.barscanner.injectors.PerActivity;
import com.whooo.barscanner.injectors.modules.ActivityModule;

import dagger.Component;

/**
 * Created by thuongle on 12/23/15.
 */
@PerActivity
@Component(modules = ActivityModule.class, dependencies = ApplicationComponent.class)
public interface ActivityComponent {

    Context context();
    
    SharedPreferences sharedPreferences();

    Activity activity();

}
