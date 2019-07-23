package com.example.sai.girlstalk.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sai.GirlsTalk.R;
import com.example.sai.girlstalk.activities.GroupChatActivity;
import com.example.sai.girlstalk.models.Group;

import java.util.ArrayList;

public class GroupRecyclerAdapter extends RecyclerView.Adapter<GroupRecyclerAdapter.ViewHolder> {

    private ArrayList<Group> list;
    private Context mContext;

    public GroupRecyclerAdapter(ArrayList<Group> list, Context context) {
        this.list = list;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.group_rows, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Group current = list.get(i);
        viewHolder.groupTitle.setText(current.getTitle());

        viewHolder.itemView.setOnClickListener(v ->
        {
            Intent intent = new Intent(mContext, GroupChatActivity.class);
            intent.putExtra("GROUP TITLE", current.getTitle());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView groupTitle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            groupTitle = itemView.findViewById(R.id.groupTitle);

        }
    }
}
