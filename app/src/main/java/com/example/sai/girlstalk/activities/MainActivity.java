package com.example.sai.girlstalk.activities;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import im.delight.android.location.SimpleLocation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sai.GirlsTalk.R;
import com.example.sai.girlstalk.adapters.ExpertRecyclerAdapter;
import com.example.sai.girlstalk.chatbot.Chatbot;
import com.example.sai.girlstalk.models.StoryModel;
import com.example.sai.girlstalk.utils.FirebaseUtils;
import com.example.sai.girlstalk.utils.NetworkHelper;
import com.example.sai.girlstalk.viewModels.GroupViewModel;
import com.example.sai.girlstalk.viewModels.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private CoordinatorLayout coordinatorLayout;
    private SimpleLocation location;
    private TextView uname;
    private FirebaseAuth auth;
    private Boolean exit = false;
    private String nametxt;
    private SwipeRefreshLayout refresh;
    private LinearLayout viewblogsbtn;
    private FirebaseUser user;
    private TextView viewAllPosts;
    private RecyclerView recyclerView;
    private DrawerLayout drawer;
    private TextView groupchattxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refresh = findViewById(R.id.refreshcontainer);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startActivity(new Intent(MainActivity.this,MainActivity.class));
                refresh.setRefreshing(false);
            }
        });
        Toolbar toolbar =  findViewById(R.id.toolbar);




        recyclerView = (RecyclerView) findViewById(R.id.recyclerview1);
      //  RecyclerView storyRecycler = findViewById(R.id.storyRecycler);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        RecyclerView groupRecycler =findViewById(R.id.groupsRecycler);
        RecyclerView expertsRecycler = findViewById(R.id.expertsRecycler);
        viewAllPosts = findViewById(R.id.viewBlogs);
        viewblogsbtn = findViewById(R.id.linearblogs);
        groupchattxt = findViewById(R.id.groupchat);
        auth = FirebaseAuth.getInstance();
         user= auth.getCurrentUser();

        NetworkHelper helper = new NetworkHelper();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        View headerview = navigationView.getHeaderView(0);
        uname = headerview.findViewById(R.id.username);
        SharedPreferences prf = getSharedPreferences("NAME",MODE_PRIVATE);
        nametxt= prf.getString("uName",null);


        if (user != null && user.getDisplayName() != null){
            uname.setText(user.getDisplayName());
        }else if(user != null) {
            uname.setText(nametxt);
        }
//        else{
//            startActivity(new Intent(MainActivity.this,LoginActivity.class));
//        }

        helper.checkNet(coordinatorLayout,this);
        ArrayList<StoryModel> expertList = new ArrayList<>();
        setSupportActionBar(toolbar);
        toolbar.getOverflowIcon().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->
               startActivity(new Intent(MainActivity.this, Chatbot.class))
        );

        drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        TextView profile = (TextView) headerview.findViewById(R.id.viewprofile);


        profile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,Profile.class);
            startActivity(intent);
        });

        viewblogsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ExecuteRSS.class));
            }
        });

        groupchattxt.setOnClickListener(
                v -> startActivity(new Intent(MainActivity.this,MainGroupChat.class))
        );
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        ReadRSS readRss = new ReadRSS(this, recyclerView);
        readRss.execute();

        expertList.add(new StoryModel("https://static.boredpanda.com/blog/wp-content/uploads/2017/08/Epic-Portraits-Shot-with-an-iPhone-and-a-Big-Mac-59a54f2641195__880.jpg","ABC Kumar"));
        expertList.add(new StoryModel("http://www.keatleyphoto.com/wp-content/uploads/2016/03/John_Keatley_iPhone_portrait_Andrew_5055.jpg","XYZ Kumar"));
        expertList.add(new StoryModel("http://farm3.static.flickr.com/2792/4285995840_72a6e4ff43.jpg","Taru Sart"));

        expertsRecycler.setAdapter(new ExpertRecyclerAdapter(expertList,this));
        expertsRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        GroupViewModel groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);


//        userViewModel.getUser(FirebaseUtils.getInstance().getAuthInstance().getCurrentUser().getEmail()).observe(this,currentUser ->
//        {
//            if (currentUser != null)
//            {
//                groupViewModel.getUserGroups(currentUser.getProfile()).observe(this,allGroups ->
//                {
//                    groupViewModel.addMember(currentUser.getProfile(),"GRoup Title").observe(this,s ->
//                    {
//                        if (s != null) Toast.makeText(this, "IIIII", Toast.LENGTH_SHORT).show();
//                        else Toast.makeText(this, "DDDDDDd", Toast.LENGTH_SHORT).show();
//                    });
//                    /*if (allGroups != null)
//                    {
//                        groupRecycler.setHasFixedSize(true);
//                        groupRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                        groupRecycler.setAdapter(new GroupRecyclerAdapter((ArrayList<Group>) allGroups,getApplicationContext()));
//                    }*/
//                });
//            }
//        });

        //permission for accessing //

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_PHONE_STATE,Manifest.permission.SEND_SMS},10);

        }
    }


    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (exit) {
            finish(); // finish activity
        } else if (!drawer.isDrawerOpen(GravityCompat.START)){ // if drawer isn't opened
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 2 * 1000);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_invites) {
            sendinvite();
            return true;
        }else if(id == R.id.sendloc){
            AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
            builder2.setTitle("Alert!");
            builder2.setMessage("Do you want to send your location to your selected contacts?");
            builder2.setIcon(R.drawable.alert);
            builder2.setPositiveButton("Yes!", (dialog, which) -> sendLocation()).setNegativeButton("No", (dialog, which) -> dialog.cancel());
            AlertDialog dialog2 = builder2.create();
            dialog2.show();

        }else if (id == R.id.action_feedback){
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","werpgirlstalk@gmail.com", null));

            if (user != null && !user.getDisplayName().isEmpty()){
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from "+user.getDisplayName());
            }else if(user != null){
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from "+nametxt);
            }
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {

        } else if (id == R.id.nav_friends) {
            startActivity(new Intent(getApplicationContext(),SearchActivity.class));
        } else if (id == R.id.nav_create_group) {
        } else if (id == R.id.nav_vacancy) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_tollfree) {
            startActivity(new Intent(MainActivity.this,TollFree.class));
        }else if (id == R.id.nav_logout) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Alert!")
                    .setMessage("Are you sure you want to sign out?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "You are signed out!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(R.drawable.alert)
                    .show();

        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    // invites
    public void sendinvite(){

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareLink ="https://play.google.com/store?hl=en";
        String shareBody ="COME ,JOIN WERP AND MAKE A DIFFERENCE IN SOCIETY";
        intent.putExtra(Intent.EXTRA_SUBJECT,shareBody);
        intent.putExtra(Intent.EXTRA_TITLE,"Download GirlsTalk App");
        intent.putExtra(Intent.EXTRA_TEXT,"Join by installing our app GirlsTalk from here -> \n"+shareLink);
        startActivity(intent.createChooser(intent,"Share using"));
    }
    private void sendLocation(){
        location = new SimpleLocation(this);
        // if we can't access the location yet
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }
        String uri = "http://maps.google.com/maps?saddr=" + location.getLatitude()+","+location.getLongitude();
        String helpMsg ="I am in trouble! Please help me! Here is my location --> \n";
        SmsManager smsManager = SmsManager.getDefault();
        StringBuffer smsBody = new StringBuffer();
        smsBody.append(Uri.parse(uri));
        smsManager.sendTextMessage("+917720979245", null,helpMsg+ smsBody.toString(), null, null);
        Toast.makeText(this, "Location sent!", Toast.LENGTH_SHORT).show();
    }
}
