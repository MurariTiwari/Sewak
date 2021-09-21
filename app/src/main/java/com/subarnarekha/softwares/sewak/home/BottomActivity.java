package com.subarnarekha.softwares.sewak.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.subarnarekha.softwares.sewak.Login.LoginForm;
import com.subarnarekha.softwares.sewak.R;
import com.subarnarekha.softwares.sewak.Splash;
import com.subarnarekha.softwares.sewak.addService.SelectProfession;
import com.subarnarekha.softwares.sewak.fragments.CallHistory;
import com.subarnarekha.softwares.sewak.fragments.Contact;
import com.subarnarekha.softwares.sewak.fragments.Home;
import com.subarnarekha.softwares.sewak.service.ViewService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static maes.tech.intentanim.CustomIntent.customType;

public class BottomActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FirebaseUser user;
    FirebaseFirestore db;
    SharedPreferences preferences;
    Dialog dialog;
    Button deleteBtn,closeBtn;
    private FirebaseStorage storageReference;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = preferences.edit();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnavigationbar);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        bottomNavigationView.setItemIconTintList(null);
        getSupportFragmentManager().beginTransaction().replace(R.id.framecontainer,new Home()).commit();
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.service_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deleteBtn = dialog.findViewById(R.id.delete_service);
        closeBtn = dialog.findViewById(R.id.close_service_dialog);
        Intent i = getIntent();
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment temp = null;
            switch (item.getItemId())
            {
                case R.id.mHome : temp = new Home();
                    break;
                case R.id.mService: temp = new ViewService();
                    break;
                case R.id.mContact: temp = new Contact();
                    break;
                case R.id.mPhone: temp = new CallHistory();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.framecontainer,temp).commit();
            return true;
        });
        closeBtn.setOnClickListener(v -> dialog.dismiss());
        deleteBtn.setOnClickListener(v -> deleteImages());
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wificon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo netcon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(!((wificon!=null && wificon.isConnected())||(netcon!=null && netcon.isConnected())))
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(BottomActivity.this);
            builder.setMessage("You are offline please check your internet connection")
                    .setTitle("No Internet Connection")
                    .setCancelable(true)
                    .setPositiveButton("Connect", (dialog, which) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                    .setNegativeButton("cancle", (dialog, which) -> {
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void deleteImages() {
        deleteBtn.setText("deleting ...");
        if(preferences.contains("service")&&!preferences.getString("service","").equals("")){
            DocumentReference serviceReference = db.document(preferences.getString("service",""));
            serviceReference.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        ArrayList<String> images = (ArrayList<String>) documentSnapshot.get("images");
                        for(int i=0;i<images.size();i++) {
                            StorageReference photoRef = storageReference.getReferenceFromUrl(images.get(i));
                            if(i==images.size()-1){
                                photoRef.delete().addOnSuccessListener(unused -> deleteService());
                            } else
                            photoRef.delete();
                        }
                    });
        }
    }

    private void deleteService() {
        if (preferences.contains("service") && !preferences.getString("service", "").equals("")) {
            DocumentReference serviceReference = db.document(preferences.getString("service", ""));
            serviceReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Map<String, Object> docUpdate = new HashMap<>();
                    docUpdate.put("service","");
                    db.collection("users").document(user.getUid()).update(docUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            editor.putString("service", "");
                            editor.commit();
                            Toast.makeText(getApplicationContext(),"Service Deleted Successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
            });
        }
    }

    public void addService(View view) {

        if(preferences.contains("service")&&!preferences.getString("service","").equals("")){
            dialog.show();
        }else{
            Intent i = new Intent(BottomActivity.this, SelectProfession.class);
            startActivity(i);
            customType(BottomActivity.this,"up-to-bottom");
        }
    }
}