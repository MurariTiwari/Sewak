package com.subarnarekha.softwares.sewak.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.subarnarekha.softwares.sewak.R;
import com.subarnarekha.softwares.sewak.Splash;
import com.subarnarekha.softwares.sewak.addService.Adapter;
import com.subarnarekha.softwares.sewak.home.BottomActivity;
import com.subarnarekha.softwares.sewak.profile.ProfileScreen;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class Home extends Fragment {

    RecyclerView datalist;
    CircleImageView profile;
    List<String> names = new ArrayList<>();
    List<Integer> images = new ArrayList<>();
    Adapter adapter;
    ImageView menubar;
    SharedPreferences preferences;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        datalist = view.findViewById(R.id.categories);
        profile = view.findViewById(R.id.profile_page);
        menubar = view.findViewById(R.id.menubar);

        preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        names.add("Barber");
        names.add("Beautician");
        names.add("Carpenter");
        names.add("Cook & Catering");
        names.add("Construction");
        names.add("Doctor");
        names.add("Electrician");
        names.add("Farmer");
        names.add("GYM");
        names.add("Light & Sound");
        names.add("Laundry");
        names.add("Mechanic");
        names.add("Milkman");
        names.add("Musician & Band");
        names.add("Plumber");
        names.add("Painter");
        names.add("Photographer");
        names.add("Priest");
        names.add("Poultry Farm");
        names.add("Tutor");
        names.add("Tent & House");
        names.add("Tailor");
        names.add("Welder");


        images.add(R.drawable.barber);
        images.add(R.drawable.makeover);
        images.add(R.drawable.carpenter);
        images.add(R.drawable.cooking);
        images.add(R.drawable.brickwall);
        images.add(R.drawable.doctor);
        images.add(R.drawable.electrician);
        images.add(R.drawable.harvest);
        images.add(R.drawable.gym);
        images.add(R.drawable.concert);
        images.add(R.drawable.washing_clothes);
        images.add(R.drawable.car_repair);
        images.add(R.drawable.milk);
        images.add(R.drawable.concert_2);
        images.add(R.drawable.plumber);
        images.add(R.drawable.painter);
        images.add(R.drawable.photo);
        images.add(R.drawable.guru);
        images.add(R.drawable.hen);
        images.add(R.drawable.presentation);
        images.add(R.drawable.tent);
        images.add(R.drawable.tailor);
        images.add(R.drawable.welder);

        adapter = new Adapter(names,images,getContext());
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),4,GridLayoutManager.VERTICAL,false);
        datalist.setLayoutManager(gridLayoutManager);
        datalist.setAdapter(adapter);
        profile.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), ProfileScreen.class);
            startActivity(i);
        });
        if(preferences.contains("profileimg"))
        Glide.with(getContext()).load(preferences.getString("profileimg","")).into(profile);
        menubar.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(),menubar);
            popupMenu.getMenuInflater().inflate(R.menu.drawer_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId())
                    {

                    }
                    return false;
                }
            });
            popupMenu.show();
        });
        return view;
    }
}