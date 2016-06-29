package com.jokotech.babr.view.main;

import com.jokotech.babr.di.PerActivity;
import com.jokotech.babr.di.components.ApplicationComponent;

import dagger.Component;

@PerActivity
@Component(modules = MainModule.class, dependencies = ApplicationComponent.class)
public interface MainComponent {
}
