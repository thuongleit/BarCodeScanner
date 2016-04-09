package com.jokotech.babr.data.remote.walmartlabs;

import com.jokotech.babr.data.remote.walmartlabs.model.Walmartlabs;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ThongLe on 3/19/2016.
 */
public interface WalmartlabsService {
    @GET("items?apiKey=94tn78aagszxemmrppbjxyjp")
    Call<Walmartlabs> getProductWalmartlabs(@Query("upc") String code);
}
