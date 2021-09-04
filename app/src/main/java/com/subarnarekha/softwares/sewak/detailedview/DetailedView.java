package com.subarnarekha.softwares.sewak.detailedview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.subarnarekha.softwares.sewak.Login.VerifyOtp;
import com.subarnarekha.softwares.sewak.R;
import com.subarnarekha.softwares.sewak.Splash;
import com.subarnarekha.softwares.sewak.home.BottomActivity;
import com.subarnarekha.softwares.sewak.service.ViewServiceAdapter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailedView extends AppCompatActivity {
    ViewPager viewPager;
    FirebaseUser loggedinUser;
    ViewServiceAdapter adapter;
    private ProgressBar progressBar;
    String desc,address,from,service,user,allowPhone;
    List<String> images,serviceMenu,priceMenu;
    String phoneNumber,profileImg,name;
    TextView descText,addressText,fromText,serviceText,profileName;
    ImageView saveBtn,callBtn;
    CircleImageView profilePic;
    LinearLayout serviceMenuLayout;
    FirebaseFirestore db;
    DocumentReference documentReference;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        desc = getIntent().getStringExtra("desc");
        address = getIntent().getStringExtra("address");
        from = getIntent().getStringExtra("from");
        user = getIntent().getStringExtra("user");
        allowPhone = getIntent().getStringExtra("allow");
        images = getIntent().getStringArrayListExtra("images");
        serviceMenu = getIntent().getStringArrayListExtra("serviceMenu");
        priceMenu = getIntent().getStringArrayListExtra("priceMenu");
        service = getIntent().getStringExtra("service");
        serviceText = findViewById(R.id.serviceName);
        descText = findViewById(R.id.serviceDescription);
        addressText = findViewById(R.id.serviceLocation);
        fromText = findViewById(R.id.serviceTime);
        viewPager = findViewById(R.id.service_images);
        progressBar = findViewById(R.id.progressBar);
        serviceMenuLayout = findViewById(R.id.service_menu);
        profilePic = findViewById(R.id.profile_page);
        profileName = findViewById(R.id.your_name);
        callBtn = findViewById(R.id.call);
        saveBtn = findViewById(R.id.save);
        descText.setText(desc);
        addressText.setText(address);
        fromText.setText("SINCE: "+from);
        serviceText.setText(service);

        db = FirebaseFirestore.getInstance();
        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = preferences.edit();

        if(!allowPhone.equals("yes")){
            callBtn.setVisibility(View.GONE);
            saveBtn.setVisibility(View.GONE);
        }
        progressBar.setProgress(100/images.size());
        adapter = new ViewServiceAdapter(this, (ArrayList<String>) images);
        viewPager.setAdapter(adapter);
        for(int i=0;i<serviceMenu.size();i++)
        {
            View serviceView = getLayoutInflater().inflate(R.layout.view_service_menu,null,false);
            TextView serviceName = serviceView.findViewById(R.id.view_service_name);
            TextView servicePrice = serviceView.findViewById(R.id.view_service_price);
            serviceName.setText(serviceMenu.get(i));
            servicePrice.setText(priceMenu.get(i));
            serviceMenuLayout.addView(serviceView);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                progressBar.setProgress(100*(position+1)/images.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        loggedinUser = FirebaseAuth.getInstance().getCurrentUser();

        documentReference = db.document("users/"+user);
        documentReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    name = (String) documentSnapshot.get("name");
                    profileImg = (String) documentSnapshot.get("profileimg");
                    phoneNumber = (String) documentSnapshot.get("phoneno");
                    if (name.equals("")) {
                        profileName.setText("Unknown");
                    } else {
                        profileName.setText(name);
                    }
                    if(!profileImg.equals("")){
                        Glide.with(this).load(profileImg).into(profilePic);
                    }

                });
        callBtn.setOnClickListener(v -> {
            if(user!=null) {
                Map<String, Object> docUpdate = new HashMap<>();
                Map<String, Object> historyData = new HashMap<>();
                historyData.put("phoneno", preferences.getString("phoneno", ""));
                historyData.put("time", DateFormat.getDateTimeInstance().format(new Date()));
                historyData.put("name", preferences.getString("name", ""));
                historyData.put("img", preferences.getString("profileimg", ""));
                documentReference = db.document("users/" + user+"/incomming/"+preferences.getString("phoneno", ""));
                documentReference.set(historyData)
                        .addOnSuccessListener(unused -> {
                            docUpdate.put("incomming","yes");
                                  db.document("users/" + user).update(docUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                                            startActivity(intent);
                                            }
                                    });
                        });

            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!=null) {
                    Map<String, Object> docUpdate = new HashMap<>();
                    Map<String, Object> contactData = new HashMap<>();
                    contactData.put("phoneno", phoneNumber);
                    contactData.put("service", service);
                    contactData.put("name", name);
                    contactData.put("img", profileImg);
                    documentReference = db.document("users/" + loggedinUser.getUid()+"/contact/"+phoneNumber);
                    documentReference.set(contactData)
                            .addOnSuccessListener(unused -> {
                                if(preferences.contains("contact")) {
                                    if (preferences.getString("contact", "").equals("")) {
                                        docUpdate.put("contact","yes");
                                        db.document("users/" + loggedinUser.getUid()).update(docUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                editor.putString("contact", "yes");
                                                editor.commit();
                                                Toast.makeText(getApplicationContext(), "Contact Saved Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                                Toast.makeText(getApplicationContext(), "Contact Saved Successfully", Toast.LENGTH_SHORT).show();
                            });
                }
                }

        });
    }

}