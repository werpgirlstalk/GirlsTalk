package com.example.sai.girlstalk.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.example.sai.GirlsTalk.R;

public class NewsDetails extends AppCompatActivity {
    private WebView mwebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        mwebview = findViewById(R.id.rel);
        Bundle bundle = getIntent().getExtras();
        mwebview.loadUrl(bundle.getString("link"));
    }
}
