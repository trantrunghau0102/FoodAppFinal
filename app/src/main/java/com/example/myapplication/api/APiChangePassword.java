package com.example.myapplication.api;

import com.example.myapplication.model.ResponseDTO;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APiChangePassword {
    @FormUrlEncoded
    @POST("/app_manager/changePass.php")
    Observable<ResponseDTO> postPassword(@Field("id") int id, @Field("password") String pass);
}
