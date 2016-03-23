package com.thuongleit.babr.data.remote.amazon;

import com.thuongleit.babr.data.remote.amazon.model.AmazonProductResponse;

import retrofit.http.GET;
import retrofit.http.Url;
import rx.Observable;

/**
 * Created by thuongle on 2/24/16.
 */
public interface AmazonService {

    @GET
    Observable<AmazonProductResponse> search(@Url String requestUrl);
}
