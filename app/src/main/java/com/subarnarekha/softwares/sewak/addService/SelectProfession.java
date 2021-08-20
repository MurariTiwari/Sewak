package com.subarnarekha.softwares.sewak.addService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.subarnarekha.softwares.sewak.R;
import com.subarnarekha.softwares.sewak.Splash;

import java.util.ArrayList;
import java.util.List;

public class SelectProfession extends AppCompatActivity {
RecyclerView datalist;
List<String> names = new ArrayList<>();
List<Integer> images = new ArrayList<>();
Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_profession);
        datalist = findViewById(R.id.datalist);

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


        adapter = new Adapter(names,images,this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
        datalist.setLayoutManager(gridLayoutManager);
        datalist.setAdapter(adapter);
    }
    @SuppressLint("ResourceAsColor")
    public void selectedProfession(View v)
    {
        TextView titleView = v.findViewById(R.id.name_proffesion);
        Intent i = new Intent(SelectProfession.this, AddService.class);
        i.putExtra("profession",titleView.getText());
        startActivity(i);

    }
}