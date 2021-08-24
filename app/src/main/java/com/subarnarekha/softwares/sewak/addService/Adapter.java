package com.subarnarekha.softwares.sewak.addService;

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

import com.subarnarekha.softwares.sewak.R;
import com.subarnarekha.softwares.sewak.home.BottomActivity;
import com.subarnarekha.softwares.sewak.searchresults.Results;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    List<String> title;
    List<Integer> images;
    String activity;
    LayoutInflater layoutInflater;
    public Adapter(List<String> title, List<Integer> images, Context context, String activity) {
        this.title = title;
        this.images = images;
        this.layoutInflater = LayoutInflater.from(context);
        this.activity = activity;
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_grid_proffesion,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Adapter.ViewHolder holder, int position) {
        holder.title.setText(title.get(position));
        holder.img.setImageResource(images.get(position));
        holder.card.setOnClickListener(v -> {
            if(activity.equals("home")){
                Intent i = new Intent(v.getContext(), Results.class);
                i.putExtra("profession",title.get(position));
                v.getContext().startActivity(i);
            }
            else if(activity.equals("service")){
                Intent i = new Intent(v.getContext(), AddService.class);
                i.putExtra("profession",title.get(position));
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView img;
        CardView card;
         public ViewHolder(@NonNull @NotNull View itemView) {
             super(itemView);
             title = itemView.findViewById(R.id.name_proffesion);
             img = itemView.findViewById(R.id.profession_icon);
             card = itemView.findViewById(R.id.proffesion_card);
         }
     }
}
