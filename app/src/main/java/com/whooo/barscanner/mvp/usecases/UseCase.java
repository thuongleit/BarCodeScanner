package com.whooo.barscanner.mvp.usecases;

import com.whooo.barscanner.mvp.schedulers.ObserverOn;
import com.whooo.barscanner.mvp.schedulers.SubscribeOn;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

/**
 * Created by thuongle on 12/23/15.
 */
public abstract class UseCase<T>{

    private ObserverOn mObserverOn;
    private SubscribeOn mSubscribeOn;
    protected Subscription mSubscription = Subscriptions.empty();

    protected UseCase(ObserverOn observerOn, SubscribeOn subscribeOn) {
        this.mObserverOn = observerOn;
        this.mSubscribeOn = subscribeOn;
    }

    public void execute(Subscriber<? super T> subscriber) {
        mSubscription = buildUseCaseObservable().subscribeOn(mSubscribeOn.getSubscriber())
                .observeOn(mObserverOn.getSubscriber())
                .subscribe(subscriber);
    }

    public void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    protected abstract Observable<T> buildUseCaseObservable();
}
