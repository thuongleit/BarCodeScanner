package com.whooo.babr.view.scan.result;

import com.whooo.babr.di.PerActivity;
import com.whooo.babr.di.components.ApplicationComponent;

import dagger.Component;

@PerActivity
@Component(modules = ResultModule.class, dependencies = ApplicationComponent.class)
public interface ResultComponent {

    ResultContract.Presenter presenter();

    void inject(ResultActivity activity);
}
