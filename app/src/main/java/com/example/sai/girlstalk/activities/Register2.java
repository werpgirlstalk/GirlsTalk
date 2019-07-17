package com.example.sai.girlstalk.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sai.GirlsTalk.R;
import com.example.sai.girlstalk.models.User;
import com.example.sai.girlstalk.models.UserProfile;
import com.example.sai.girlstalk.viewModels.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Register2 extends AppCompatActivity {

    TextView name;
    LinearLayout input1, input2, input3;
    Animation slideleft, slideright;
    FloatingActionButton mFloatingActionButton;
    private EditText mailid, pass, confirmpass;
    private FirebaseAuth firebaseAuth;
    private String emailString;
    private String nametxt;
    private String usernameText;
    private UserViewModel userViewModel;
    private CoordinatorLayout mcoordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.register2);


        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
//        usernameText = getIntent().getStringExtra("name");
        name = findViewById(R.id.name);
        input1 = findViewById(R.id.input1);
        input2 = findViewById(R.id.input2);
        input3 = findViewById(R.id.input3);
        mailid = findViewById(R.id.mail);
        pass = findViewById(R.id.pass1);
        confirmpass = findViewById(R.id.pass2);
        mcoordinatorLayout = findViewById(R.id.coordinatorLayout);


        firebaseAuth = FirebaseAuth.getInstance();
        mFloatingActionButton = findViewById(R.id.faBtnlg2);

        SharedPreferences preferences = getSharedPreferences("NAME", MODE_PRIVATE);
        nametxt = preferences.getString("uName", null);

        if (nametxt != null && nametxt.length() > 6) {
            nametxt = nametxt.substring(0, 6) + "...";
        }

        slideleft = AnimationUtils.loadAnimation(this, R.anim.slideleft);
        slideright = AnimationUtils.loadAnimation(this, R.anim.slideright);

        input1.setAnimation(slideleft);
        input2.setAnimation(slideleft);
        input3.setAnimation(slideleft);

        name.setText(String.format("Hi, %s !", nametxt));
        name.setAnimation(slideright);

        mFloatingActionButton.setOnClickListener(v -> {
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception ignored) {

            }
            if (!pass.getText().toString().equals(confirmpass.getText().toString())) {
//                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                Snackbar.make(v, "Passwords do not match!", Snackbar.LENGTH_SHORT).show();
            } else if (pass.getText().toString().isEmpty() || confirmpass.getText().toString().isEmpty()) {
//                Toast.makeText(this, "Empty fields!!!", Toast.LENGTH_SHORT).show();
                Snackbar.make(v, "Empty fields!", Snackbar.LENGTH_SHORT).show();
            } else {
                createAccount();
            }

        });
    }

    private void createAccount() {

        emailString = mailid.getText().toString();
        final String passString = pass.getText().toString();
        firebaseAuth.createUserWithEmailAndPassword(emailString, passString)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        userViewModel.signUp(new User(nametxt, emailString, passString, new UserProfile("", "", emailString))).observe(Register2.this, isSuccessful ->
                        {
                            Toast.makeText(Register2.this, "User verification Mail sent!!!" + isSuccessful, Toast.LENGTH_LONG).show();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        });

                    } else {
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (FirebaseAuthWeakPasswordException weakPass) {
                            Snackbar.make(mcoordinatorLayout, "Make a minimum 6 characters password ! May include symbols ,numbers,letters combination.", Snackbar.LENGTH_SHORT).show();
                        } catch (FirebaseAuthUserCollisionException existEmail) {
                            Snackbar.make(mcoordinatorLayout, "This account already exist!", Snackbar.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Snackbar.make(mcoordinatorLayout, "Please try again!", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(Register2.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
