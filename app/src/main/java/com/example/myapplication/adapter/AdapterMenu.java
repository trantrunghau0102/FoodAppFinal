package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.SendData;
import com.example.myapplication.SendDataMenu;
import com.example.myapplication.model.MenuItem;

import java.util.List;

public class AdapterMenu extends RecyclerView.Adapter<AdapterMenu.Viewholider> {
    Context context;
    SendDataMenu sendDataMenu;
    List<MenuItem> list;
    SendData sendData;

    public AdapterMenu(Context context, List<MenuItem> list, SendDataMenu sendDataMenu, SendData sendData) {
        this.context = context;
        this.list = list;
        this.sendDataMenu = sendDataMenu;
        this.sendData = sendData;
    }

    @NonNull
    @Override
    public Viewholider onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_home, parent, false);
        Viewholider viewHolder = new Viewholider(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholider holder, int position) {
        MenuItem menuItem = list.get(position);

        Glide.with(context)
                .load(menuItem.getLinkAnh())
                .into(holder.imgHinh);

        holder.txtTenMon.setText(menuItem.getTenMon());
        holder.txtDescription.setText(menuItem.getMoTa());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataMenu.sendData(menuItem);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                sendData.sendData(menuItem);
                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholider extends RecyclerView.ViewHolder {
        ImageView imgHinh;
        TextView txtTenMon;
        TextView txtDescription;


        public Viewholider(@NonNull View itemView) {
            super(itemView);
            imgHinh = itemView.findViewById(R.id.imgItemHome);
            txtTenMon = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);

        }
    }
}
