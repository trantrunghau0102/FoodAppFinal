package com.example.myapplication.api;

import com.example.myapplication.model.ResponseDTO;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiUpdateMenu {
    @FormUrlEncoded
    @POST("/app_manager/updateMenu.php")
    Observable<ResponseDTO> updateMenu(@Field("id") int id, @Field("id_user") int id_user,
                                       @Field("favorite") boolean favorite);
}
