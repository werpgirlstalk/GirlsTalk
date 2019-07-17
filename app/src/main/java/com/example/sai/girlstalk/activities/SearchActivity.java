package com.example.sai.girlstalk.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.widget.Toast;

import com.example.sai.GirlsTalk.R;
import com.example.sai.girlstalk.adapters.FriendsRecyclerAdapter;
import com.example.sai.girlstalk.models.User;
import com.example.sai.girlstalk.utils.FirebaseUtils;
import com.example.sai.girlstalk.viewModels.UserViewModel;

public class SearchActivity extends AppCompatActivity implements FriendClickListener {

    private RecyclerView searchResultList;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchResultList = findViewById(R.id.friendsResultList);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

    }

    @Override
    public void onClick(User user) {
        AlertDialog.Builder requestDialog = new AlertDialog.Builder(SearchActivity.this);
        requestDialog.setMessage("Send " + user.getUsername() + " A Friend Request");
        requestDialog.setPositiveButton("Yes", (dialog, which) ->
        {
            userViewModel.getUser(FirebaseUtils.getInstance().getAuthInstance().getCurrentUser().getEmail()).observe(this, currentUser ->
            {
                if (currentUser != null)
                    userViewModel.sendFriendRequest(user.getProfile().getEmail(), currentUser.getProfile())
                            .observe(SearchActivity.this, result ->
                            {
                                if (result != null) if (result)
                                    Toast.makeText(SearchActivity.this, "Request Sent", Toast.LENGTH_SHORT).show();
                            });

            });
        });
        requestDialog.setNegativeButton("No", (dialog, which) -> {
        });
        requestDialog.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchBtn).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!s.trim().isEmpty())
                    userViewModel.getUsers(s).observe(SearchActivity.this, friends ->
                    {
                        if (friends != null) {
                            RecyclerView.Adapter adapter = new FriendsRecyclerAdapter(friends, getApplicationContext(), SearchActivity.this);
                            searchResultList.setHasFixedSize(true);
                            searchResultList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            searchResultList.setAdapter(adapter);
                        }
                    });

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
