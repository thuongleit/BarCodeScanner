package com.jokotech.babr.view.session.signin;

import com.jokotech.babr.di.PerFragment;
import com.jokotech.babr.di.components.ApplicationComponent;

import dagger.Component;

@PerFragment
@Component(modules = SignInModule.class, dependencies = ApplicationComponent.class)
public interface SignInComponent {
}
