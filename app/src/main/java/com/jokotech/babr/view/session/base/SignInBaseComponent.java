package com.jokotech.babr.view.session.base;

import com.jokotech.babr.di.PerActivity;
import com.jokotech.babr.di.components.ApplicationComponent;

import dagger.Component;

@PerActivity
@Component(modules = SignInBaseModule.class, dependencies = ApplicationComponent.class)
public interface SignInBaseComponent {

    SignInBaseContract.Presenter presenter();

    void inject(MainSignInActivity activity);
}
