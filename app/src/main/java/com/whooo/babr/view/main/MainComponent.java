package com.whooo.babr.view.main;

import com.whooo.babr.di.PerActivity;
import com.whooo.babr.di.ApplicationComponent;

import dagger.Component;

@PerActivity
@Component(modules = MainModule.class, dependencies = ApplicationComponent.class)
interface MainComponent {
    void inject(MainActivity activity);

    MainContract.Presenter presenter();

    MainViewModel handler();
}
