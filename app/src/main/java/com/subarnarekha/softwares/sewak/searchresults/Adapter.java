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
    ArrayList<Modal> data;
    public Adapter(Context context,
                   ArrayList<Modal> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data=data;
        this.ctx=context;
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
        holder.desc.setText(data.get(position).getDesc());
        holder.address.setText(data.get(position).getAddress());
        holder.distance.setText(String.format("%.1f",data.get(position).getDistance())+"Km");
        Glide.with(ctx).load(data.get(position).getImages()).into(holder.img);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Map<String,Object>> singleServiceMenu = data.get(position).getServiceMenu();
                ArrayList<String> serviceMenu = new ArrayList<>();
                ArrayList<String> priceMenu = new ArrayList<>();
                for(int i=0;i<singleServiceMenu.size();i++)
                {
                    Map<String, Object> item = singleServiceMenu.get(i);
                    serviceMenu.add(item.get("serviceName").toString());
                    priceMenu.add(item.get("servicePrice").toString());
                }
                Intent i = new Intent(ctx, DetailedView.class);
                i.putExtra("desc",data.get(position).getDesc());
                i.putExtra("address",data.get(position).getAddress());
                i.putExtra("from",data.get(position).getFrom());
                i.putExtra("user",data.get(position).getUser());
                i.putExtra("service",data.get(position).getService());
                i.putExtra("businessName",data.get(position).getBusinessName());
                i.putExtra("allow",data.get(position).getAllowPhone());
                i.putExtra("servicePhoneNumber",data.get(position).getServiceNumber());
                i.putStringArrayListExtra("images", (ArrayList<String>) data.get(position).getAllImages());
                i.putStringArrayListExtra("serviceMenu", (ArrayList<String>) serviceMenu);
                i.putStringArrayListExtra("priceMenu", (ArrayList<String>) priceMenu);
                ctx.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
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

