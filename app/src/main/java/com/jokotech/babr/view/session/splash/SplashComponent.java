package com.jokotech.babr.view.session.splash;


import com.jokotech.babr.di.PerActivity;
import com.jokotech.babr.di.components.ApplicationComponent;

import dagger.Component;

@PerActivity
@Component(modules = SplashModule.class, dependencies = ApplicationComponent.class)
public interface SplashComponent {

    void inject(SplashActivity activity);

    SplashContract.Presenter presenter();
}
