package com.example.myapplication.api;

import android.view.Menu;

import com.example.myapplication.model.MenuItem;
import com.example.myapplication.model.Users;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ApiMenu {
    @GET("/app_manager/getMenu.php")
    Observable<List<MenuItem>> getMenu();
}
