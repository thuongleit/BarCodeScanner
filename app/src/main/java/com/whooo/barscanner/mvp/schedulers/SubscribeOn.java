package com.whooo.barscanner.mvp.schedulers;

import rx.Scheduler;

/**
 * Created by thuongle on 12/23/15.
 */
public interface SubscribeOn {

    Scheduler getSubscriber();
}
