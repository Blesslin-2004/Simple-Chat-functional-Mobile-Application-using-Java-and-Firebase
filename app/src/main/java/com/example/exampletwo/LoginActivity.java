package com.example.exampletwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private EditText mobileET, otpET;
    private Button getotpbtn, signupbtn;
    private ImageButton eye;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private String verificationId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        mobileET = findViewById(R.id.mobileET);
        getotpbtn = findViewById(R.id.getotpbtn);
        progressBar = findViewById(R.id.progressBar);
        otpET = findViewById(R.id.otpET);
        signupbtn = findViewById(R.id.signupbtn);



       getotpbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               String mobile = mobileET.getText().toString().trim();
               if (mobile.isEmpty()) {
                   Toast.makeText(LoginActivity.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                   mobileET.setError("Enter a valid phone number");

               }
               if (TextUtils.isEmpty(mobile) || mobile.length() != 10) {
                   mobileET.setError("Enter a valid phone number");
                   mobileET.requestFocus();
                   return;
               }
               progressBar.setVisibility(View.VISIBLE);
               getotpbtn.setVisibility(View.INVISIBLE);


               PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + mobile,
                       60,
                       TimeUnit.SECONDS,
                       LoginActivity.this,
                       new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                           @Override
                           public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                               signInWithPhoneAuthCredential(phoneAuthCredential);
                               progressBar.setVisibility(View.GONE);


                           }

                           @Override
                           public void onVerificationFailed(@NonNull FirebaseException e) {
                               progressBar.setVisibility(View.GONE);

                               Toast.makeText(LoginActivity.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                           }
                           @Override
                           public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                               super.onCodeSent(s, forceResendingToken);
                               verificationId = s;
                               Toast.makeText(LoginActivity.this, "OTP Sent Successfully", Toast.LENGTH_SHORT).show();
                               progressBar.setVisibility(View.GONE);
                               mobileET.setEnabled(false);
                               otpET.setVisibility(View.VISIBLE);
                               signupbtn.setVisibility(View.VISIBLE);

                           }
                       });
           }

       });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = otpET.getText().toString().trim();
                verifyOtp(otp);
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        signupbtn = findViewById(R.id.signupbtn);


                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),ChatActivity.class));

                            // Here you can start the next activity or do whatever you want
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void verifyOtp(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
        signInWithPhoneAuthCredential(credential);
    }




    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already signed in, navigate to the main activity or home screen
            startActivity(new Intent(LoginActivity.this, ChatActivity.class));
            finish(); // Finish MainActivity to prevent going back to it when pressing back button from HomeActivity
        } else {
            // User is not signed in, proceed with the registration/login process
            // You can launch the registration activity here if needed
        }
    }

    }

