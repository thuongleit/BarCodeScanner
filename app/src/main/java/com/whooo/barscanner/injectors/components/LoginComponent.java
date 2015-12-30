package com.whooo.barscanner.injectors.components;

import com.whooo.barscanner.activities.LoginActivity;
import com.whooo.barscanner.activities.SignUpActivity;
import com.whooo.barscanner.injectors.PerActivity;
import com.whooo.barscanner.injectors.modules.ActivityModule;
import com.whooo.barscanner.injectors.modules.LoginModule;
import com.whooo.barscanner.mvp.presenters.LoginPresenter;
import com.whooo.barscanner.mvp.presenters.SignUpPresenter;

import dagger.Component;

/**
 * Created by thuongle on 12/30/15.
 */
@PerActivity
@Component(modules = {ActivityModule.class, LoginModule.class}, dependencies = ApplicationComponent.class)
public interface LoginComponent extends ActivityComponent {

    LoginPresenter loginPresenter();

    SignUpPresenter signUpPresenter();
    
    void inject(LoginActivity loginActivity);

    void inject(SignUpActivity signUpActivity);
}
