package com.thuongleit.babr.data.remote.searchupc;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by ThongLe on 3/18/2016.
 */
public interface SearchUpcService {
    @GET("upcsearch.ashx?request_type=3&access_token=DFE676EF-3D21-4E78-B91B-DB8FC9CAE154")
    Call<SearchUpc> getProductSearchUpc(@Query("upc")String code);
}
