package com.subarnarekha.softwares.sewak.searchresults;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.subarnarekha.softwares.sewak.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    LayoutInflater layoutInflater;
    Context ctx;
    List<String> desc,address,distance,images;
    public Adapter(Context context, List<String> desc,List<String> address, List<String> distance, List<String> images) {
        this.layoutInflater = LayoutInflater.from(context);
        this.desc = desc;
        this.address = address;
        this.distance = distance;
        this.images = images;
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
        holder.desc.setText(desc.get(position));
        holder.address.setText(address.get(position));
        holder.distance.setText(distance.get(position));
        Glide.with(ctx).load(images.get(position)).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return desc.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView desc,address,distance;
        ImageView img;
        //CardView card;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            desc = itemView.findViewById(R.id.textView6);
            address = itemView.findViewById(R.id.serviceLocation);
            img = itemView.findViewById(R.id.display_image);
            distance = itemView.findViewById(R.id.service_distance);
        }
    }
}

