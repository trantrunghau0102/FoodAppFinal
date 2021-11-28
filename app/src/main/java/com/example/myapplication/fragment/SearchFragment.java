package com.example.myapplication.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.SendData;
import com.example.myapplication.SendDataMenu;
import com.example.myapplication.adapter.AdapterMenu;
import com.example.myapplication.api.ApiDeleteMenu;
import com.example.myapplication.api.ApiRegisterUser;
import com.example.myapplication.api.ApiSearch;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.model.MenuItem;
import com.example.myapplication.model.ResponseDTO;
import com.example.myapplication.utils.Contants;
import com.example.myapplication.utils.StoreUtils;
import com.example.myapplication.view.DescriptionActivity;
import com.example.myapplication.view.HomeActivity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchFragment extends Fragment {

    View view;
    SearchView searchView;
    Handler handler;
    RecyclerView recyclerView;
    String text = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_seach, container, false);
        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.recyclerViewSearch);

        handler = new Handler();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        text = newText;

                        CompositeDisposable compositeDisposable = new CompositeDisposable();
                        Disposable disposable = RetrofitClient.getClient()
                                .create(ApiSearch.class)
                                .getSearch(newText)
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

                    }
                }, 500);

                return false;
            }
        });

        return view;
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
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(this::onDestroy);
    }
}
