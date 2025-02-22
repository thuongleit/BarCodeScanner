package com.whooo.babr.view.session.splash;


import com.whooo.babr.di.PerActivity;
import com.whooo.babr.di.ApplicationComponent;

import dagger.Component;

@PerActivity
@Component(modules = SplashModule.class, dependencies = ApplicationComponent.class)
interface SplashComponent {

    void inject(SplashActivity activity);
    
}
