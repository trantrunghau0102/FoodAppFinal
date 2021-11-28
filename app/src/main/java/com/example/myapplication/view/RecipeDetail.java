package com.example.myapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.example.myapplication.R;

public class RecipeDetail extends AppCompatActivity {
    Toolbar toolbardetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
    }
    private void ActionToolbar() {
        setSupportActionBar(toolbardetail);
        //nút home
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbardetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}