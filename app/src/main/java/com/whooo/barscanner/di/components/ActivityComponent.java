package com.whooo.barscanner.di.components;

import android.app.Activity;
import android.content.Context;

import com.whooo.barscanner.di.ActivityScope;
import com.whooo.barscanner.di.PerActivity;
import com.whooo.barscanner.di.modules.ActivityModule;
import com.whooo.barscanner.view.product.BarViewActivity;
import com.whooo.barscanner.view.scan.CameraFragment;
import com.whooo.barscanner.view.signin.SignInActivity;
import com.whooo.barscanner.view.signup.SignUpActivity;

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

    void inject(CameraFragment fragment);

    void inject(BarViewActivity activity);
}
