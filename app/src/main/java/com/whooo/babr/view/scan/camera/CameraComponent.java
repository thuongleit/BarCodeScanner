package com.whooo.babr.view.scan.camera;

import com.whooo.babr.di.ApplicationComponent;
import com.whooo.babr.di.PerFragment;

import dagger.Component;

@PerFragment
@Component(modules = CameraModule.class, dependencies = ApplicationComponent.class)
interface CameraComponent {

    void inject(FullScannerFragment fragment);
}
