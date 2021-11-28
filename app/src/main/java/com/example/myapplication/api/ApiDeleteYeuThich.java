package com.example.myapplication.api;

import com.example.myapplication.model.ResponseDTO;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiDeleteYeuThich {
    @FormUrlEncoded
    @POST("/app_manager/deleteYeuThich.php")
    Observable<ResponseDTO> deleteFaverite( @Field("id") int id,@Field("id_menu") int id_menu, @Field("id_user") int id_user);
}
