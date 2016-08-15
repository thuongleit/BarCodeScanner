package com.whooo.babr.di;

import android.app.Application;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.whooo.babr.config.Config;
import com.whooo.babr.data.cart.CartRepository;
import com.whooo.babr.data.product.ProductRepository;
import com.whooo.babr.data.remote.ApiModule;
import com.whooo.babr.data.remote.DebugModule;
import com.whooo.babr.util.CircleTransform;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, DebugModule.class, ApiModule.class})
public interface ApplicationComponent extends android.databinding.DataBindingComponent {

    Application application();

    @ApplicationScope
    Context context();

    Config config();

    Picasso picasso();

    FirebaseAuth firebaseAuth();

    ProductRepository productRepository();

    CartRepository cartRepository();

    CircleTransform imageTransform();
}
