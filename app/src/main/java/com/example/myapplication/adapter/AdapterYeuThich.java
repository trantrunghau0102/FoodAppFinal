package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.MenuItem;
import com.example.myapplication.view.DescriptionActivity;

import java.util.List;

public class AdapterYeuThich extends RecyclerView.Adapter<AdapterYeuThich.Viewholider> {
    Context context;
    List<MenuItem> list;

    public AdapterYeuThich(Context context, List<MenuItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Viewholider onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_yeuthich, parent, false);
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


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DescriptionActivity.class);
                intent.putExtra("menuItem", menuItem);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholider extends RecyclerView.ViewHolder {
        TextView txtTenMon;
        ImageView imgHinh;

        public Viewholider(@NonNull View itemView) {
            super(itemView);
            txtTenMon = itemView.findViewById(R.id.txtTenMonAn);
            imgHinh = itemView.findViewById(R.id.imgHinhYeuThich);

        }
    }
}
