package com.subarnarekha.softwares.sewak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.subarnarekha.softwares.sewak.AppIntroduction.AppIntro;
import com.subarnarekha.softwares.sewak.Login.LoginForm;
import com.subarnarekha.softwares.sewak.addService.AddService;
import com.subarnarekha.softwares.sewak.addService.SelectProfession;
import com.subarnarekha.softwares.sewak.home.BottomActivity;
import com.subarnarekha.softwares.sewak.profile.ProfileScreen;
import com.subarnarekha.softwares.sewak.service.ViewService;

public class Splash extends AppCompatActivity {
    FirebaseUser user;
    FirebaseFirestore db;
    DocumentReference documentReference;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView logo = findViewById(R.id.logo);
        TextView slogan = findViewById(R.id.slogan);
        Animation transition = AnimationUtils.loadAnimation(this,R.anim.transition);
        logo.startAnimation(transition);
        slogan.startAnimation(transition);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if(user!=null)
        {
            documentReference = db.document("users/"+user.getUid());
            documentReference.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        editor.putString("address", (String) documentSnapshot.get("address"));
                        editor.putString("name", (String) documentSnapshot.get("name"));
                        editor.putString("phoneno", (String) documentSnapshot.get("phoneno"));
                        editor.putString("profileimg", (String) documentSnapshot.get("profileimg"));
                        editor.putString("service", (String) documentSnapshot.get("service"));
                        editor.commit();

                        Intent i = new Intent(Splash.this, BottomActivity.class);
                        startActivity(i);
                        finish();
                    });
        }
        else {
            Intent i = new Intent(Splash.this, LoginForm.class);
            startActivity(i);
            finish();
        }
    }
}