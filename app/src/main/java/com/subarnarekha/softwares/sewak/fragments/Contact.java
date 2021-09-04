package com.subarnarekha.softwares.sewak.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.subarnarekha.softwares.sewak.R;

import java.util.ArrayList;
import java.util.List;

public class Contact extends Fragment {
    SharedPreferences preferences;
    ConstraintLayout noData;
    RecyclerView dataView;
    FirebaseUser loggedinUser;
    FirebaseFirestore db;
    DocumentReference documentReference;
    List<String> img,name,service,phoneno;
    ConstraintLayout header;
    TextView tv1,tv2;
    LottieAnimationView l1;
    ShimmerFrameLayout shimmer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        noData = view.findViewById(R.id.layout);
        dataView = view.findViewById(R.id.with_data);
        header = view.findViewById(R.id.with_data_header);
        tv1 = view.findViewById(R.id.textView4);
        tv2 = view.findViewById(R.id.textView5);
        l1 = view.findViewById(R.id.lottieAnimationView);
        shimmer = view.findViewById(R.id.search_result_shimmer);
        dataView.setLayoutManager(new LinearLayoutManager(getActivity()));
        db = FirebaseFirestore.getInstance();
        shimmer.setVisibility(View.GONE);
        loggedinUser = FirebaseAuth.getInstance().getCurrentUser();

        img = new ArrayList<>();
        name = new ArrayList<>();
        service = new ArrayList<>();
        phoneno = new ArrayList<>();

        if(preferences.contains("contact")) {
            if (preferences.getString("contact", "").equals("")) {
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                l1.setVisibility(View.VISIBLE);
                dataView.setVisibility(View.GONE);
                header.setVisibility(View.GONE);
            }else{
                shimmer.setVisibility(View.VISIBLE);
                db.collection("users/"+loggedinUser.getUid()+"/contact")
                        .get()
                        .addOnCompleteListener(task -> {
                            if(isAdded())
                            {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        img.add((String) document.get("img"));
                                        name.add((String) document.get("name"));
                                        phoneno.add((String) document.get("phoneno"));
                                        service.add((String) document.get("service"));
                                        dataView.setVisibility(View.VISIBLE);
                                        shimmer.setVisibility(View.GONE);
                                    }
                                }
                                ContactAdapter adapter = new ContactAdapter(getActivity(),
                                        name,
                                        img,
                                        service,
                                        phoneno);
                                dataView.setAdapter(adapter);
                            }
                        });
                tv1.setVisibility(View.GONE);
                tv2.setVisibility(View.GONE);
                l1.setVisibility(View.GONE);
                header.setVisibility(View.VISIBLE);
            }
        }
        return view;
    }
}