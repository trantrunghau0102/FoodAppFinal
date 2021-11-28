package com.example.myapplication.api;

import com.example.myapplication.model.YeuThichItem;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;

public interface ApiGetYeuthich {
    @GET("/app_manager/getFaverite.php")
    Observable<List<YeuThichItem>> getYeuThich();
}
