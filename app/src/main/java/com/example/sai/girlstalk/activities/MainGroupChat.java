package com.example.sai.girlstalk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sai.GirlsTalk.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.github.library.bubbleview.BubbleTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class MainGroupChat extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessege> adapter;
    RelativeLayout activity_main;
    EmojiconEditText emojiconEditText;
    ImageView memojibtn, submitbtn;
    EmojIconActions emojIconActions;
    FloatingActionButton fab;
    ListView listview;

    DatabaseReference mGroupRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_group);

        activity_main = findViewById(R.id.rel);
        fab = findViewById(R.id.fab);
        listview = findViewById(R.id.list_of_msgs);
        memojibtn = findViewById(R.id.emojibtn);
        emojiconEditText = findViewById(R.id.inputmsg);

        emojIconActions = new EmojIconActions(this, activity_main, memojibtn, emojiconEditText);
        emojIconActions.ShowEmojicon();
        mGroupRef = FirebaseDatabase.getInstance().getReference().child("main_group");

        fab.setOnClickListener(v -> {
            //   EditText input = findViewById(R.id.input);

            mGroupRef.push().setValue(new ChatMessege(emojiconEditText.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail()));
            emojiconEditText.setText("");
            emojiconEditText.requestFocus();
        });

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_REQUEST_CODE);

        } else {
            displayChatMsg();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (requestCode == RESULT_OK) {
                Snackbar.make(activity_main, "Successfully signed in! Welcome!", Snackbar.LENGTH_SHORT).show();
                displayChatMsg();
            } else {
                Snackbar.make(activity_main, "Failed to sign in!", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void displayChatMsg() {

        adapter = new FirebaseListAdapter<ChatMessege>(this, ChatMessege.class, R.layout.list_item, mGroupRef) {
            @Override
            protected void populateView(View v, ChatMessege model, int position) {
                TextView msg_txt, msg_user, msg_time;
                msg_txt = (BubbleTextView) v.findViewById(R.id.msg_text);
                msg_user = v.findViewById(R.id.msg_user);
                msg_time = v.findViewById(R.id.msg_time);

                msg_txt.setText(model.getMessgeText());
                msg_user.setText(model.getMessegeUser());
                msg_time.setText(String.valueOf(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessegeTime())));
            }
        };
        listview.setAdapter(adapter);
    }


}
