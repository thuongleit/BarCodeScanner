package com.whooo.babr.data.remote;

import android.support.annotation.NonNull;

import com.whooo.babr.vo.Product;

import java.util.List;

import rx.Observable;

public interface ParseService {

    Observable<List<Product>> searchProductsByCode(@NonNull String code);
}
