package com.example.myapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.api.ApiRegisterUser;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.model.ResponseDTO;
import com.kaopiz.kprogresshud.KProgressHUD;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Register extends AppCompatActivity {
    Button btnRegister;
    EditText edtUsername, edtPassWord, edtEmail, edtConfirmPass;
    CheckBox reg_checkBox;
    private KProgressHUD hud;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_status));

        inti();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = edtUsername.getText().toString();
                String password = edtPassWord.getText().toString();
                String email = edtEmail.getText().toString();
                String confirmPass = edtConfirmPass.getText().toString();

                if (username.equals("") && email.equals("") && password.equals("") && confirmPass.equals("") && confirmPass.equals("")) {
                    Toast.makeText(Register.this, "Vui lòng không được để trống !!!", Toast.LENGTH_SHORT).show();
                } else if (username.equals("")) {
                    Toast.makeText(Register.this, "Tên đăng nhập không được để trống !!!", Toast.LENGTH_SHORT).show();
                } else if (email.equals("")) {
                    Toast.makeText(Register.this, "Email không được để trống !!!", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(Register.this, "Mật khẩu không được để trống !!!", Toast.LENGTH_SHORT).show();
                } else if (confirmPass.equals("")) {
                    Toast.makeText(Register.this, "Xác nhận mật khẩu không được để trống !!!", Toast.LENGTH_SHORT).show();
                } else {
                    if (reg_checkBox.isChecked()) {
                        if (confirmPass.equals(password)) {
                            if (email.contains("@") && email.contains(".")) {
                                try {
                                    hud = KProgressHUD.create(Register.this)
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
                                                    .create(ApiRegisterUser.class)
                                                    .register(username, email, password)
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribeOn(Schedulers.newThread())
                                                    .subscribe(new Consumer<ResponseDTO>() {
                                                        @Override
                                                        public void accept(ResponseDTO responseBody) throws Exception {
                                                        }
                                                    }, new Consumer<Throwable>() {
                                                        @Override
                                                        public void accept(Throwable throwable) throws Exception {
                                                            throwable.printStackTrace();
                                                        }
                                                    });
                                            compositeDisposable.add(disposable);

                                            Toast.makeText(Register.this, "Thành công !!!", Toast.LENGTH_SHORT).show();
                                            onBackPressed();

                                            hud.dismiss();
                                        }
                                    }, 2000);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Toast.makeText(Register.this, "Email chưa đúng định dạng !!!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Register.this, "Nhập lại mật khẩu !!!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Register.this, " Vui lòng đồng ý các điều khoản !!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void inti() {
        imgBack = findViewById(R.id.imgBackRegister);
        reg_checkBox = findViewById(R.id.reg_checkBox);
        btnRegister = findViewById(R.id.reg_btn_createAccount);
        edtUsername = findViewById(R.id.reg_edit_txt_username);
        edtEmail = findViewById(R.id.reg_edit_txt_email);
        edtPassWord = findViewById(R.id.reg_edit_txt_passwword);
        edtConfirmPass = findViewById(R.id.reg_edit_txt_confirm_passwword);
    }
}