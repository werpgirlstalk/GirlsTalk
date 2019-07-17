package com.example.sai.girlstalk.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sai.GirlsTalk.R;


public class Register1 extends AppCompatActivity {

    EditText inputName;
    FloatingActionButton lgprogress1;
    Animation slideleft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.register1);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        inputName = findViewById(R.id.inputName);
        lgprogress1 = findViewById(R.id.lgprogress1);

        slideleft = AnimationUtils.loadAnimation(this, R.anim.slideup);

        inputName.setAnimation(slideleft);


        lgprogress1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputName.getText().toString();
                if (TextUtils.isEmpty(name))
                    Toast.makeText(Register1.this, "Please Fill Up Your Name", Toast.LENGTH_LONG).show();
                else {
                    Intent i = new Intent(Register1.this, Register2.class);
//                    i.putExtra("name",name);
                    startActivity(i);

                    SharedPreferences preferences = getSharedPreferences("NAME", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("uName", name);
                    editor.apply();
                    finish();
                }
            }
        });

    }


}


