package com.example.sai.girlstalk.activities;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sai.GirlsTalk.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class MyChildViewHolder extends ChildViewHolder {

    public TextView listChildtitle;
    public TextView listChildbody;
    public ImageView listphoneicon;

    public MyChildViewHolder(View itemView) {
        super(itemView);
        listChildtitle = itemView.findViewById(R.id.listChildtitle);
        listChildbody = itemView.findViewById(R.id.listChildbody);
        listphoneicon = itemView.findViewById(R.id.listdial);


    }

    public void onBind(String title, String body) {
        listChildtitle.setText(title);
        listChildbody.setText(body);

    }


}