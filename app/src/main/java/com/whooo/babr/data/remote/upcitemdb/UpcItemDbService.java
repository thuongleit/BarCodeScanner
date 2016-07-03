package com.whooo.babr.data.remote.upcitemdb;

import com.whooo.babr.data.remote.upcitemdb.model.UpcItemDb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ThongLe on 3/19/2016.
 */
public interface UpcItemDbService {
    @GET("prod/trial/lookup")
    Call<UpcItemDb> getProductUpcItemDb(@Query("upc") String code);

}
