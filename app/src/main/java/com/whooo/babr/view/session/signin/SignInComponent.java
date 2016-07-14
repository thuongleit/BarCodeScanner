package com.whooo.babr.view.session.signin;

import com.whooo.babr.di.PerActivity;
import com.whooo.babr.di.ApplicationComponent;

import dagger.Component;

@PerActivity
@Component(modules = SignInModule.class, dependencies = ApplicationComponent.class)
public interface SignInComponent {

    SignInContract.Presenter presenter();

    void inject(SignInActivity activity);
}
