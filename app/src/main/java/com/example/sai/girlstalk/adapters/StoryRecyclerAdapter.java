package com.example.sai.girlstalk.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.sai.GirlsTalk.R;
import com.example.sai.girlstalk.models.StoryModel;

import java.util.ArrayList;

public class StoryRecyclerAdapter extends RecyclerView.Adapter<StoryRecyclerAdapter.ViewHolder>
{

    private ArrayList<StoryModel> list;
    private Context mContext;

    public StoryRecyclerAdapter(ArrayList<StoryModel> list, Context context) {
        this.list = list;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.stories_row,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        StoryModel current = list.get(i);
        Glide.with(mContext).load(current.getThumbnailUrl())
                .centerCrop().into(viewHolder.thumbnail);

        viewHolder.title.setClickable(true);
        viewHolder.title.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='http://www.google.com'> Google </a>";
        viewHolder.title.setText(Html.fromHtml(text));
        viewHolder.title.setText(current.getTitle());

        viewHolder.itemView.setOnClickListener(v -> {
            Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;
        ViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.storyThumbnail);
            title = itemView.findViewById(R.id.storyTitle);
        }

    }
}
