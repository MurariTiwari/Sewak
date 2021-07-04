package com.subarnarekha.softwares.sewak.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.subarnarekha.softwares.sewak.AppIntroduction.AppIntro;
import com.subarnarekha.softwares.sewak.R;

public class LoginForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        EditText phone = findViewById(R.id.phoneno);
        FloatingActionButton loginBtn = findViewById(R.id.login);


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