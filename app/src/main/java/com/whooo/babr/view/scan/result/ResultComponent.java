package com.whooo.babr.view.scan.result;

import com.whooo.babr.di.ApplicationComponent;
import com.whooo.babr.di.PerActivity;

import dagger.Component;

@PerActivity
@Component(modules = ResultModule.class, dependencies = ApplicationComponent.class)
interface ResultComponent {

    ResultContract.Presenter presenter();

    void inject(ResultActivity activity);
}
