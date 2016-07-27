package com.whooo.babr.data.product;

import com.whooo.babr.data.remote.amazon.AmazonParseService;
import com.whooo.babr.data.remote.searchupc.SearchUpcParseService;
import com.whooo.babr.data.remote.upcdatabase.UpcDatabaseParseService;
import com.whooo.babr.data.remote.upcitemdb.UpcItemDbParseService;
import com.whooo.babr.data.remote.walmartlabs.WalmartlabsParseService;
import com.whooo.babr.vo.Product;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class SearchService {

    private final SearchUpcParseService mUpcService;
    private final WalmartlabsParseService mWalmartService;
    private final UpcItemDbParseService mUpcItemDbService;
    private final UpcDatabaseParseService mUpcDatabaseService;
    private final AmazonParseService mAmazonService;

    @Inject
    SearchService(SearchUpcParseService upcParseService,
                  WalmartlabsParseService walmartService,
                  UpcItemDbParseService upcItemDbService,
                  UpcDatabaseParseService upcDatabaseService,
                  AmazonParseService amazonService) {
        this.mUpcService = upcParseService;
        this.mWalmartService = walmartService;
        this.mUpcItemDbService = upcItemDbService;
        this.mUpcDatabaseService = upcDatabaseService;
        this.mAmazonService = amazonService;
    }

    Observable<List<Product>> searchProducts(ProductSource source, String code) {
        switch (source) {
            case SEARCH_UPC:
                return mUpcService.searchProductsByCode(code);
            case WALMART:
                return mWalmartService.searchProductsByCode(code);
            case UPC_ITEM_DB:
                return mUpcItemDbService.searchProductsByCode(code);
            case UPC_DATABASE:
                return mUpcDatabaseService.searchProductsByCode(code);
            case AMAZON:
                return mAmazonService.searchProductsByCode(code);
            case IN_APP:
            default:
                return Observable.error(new IllegalArgumentException("Need to put right product source"));
        }
    }
}
