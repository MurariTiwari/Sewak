package com.subarnarekha.softwares.sewak.addService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.subarnarekha.softwares.sewak.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddService extends AppCompatActivity {

    TextView startDate,proffesionView;
    EditText workLocation,biography;
    Switch allowPhone;
    DatePickerDialog.OnDateSetListener dateChangedListener;
    LinearLayout layoutList;
    Button add,save,upload;
    String API_KEY = "AIzaSyBxhYXmUwZeB-Tp17KHEgeHpDoAGtXWebI";
    String profession;
    View parent;
    StorageReference storageReference;
    FirebaseFirestore db;
    FirebaseUser user;
    DocumentReference documentReference;
    List<String> files,status;
    RecyclerView recyclerView;
    ImageUploadAdapter adapter;
    FusedLocationProviderClient fusedLocationProviderClient;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    String dataAddress="",dataBio="",dataStartDate="",dataPhoneCall="yes";
    double dataLat = 0,dataLong = 0;
    List<ServiceItemModel> dataServiceMenu;
    List<String> dataImages;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        startDate = findViewById(R.id.selectdate);
        biography = findViewById(R.id.bio);
        workLocation = findViewById(R.id.work_location);
        layoutList = findViewById(R.id.layoutList);
        add = findViewById(R.id.floatingActionButton);
        proffesionView = findViewById(R.id.profession);
        save = findViewById(R.id.savedata);
        upload = findViewById(R.id.upload);
        recyclerView = findViewById(R.id.imagelist);
        parent = findViewById(R.id.parent_layout);
        allowPhone = findViewById(R.id.allow_phone_call);
        storageReference = FirebaseStorage.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        files = new ArrayList<>();
        status = new ArrayList<>();

        dataImages =new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        profession = getIntent().getStringExtra("profession");
        proffesionView.setText(profession);
        adapter = new ImageUploadAdapter(files,status);
        recyclerView.setAdapter(adapter);


        preferences = getSharedPreferences("service", Context.MODE_PRIVATE);
        editor = preferences.edit();


            Dexter.withContext(getApplicationContext())
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
    if(location != null)
    {
       Geocoder geocoder = new Geocoder(AddService.this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            workLocation.setText(addresses.get(0).getAddressLine(0));
            dataAddress = addresses.get(0).getAddressLine(0);
            dataLat = location.getLatitude();
            dataLong = location.getLongitude();
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
        startDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddService.this,
                    android.R.style.Theme_Material_Dialog_MinWidth,
                    dateChangedListener,
                    year,month,day
            );
            //datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });

        dateChangedListener = (view, year, month, dayOfMonth) -> {
            month+=1;
            String date = dayOfMonth+"/"+month+"/"+year;
            startDate.setText(date);
            dataStartDate = date;
        };

        allowPhone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    dataPhoneCall="yes";

                } else {
                    dataPhoneCall="no";
                }
            }
        });

        Places.initialize(getApplicationContext(),API_KEY);

        workLocation.setFocusable(false);
        workLocation.setOnClickListener(v -> {
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                    Place.Field.LAT_LNG,
                    Place.Field.NAME);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,fieldList)
                    .setCountry("IN")
                    .build(AddService.this);
            startActivityForResult(intent,100);
        });
        addView();
        add.setOnClickListener(v -> addView());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBio = biography.getText().toString();
                if(isValid())
                {
                    Map<String, Object> docData = new HashMap<>();
                    Map<String, Object> docUpdate = new HashMap<>();
                    docData.put("user", user.getUid());
                    docData.put("address", dataAddress);
                    docData.put("longitude", dataLong);
                    docData.put("latitude", dataLat);
                    docData.put("biography", dataBio);
                    docData.put("workStart", dataStartDate);
                    docData.put("serviceMenu", dataServiceMenu);
                    docData.put("images", dataImages);
                    docData.put("allowPhone", dataPhoneCall);
                    docData.put("service",profession);
                    db.collection(profession.replaceAll("\\s", ""))
                            .add(docData)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    // Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                    docUpdate.put("service",profession.replaceAll("\\s", "")+"/"+documentReference.getId());
                                    db.collection("users").document(user.getUid()).update(docUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            // save to preferences
                                            editor.putString("user", user.getUid());
                                            editor.putString("address",dataAddress);
                                            editor.putFloat("longitude", (float) dataLong);
                                            editor.putFloat("latitude", (float) dataLong);
                                            editor.putString("biography", dataBio);
                                            editor.putString("workStart", dataStartDate);
                                            // editor.putString("serviceMenu", String.valueOf(dataServiceMenu));
                                            // editor.putString("images", dataImages);
                                            editor.putString("allowPhone", dataPhoneCall);
                                            editor.putString("service", profession);

                                            editor.commit();
                                            Snackbar snackbar = Snackbar
                                                    .make(parent, "Service added successfully", Snackbar.LENGTH_LONG);
                                            snackbar.show();
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Log.w(TAG, "Error adding document", e);
                                }
                            });
                }
            }
        });
        upload.setOnClickListener(v -> Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Select your Professional images "),101);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check());

    }

    private boolean isValid() {
        dataServiceMenu = new ArrayList<>();

        if(dataAddress.trim().length()==0 || dataLong==0 ||dataLat==0)
        {
            Snackbar snackbar = Snackbar
                    .make(parent, "Please select your work location", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        else if(dataBio.trim().length()==0 )
        {
            Snackbar snackbar = Snackbar
                    .make(parent, "Please enter your biography", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        else if(dataStartDate.trim().length()==0)
        {
            Snackbar snackbar = Snackbar
                    .make(parent, "Please select from when you started working", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        else if(dataImages.size() ==0)
        {
            Snackbar snackbar = Snackbar
                    .make(parent, "Please select your professional images", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        for(int i=0;i<layoutList.getChildCount();i++)
        {
            View  item = layoutList.getChildAt(i);
            EditText serviceName = item.findViewById(R.id.add_service_text);
            EditText servicePrice = item.findViewById(R.id.add_price);
            if(serviceName.getText().toString().trim().equals("")){

                Snackbar snackbar = Snackbar
                        .make(parent, "Please add a valid Service Menu", Snackbar.LENGTH_LONG);
                snackbar.show();
                return false;
            }
            else if(servicePrice.getText().toString().trim().equals(""))
            {
                Snackbar snackbar = Snackbar
                        .make(parent, "Please add a valid Service Menu", Snackbar.LENGTH_LONG);
                snackbar.show();
                return false;
            }
            ServiceItemModel serviceItemModel = new ServiceItemModel(serviceName.getText().toString(),
                    servicePrice.getText().toString());
            dataServiceMenu.add(i,serviceItemModel);
        }
        return  true;
    }

    private void addView() {
        View addServiceView = getLayoutInflater().inflate(R.layout.service_menu,null,false);
        EditText serviceName = addServiceView.findViewById(R.id.add_service_text);
        EditText servicePrice = addServiceView.findViewById(R.id.add_price);
        ImageView close = addServiceView.findViewById(R.id.close_service_menu);
        close.setOnClickListener(v1 -> removeView(addServiceView));
        layoutList.addView(addServiceView);
    }

    private void removeView(View v) {
        layoutList.removeView(v);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            workLocation.setText(place.getAddress());
            dataAddress = place.getAddress();
            dataLat = place.getLatLng().latitude;
            dataLong = place.getLatLng().longitude;
        }
        else if(requestCode==101 && resultCode == RESULT_OK)
        {
            if(data.getClipData()!=null)
            {
                for(int i=0;i<data.getClipData().getItemCount();i++)
                {
                    Uri fileuri = data.getClipData().getItemAt(i).getUri();

                    int index = files.size();
                    files.add(fileuri.getPath());
                    status.add("loading");
                    adapter.notifyDataSetChanged();



                    StorageReference uploader = storageReference.child("/serviceimages").child("img_"+profession+index+"_"+user.getUid());
                    uploader.putFile(fileuri)
                            .addOnSuccessListener(taskSnapshot -> {
                                status.remove(index);
                                status.add(index,"done");
                                adapter.notifyDataSetChanged();
                                uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        dataImages.add(uri.toString());
                                    }
                                });
                            });
                }
            }
            else if(data.getData()!=null)
            {
                int index = files.size();
                files.add(data.getData().getPath());
                status.add("loading");
                adapter.notifyDataSetChanged();

                StorageReference uploader = storageReference.child("/serviceimages").child("img_"+profession+index+"_"+user.getUid());
                uploader.putFile(data.getData())
                        .addOnSuccessListener(taskSnapshot -> {
                            status.remove(index);
                            status.add(index,"done");
                            adapter.notifyDataSetChanged();
                            uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    dataImages.add(uri.toString());
                                }
                            });
                        });

            }
        }
        else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status= Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(),status.getStatusMessage(),Toast.LENGTH_LONG).show();
        }
    }
}