package com.example.myapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.SendDataMenu;
import com.example.myapplication.adapter.AdapterMenu;
import com.example.myapplication.api.ApiDeleteYeuThich;
import com.example.myapplication.api.ApiDetailsMenu;
import com.example.myapplication.api.ApiGetYeuthich;
import com.example.myapplication.api.ApiInsertFaverite;
import com.example.myapplication.api.ApiMenu;
import com.example.myapplication.api.ApiRegisterUser;
import com.example.myapplication.api.ApiUpdateMenu;
import com.example.myapplication.api.ApiUsers;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.databinding.ActivityDescriptionBinding;
import com.example.myapplication.model.MenuItem;
import com.example.myapplication.model.ResponseDTO;
import com.example.myapplication.model.Users;
import com.example.myapplication.model.YeuThichItem;
import com.example.myapplication.utils.Contants;
import com.example.myapplication.utils.JavaMailAPI;
import com.example.myapplication.utils.StoreUtils;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DescriptionActivity extends AppCompatActivity {
    ActivityDescriptionBinding binding;
    private KProgressHUD hud;
    boolean check = true;
    boolean check1 = true;
    LinearLayout linearLayout;
    boolean check2 = true;
    MenuItem menuItem;
    List<MenuItem> itemList;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDescriptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        itemList = new ArrayList<>();

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_status));

        linearLayout = findViewById(R.id.linear2);

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();

        if (intent != null) {
            menuItem = (MenuItem) intent.getSerializableExtra("menuItem");

            CompositeDisposable compositeDisposable = new CompositeDisposable();
            Disposable disposable = RetrofitClient.getClient()
                    .create(ApiGetYeuthich.class)
                    .getYeuThich()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new Consumer<List<YeuThichItem>>() {
                        @Override
                        public void accept(List<YeuThichItem> yeuThichItems) throws Exception {
                            for (int i = 0; i < yeuThichItems.size(); i++) {
                                if (menuItem.getId() == yeuThichItems.get(i).getIdMenu() &&
                                        Integer.parseInt(StoreUtils.get(DescriptionActivity.this, Contants.Id)) == yeuThichItems.get(i).getIdUser()) {
                                    id = yeuThichItems.get(i).getId();
                                    binding.imgYeuThich.setImageDrawable(getResources().getDrawable(R.drawable.star_red));
                                    check2 = false;
                                    break;
                                }
                            }
                        }
                    });
            compositeDisposable.add(disposable);

        }

        hud = KProgressHUD.create(DescriptionActivity.this)
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
                        .create(ApiDetailsMenu.class)
                        .getDetails(menuItem.getId())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new Consumer<List<MenuItem>>() {
                            @Override
                            public void accept(List<MenuItem> menuItems) throws Exception {
                                binding.txtTitleMon.setText(menuItems.get(0).getTenMon());
                                binding.txtNguyenLieu.setText(menuItems.get(0).getNguyenLieu());
                                binding.txtCachLam.setText(menuItems.get(0).getCachNau());
                                binding.txtSoChe.setText(menuItems.get(0).getSoChe());

                                binding.imgMore.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (check) {
                                            binding.imgMore.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_expand_more_24));
                                            TextView descCol = findViewById(R.id.txtNguyenLieu);
                                            ViewGroup.LayoutParams params = descCol.getLayoutParams();
                                            params.height = 0;
                                            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                            descCol.setLayoutParams(params);

                                            check = false;
                                        } else {
                                            binding.imgMore.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_expand_less_24));
                                            check = true;
                                            TextView descCol = findViewById(R.id.txtNguyenLieu);
                                            ViewGroup.LayoutParams params = descCol.getLayoutParams();
                                            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                            descCol.setLayoutParams(params);

                                        }
                                    }
                                });

                                binding.imgMoreThen.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        TextView txtSoCheNguyenLieu;
                                        TextView descCol = findViewById(R.id.txtCachLam);
                                        TextView txtSoChe = findViewById(R.id.txtSoChe);
                                        txtSoCheNguyenLieu = findViewById(R.id.txtSoCheNguyenLieu);
                                        TextView txtCachLamMonAn = findViewById(R.id.txtCheBien);


                                        if (check1) {

                                            binding.imgMoreThen.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_expand_more_24));

                                            txtSoCheNguyenLieu.setVisibility(View.GONE);
                                            txtCachLamMonAn.setVisibility(View.GONE);

                                            // cach lam
                                            ViewGroup.LayoutParams params = descCol.getLayoutParams();
                                            params.height = 0;
                                            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                            descCol.setLayoutParams(params);

                                            //so che
                                            ViewGroup.LayoutParams params1 = txtSoChe.getLayoutParams();
                                            params1.height = 0;
                                            params1.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                            txtSoChe.setLayoutParams(params1);


                                            check1 = false;
                                        } else {
                                            txtSoCheNguyenLieu.setVisibility(View.VISIBLE);
                                            txtCachLamMonAn.setVisibility(View.VISIBLE);
                                            binding.imgMoreThen.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_expand_less_24));
                                            check1 = true;

                                            // cach lam
                                            ViewGroup.LayoutParams params = descCol.getLayoutParams();
                                            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                            descCol.setLayoutParams(params);

                                            // so che
                                            ViewGroup.LayoutParams params1 = txtSoChe.getLayoutParams();
                                            params1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                            params1.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                            txtSoChe.setLayoutParams(params1);

                                        }
                                    }
                                });


                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                            }
                        });
                compositeDisposable.add(disposable);

                hud.dismiss();
            }
        }, 2000);

        binding.imgYeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (check2 == false) {
                    binding.imgYeuThich.setImageDrawable(getResources().getDrawable(R.drawable.star));
                    CompositeDisposable compositeDisposable = new CompositeDisposable();
                    Disposable disposable = RetrofitClient.getClient()
                            .create(ApiDeleteYeuThich.class)
                            .deleteFaverite(id, menuItem.getId(), Integer.parseInt(StoreUtils.get(DescriptionActivity.this, Contants.Id)))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.newThread())
                            .subscribe(new Consumer<ResponseDTO>() {
                                @Override
                                public void accept(ResponseDTO responseDTO) throws Exception {

                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    throwable.printStackTrace();
                                }
                            });
                    compositeDisposable.add(disposable);
                    Toast.makeText(DescriptionActivity.this, "Xoá thành công vào danh mục yêu thích !!!", Toast.LENGTH_SHORT).show();
                    check2 = true;
                } else {
                    binding.imgYeuThich.setImageDrawable(getResources().getDrawable(R.drawable.star_red));
                    CompositeDisposable compositeDisposable = new CompositeDisposable();
                    Disposable disposable = RetrofitClient.getClient()
                            .create(ApiInsertFaverite.class)
                            .insertFaverite(menuItem.getId(), menuItem.getTenMon(), menuItem.getLinkAnh(), menuItem.getMoTa(), menuItem.getNguyenLieu(), menuItem.getSoChe(),
                                    menuItem.getCachNau(), Integer.parseInt(StoreUtils.get(DescriptionActivity.this, Contants.Id)), true)
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
                    Toast.makeText(DescriptionActivity.this, "Thêm thành công vào danh mục yêu thích !!!", Toast.LENGTH_SHORT).show();
                    check2 = false;
                }
            }
        });
    }
}
