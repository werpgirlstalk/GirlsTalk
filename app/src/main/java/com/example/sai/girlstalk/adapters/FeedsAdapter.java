package com.example.sai.girlstalk.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.sai.GirlsTalk.R;
import com.example.sai.girlstalk.activities.FeedItem;
import com.example.sai.girlstalk.activities.NewsDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.MyViewHolder> {
    ArrayList<FeedItem>feedItems;
    Context context;
    public FeedsAdapter(Context context, ArrayList<FeedItem>feedItems){
        this.feedItems=feedItems;
        this.context=context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.custum_row_news_item,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        YoYo.with(Techniques.FadeIn).playOn(holder.cardView);
        final FeedItem current=feedItems.get(position);
        holder.Title.setText(current.getTitle());
        holder.Description.setText(current.getDescription());
        holder.Date.setText(current.getPubDate());
        Picasso.get().load(current.getThumbnailUrl()).into(holder.Thumbnail);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsDetails.class);
                intent.putExtra("link",current.getLink());
                context.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
//        if (feedItems.size()!=0){
//            return feedItems.size();
//        }else{
//            Toast.makeText(context, "Error loading..", Toast.LENGTH_SHORT).show();
//            return 0;
//        }
return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Title,Description,Date;
        ImageView Thumbnail;
        CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            Title= (TextView) itemView.findViewById(R.id.title_text);
            Description= (TextView) itemView.findViewById(R.id.description_text);
            Date= (TextView) itemView.findViewById(R.id.date_text);
            Thumbnail= (ImageView) itemView.findViewById(R.id.thumb_img);
            cardView= (CardView) itemView.findViewById(R.id.cardview);
        }
    }
}