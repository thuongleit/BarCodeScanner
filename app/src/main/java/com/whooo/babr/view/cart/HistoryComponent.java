package com.whooo.babr.view.cart;

import com.whooo.babr.di.PerActivity;
import com.whooo.babr.di.ApplicationComponent;

import dagger.Component;

@PerActivity
@Component(modules = HistoryModule.class,dependencies = ApplicationComponent.class)
interface HistoryComponent {
    void inject(HistoryActivity historyActivity);
}
