package com.thuongleit.babr.data.remote.walmartlabs;

import com.thuongleit.babr.data.remote.walmartlabs.model.Walmartlabs;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by ThongLe on 3/19/2016.
 */
public interface WalmartlabsService {
    @GET("items?apiKey=94tn78aagszxemmrppbjxyjp")
    Call<Walmartlabs> getProductWalmartlabs(@Query("upc") String code);
}
