package com.example.myapplication.api;

import com.example.myapplication.model.ResponseDTO;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiRegisterUser {
    @FormUrlEncoded
    @POST("/app_manager/register_user.php")
    Observable<ResponseDTO> register(@Field("username") String username, @Field("email") String email, @Field("password") String password);
}
