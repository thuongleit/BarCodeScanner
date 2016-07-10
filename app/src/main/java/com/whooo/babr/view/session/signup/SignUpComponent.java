package com.whooo.babr.view.session.signup;

import com.whooo.babr.di.PerActivity;
import com.whooo.babr.di.components.ApplicationComponent;

import dagger.Component;

@PerActivity
@Component(modules = SignUpModule.class, dependencies = ApplicationComponent.class)
public interface SignUpComponent {

    SignUpContract.Presenter presenter();

    void inject(SignUpActivity activity);
}
