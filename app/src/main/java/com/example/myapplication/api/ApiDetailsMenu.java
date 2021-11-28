package com.example.myapplication.api;

import com.example.myapplication.model.MenuItem;

import java.util.List;


import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiDetailsMenu {
    @FormUrlEncoded
    @POST("/app_manager/getDetailsMenu.php")
    Observable<List<MenuItem>> getDetails(@Field("id") int id);
}
