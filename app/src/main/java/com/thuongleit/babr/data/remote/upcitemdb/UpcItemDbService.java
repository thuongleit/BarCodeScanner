package com.thuongleit.babr.data.remote.upcitemdb;

import com.thuongleit.babr.data.remote.upcitemdb.model.UpcItemDb;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by ThongLe on 3/19/2016.
 */
public interface UpcItemDbService {
    @GET("prod/trial/lookup")
    Call<UpcItemDb> getProductUpcItemDb(@Query("upc") String code);

}
