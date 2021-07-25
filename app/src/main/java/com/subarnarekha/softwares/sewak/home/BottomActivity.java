package com.subarnarekha.softwares.sewak.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.subarnarekha.softwares.sewak.MainActivity;
import com.subarnarekha.softwares.sewak.R;
import com.subarnarekha.softwares.sewak.Splash;
import com.subarnarekha.softwares.sewak.addService.SelectProfession;
import com.subarnarekha.softwares.sewak.fragments.AddService;
import com.subarnarekha.softwares.sewak.fragments.CallHistory;
import com.subarnarekha.softwares.sewak.fragments.Contact;
import com.subarnarekha.softwares.sewak.fragments.Home;
import com.subarnarekha.softwares.sewak.fragments.Service;

import org.jetbrains.annotations.NotNull;

import static maes.tech.intentanim.CustomIntent.customType;

public class BottomActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnavigationbar);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        bottomNavigationView.setItemIconTintList(null);
        getSupportFragmentManager().beginTransaction().replace(R.id.framecontainer,new Home()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment temp = null;
            switch (item.getItemId())
            {
                case R.id.mHome : temp = new Home();
                    break;
                case R.id.mService: temp = new Service();
                    break;
                case R.id.mContact: temp = new Contact();
                    break;
                case R.id.mPhone: temp = new CallHistory();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.framecontainer,temp).commit();
            return true;
        });

    }

    public void addService(View view) {

        Intent i = new Intent(BottomActivity.this, SelectProfession.class);
        startActivity(i);
        customType(BottomActivity.this,"up-to-bottom");

    }
}