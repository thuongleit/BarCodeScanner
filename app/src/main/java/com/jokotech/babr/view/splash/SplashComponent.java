package com.jokotech.babr.view.splash;


import com.jokotech.babr.di.PerFragment;
import com.jokotech.babr.di.components.ApplicationComponent;

import dagger.Component;

@PerFragment
@Component(modules = SplashModule.class, dependencies = ApplicationComponent.class)
public interface SplashComponent {
}
