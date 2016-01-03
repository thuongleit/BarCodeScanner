package com.whooo.barscanner.injectors.components;

import com.whooo.barscanner.activities.LoginActivity;
import com.whooo.barscanner.activities.SignUpActivity;
import com.whooo.barscanner.fragments.CameraFragment;
import com.whooo.barscanner.injectors.PerActivity;
import com.whooo.barscanner.injectors.modules.ActivityModule;
import com.whooo.barscanner.injectors.modules.JobsModule;
import com.whooo.barscanner.mvp.presenters.LoginPresenter;
import com.whooo.barscanner.mvp.presenters.ScanQrCodePresenter;
import com.whooo.barscanner.mvp.presenters.SignUpPresenter;

import dagger.Component;

/**
 * Created by thuongle on 12/30/15.
 */
@PerActivity
@Component(modules = {ActivityModule.class, JobsModule.class}, dependencies = ApplicationComponent.class)
public interface JobsComponent extends ActivityComponent {

    LoginPresenter loginPresenter();

    SignUpPresenter signUpPresenter();
    
    ScanQrCodePresenter scanQrCodePresenter();
    
    void inject(LoginActivity loginActivity);

    void inject(SignUpActivity signUpActivity);

    void inject(CameraFragment cameraFragment);
}
