package com.thuongleit.babr.di.components;

import android.app.Activity;
import android.content.Context;

import com.thuongleit.babr.di.ActivityScope;
import com.thuongleit.babr.di.PerActivity;
import com.thuongleit.babr.di.modules.ActivityModule;
import com.thuongleit.babr.view.history.DetailActivity;
import com.thuongleit.babr.view.history.HistoryActivity;
import com.thuongleit.babr.view.main.MainActivity;
import com.thuongleit.babr.view.product.BarViewActivity;
import com.thuongleit.babr.view.scan.CameraActivity;
import com.thuongleit.babr.view.scan.SearchResultActivity;
import com.thuongleit.babr.view.signin.SignInActivity;
import com.thuongleit.babr.view.signin.SignInWithEmailActivity;
import com.thuongleit.babr.view.signup.SignUpActivity;

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

    void inject(SignUpActivity activity);

    void inject(BarViewActivity activity);

    void inject(MainActivity activity);

    void inject(CameraActivity activity);

    void inject(SearchResultActivity activity);

    void inject(SignInWithEmailActivity activity);

    void inject(HistoryActivity activity);

    void inject(DetailActivity activity);
}
