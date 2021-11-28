package com.example.myapplication.api;

import com.example.myapplication.model.MenuItem;
import com.example.myapplication.model.ResponseDTO;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiDeleteMenu {
    @FormUrlEncoded
    @POST("/app_manager/deleteMenu.php")
    Observable<List<MenuItem>> deleteMenu(@Field("id") int id);
}
