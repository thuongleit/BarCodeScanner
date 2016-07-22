package com.whooo.babr.view.cart.detail;

import com.whooo.babr.di.ApplicationComponent;
import com.whooo.babr.di.PerActivity;

import dagger.Component;

@PerActivity
@Component(modules = DetailModule.class, dependencies = ApplicationComponent.class)
interface DetailComponent {
    void inject(DetailActivity activity);
}
