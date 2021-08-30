package com.subarnarekha.softwares.sewak.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.subarnarekha.softwares.sewak.searchresults.Adapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    LayoutInflater layoutInflater;
    List<String> name,img,service,phoneno;
    Context ctx;
    public ContactAdapter(Context ctx, List<String> name, List<String> img, List<String> service, List<String> phoneno) {
        this.layoutInflater = LayoutInflater.from(ctx);
        this.name = name;
        this.img = img;
        this.service = service;
        this.phoneno = phoneno;
        this.ctx=ctx;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.single_contact,parent,false);
        return new ContactAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ContactAdapter.ViewHolder holder, int position) {
        if(!name.get(position).equals("")) holder.name.setText(name.get(position));
        holder.service.setText(service.get(position));
        if(!img.get(position).equals("")) Glide.with(ctx).load(img.get(position)).into(holder.img);
        holder.phone.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneno.get(position), null));
            ctx.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return phoneno.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,service;
        ImageView img,phone;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.contact_name);
            phone = itemView.findViewById(R.id.call_button);
            img = itemView.findViewById(R.id.display_image);
            service = itemView.findViewById(R.id.contact_service);
        }
    }
}
