package com.whooo.babr.view.session.base;

import com.whooo.babr.di.PerActivity;
import com.whooo.babr.di.ApplicationComponent;

import dagger.Component;

@PerActivity
@Component(modules = SignInBaseModule.class, dependencies = ApplicationComponent.class)
public interface SignInBaseComponent {

    SignInBaseContract.Presenter presenter();

    void inject(MainSignInActivity activity);
}
