package com.subarnarekha.softwares.sewak.addService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.subarnarekha.softwares.sewak.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SelectProfession extends AppCompatActivity {
RecyclerView datalist;
ImageView backbtn;
List<String> names = new ArrayList<>();
List<Integer> images = new ArrayList<>();
Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_profession);
        datalist = findViewById(R.id.search_results);
        backbtn = findViewById(R.id.backbtn);
        names.add("Tutor");
        names.add("Beautician");
        names.add("GYM");
        names.add("Cook & Catering");
        names.add("Doctor");
        names.add("Photographer");
        names.add("Electrician");
        names.add("Priest");
        names.add("Construction");
        names.add("Painter");
        names.add("Carpenter");
        names.add("Mechanic");
        names.add("Tailor");
        names.add("Welder");
        names.add("Plumber");
        names.add("Barber");
        names.add("Milkman");
        names.add("Farmer");
        names.add("Light & Sound");
        names.add("Laundry");
        names.add("Musician & Band");
        names.add("Poultry Farm");
        names.add("Tent & House");

        images.add(R.drawable.presentation);
        images.add(R.drawable.makeover);
        images.add(R.drawable.gym);
        images.add(R.drawable.cooking);
        images.add(R.drawable.doctor);
        images.add(R.drawable.photo);
        images.add(R.drawable.electrician);
        images.add(R.drawable.guru);
        images.add(R.drawable.brickwall);
        images.add(R.drawable.painter);
        images.add(R.drawable.carpenter);
        images.add(R.drawable.car_repair);
        images.add(R.drawable.tailor);
        images.add(R.drawable.welder);
        images.add(R.drawable.plumber);
        images.add(R.drawable.barber);
        images.add(R.drawable.milk);
        images.add(R.drawable.harvest);
        images.add(R.drawable.concert);
        images.add(R.drawable.washing_clothes);
        images.add(R.drawable.concert_2);
        images.add(R.drawable.hen);
        images.add(R.drawable.tent);


        adapter = new Adapter(names,images,this,"service");
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
        datalist.setLayoutManager(gridLayoutManager);
        datalist.setAdapter(adapter);

        backbtn.setOnClickListener(v -> finish());
    }

}