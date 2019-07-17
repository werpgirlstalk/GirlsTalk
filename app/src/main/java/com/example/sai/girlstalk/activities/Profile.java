package com.example.sai.girlstalk.activities;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sai.GirlsTalk.R;
import com.example.sai.girlstalk.utils.NetworkHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class Profile extends AppCompatActivity {

    String savedSkills;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef = db.document("Users/u39FwJiCjuuPJZBWSIGt");
    private TextView name, state;
    private ListView listitems;
    private String[] title = {"email", "phone number", "My skills"};
    private ArrayList<String> info;
    private RatingBar rating;
    private TextView rate;
    private boolean isFirstRun = true;
    private CoordinatorLayout coordinatorLayout;
    private int[] icons = {R.drawable.gmail, R.drawable.phone, R.drawable.skills};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.uname);
        state = findViewById(R.id.ustate);
        listitems = findViewById(R.id.list);
        rating = findViewById(R.id.rating);
        rate = findViewById(R.id.rtext);

        coordinatorLayout = findViewById(R.id.coor);


        NetworkHelper helper = new NetworkHelper();
        helper.checkNet(coordinatorLayout, this);

        rating.setNumStars(5);
        rating.setFocusable(false);
        rating.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.golden)));

        info = new ArrayList<>();
        info.add(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
        SharedPreferences getPhoneNo = getSharedPreferences("PHONE_NO", MODE_PRIVATE);
        info.add(getPhoneNo.getString("p_no", null));
        info.add("View my skills");
        loadInfo();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        CustomAdapter customAdapter = new CustomAdapter();
        listitems.setAdapter(customAdapter);

    }

    private void loadInfo() {
        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String uName = documentSnapshot.getString("username");
                        long ratings = documentSnapshot.getLong("ratings");

                        name.setText(uName);
                        rating.setRating(ratings);
                        state.setText("Maharashtra");
                    } else {
                        Toast.makeText(Profile.this, "Info loading error!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> Toast.makeText(Profile.this, "Something went wrong!", Toast.LENGTH_SHORT).show());
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return title.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.custom_layout_list, null);
            TextView text = convertView.findViewById(R.id.txt);
            TextView detailData = convertView.findViewById(R.id.contenttxt);
            ImageView micon = convertView.findViewById(R.id.img);
            micon.setImageResource(icons[position]);
            text.setText(title[position]);
            detailData.setText(info.get(position));
            return convertView;
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            startActivity(new Intent(Profile.this,MainActivity.class));
//            finish();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
