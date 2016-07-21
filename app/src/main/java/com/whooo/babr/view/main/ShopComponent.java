package com.whooo.babr.view.main;

import com.whooo.babr.di.PerActivity;
import com.whooo.babr.di.ApplicationComponent;

import dagger.Component;

@PerActivity
@Component(modules = ShopModule.class, dependencies = ApplicationComponent.class)
interface ShopComponent {
    void inject(MainActivity activity);

    ShopContract.Presenter presenter();

    ShopViewModel handler();

    void inject(ShopFragment shop);
}
