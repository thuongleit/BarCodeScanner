package com.jokotech.babr.data.remote.searchupc;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ThongLe on 3/18/2016.
 */
public interface SearchUpcService {
    @GET("upcsearch.ashx?request_type=3&access_token=DFE676EF-3D21-4E78-B91B-DB8FC9CAE154")
    Call<SearchUpc> getProductSearchUpc(@Query("upc")String code);
}
