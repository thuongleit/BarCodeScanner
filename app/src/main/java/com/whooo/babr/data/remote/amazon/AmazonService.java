package com.whooo.babr.data.remote.amazon;

import com.whooo.babr.data.remote.amazon.model.AmazonProductResponse;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by thuongle on 2/24/16.
 */
public interface AmazonService {

    @GET
    Observable<AmazonProductResponse> search(@Url String requestUrl);
}
