package com.whooo.babr.view.scan.camera;

import com.whooo.babr.di.PerActivity;
import com.whooo.babr.di.components.ApplicationComponent;

import dagger.Component;

@PerActivity
@Component(modules = CameraModule.class, dependencies = ApplicationComponent.class)
public interface CameraComponent {
    void inject(CameraActivity activity);
}
