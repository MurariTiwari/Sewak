package com.subarnarekha.softwares.sewak.service;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.subarnarekha.softwares.sewak.DeleteConfirmation;
import com.subarnarekha.softwares.sewak.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewService extends Fragment implements DeleteConfirmation.DeleteConfirmationListener {
    ViewPager viewPager;
    ViewServiceAdapter adapter;
    FirebaseFirestore db;
    private ProgressBar progressBar;
    FirebaseUser user;
    TextView serviceNameText,serviceDescription,serviceLocation,serviceTime;
    LinearLayout serviceMenuLayout,viewServiceLayout;
    ShimmerFrameLayout shimmer;
    ArrayList<String> images;
    ArrayList<Map<String,Object>> serviceItemModel;
    View withData,withoutdata;
    Task<DocumentSnapshot> task;
    Button deleteBtn;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ConstraintLayout header;

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
        withoutdata = view.findViewById(R.id.layout);
        deleteBtn = view.findViewById(R.id.deleteBtn);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        header = view.findViewById(R.id.with_data_header);

        preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = preferences.edit();
        fetchData();


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

        deleteBtn.setOnClickListener(v -> {
            DeleteConfirmation deleteConfirmation = new DeleteConfirmation();
            deleteConfirmation.setTargetFragment(ViewService.this,1);
            deleteConfirmation.show(getActivity().getSupportFragmentManager(), "Delete");
            //deleteData();
        });
        return view;
    }

    private void deleteData() {
        DocumentReference serviceReference = db.document(preferences.getString("service", ""));
        serviceReference.delete().addOnSuccessListener(unused -> {
            Map<String, Object> docUpdate = new HashMap<>();
            docUpdate.put("service","");
            db.collection("users").document(user.getUid()).update(docUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    editor.putString("service", "");
                    editor.commit();
                    Toast.makeText(getContext(),"Service Deleted Successfully", Toast.LENGTH_SHORT).show();
                    fetchData();
                }
            });
        });
    }

    private void fetchData() {
        if(preferences.contains("service"))
        {
            if(preferences.getString("service","").equals(""))
            {
                withoutdata.setVisibility(View.VISIBLE);
                withData.setVisibility(View.GONE);
                header.setVisibility(View.GONE);
            }else {
                String servicePath = preferences.getString("service","");
                DocumentReference serviceReference = db.document(servicePath);
                task= serviceReference.get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if(isAdded()){
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
                                    header.setVisibility(View.VISIBLE);
                                    viewServiceLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        });

            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onDeleteConfirmation() {
        deleteData();
    }
}