package com.example.sai.girlstalk.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sai.GirlsTalk.R;
import com.example.sai.girlstalk.utils.BounceInterpolar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;


public class OTP extends AppCompatActivity {

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private View parentlayout;
    ProgressDialog progress;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private TextView t1;
    private ImageView i1,i2;
    private Animation bounceanime,moveanime;
    private EditText e1;
    private Button b1;
    private boolean isFirstRun=true;
    private TextView exampleTxt;
    private SharedPreferences preferences;
    private CountryCodePicker codePicker;
    private String number;
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_otp);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        // getting first run boolean value(false) after 1st run
        SharedPreferences getpref = getSharedPreferences("FIRST_RUN",MODE_PRIVATE);
        isFirstRun = getpref.getBoolean("checkrunstatus",true);

        if(!isFirstRun){  // if its not the 1st run
            Intent intent = new Intent(OTP.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        parentlayout = findViewById(android.R.id.content);
        exampleTxt = findViewById(R.id.exampletxt);
        e1 =  findViewById(R.id.Phonenoedittext);
        b1 = findViewById(R.id.PhoneVerify);
        t1 = findViewById(R.id.textView2Phone);
        i2 = findViewById(R.id.logo);
        codePicker = findViewById(R.id.ccp);
        view = findViewById(R.id.mView);
        codePicker.registerCarrierNumberEditText(e1);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this ,new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_SMS},10);

        }
        bounceanime = AnimationUtils.loadAnimation(this,R.anim.bounce);
        moveanime = AnimationUtils.loadAnimation(this, R.anim.down);
        BounceInterpolar interpolator = new BounceInterpolar(0.2, 20);
        bounceanime.setInterpolator(interpolator);
        i2.startAnimation(bounceanime);
        i2.setOnClickListener(v -> i2.startAnimation(bounceanime));

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                mVerificationInProgress = false;
                Toast.makeText(OTP.this,"Verification Complete",Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(credential);
                progress.hide();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(OTP.this,"Verification Failed! Try again..",Toast.LENGTH_SHORT).show();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(OTP.this,"InValid Phone Number",Toast.LENGTH_SHORT).show();
                    progress.hide();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(OTP.this, "Something went wrong...please try again", Toast.LENGTH_SHORT).show();
                    progress.hide();
                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Toast.makeText(OTP.this,"Verification code has been send on your number",Toast.LENGTH_SHORT).show();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

            }
        };

        b1.setOnClickListener(v -> {

            progress = ProgressDialog.show(OTP.this, "Please Wait..",
                    "Authenticating...", true);
            number = codePicker.getFullNumberWithPlus();
            if(e1.getText().toString().isEmpty()){
                Toast.makeText(OTP.this, "Enter phone number!", Toast.LENGTH_SHORT).show();
            }

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    number,
                    60,
                    java.util.concurrent.TimeUnit.SECONDS,
                    OTP.this,
                    mCallbacks);   // this wil verify phone number
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        SharedPreferences preferences = getSharedPreferences("PHONE_NO",MODE_PRIVATE);
                        SharedPreferences.Editor editorPhone = preferences.edit();
                        editorPhone.putString("p_no",e1.getText().toString());
                        editorPhone.apply();
                        //mAuth.getInstance().signOut();
                        startActivity(new Intent(OTP.this,LoginActivity.class));

                        progress.hide();
                        // saving status of first run as false if the task is successful.
                        preferences = getSharedPreferences("FIRST_RUN",MODE_PRIVATE);
                        isFirstRun = false;
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("checkrunstatus",isFirstRun);
                        editor.apply();

                        finish();
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            progress.hide();
                            Toast.makeText(OTP.this,"Invalid Verification",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}
