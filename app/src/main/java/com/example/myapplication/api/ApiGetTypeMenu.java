package com.example.myapplication.api;

import com.example.myapplication.model.MenuItem;
import com.example.myapplication.model.ResponseDTO;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiGetTypeMenu {
    @FormUrlEncoded
    @POST("/app_manager/getMenuType.php")
    Observable<List<MenuItem>> getMenu(@Field("type") String type);
}
