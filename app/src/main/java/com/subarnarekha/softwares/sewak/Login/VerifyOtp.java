package com.subarnarekha.softwares.sewak.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.subarnarekha.softwares.sewak.R;
import com.subarnarekha.softwares.sewak.home.BottomActivity;
import com.subarnarekha.softwares.sewak.profile.ProfileScreen;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VerifyOtp extends AppCompatActivity {
    Pinview pin;
    FloatingActionButton login;
    String phoneno,otpid,pinfrompinview="";
    ProgressBar spinner;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    DocumentReference documentReference;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        phoneno = getIntent().getStringExtra("phoneno");
        pin = findViewById(R.id.pin);
        login = findViewById(R.id.login);
        spinner = findViewById(R.id.profile_spinner);
        spinner.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        initiateOTP();
        login.setOnClickListener(v -> {
           pinfrompinview = pin.getValue();
           if(pinfrompinview.length()!=6)
           {
               Toast.makeText(getApplicationContext(),"Enter valid Pin", Toast.LENGTH_SHORT).show();
           }
           else{
               PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid,pinfrompinview);
               login.setVisibility(View.GONE);
               spinner.setVisibility(View.VISIBLE);
               signInWithPhoneAuthCredential(credential);
           }
        });
    }

    private void initiateOTP() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneno,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    // sim not in device
                    @Override
                    public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        // super.onCodeSent(s, forceResendingToken);
                        otpid=s;
                    }
                    // sim in same device
                    @Override
                    public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                        login.setVisibility(View.GONE);
                        spinner.setVisibility(View.VISIBLE);
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
        );
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        // Log.d(TAG, "signInWithCredential:success");

                        FirebaseUser fuser = task.getResult().getUser();
                        // Update UI
                        Map<String, Object> user = new HashMap<>();
                        user.put("phoneno", fuser.getPhoneNumber());
                        user.put("profileimg", "");
                        user.put("name", "");
                        user.put("address", "");
                        user.put("service", "");

                        documentReference = db.collection("users").document(fuser.getUid());
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists())
                                {
                                    editor.putString("address", (String) documentSnapshot.get("address"));
                                    editor.putString("name",(String) documentSnapshot.get("name"));
                                    editor.putString("phoneno", (String) documentSnapshot.get("phoneno"));
                                    editor.putString("profileimg", (String) documentSnapshot.get("profileimg"));
                                    editor.putString("service", (String) documentSnapshot.get("service"));
                                    editor.commit();
                                    startActivity(new Intent(VerifyOtp.this, BottomActivity.class));
                                    finish();
                                }
                                else {
                                    documentReference.set(user)
                                            .addOnSuccessListener(unused -> {
                                                editor.putString("address", "");
                                                editor.putString("name","");
                                                editor.putString("phoneno", (String) documentSnapshot.get("phoneno"));
                                                editor.putString("profileimg", "");
                                                editor.putString("service", "");
                                                editor.commit();
                                                startActivity(new Intent(VerifyOtp.this, BottomActivity.class));
                                                finish();
                                            });
                                }
                            }
                        });
                    } else {
                        login.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"SignIn Error", Toast.LENGTH_SHORT).show();

                        // Sign in failed, display a message and update the UI
                        // Log.w(TAG, "signInWithCredential:failure", task.getException());
                        // if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        // }
                    }
                });
    }
}