package com.thuongleit.babr.data.remote.upcdatabase;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by ThongLe on 3/19/2016.
 */
public interface UpcDatabaseService {

        @GET("json/93c0936673cb830e7f3d3d03bb2e4361/{code}")
        Call<UpcDatabase> getProductUpcDatabase(@Path("code") String code);

}
