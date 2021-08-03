package com.subarnarekha.softwares.sewak.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.subarnarekha.softwares.sewak.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ViewServiceAdapter extends PagerAdapter {
    Context ctx;
    ArrayList<String> images;

    public ViewServiceAdapter(Context ctx, ArrayList<String> images) {
        this.ctx = ctx;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view==object;
    }

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_service_images,container,false);


        ImageView imageView = view.findViewById(R.id.image_ser);

        Glide.with(ctx).load(images.get(position)).into(imageView);
        container.addView(view);
        return view;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
