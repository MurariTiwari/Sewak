package com.subarnarekha.softwares.sewak.AppIntroduction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.subarnarekha.softwares.sewak.Login.LoginForm;
import com.subarnarekha.softwares.sewak.R;

public class AppIntro extends AppCompatActivity {
    public  static ViewPager viewPager;
    private ProgressBar progressBar;
    private ImageView next,back;
    private Button start;
    AppIntroAdapter adapter;
    int position;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_intro);

        viewPager = findViewById(R.id.viewPager);

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("intro", "done");
        editor.commit();

        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(25);

        back = findViewById(R.id.back);
        back.setVisibility(View.GONE);
        back.setOnClickListener(v -> {
            position = viewPager.getCurrentItem();
            viewPager.setCurrentItem(position-1);
        });

        next = findViewById(R.id.next);
        next.setOnClickListener(v -> {
            position = viewPager.getCurrentItem();
            viewPager.setCurrentItem(position+1);
        });

        start = findViewById(R.id.deleteBtn);
        start.setVisibility(View.GONE);
        start.setOnClickListener(v -> {
            Intent i = new Intent(AppIntro.this, LoginForm.class);
            startActivity(i);
            finish();
        });
        adapter = new AppIntroAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(pageChangeListener);
    }
    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position)
            {
                case 0:
                    progressBar.setProgress(20);
                    back.setVisibility(View.GONE);
                    break;
                case 1:
                    progressBar.setProgress(40);
                    back.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    progressBar.setProgress(60);
                    break;
                case 3:
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(80);
                    next.setVisibility(View.VISIBLE);
                    back.setVisibility(View.VISIBLE);
                    start.setVisibility(View.GONE);
                    break;
                case 4:
                    progressBar.setVisibility(View.GONE);
                    next.setVisibility(View.GONE);
                    back.setVisibility(View.GONE);
                    start.setVisibility(View.VISIBLE);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}