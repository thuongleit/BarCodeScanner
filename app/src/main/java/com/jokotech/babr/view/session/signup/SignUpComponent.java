package com.jokotech.babr.view.session.signup;

import com.jokotech.babr.di.PerActivity;
import com.jokotech.babr.di.components.ApplicationComponent;

import dagger.Component;

@PerActivity
@Component(modules = SignUpModule.class, dependencies = ApplicationComponent.class)
public interface SignUpComponent {
}
