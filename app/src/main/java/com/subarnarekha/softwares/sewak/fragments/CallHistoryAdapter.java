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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.subarnarekha.softwares.sewak.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CallHistoryAdapter extends RecyclerView.Adapter<CallHistoryAdapter.ViewHolder> {
    Context ctx;
    LayoutInflater layoutInflater;
    List<String> img,name,time,phoneno;

    public CallHistoryAdapter(Context ctx, List<String> img, List<String> name, List<String> time, List<String> phoneno) {
        this.ctx = ctx;
        this.layoutInflater = LayoutInflater.from(ctx);
        this.img = img;
        this.name = name;
        this.time = time;
        this.phoneno = phoneno;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.single_call_history,parent,false);
        return new CallHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CallHistoryAdapter.ViewHolder holder, int position) {
        if(!name.get(position).equals("")) holder.name.setText(name.get(position));
        holder.time.setText(time.get(position));
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
        TextView name,time;
        ImageView img,phone;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.contact_name);
            phone = itemView.findViewById(R.id.call_button);
            img = itemView.findViewById(R.id.display_image);
            time = itemView.findViewById(R.id.contact_time);
        }
    }
}
