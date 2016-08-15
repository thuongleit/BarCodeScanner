package com.whooo.babr.data.product;

import com.whooo.babr.data.remote.InAppService;
import com.whooo.babr.data.remote.amazon.AmazonParseService;
import com.whooo.babr.data.remote.searchupc.SearchUpcParseService;
import com.whooo.babr.data.remote.upcdatabase.UpcDatabaseParseService;
import com.whooo.babr.data.remote.upcitemdb.UpcItemDbParseService;
import com.whooo.babr.data.remote.walmartlabs.WalmartlabsParseService;
import com.whooo.babr.vo.Product;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import timber.log.Timber;

@Singleton
public class SearchService {

    private final SearchUpcParseService mUpcService;
    private final WalmartlabsParseService mWalmartService;
    private final UpcItemDbParseService mUpcItemDbService;
    private final UpcDatabaseParseService mUpcDatabaseService;
    private final AmazonParseService mAmazonService;
    private final InAppService mInAppService;

    @Inject
    SearchService(SearchUpcParseService upcParseService,
                  WalmartlabsParseService walmartService,
                  UpcItemDbParseService upcItemDbService,
                  UpcDatabaseParseService upcDatabaseService,
                  AmazonParseService amazonService,
                  InAppService inAppService) {
        this.mUpcService = upcParseService;
        this.mWalmartService = walmartService;
        this.mUpcItemDbService = upcItemDbService;
        this.mUpcDatabaseService = upcDatabaseService;
        this.mAmazonService = amazonService;
        this.mInAppService = inAppService;
    }

    Observable<List<Product>> searchProducts(ProductSource source, String code) {
        Timber.d("Searching with %s for %s", source.getDisplay(), code);
        Observable<List<Product>> observable;
        switch (source) {
            case SEARCH_UPC:
                observable = mUpcService.searchProductsByCode(code);
                break;
            case WALMART:
                observable = mWalmartService.searchProductsByCode(code);
                break;
            case UPC_ITEM_DB:
                observable = mUpcItemDbService.searchProductsByCode(code);
                break;
            case UPC_DATABASE:
                observable = mUpcDatabaseService.searchProductsByCode(code);
                break;
            case AMAZON:
                observable = mAmazonService.searchProductsByCode(code);
                break;
            case IN_APP:
                observable = mInAppService.searchProductsByCode(code);
                break;
            default:
                return Observable.error(new IllegalArgumentException("Need to put right product source"));
        }

        if (observable != null) {
            observable
                    .onErrorResumeNext(Observable.just(Collections.emptyList()));
        }
        return observable != null ? observable : Observable.just(Collections.emptyList());
    }
}
