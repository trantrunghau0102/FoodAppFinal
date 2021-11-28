package com.example.myapplication.api;

import android.view.Menu;

import com.example.myapplication.model.MenuItem;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiSearch {
    @FormUrlEncoded
    @POST("/app_manager/searchMenu.php")
    Observable<List<MenuItem>> getSearch(@Field("TenMon") String TenMon);
}
