package com.subarnarekha.softwares.sewak.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.subarnarekha.softwares.sewak.R;
import com.subarnarekha.softwares.sewak.Splash;
import com.subarnarekha.softwares.sewak.addService.Adapter;
import com.subarnarekha.softwares.sewak.addService.AddService;
import com.subarnarekha.softwares.sewak.addService.SelectProfession;
import com.subarnarekha.softwares.sewak.home.BottomActivity;
import com.subarnarekha.softwares.sewak.profile.ProfileScreen;
import com.subarnarekha.softwares.sewak.searchresults.Results;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class Home extends Fragment {

    RecyclerView datalist;
    CircleImageView profile;
    TextView yourLocation;
    List<String> names = new ArrayList<>();
    List<Integer> images = new ArrayList<>();
    Adapter adapter;
    ImageView menubar;
    SharedPreferences preferences;
    FusedLocationProviderClient fusedLocationProviderClient;
    String API_KEY = "AIzaSyBxhYXmUwZeB-Tp17KHEgeHpDoAGtXWebI";
    SharedPreferences.Editor editor;

    public Home() {
        // Required empty public constructor
    }
    //https://firebase.google.com/docs/firestore/solutions/geoqueries#java
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        datalist = view.findViewById(R.id.categories);
        profile = view.findViewById(R.id.profile_page);
        menubar = view.findViewById(R.id.menubar);
        yourLocation = view.findViewById(R.id.your_location);
        preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = preferences.edit();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        Places.initialize(getContext(),API_KEY);
        names.add("Barber");
        names.add("Beautician");
        names.add("Carpenter");
        names.add("Cook & Catering");
        names.add("Construction");
        names.add("Doctor");
        names.add("Electrician");
        names.add("Farmer");
        names.add("GYM");
        names.add("Light & Sound");
        names.add("Laundry");
        names.add("Mechanic");
        names.add("Milkman");
        names.add("Musician & Band");
        names.add("Plumber");
        names.add("Painter");
        names.add("Photographer");
        names.add("Priest");
        names.add("Poultry Farm");
        names.add("Tutor");
        names.add("Tent & House");
        names.add("Tailor");
        names.add("Welder");


        images.add(R.drawable.barber);
        images.add(R.drawable.makeover);
        images.add(R.drawable.carpenter);
        images.add(R.drawable.cooking);
        images.add(R.drawable.brickwall);
        images.add(R.drawable.doctor);
        images.add(R.drawable.electrician);
        images.add(R.drawable.harvest);
        images.add(R.drawable.gym);
        images.add(R.drawable.concert);
        images.add(R.drawable.washing_clothes);
        images.add(R.drawable.car_repair);
        images.add(R.drawable.milk);
        images.add(R.drawable.concert_2);
        images.add(R.drawable.plumber);
        images.add(R.drawable.painter);
        images.add(R.drawable.photo);
        images.add(R.drawable.guru);
        images.add(R.drawable.hen);
        images.add(R.drawable.presentation);
        images.add(R.drawable.tent);
        images.add(R.drawable.tailor);
        images.add(R.drawable.welder);

        adapter = new Adapter(names,images,getContext(),"home");
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),4,GridLayoutManager.VERTICAL,false);
        datalist.setLayoutManager(gridLayoutManager);
        datalist.setAdapter(adapter);
        profile.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), ProfileScreen.class);
            startActivity(i);
        });
        if(preferences.contains("profileimg")&&!preferences.getString("profileimg","").equals("")){
            Glide.with(getContext()).load(preferences.getString("profileimg","")).into(profile);
        }
        menubar.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(),menubar);
            popupMenu.getMenuInflater().inflate(R.menu.drawer_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                   //switch (item.getItemId())
                    //{

                       // Intent i = new Intent(getActivity(), Results.class);
                       // startActivity(i);
                   // }
                    return false;
                }
            });
            popupMenu.show();
        });

        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                            if(location != null)
                            {
                                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                    yourLocation.setText(addresses.get(0).getAddressLine(0));
                                    //dataAddress = addresses.get(0).getAddressLine(0);
                                    editor.putFloat("lat", (float) location.getLatitude());
                                    editor.putFloat("long", (float) location.getLongitude());
                                    editor.commit();
                                    // workLocation.setText(addresses.get(0).getPostalCode());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
        yourLocation.setFocusable(false);
        yourLocation.setOnClickListener(v -> {
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                    Place.Field.LAT_LNG,
                    Place.Field.NAME);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,fieldList)
                    .setCountry("IN")
                    .build(getContext());
            startActivityForResult(intent,100);
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(preferences.contains("profileimg")&&!preferences.getString("profileimg","").equals("")){
            Glide.with(getContext()).load(preferences.getString("profileimg","")).into(profile);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            yourLocation.setText(place.getAddress());
            editor.putFloat("lat", (float) place.getLatLng().latitude);
            editor.putFloat("long", (float) place.getLatLng().longitude);
            editor.commit();
        }
        else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status= Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getContext(),status.getStatusMessage(),Toast.LENGTH_LONG).show();
        }
    }
}