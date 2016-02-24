package com.thuongleit.babr.data.remote;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by thuongle on 2/24/16.
 */
public interface AmazonService {

    @GET("xml")
    Observable itemLookUp(@Query("AWSAccessKeyId") String awsAccessKeyId,
                          @Query("AssociateTag") String associateTag,
                          @Query("Operation") String operation,
                          @Query("ItemId") String itemId,
                          @Query("SearchIndex") String searchIndex,
                          @Query("IdType") String idType,
                          @Query("Timestamp") String timestamp,
                          @Query("Signature") String signature);
}
