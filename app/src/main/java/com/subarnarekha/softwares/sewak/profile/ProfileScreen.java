package com.subarnarekha.softwares.sewak.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.subarnarekha.softwares.sewak.Login.LoginForm;
import com.subarnarekha.softwares.sewak.R;
import com.subarnarekha.softwares.sewak.addService.SelectProfession;
import com.subarnarekha.softwares.sewak.home.BottomActivity;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfileScreen extends AppCompatActivity {
    EditText newname,newaddress;
    TextView name,address,phoneno,heading;
    View divider1,divider2;
    ImageView logout,profileimg;
    Uri filepath;
    FirebaseUser user;
    String userid="",username="",useraddress="",userphoneno="",userprofileurl="";
    ProgressBar spinner;
    FirebaseFirestore db;
    DocumentReference documentReference;
    StorageReference storageReference;
    FloatingActionButton camera,edit,save;
    Boolean call=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        newname = findViewById(R.id.newname);
        newaddress =findViewById(R.id.newaddress);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        phoneno = findViewById(R.id.phoneno);
        heading = findViewById(R.id.heading);
        divider1 = findViewById(R.id.divider);
        divider2 = findViewById(R.id.divider2);
        camera = findViewById(R.id.camra);
        edit = findViewById(R.id.edit);
        save = findViewById(R.id.save);
        logout = findViewById(R.id.logout);
        phoneno = findViewById(R.id.phone);
        name = findViewById(R.id.name);
        profileimg = findViewById(R.id.profile_image);
        spinner = findViewById(R.id.profile_spinner);


        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

       // documentReference=db.collection("users").document(userid);

        userphoneno=user.getPhoneNumber();
        phoneno.setText(user.getPhoneNumber());
        userid = user.getUid();

        // Initialize data to Profile
        if(call)
        db.collection("users").document(userid).addSnapshotListener((value, error) -> {
            if(value.exists())
            {
                if(value.get("name")!=null&&value.get("name")!="")
                {
                    username = value.get("name").toString();
                    name.setText(username);

                }
                if(value.get("address")!=null&& value.get("address")!="")
                {
                    useraddress = value.get("address").toString();
                    address.setText(useraddress);
                }
                if(value.get("profileimg")!=null&&value.get("profileimg")!="")
                {
                    userprofileurl = value.get("profileimg").toString();
                    Glide.with(getApplicationContext()).load(value.get("profileimg").toString()).into(profileimg);
                }
            }
        });


        edit.setOnClickListener(v -> {
                heading.setText("Edit Profile");
                editProfileScreen();
        });

        camera.setOnClickListener(v -> {
            Dexter.withContext(getApplicationContext())
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent,"Select Your Profile Image"),101);

                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                        }
                    }).check();
        });

        logout.setOnClickListener(v -> {
            call=false;
            FirebaseAuth.getInstance().signOut();
            finishAffinity();
            Intent intent = new Intent(ProfileScreen.this, LoginForm.class);

            startActivity(intent);
        });


        save.setOnClickListener(v -> {
            if(newname.getText().toString().trim().length()!=0&&newaddress.getText().toString().length()!=0)
            {
                if(newname.getText().toString().trim().equals(username)&&newaddress.getText().toString().trim().equals(useraddress))
                {
                    profileScreen();
                    heading.setText("Profile");
                }else {
                    username = newname.getText().toString().trim();
                    useraddress = newaddress.getText().toString();
                    Map<String, Object> user = new HashMap<>();
                    user.put("name", username);
                    user.put("address", useraddress);
                    spinner.setVisibility(View.VISIBLE);
                    save.setVisibility(View.GONE);

                    db.collection("users").document(userid).addSnapshotListener((value, error) -> {
                        db.collection("users").document(userid).update(user)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(getApplicationContext(), "Profile updated Sucessfully", Toast.LENGTH_SHORT).show();
                                    spinner.setVisibility(View.GONE);
                                    profileScreen();
                                    heading.setText("Profile");
                                });
                    });
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Enter valid data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode==RESULT_OK)
        {
            filepath = data.getData();
            try {
                InputStream inputStream =  getContentResolver().openInputStream(filepath);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                profileimg.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            final StorageReference uploader = storageReference.child("profileimages/"+"img_"+userid);
            spinner.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE);
            uploader.putFile(filepath)
                    .addOnSuccessListener(taskSnapshot -> uploader.getDownloadUrl().addOnSuccessListener(uri -> {

                        Map<String, Object> user = new HashMap<>();
                        user.put("profileimg", uri.toString());
                            db.collection("users").document(userid).update(user);
                        spinner.setVisibility(View.GONE);
                        edit.setVisibility(View.VISIBLE);
                    }))
                    .addOnProgressListener(snapshot -> {

                    });
        }
    }

    public void profileScreen()
    {
        newname.setVisibility(View.GONE);
        newaddress.setVisibility(View.GONE);
        name.setVisibility(View.VISIBLE);
        address.setVisibility(View.VISIBLE);
        divider2.setVisibility(View.VISIBLE);
        divider1.setVisibility(View.VISIBLE);
        camera.setVisibility(View.VISIBLE);
        logout.setVisibility(View.VISIBLE);
        save.setVisibility(View.GONE);
        edit.setVisibility(View.VISIBLE);
    }
    public  void editProfileScreen()
    {
        newname.setVisibility(View.VISIBLE);
        newname.setText(username);
        newaddress.setVisibility(View.VISIBLE);
        newaddress.setText(useraddress);
        name.setVisibility(View.GONE);
        address.setVisibility(View.GONE);
        divider2.setVisibility(View.GONE);
        divider1.setVisibility(View.GONE);
        camera.setVisibility(View.GONE);
        logout.setVisibility(View.GONE);
        save.setVisibility(View.VISIBLE);
        edit.setVisibility(View.GONE);

    }
}