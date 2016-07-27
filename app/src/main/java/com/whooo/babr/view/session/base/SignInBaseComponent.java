package com.whooo.babr.view.session.base;

import com.whooo.babr.di.PerActivity;
import com.whooo.babr.di.ApplicationComponent;

import dagger.Component;

@PerActivity
@Component(modules = SignInBaseModule.class, dependencies = ApplicationComponent.class)
interface SignInBaseComponent {

    void inject(MainSignInActivity activity);
}
