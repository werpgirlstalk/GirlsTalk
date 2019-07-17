package com.example.sai.girlstalk.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sai.GirlsTalk.R;
import com.example.sai.girlstalk.activities.FriendClickListener;
import com.example.sai.girlstalk.models.User;

import java.util.ArrayList;
import java.util.List;

public class FriendsRecyclerAdapter extends RecyclerView.Adapter<FriendsRecyclerAdapter.ViewHolder> {

    private ArrayList<User> friends;
    private Context mContext;
    private FriendClickListener clickListener;

    public FriendsRecyclerAdapter(List<User> friends, Context context, FriendClickListener clickListener) {
        this.friends = (ArrayList<User>) friends;
        mContext = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.friend_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        User currentUser = friends.get(i);

        viewHolder.title.setText(currentUser.getUsername());
        viewHolder.itemView.setOnClickListener(v -> clickListener.onClick(currentUser));
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.friendName);
        }

    }
}
