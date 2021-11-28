package com.example.myapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.api.ApiUsers;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.model.Users;
import com.example.myapplication.utils.Contants;
import com.example.myapplication.utils.StoreUtils;
import com.example.myapplication.utils.Utils;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button btnRegister;
    Button btnLogin;
    TextView txtForgot;
    TextView txtUserName;
    TextView txtPassword;
    private KProgressHUD hud;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        if (!StoreUtils.get(Login.this, Contants.username).equals("") && !StoreUtils.get(Login.this, Contants.password).equals("")) {
            txtUserName.setText(StoreUtils.get(Login.this, Contants.username));
            txtPassword.setText(StoreUtils.get(Login.this, Contants.password));
        }

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                Login.this.finish();
            }
        });
    }

    public void init() {
        imgBack = findViewById(R.id.imgBackLogin);
        btnRegister = findViewById(R.id.login_btn_createAccount);
        txtForgot = findViewById(R.id.txtForgot);
        btnLogin = findViewById(R.id.login_btn_login);
        txtUserName = findViewById(R.id.login_edit_txt_username);
        txtPassword = findViewById(R.id.login_edit_txt_password);

        txtForgot.setOnClickListener(Login.this);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(Login.this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.login_btn_createAccount:
                intent = new Intent(Login.this, Register.class);
                break;
            case R.id.txtForgot:
                intent = new Intent(Login.this, ForgotPassword.class);
                break;
            case R.id.login_btn_login:
                login();
                break;

            default:
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }

    public void login() {

        String username = txtUserName.getText().toString();
        String password = txtPassword.getText().toString();

        Log.i("TAG", "login: " + username + "--" + password);

        hud = KProgressHUD.create(Login.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CompositeDisposable compositeDisposable = new CompositeDisposable();
                Disposable disposable = RetrofitClient.getClient()
                        .create(ApiUsers.class)
                        .getUser()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new Consumer<List<Users>>() {
                            @Override
                            public void accept(List<Users> users) throws Exception {
                                Log.i("TAG", "accept: " + users.size());
                                int dem = 0;
                                for (int i = 0; i < users.size(); i++) {
                                    if (users.get(i).getUsername().equals(username) && users.get(i).getPassword().equals(password)) {

                                        StoreUtils.save(Login.this, Contants.Id, users.get(i).getId() + "");
                                        StoreUtils.save(Login.this, Contants.username, users.get(i).getUsername());
                                        StoreUtils.save(Login.this, Contants.password, users.get(i).getPassword());

                                        Intent intent = new Intent(Login.this, HomeActivity.class);
                                        startActivity(intent);

                                        Login.this.finish();

                                        break;
                                    } else {
                                        dem++;
                                    }
                                }

                                if (dem == users.size()) {
                                    Toast.makeText(Login.this, "Vui lòng nhập lại tài khoản mạt khẩu !!!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        });
                compositeDisposable.add(disposable);

                hud.dismiss();
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        Login.this.finish();
    }
}