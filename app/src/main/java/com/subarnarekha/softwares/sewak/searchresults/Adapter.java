package com.subarnarekha.softwares.sewak.searchresults;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.subarnarekha.softwares.sewak.R;
import com.subarnarekha.softwares.sewak.detailedview.DetailedView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    LayoutInflater layoutInflater;
    Context ctx;
    List<String> desc,address,distance,images,from,service;
    List<List<String>> allImages;
    List<String> user;
    List<String> allowPhone;
    ArrayList<ArrayList<Map<String,Object>>> serviceMenu;
    public Adapter(Context context,
                   List<String> desc,
                   List<String> address,
                   List<String> distance,
                   List<String> images,
                   List<String> from,
                   List<String> service,
                   List<List<String>> allImages,
                   ArrayList<ArrayList<Map<String,Object>>> serviceMenu,
                   List<String> user,
    List<String> allowPhone) {
        this.layoutInflater = LayoutInflater.from(context);
        this.desc = desc;
        this.address = address;
        this.distance = distance;
        this.images = images;
        this.ctx=context;
        this.from=from;
        this.service=service;
        this.allImages=allImages;
        this.serviceMenu=serviceMenu;
        this.user=user;
        this.allowPhone=allowPhone;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.single_search,parent,false);
        return new Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.desc.setText(desc.get(position));
        holder.address.setText(address.get(position));
        holder.distance.setText(distance.get(position));
        Glide.with(ctx).load(images.get(position)).into(holder.img);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Map<String,Object>> singleServiceMenu = serviceMenu.get(position);
                ArrayList<String> serviceMenu = new ArrayList<>();
                ArrayList<String> priceMenu = new ArrayList<>();
                for(int i=0;i<singleServiceMenu.size();i++)
                {
                    Map<String, Object> item = singleServiceMenu.get(i);
                    serviceMenu.add(item.get("serviceName").toString());
                    priceMenu.add(item.get("servicePrice").toString());
                }
                Intent i = new Intent(ctx, DetailedView.class);
                i.putExtra("desc",desc.get(position));
                i.putExtra("address",address.get(position));
                i.putExtra("from",from.get(position));
                i.putExtra("user",user.get(position));
                i.putExtra("service",service.get(position));
                i.putExtra("allow",allowPhone.get(position));
                i.putStringArrayListExtra("images", (ArrayList<String>) allImages.get(position));
                i.putStringArrayListExtra("serviceMenu", (ArrayList<String>) serviceMenu);
                i.putStringArrayListExtra("priceMenu", (ArrayList<String>) priceMenu);
                ctx.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return desc.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView desc,address,distance;
        ImageView img;
        CardView card;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            desc = itemView.findViewById(R.id.contact_service);
            address = itemView.findViewById(R.id.serviceLocation);
            img = itemView.findViewById(R.id.display_image);
            distance = itemView.findViewById(R.id.service_distance);
            card = itemView.findViewById(R.id.result_card);
        }
    }
}

