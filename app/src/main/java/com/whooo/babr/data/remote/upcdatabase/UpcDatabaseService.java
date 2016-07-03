package com.whooo.babr.data.remote.upcdatabase;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ThongLe on 3/19/2016.
 */
public interface UpcDatabaseService {

        @GET("json/93c0936673cb830e7f3d3d03bb2e4361/{code}")
        Call<UpcDatabase> getProductUpcDatabase(@Path("code") String code);

}
