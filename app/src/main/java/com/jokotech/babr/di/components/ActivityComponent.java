package com.jokotech.babr.di.components;

import android.app.Activity;
import android.content.Context;

import com.jokotech.babr.di.ActivityScope;
import com.jokotech.babr.di.PerActivity;
import com.jokotech.babr.di.modules.ActivityModule;
import com.jokotech.babr.view.history.DetailActivity;
import com.jokotech.babr.view.history.HistoryActivity;
import com.jokotech.babr.view.main.MainActivity;
import com.jokotech.babr.view.product.BarViewActivity;
import com.jokotech.babr.view.scan.CameraActivity;
import com.jokotech.babr.view.scan.SearchResultActivity;
import com.jokotech.babr.view.signin.SignInActivity;

import dagger.Component;

/**
 * Created by thuongle on 12/23/15.
 */
@PerActivity
@Component(modules = ActivityModule.class, dependencies = ApplicationComponent.class)
public interface ActivityComponent {

    @ActivityScope
    Context context();

    Activity activity();

    void inject(SignInActivity activity);

    void inject(BarViewActivity activity);

    void inject(MainActivity activity);

    void inject(CameraActivity activity);

    void inject(SearchResultActivity activity);

    void inject(HistoryActivity activity);

    void inject(DetailActivity activity);
}
