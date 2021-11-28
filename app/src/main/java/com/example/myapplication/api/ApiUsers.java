package com.example.myapplication.api;

import com.example.myapplication.model.Users;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ApiUsers {
    @GET("/app_manager/getUser.php")
    Observable<List<Users>> getUser();
}
