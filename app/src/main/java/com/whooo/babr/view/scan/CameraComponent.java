package com.whooo.babr.view.scan;

import com.whooo.babr.di.PerActivity;
import com.whooo.babr.di.components.ApplicationComponent;

import dagger.Component;

@PerActivity
@Component(modules = CameraModule.class, dependencies = ApplicationComponent.class)
interface CameraComponent {
    void inject(CameraActivity activity);

    CameraContract.Presenter presenter();
}
