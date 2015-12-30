package com.whooo.barscanner.injectors.modules;

import com.whooo.barscanner.injectors.PerActivity;
import com.whooo.barscanner.mvp.presenters.LoginPresenter;
import com.whooo.barscanner.mvp.presenters.LoginPresenterImpl;
import com.whooo.barscanner.mvp.presenters.SignUpPresenter;
import com.whooo.barscanner.mvp.presenters.SignUpPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by thuongle on 12/30/15.
 */
@Module
@PerActivity
public class LoginModule {

    @PerActivity
    @Provides
    LoginPresenter provideLoginPresenter() {
        return new LoginPresenterImpl();
    }

    @PerActivity
    @Provides
    SignUpPresenter provideSignUpPresenter() {
        return new SignUpPresenterImpl();
    }
}
