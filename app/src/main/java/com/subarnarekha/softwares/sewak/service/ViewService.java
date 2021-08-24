package com.subarnarekha.softwares.sewak.service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.subarnarekha.softwares.sewak.AppIntroduction.AppIntroAdapter;
import com.subarnarekha.softwares.sewak.R;
import com.subarnarekha.softwares.sewak.addService.ServiceItemModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewService extends Fragment {
    ViewPager viewPager;
    ViewServiceAdapter adapter;
    FirebaseFirestore db;
    private ProgressBar progressBar;
    FirebaseUser user;
    String serviceName;
    TextView serviceNameText,serviceDescription,serviceLocation,serviceTime;
    LinearLayout serviceMenuLayout,viewServiceLayout;
    ShimmerFrameLayout shimmer;
    ArrayList<String> images;
    ArrayList<Map<String,Object>> serviceItemModel;
    View withData,withoutdata;


    SharedPreferences preferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_view_service, container, false);

        viewPager = view.findViewById(R.id.service_images);
        serviceNameText = view.findViewById(R.id.serviceName);
        serviceDescription = view.findViewById(R.id.serviceDescription);
        serviceLocation = view.findViewById(R.id.serviceLocation);
        serviceTime = view.findViewById( R.id.serviceTime);
        progressBar = view.findViewById(R.id.progressBar);
        serviceMenuLayout = view.findViewById(R.id.service_menu);
        shimmer = view.findViewById(R.id.view_service_shimmer);
        viewServiceLayout = view.findViewById(R.id.view_service);
        withData = view.findViewById(R.id.with_data);
        withoutdata = view.findViewById(R.id.no_data);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        if(preferences.contains("service"))
        {
            if(preferences.getString("service","").equals(""))
            {
                withoutdata.setVisibility(View.VISIBLE);
                withData.setVisibility(View.GONE);
            }else {
                String servicePath = preferences.getString("service","");
                DocumentReference serviceReference = db.document(servicePath);
                serviceReference.get()
                        .addOnSuccessListener(documentSnapshot -> {
                            images = (ArrayList<String>) documentSnapshot.get("images");
                            progressBar.setProgress(100/images.size());
                            adapter = new ViewServiceAdapter(getContext(),images);
                            viewPager.setAdapter(adapter);
                            serviceNameText.setText(documentSnapshot.getString("service"));
                            serviceDescription.setText(documentSnapshot.getString("biography"));
                            serviceLocation.setText(documentSnapshot.getString("address"));
                            serviceTime.setText("SINCE :  "+documentSnapshot.getString("workStart"));
                            serviceItemModel = (ArrayList<Map<String, Object>>) documentSnapshot.get("serviceMenu");
                            for(int i=0;i<serviceItemModel.size();i++)
                            {
                                Map<String, Object> item = serviceItemModel.get(i);
                                View serviceView = getLayoutInflater().inflate(R.layout.view_service_menu,null,false);
                                TextView serviceName = serviceView.findViewById(R.id.view_service_name);
                                TextView servicePrice = serviceView.findViewById(R.id.view_service_price);
                                serviceName.setText(item.get("serviceName").toString());
                                servicePrice.setText(item.get("servicePrice").toString());
                                serviceMenuLayout.addView(serviceView);
                                shimmer.setVisibility(View.GONE);
                                viewServiceLayout.setVisibility(View.VISIBLE);
                            }
                        });

            }
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

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

}