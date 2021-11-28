package com.example.myapplication.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.AdapterMenu;
import com.example.myapplication.api.ApiMenu;
import com.example.myapplication.api.ApiRegisterUser;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.databinding.ActivityHomeBinding;
import com.example.myapplication.eventbus.EventItemBottom;
import com.example.myapplication.eventbus.EventTypeMenu;
import com.example.myapplication.fragment.HomeFragment;
import com.example.myapplication.fragment.SearchFragment;
import com.example.myapplication.fragment.SettingFragment;
import com.example.myapplication.fragment.YeuThichFragment;
import com.example.myapplication.model.MenuItem;
import com.example.myapplication.model.ResponseDTO;
import com.example.myapplication.utils.ChangColorStatus;
import com.example.myapplication.utils.Contants;
import com.example.myapplication.utils.StoreUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.nambimobile.widgets.efab.ExpandableFabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    HomeFragment homeFragment;
    FragmentManager fragmentManager;
    LinearLayout linearLayout;
    FragmentTransaction fragmentTransaction;
    FloatingActionButton floatingActionButton, floatingActionAdd, floatingActionFilter;
    TextView txtAddMenu, txtFilter;
    boolean checkFloat = true;

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_status));

        linearLayout = findViewById(R.id.linearLayout);
        floatingActionButton = findViewById(R.id.floatButton);
        floatingActionAdd = findViewById(R.id.addFloatButton);
        floatingActionFilter = findViewById(R.id.floatButtonFilter);
        txtAddMenu = findViewById(R.id.txtAddMenu);
        txtFilter = findViewById(R.id.txtFilter);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.frame_layout, new HomeFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        floatingActionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFloat) {
                    goneButton();
                } else {
                    if (StoreUtils.get(HomeActivity.this, Contants.username).equals("admin")
                            && StoreUtils.get(HomeActivity.this, Contants.password).equals("admin")) {
                        floatingActionButton.setVisibility(View.GONE);
                        txtAddMenu.setVisibility(View.GONE);
                    }

                    floatingActionFilter.setVisibility(View.GONE);
                    txtFilter.setVisibility(View.GONE);
                    checkFloat = true;
                }
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddMenuActivity.class);
                startActivity(intent);
                if (StoreUtils.get(HomeActivity.this, Contants.username).equals("admin")
                        && StoreUtils.get(HomeActivity.this, Contants.password).equals("admin")) {
                    floatingActionButton.setVisibility(View.GONE);
                    txtAddMenu.setVisibility(View.GONE);
                }

                floatingActionFilter.setVisibility(View.GONE);
                txtFilter.setVisibility(View.GONE);
                checkFloat = true;

            }
        });

        floatingActionFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StoreUtils.get(HomeActivity.this, Contants.username).equals("admin")
                        && StoreUtils.get(HomeActivity.this, Contants.password).equals("admin")) {
                    floatingActionButton.setVisibility(View.GONE);
                    txtAddMenu.setVisibility(View.GONE);
                }

                floatingActionFilter.setVisibility(View.GONE);
                txtFilter.setVisibility(View.GONE);
                checkFloat = true;
                displayAlertDialog();
            }
        });

    }

    public void displayAlertDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_type_menu, null);

        ImageView imgRide = alertLayout.findViewById(R.id.imgRide);
        ImageView imgNoodle_dishes = alertLayout.findViewById(R.id.imgNoodle_dishes);
        ImageView imgSalad = alertLayout.findViewById(R.id.imgSalad);
        ImageView imgGrilled = alertLayout.findViewById(R.id.imgGrilled);
        ImageView img_beef = alertLayout.findViewById(R.id.img_beef);
        ImageView img_pig = alertLayout.findViewById(R.id.img_pig);
        ImageView img_chicken = alertLayout.findViewById(R.id.img_chicken);
        ImageView img_seafood = alertLayout.findViewById(R.id.img_seafood);
        ImageView img_dessert = alertLayout.findViewById(R.id.img_dessert);
        ImageView img_snacks = alertLayout.findViewById(R.id.img_snacks);
        ImageView img_drink = alertLayout.findViewById(R.id.img_drink);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        AlertDialog dialog = alert.create();
        dialog.show();

        imgRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                EventBus.getDefault().post(new EventTypeMenu("1"));
            }
        });

        imgNoodle_dishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                EventBus.getDefault().post(new EventTypeMenu("2"));
            }
        });

        imgSalad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                EventBus.getDefault().post(new EventTypeMenu("3"));
            }
        });

        imgGrilled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                EventBus.getDefault().post(new EventTypeMenu("4"));
            }
        });

        img_beef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                EventBus.getDefault().post(new EventTypeMenu("5"));
            }
        });

        img_pig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                EventBus.getDefault().post(new EventTypeMenu("6"));
            }
        });

        img_chicken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                EventBus.getDefault().post(new EventTypeMenu("7"));
            }
        });

        img_seafood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                EventBus.getDefault().post(new EventTypeMenu("8"));
            }
        });

        img_dessert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                EventBus.getDefault().post(new EventTypeMenu("9"));
            }
        });

        img_snacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                EventBus.getDefault().post(new EventTypeMenu("10"));
            }
        });

        img_drink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                EventBus.getDefault().post(new EventTypeMenu("11"));
            }
        });

    }

    public void goneButton() {
        if (StoreUtils.get(HomeActivity.this, Contants.username).equals("admin")
                && StoreUtils.get(HomeActivity.this, Contants.password).equals("admin")) {
            floatingActionButton.setVisibility(View.VISIBLE);
            txtAddMenu.setVisibility(View.VISIBLE);
        }

        floatingActionFilter.setVisibility(View.VISIBLE);
        txtFilter.setVisibility(View.VISIBLE);
        checkFloat = false;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
            FragmentManager fragmentManager;
            FragmentTransaction fragmentTransaction;
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.menu_home:
                    fragmentTransaction.replace(R.id.frame_layout, new HomeFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    if (linearLayout.getVisibility() == View.GONE) {
                        EventBus.getDefault().post(new EventItemBottom(true));
                    }
                    return true;
                case R.id.menu_Search:
                    fragmentTransaction.replace(R.id.frame_layout, new SearchFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    if (linearLayout.getVisibility() == View.VISIBLE) {
                        EventBus.getDefault().post(new EventItemBottom(false));
                    }
                    return true;

                case R.id.menu_Heart:
                    fragmentTransaction.replace(R.id.frame_layout, new YeuThichFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    if (linearLayout.getVisibility() == View.GONE) {
                        EventBus.getDefault().post(new EventItemBottom(true));
                    }
                    return true;
                case R.id.menu_Setting:
                    fragmentTransaction.replace(R.id.frame_layout, new SettingFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    if (linearLayout.getVisibility() == View.VISIBLE) {
                        EventBus.getDefault().post(new EventItemBottom(false));
                    }
                    return true;
            }

            return false;
        }
    };

    @Subscribe
    public void eventBottom(EventItemBottom eventItemBottom) {
        if (eventItemBottom.isCheck()) {
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}