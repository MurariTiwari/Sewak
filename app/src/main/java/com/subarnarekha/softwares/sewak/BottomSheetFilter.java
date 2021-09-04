package com.subarnarekha.softwares.sewak;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.ChipGroup;
import com.subarnarekha.softwares.sewak.searchresults.Results;

import org.jetbrains.annotations.NotNull;

public class BottomSheetFilter extends BottomSheetDialogFragment {

    private int radius;
    private  OnDistanceSelectedListener distanceSelectedListener;
    private ChipGroup chipGroup;
    private Button apply;
    public BottomSheetFilter(int radius) {
        // Required empty public constructor
        this.radius=radius;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_filter, container, false);
        chipGroup = view.findViewById(R.id.distance);
        apply = view.findViewById(R.id.apply);
        switch (radius){
            case 10:
                chipGroup.check(R.id.chip10);
                break;
            case 25:
                chipGroup.check(R.id.chip25);
                break;
            case 50:
                chipGroup.check(R.id.chip50);
                break;
            case 100:
                chipGroup.check(R.id.chip100);
                break;
        }

        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.chip10:
                    radius=10;
                    break;
                case R.id.chip25:
                    radius=25;
                    break;
                case R.id.chip50:
                    radius=50;
                    break;
                case R.id.chip100:
                    radius=100;
            }
        });
        apply.setOnClickListener(v -> {
            distanceSelectedListener.onSearchRadiusChange(radius);
            dismiss();
        });
        return view;
    }

    public interface OnDistanceSelectedListener{
        void onSearchRadiusChange(int r);
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        try{
            this.distanceSelectedListener= (OnDistanceSelectedListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(" need to implement OnDistanceSelectedListener interface.");
        }
    }
}
