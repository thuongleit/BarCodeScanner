package com.whooo.babr.di.components;

import android.app.Application;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;
import com.whooo.babr.config.Config;
import com.whooo.babr.data.product.ProductRepository;
import com.whooo.babr.data.product.SearchService;
import com.whooo.babr.data.remote.ApiModule;
import com.whooo.babr.data.remote.ParseServiceOK;
import com.whooo.babr.data.remote.amazon.AmazonParseService;
import com.whooo.babr.data.remote.searchupc.SearchUpcParseService;
import com.whooo.babr.data.remote.upcdatabase.UpcDatabaseParseService;
import com.whooo.babr.data.remote.upcitemdb.UpcItemDbParseService;
import com.whooo.babr.data.remote.walmartlabs.WalmartlabsParseService;
import com.whooo.babr.di.ApplicationScope;
import com.whooo.babr.di.modules.ApplicationModule;
import com.whooo.babr.util.FirebaseUtils;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface ApplicationComponent {

    Application application();

    @ApplicationScope
    Context context();

    Config config();

    Picasso picasso();

    FirebaseAuth firebaseAuth();

    DatabaseReference firebaseDbRef();

    SearchService searchService();

    ProductRepository productRepository();

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

    ParseServiceOK appService();

//    FirebaseUtils firebaseUtils();
//
//    void inject(FirebaseUtils firebaseUtils);
}
