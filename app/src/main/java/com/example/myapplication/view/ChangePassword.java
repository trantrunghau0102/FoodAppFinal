package com.example.myapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.api.APiChangePassword;
import com.example.myapplication.api.ApiRegisterUser;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.model.ResponseDTO;
import com.example.myapplication.utils.Contants;
import com.example.myapplication.utils.StoreUtils;
import com.kaopiz.kprogresshud.KProgressHUD;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ChangePassword extends AppCompatActivity {

    EditText forgot_edit_txt_current_passwword;
    EditText forgot_edit_txt_new_passwword;
    EditText reg_edit_txt_confirm_new_passwword;
    Button forgot_btn_save;
    private KProgressHUD hud;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        forgot_edit_txt_current_passwword = findViewById(R.id.forgot_edit_txt_current_passwword);
        forgot_edit_txt_new_passwword = findViewById(R.id.forgot_edit_txt_new_passwword);
        reg_edit_txt_confirm_new_passwword = findViewById(R.id.reg_edit_txt_confirm_new_passwword);
        forgot_btn_save = findViewById(R.id.forgot_btn_save);
        imgBack = findViewById(R.id.imgBackChangePass);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_status));

        forgot_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass_current = forgot_edit_txt_current_passwword.getText().toString();
                String new_passwword = forgot_edit_txt_new_passwword.getText().toString();
                String confirm_new_passwword = reg_edit_txt_confirm_new_passwword.getText().toString();

                if (pass_current.equals("") && new_passwword.equals("") && confirm_new_passwword.equals("")) {
                    Toast.makeText(ChangePassword.this, "Vui lòng nhập mật khẩu tài khoản !!!", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass_current.equals("")) {
                        Toast.makeText(ChangePassword.this, "Vui lòng nhập mật khẩu hiện tại !!!", Toast.LENGTH_SHORT).show();
                    } else if (new_passwword.equals("")) {
                        Toast.makeText(ChangePassword.this, "Vui lòng nhập mật khẩu mới !!!", Toast.LENGTH_SHORT).show();
                    } else if (confirm_new_passwword.equals("")) {
                        Toast.makeText(ChangePassword.this, "Vui lòng nhập xác nhận mật khẩu !!!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!new_passwword.equals(confirm_new_passwword)) {
                            Toast.makeText(ChangePassword.this, "Vui lòng kiểm tra lại mật khẩu !!!", Toast.LENGTH_SHORT).show();
                        } else {
                            hud = KProgressHUD.create(ChangePassword.this)
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
                                            .create(APiChangePassword.class)
                                            .postPassword(Integer.parseInt(StoreUtils.get(ChangePassword.this, Contants.Id)), new_passwword)
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.newThread())
                                            .subscribe(new Consumer<ResponseDTO>() {
                                                @Override
                                                public void accept(ResponseDTO responseDTO) throws Exception {

                                                }
                                            }, new Consumer<Throwable>() {
                                                @Override
                                                public void accept(Throwable throwable) throws Exception {

                                                }
                                            });
                                    compositeDisposable.add(disposable);

                                    Toast.makeText(ChangePassword.this, "Thành công !!!", Toast.LENGTH_SHORT).show();
                                    hud.dismiss();
                                }
                            }, 2000);

                        }
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ChangePassword.this.finish();
    }
}