package com.example.myapplication.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.myapplication.R;
import com.example.myapplication.adapter.AdapterYeuThich;
import com.example.myapplication.api.ApiGetYeuthich;
import com.example.myapplication.api.ApiMenu;
import com.example.myapplication.api.ApiRegisterUser;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.model.MenuItem;
import com.example.myapplication.model.ResponseDTO;
import com.example.myapplication.model.YeuThichItem;
import com.example.myapplication.utils.Contants;
import com.example.myapplication.utils.StoreUtils;
import com.example.myapplication.view.DescriptionActivity;
import com.example.myapplication.view.Register;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class YeuThichFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    private KProgressHUD hud;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_yeuthich, container, false);

        recyclerView = view.findViewById(R.id.recylerYeuThich);

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
                        .create(ApiGetYeuthich.class)
                        .getYeuThich()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new Consumer<List<YeuThichItem>>() {
                            @Override
                            public void accept(List<YeuThichItem> yeuThichItems) throws Exception {

                                List<YeuThichItem> itemList = new ArrayList<>();

                                for (int i = 0; i < yeuThichItems.size(); i++) {
                                    if (!itemList.contains(yeuThichItems.get(i))) {
                                        itemList.add(yeuThichItems.get(i));
                                    }
                                }

                                List<MenuItem> list = new ArrayList<>();
                                for (int i = 0; i < itemList.size(); i++) {
                                    if (Integer.parseInt(StoreUtils.get(getActivity(), Contants.Id)) == itemList.get(i).getIdUser()) {
                                        MenuItem menuItem = new MenuItem(itemList.get(i).getIdMenu(), itemList.get(i).getTenMon(),
                                                itemList.get(i).getLinkAnh(), itemList.get(i).getMoTa(), itemList.get(i).getNguyenLieu(),
                                                itemList.get(i).getSoChe(), itemList.get(i).getCachNau());
                                        list.add(menuItem);
                                    }
                                }

                                AdapterYeuThich adapterYeuThich = new AdapterYeuThich(getContext(), list);
                                recyclerView.setAdapter(adapterYeuThich);
                                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

                            }
                        });
                compositeDisposable.add(disposable);

                hud.dismiss();
            }
        }, 2000);

        return view;
    }
}
