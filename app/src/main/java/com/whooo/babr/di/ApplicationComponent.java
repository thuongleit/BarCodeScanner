package com.whooo.babr.di;

import android.app.Application;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.whooo.babr.data.remote.DebugModule;
import com.squareup.picasso.Picasso;
import com.whooo.babr.config.Config;
import com.whooo.babr.data.cart.CartRepository;
import com.whooo.babr.data.product.ProductRepository;
import com.whooo.babr.data.product.SearchService;
import com.whooo.babr.data.remote.ApiModule;
import com.whooo.babr.data.remote.amazon.AmazonParseService;
import com.whooo.babr.data.remote.searchupc.SearchUpcParseService;
import com.whooo.babr.data.remote.upcdatabase.UpcDatabaseParseService;
import com.whooo.babr.data.remote.upcitemdb.UpcItemDbParseService;
import com.whooo.babr.data.remote.walmartlabs.WalmartlabsParseService;
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

    SearchService searchService();

    ProductRepository productRepository();

    CartRepository cartRepository();

    AmazonParseService.RetrofitService amazonService();

    SearchUpcParseService.RetrofitService searchUpcService();

    UpcDatabaseParseService.RetrofitService upcDbService();

    UpcItemDbParseService.RetrofitService upcItemDbService();

    WalmartlabsParseService.RetrofitService walmartService();

    AmazonParseService amazonParseService();

    SearchUpcParseService searchUpcParseService();

    UpcDatabaseParseService upcDbParseService();

    UpcItemDbParseService upcItemDbParseService();

    WalmartlabsParseService walmartParseService();

    CircleTransform imageTransform();

//    FirebaseUtils firebaseUtils();
//
//    void inject(FirebaseUtils firebaseUtils);
}
