package com.example.sai.girlstalk.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.example.sai.GirlsTalk.R;
import com.example.sai.girlstalk.adapters.GroupMessagesAdapter;
import com.example.sai.girlstalk.models.GroupMessage;
import com.example.sai.girlstalk.utils.FirebaseUtils;
import com.example.sai.girlstalk.viewModels.GroupViewModel;
import com.example.sai.girlstalk.viewModels.UserViewModel;
import com.google.firebase.Timestamp;

public class GroupChatActivity extends AppCompatActivity {
    private EditText message;
    private RecyclerView groupMessagesList;
    private FloatingActionButton sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        message = findViewById(R.id.message);
        groupMessagesList = findViewById(R.id.groupMessages);
        sendBtn = findViewById(R.id.sendMessageBtn);

        String groupTitle = getIntent().getStringExtra("GROUP TITLE");
        GroupViewModel groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
        groupViewModel.getAllMessages("Group Title").observe(this, groupMessages ->
        {
            if (groupMessages != null) {
                groupMessagesList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                groupMessagesList.setHasFixedSize(true);
                groupMessagesList.setAdapter(new GroupMessagesAdapter(groupMessages, getApplicationContext()));
            }
        });

        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        sendBtn.setOnClickListener(v ->
                userViewModel.getUser(FirebaseUtils.getInstance().getAuthInstance().getCurrentUser().getEmail()).observe(this, currentUser ->
                {
                    GroupMessage newMessage = new GroupMessage(message.getText().toString(), Timestamp.now().toString(), currentUser.getProfile());
                    if (currentUser != null) groupViewModel.addMessage(groupTitle, newMessage)
                            .observe(this, isSuccessful ->
                            {
                                if (isSuccessful != null) if (isSuccessful) {
                                    GroupMessagesAdapter adapter = (GroupMessagesAdapter) groupMessagesList.getAdapter();
                                    adapter.addMessage(newMessage);
                                }
                            });

                }));

    }
}
