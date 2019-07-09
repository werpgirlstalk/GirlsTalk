package com.example.sai.girlstalk.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sai.GirlsTalk.R;
import com.example.sai.girlstalk.utils.NetworkHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TollFree extends AppCompatActivity {

    private RecyclerView recycler_view;
    private CoordinatorLayout coordinatorLayout;
    private String TAG = "AppointmentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toll_free);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        NetworkHelper helper = new NetworkHelper();
        helper.checkNet(coordinatorLayout,this);
        //Define recycleview
        recycler_view = (RecyclerView) findViewById(R.id.recycler_Expand);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));

        //Initialize your Firebase app
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to your entire Firebase database
        //DatabaseReference parentReference = database.getReference();

        //reading data from firebase

        db.collection("Toll Free Numbers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    final List<ParentList> Parent = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        final String ParentKey = document.getId();

                        Log.d(TAG, document.getId() + " => " + document.getData());


                        final DocumentReference docRef = db.collection("Toll Free Numbers").document(ParentKey);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                final List<ChildList> Child = new ArrayList<>();

                                if (task.isSuccessful()) {


                                    Map<String, Object> map = task.getResult().getData();

                                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                                        Child.add(new ChildList(entry.getKey(), task.getResult().getString(entry.getKey())));
                                        Log.d("TAG", entry.getKey());
                                    }


                                    Parent.add(new ParentList(ParentKey, Child));
                                    DocExpandableRecyclerAdapter adapter = new DocExpandableRecyclerAdapter(Parent);

                                    recycler_view.setAdapter(adapter);

                                }
                            }
                        });

                    }
                } else {
                    Snackbar.make(coordinatorLayout,"Error loading data",Snackbar.LENGTH_SHORT).show();
                }
            }
        });




    }


    public class DocExpandableRecyclerAdapter extends ExpandableRecyclerViewAdapter<MyParentViewHolder, MyChildViewHolder> {


        public DocExpandableRecyclerAdapter(List<ParentList> groups) {
            super(groups);
        }

        @Override
        public MyParentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parent, parent, false);
            return new MyParentViewHolder(view);
        }

        @Override
        public MyChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child, parent, false);
            return new MyChildViewHolder(view);
        }

        @Override
        public void onBindChildViewHolder(MyChildViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {

            final ChildList childItem = ((ParentList) group).getItems().get(childIndex);
            holder.onBind(childItem.getTitle(), childItem.getBody());
            holder.listphoneicon.setOnClickListener(view -> call(childItem));
            holder.listChildbody.setOnClickListener(v -> call(childItem));

        }

        @Override
        public void onBindGroupViewHolder(MyParentViewHolder holder, int flatPosition, final ExpandableGroup group) {
            holder.setParentTitle(group);

            if (group.getItems() == null) {
                holder.listGroup.setOnClickListener(view -> {
                    Toast toast = Toast.makeText(getApplicationContext(), group.toString(), Toast.LENGTH_SHORT);
                    toast.show();
                });

            }
        }


    }
    public void call(ChildList childItem){
        String number = childItem.getBody();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

}