package com.example.sai.girlstalk.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sai.GirlsTalk.R;
import com.example.sai.girlstalk.models.GroupMessage;
import com.example.sai.girlstalk.utils.FirebaseUtils;

import java.util.List;

public class GroupMessagesAdapter extends RecyclerView.Adapter<GroupMessagesAdapter.GroupMessageViewHolder>
{
    private List<GroupMessage> messages;
    private Context context;
    private int viewWidth;

    public GroupMessagesAdapter(List<GroupMessage> messages, Context context)
    {
        this.messages = messages;
        this.context = context;

        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        viewWidth = (width / 2) - 150;
    }

    @NonNull
    @Override
    public GroupMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
       View finalView = LayoutInflater.from(context).inflate(R.layout.custom_messages_list_layout,parent,false);
        return new GroupMessageViewHolder(finalView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupMessageViewHolder groupMessageViewHolder, int position)
    {
        GroupMessage currentMessage = messages.get(position);
        groupMessageViewHolder.message.setText(currentMessage.getMessage());
        if (FirebaseUtils.getInstance().getAuthInstance().getCurrentUser().getEmail().equals(currentMessage.getSender().getEmail()))
            groupMessageViewHolder.messageParent.setGravity(Gravity.END);
        else groupMessageViewHolder.messageParent.setGravity(Gravity.START);
    }
    public List<GroupMessage> getData (){return messages;}

    public void addMessage (GroupMessage newMessage)
    {
        messages.add(newMessage);
        notifyDataSetChanged();
    }

    public void addMessages(List<GroupMessage> messageList)
    {
        messages.addAll(messageList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { return messages.size(); }

    public class GroupMessageViewHolder extends RecyclerView.ViewHolder
    {
        public TextView message;
        public LinearLayout messageParent;

        public GroupMessageViewHolder(View view)
        {
            super(view);
            message = view.findViewById(R.id.message);
            messageParent = view.findViewById(R.id.messageLayoutParent);
        }
    }
}
