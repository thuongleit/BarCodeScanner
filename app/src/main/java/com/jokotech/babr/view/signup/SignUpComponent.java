package com.jokotech.babr.view.signup;

import com.jokotech.babr.di.PerFragment;
import com.jokotech.babr.di.components.ApplicationComponent;

import dagger.Component;

@PerFragment
@Component(modules = SignUpModule.class, dependencies = ApplicationComponent.class)
public class SignUpComponent {
}
