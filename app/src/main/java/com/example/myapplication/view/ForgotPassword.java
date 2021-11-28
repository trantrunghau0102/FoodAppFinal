package com.example.myapplication.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.utils.JavaMailAPI;
import com.example.myapplication.R;
import com.example.myapplication.api.ApiUsers;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.model.Users;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ForgotPassword extends AppCompatActivity {
    Button btnSend;
    EditText edtEmail;
    private KProgressHUD hud;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        inti();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senEmail();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void inti() {
        imgBack = findViewById(R.id.imgBackForgot);
        btnSend = findViewById(R.id.forgot_btn_send_new_password);
        edtEmail = findViewById(R.id.forgot_edit_txt_email);
    }

    private void senEmail() {
        String mEmail = edtEmail.getText().toString();
        String mSubject = "Forgot password you app ";
        String mMessage = "";

        hud = KProgressHUD.create(ForgotPassword.this)
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
                                int dem = 0;
                                for (int i = 0; i < users.size(); i++) {
                                    if (mEmail.equals(users.get(i).getEmail())) {
                                        JavaMailAPI javaMailAPI = new JavaMailAPI(ForgotPassword.this, mEmail, mSubject, "My password is : " + users.get(i).getPassword());
                                        javaMailAPI.execute();
                                        Toast.makeText(ForgotPassword.this, "Vui lòng kiểm tra email để kiểm tra mật khẩu !!!", Toast.LENGTH_SHORT).show();
                                        break;
                                    } else {
                                        dem++;
                                    }
                                }

                                if (dem == users.size()) {
                                    Toast.makeText(ForgotPassword.this, "Email không tồn tại !!!", Toast.LENGTH_SHORT).show();
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
}