package com.example.myapplication.api;

import com.example.myapplication.model.ResponseDTO;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInsertFaverite {
    @FormUrlEncoded
    @POST("/app_manager/insertFaverite.php")
    Observable<ResponseDTO> insertFaverite(@Field("id_menu") int id_menu, @Field("TenMon") String TenMon, @Field("LinkAnh") String LinkAnh, @Field("MoTa") String MoTa,
                                           @Field("NguyenLieu") String NguyenLieu, @Field("SoChe") String SoChe, @Field("CachNau") String CachNau, @Field("id_user") int id_user,
                                           @Field("faverite") boolean favorite);
}
