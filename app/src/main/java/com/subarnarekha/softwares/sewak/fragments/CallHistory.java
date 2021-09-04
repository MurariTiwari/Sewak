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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.subarnarekha.softwares.sewak.R;

import java.util.ArrayList;
import java.util.List;


public class CallHistory extends Fragment {
    SharedPreferences preferences;
    ConstraintLayout noData;
    RecyclerView dataView;
    FirebaseUser loggedinUser;
    FirebaseFirestore db;
    ConstraintLayout header;
    TextView tv1,tv2;
    LottieAnimationView l1;
    List<String> img,name,time,phoneno;
    ShimmerFrameLayout shimmer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.fragment_call_history, container, false);
        noData = view.findViewById(R.id.layout);
        dataView = view.findViewById(R.id.with_data);
        header = view.findViewById(R.id.with_data_header);
        shimmer = view.findViewById(R.id.search_result_shimmer);
        tv1 = view.findViewById(R.id.textView4);
        tv2 = view.findViewById(R.id.textView5);
        l1 = view.findViewById(R.id.lottieAnimationView2);

        shimmer.setVisibility(View.GONE);
        dataView.setLayoutManager(new LinearLayoutManager(getActivity()));
        db = FirebaseFirestore.getInstance();

        loggedinUser = FirebaseAuth.getInstance().getCurrentUser();

        img = new ArrayList<>();
        name = new ArrayList<>();
        time = new ArrayList<>();
        phoneno = new ArrayList<>();
        if(isAdded())
        {
        if(preferences.contains("incomming")) {
            if (preferences.getString("incomming", "").equals("")) {
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                l1.setVisibility(View.VISIBLE);
                dataView.setVisibility(View.GONE);
                header.setVisibility(View.GONE);
            }else{
                shimmer.setVisibility(View.VISIBLE);
                db.collection("users/"+loggedinUser.getUid()+"/incomming")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            img.add((String) document.get("img"));
                                            name.add((String) document.get("name"));
                                            phoneno.add((String) document.get("phoneno"));
                                            time.add((String) document.get("time"));

                                            dataView.setVisibility(View.VISIBLE);
                                            shimmer.setVisibility(View.GONE);
                                        }
                                    }
                                    CallHistoryAdapter adapter = new CallHistoryAdapter(getActivity(),img,name,time,phoneno);
                                    dataView.setAdapter(adapter);
                                }
                        });
                tv1.setVisibility(View.GONE);
                tv2.setVisibility(View.GONE);
                l1.setVisibility(View.GONE);
                header.setVisibility(View.VISIBLE);
            }
        }}
        return view;
    }
}