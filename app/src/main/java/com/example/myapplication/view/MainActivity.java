package com.example.myapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_register_now, btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_register_now = (Button) findViewById(R.id.wel_btn_register);
        btn_login = (Button) findViewById((R.id.wel_btn_login));

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        btn_register_now.setOnClickListener(this);
        btn_login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.wel_btn_register) {
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.wel_btn_login) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
    }
}