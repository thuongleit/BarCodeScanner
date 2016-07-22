package com.whooo.babr.view.cart;

import com.whooo.babr.di.ApplicationComponent;
import com.whooo.babr.di.PerFragment;

import dagger.Component;

@PerFragment
@Component(modules = CartModule.class,dependencies = ApplicationComponent.class)
interface CartComponent {
    void inject(CartFragment cartFragment);
}
