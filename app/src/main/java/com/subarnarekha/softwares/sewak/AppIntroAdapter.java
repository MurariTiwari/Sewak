package com.subarnarekha.softwares.sewak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;

public class AppIntroAdapter extends PagerAdapter {
    Context ctx;

    public AppIntroAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_screen,container,false);

        LottieAnimationView logo = view.findViewById(R.id.imageintro);
        TextView desc = view.findViewById(R.id.desc);
        switch (position)
        {
            case 0:
                logo.setAnimation("help.json");
                desc.setText("Need Help !");
                break;
            case 1:
                logo.setAnimation("search.json");
                desc.setText("Search Your SEWAK");
                break;
            case 2:
                logo.setAnimation("call.json");
                desc.setText("Enquire Them");
                break;
            case 3:
                logo.setAnimation("plumbers.json");
                desc.setText("Get it Fixed !");
                break;

            case 4:
                logo.setAnimation("start.json");
                desc.setText("WELCOME");
                break;

        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
