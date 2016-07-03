package com.whooo.babr.view.main;

import com.whooo.babr.di.PerActivity;
import com.whooo.babr.di.components.ApplicationComponent;

import dagger.Component;

@PerActivity
@Component(modules = MainModule.class, dependencies = ApplicationComponent.class)
public interface MainComponent {
}
