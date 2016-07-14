package com.whooo.babr.view.cart;

import com.whooo.babr.di.PerActivity;
import com.whooo.babr.di.ApplicationComponent;

import dagger.Component;

/**
 * Created by ThongLe on 7/12/2016.
 */
@PerActivity
@Component(modules = HistoryModule.class,dependencies = ApplicationComponent.class)
public interface HistoryComponent {
    void inject(HistoryActivity historyActivity);
}
