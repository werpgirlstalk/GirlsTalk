package com.example.sai.girlstalk.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sai.GirlsTalk.R;
import com.example.sai.girlstalk.activities.StateNames;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class FireAdapter extends FirestoreRecyclerAdapter<StateNames,FireAdapter.ViewHolder> {

    public FireAdapter(@NonNull FirestoreRecyclerOptions<StateNames> options) {
        super(options);
    }



    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull StateNames model) {
        holder.stateNames.setText(String.valueOf(model.getStatenames()));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_item,viewGroup,false);
        return new ViewHolder(v);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView stateNames;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            stateNames = itemView.findViewById(R.id.state);
        }
    }
}
