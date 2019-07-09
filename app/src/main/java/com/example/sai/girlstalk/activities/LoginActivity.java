package com.example.sai.girlstalk.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sai.GirlsTalk.R;
import com.example.sai.girlstalk.dialogs.ForgotPasswordDialog;
import com.example.sai.girlstalk.viewModels.UserViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressBar loginProgress;
    private EditText loginEmail, loginPassword;

    private UserViewModel userViewModel;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginProgress = findViewById(R.id.loginProgress);

        findViewById(R.id.newAccountBtn).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Register1.class)));
        findViewById(R.id.loginBtn).setOnClickListener(v -> userLogin());

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.googleLoginBtn).setOnClickListener(v -> startActivityForResult(googleSignInClient.getSignInIntent(), 101));
        findViewById(R.id.forgotPasswordText).setOnClickListener(v -> {
            new ForgotPasswordDialog().show(getSupportFragmentManager(), "Forgot Password");
        });
    }

    private void userLogin() {
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        if (email.isEmpty()) {
            loginEmail.setError("Email is required");
            loginEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.setError("Please enter a valid email");
            loginEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            loginPassword.setError("Password is required");
            loginPassword.requestFocus();
            return;
        }

        loginProgress.setVisibility(View.VISIBLE);
        userViewModel.logIn(email, password).observe(this, isSuccessful ->
        {
            loginProgress.setVisibility(View.GONE);
            if (isSuccessful != null) if (isSuccessful) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                userViewModel.googleLogin(account).observe(this, isSuccessful ->
                {
                    if (isSuccessful != null) if (isSuccessful) {
                        Toast.makeText(this, "Sign in succesful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else Toast.makeText(this, "Please try again!", Toast.LENGTH_SHORT).show();
                });
            } catch (ApiException e) {
                Toast.makeText(this, "Sign in failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
