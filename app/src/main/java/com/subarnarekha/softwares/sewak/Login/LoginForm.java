package com.subarnarekha.softwares.sewak.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.subarnarekha.softwares.sewak.AppIntroduction.AppIntro;
import com.subarnarekha.softwares.sewak.R;
import com.subarnarekha.softwares.sewak.home.BottomActivity;

public class LoginForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        EditText phone = findViewById(R.id.phoneno);
        FloatingActionButton loginBtn = findViewById(R.id.login);
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wificon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo netcon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(!((wificon!=null && wificon.isConnected())||(netcon!=null && netcon.isConnected())))
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(LoginForm.this);
            builder.setMessage("You are offline please check your internet connection")
                    .setTitle("No Internet Connection")
                    .setCancelable(true)
                    .setPositiveButton("Connect", (dialog, which) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                    .setNegativeButton("cancle", (dialog, which) -> {
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

        loginBtn.setOnClickListener(v -> {
            if(phone.getText().toString().length()!=10)
            {
                Toast.makeText(getApplicationContext(),"Enter valid Phone Number", Toast.LENGTH_SHORT).show();
            }
            else {
                    String phoneno = phone.getText().toString();
                    Intent i = new Intent(LoginForm.this, VerifyOtp.class);
                    i.putExtra("phoneno","+91"+phoneno);
                    startActivity(i);
            }
        });

    }
}