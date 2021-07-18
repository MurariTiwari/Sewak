package com.subarnarekha.softwares.sewak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.subarnarekha.softwares.sewak.AppIntroduction.AppIntro;
import com.subarnarekha.softwares.sewak.Login.LoginForm;
import com.subarnarekha.softwares.sewak.addService.AddService;
import com.subarnarekha.softwares.sewak.addService.SelectProfession;
import com.subarnarekha.softwares.sewak.home.BottomActivity;
import com.subarnarekha.softwares.sewak.profile.ProfileScreen;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView logo = findViewById(R.id.logo);
        TextView slogan = findViewById(R.id.slogan);
        Animation transition = AnimationUtils.loadAnimation(this,R.anim.transition);
        logo.startAnimation(transition);
        slogan.startAnimation(transition);
        new Handler().postDelayed(() -> {
            Intent i = new Intent(Splash.this, SelectProfession.class);
            startActivity(i);

            // close this activity
            finish();
        },
                4000);
    }
}