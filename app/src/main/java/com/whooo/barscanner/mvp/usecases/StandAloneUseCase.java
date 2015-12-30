package com.whooo.barscanner.mvp.usecases;

/**
 * Created by thuongle on 12/30/15.
 */
public abstract class StandAloneUseCase<T> {

    public StandAloneUseCase() {
    }

    public abstract T execute(Object... parameters);
}
