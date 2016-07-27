package com.whooo.babr.view.session.signup;

import com.whooo.babr.di.PerActivity;
import com.whooo.babr.di.ApplicationComponent;

import dagger.Component;

@PerActivity
@Component(modules = SignUpModule.class, dependencies = ApplicationComponent.class)
interface SignUpComponent {

    void inject(SignUpActivity activity);
}
