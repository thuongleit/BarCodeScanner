package com.jokotech.babr.view.session.signin;

import com.jokotech.babr.di.PerActivity;
import com.jokotech.babr.di.components.ApplicationComponent;

import dagger.Component;

@PerActivity
@Component(modules = SignInModule.class, dependencies = ApplicationComponent.class)
public interface SignInComponent {

    SignInContract.Presenter presenter();

    void inject(SignInActivity activity);
}
