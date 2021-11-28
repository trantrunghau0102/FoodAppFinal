package com.example.myapplication.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.SendData;
import com.example.myapplication.SendDataMenu;
import com.example.myapplication.adapter.AdapterMenu;
import com.example.myapplication.api.ApiDeleteMenu;
import com.example.myapplication.api.ApiGetTypeMenu;
import com.example.myapplication.api.ApiMenu;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.eventbus.EventTypeMenu;
import com.example.myapplication.model.MenuItem;
import com.example.myapplication.utils.Contants;
import com.example.myapplication.utils.StoreUtils;
import com.example.myapplication.view.DescriptionActivity;
import com.example.myapplication.view.HomeActivity;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends Fragment {
    View view;
    private KProgressHUD hud;
    RecyclerView recyclerView;


    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        getData();

        return view;
    }

    public void getData() {
        hud = KProgressHUD.create(getContext())
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
                        .create(ApiMenu.class)
                        .getMenu()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new Consumer<List<MenuItem>>() {
                            @Override
                            public void accept(List<MenuItem> menuItems) throws Exception {
                                AdapterMenu adapterMenu = new AdapterMenu(getContext(), menuItems, new SendDataMenu() {
                                    @Override
                                    public void sendData(MenuItem menu) {
                                        Intent intent = new Intent(getActivity(), DescriptionActivity.class);
                                        intent.putExtra("menuItem", menu);
                                        startActivity(intent);
                                    }
                                }, new SendData() {
                                    @Override
                                    public void sendData(MenuItem menu) {
                                        if (StoreUtils.get(getContext(), Contants.username).equals("admin")
                                                && StoreUtils.get(getContext(), Contants.password).equals("admin")) {
                                            displayDialog(menu);
                                        }
                                    }
                                });
                                recyclerView.setAdapter(adapterMenu);
                                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
                            }
                        });
                compositeDisposable.add(disposable);
                hud.dismiss();
            }
        }, 2000);

    }

    public void displayDialog(MenuItem menuItem) {
        new AlertDialog.Builder(getContext())
                .setTitle("Thông báo")
                .setMessage("Bạn chắc chắn muốn xoá?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CompositeDisposable compositeDisposable = new CompositeDisposable();
                        Disposable disposable = RetrofitClient.getClient()
                                .create(ApiDeleteMenu.class)
                                .deleteMenu(menuItem.getId())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.newThread())
                                .subscribe(new Consumer<List<MenuItem>>() {
                                    @Override
                                    public void accept(List<MenuItem> menuItems) throws Exception {
                                        AdapterMenu adapterMenu = new AdapterMenu(getContext(), menuItems, new SendDataMenu() {
                                            @Override
                                            public void sendData(MenuItem menu) {
                                                Intent intent = new Intent(getActivity(), DescriptionActivity.class);
                                                intent.putExtra("menuItem", menu);
                                                startActivity(intent);
                                            }
                                        }, new SendData() {
                                            @Override
                                            public void sendData(MenuItem menu) {
                                                if (StoreUtils.get(getContext(), Contants.username).equals("admin")
                                                        && StoreUtils.get(getContext(), Contants.password).equals("admin")) {
                                                    displayDialog(menu);
                                                }
                                            }
                                        });
                                        recyclerView.setAdapter(adapterMenu);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                        adapterMenu.notifyDataSetChanged();
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        throwable.printStackTrace();
                                    }
                                });
                        compositeDisposable.add(disposable);


                        getData();


                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Subscribe
    public void eventType(EventTypeMenu eventTypeMenu) {
        if (eventTypeMenu.getId().equals("1")) {
            readApi("1");
        } else if (eventTypeMenu.getId().equals("2")) {
            readApi("2");
        } else if (eventTypeMenu.getId().equals("3")) {
            readApi("3");
        } else if (eventTypeMenu.getId().equals("4")) {
            readApi("4");
        } else if (eventTypeMenu.getId().equals("5")) {
            readApi("5");
        } else if (eventTypeMenu.getId().equals("6")) {
            readApi("6");
        } else if (eventTypeMenu.getId().equals("7")) {
            readApi("7");
        } else if (eventTypeMenu.getId().equals("8")) {
            readApi("8");
        } else if (eventTypeMenu.getId().equals("9")) {
            readApi("9");
        } else if (eventTypeMenu.getId().equals("10")) {
            readApi("10");
        } else if (eventTypeMenu.getId().equals("11")) {
            readApi("11");
        }
    }

    public void readApi(String type) {
        hud = KProgressHUD.create(getContext())
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
                        .create(ApiGetTypeMenu.class)
                        .getMenu(type)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new Consumer<List<MenuItem>>() {
                            @Override
                            public void accept(List<MenuItem> menuItems) throws Exception {
                                AdapterMenu adapterMenu = new AdapterMenu(getContext(), menuItems, new SendDataMenu() {
                                    @Override
                                    public void sendData(MenuItem menu) {
                                        Intent intent = new Intent(getActivity(), DescriptionActivity.class);
                                        intent.putExtra("menuItem", menu);
                                        startActivity(intent);
                                    }
                                }, new SendData() {
                                    @Override
                                    public void sendData(MenuItem menu) {
                                        if (StoreUtils.get(getContext(), Contants.username).equals("admin")
                                                && StoreUtils.get(getContext(), Contants.password).equals("admin")) {
                                            displayDialog(menu);
                                        }
                                    }
                                });
                                recyclerView.setAdapter(adapterMenu);
                                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
