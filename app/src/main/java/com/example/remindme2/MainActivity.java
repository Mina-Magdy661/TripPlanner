package com.example.remindme2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.example.CheckInternetConnection;
import com.example.remindme2.Database.DatabaseAdapter;
import com.example.remindme2.Database.FirebaseWorks;
import com.example.remindme2.Database.NoteDataBaseSQL;
import com.example.remindme2.History.HistoryFragment;
import com.example.remindme2.POJOS.Note_Data;
import com.example.remindme2.POJOS.TripJC;
import com.example.remindme2.Setting.SettingFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    DatabaseReference databaseTripReference;
    NoteDataBaseSQL noteDataBaseSQL;
    DatabaseAdapter databaseAdapter;
    FirebaseWorks firebaseWorks;
   // int REQUEST_OVERLAY_PERMISSION=111;


   // @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("UpComing Trips");


        /////
        databaseAdapter=new DatabaseAdapter(this);
        noteDataBaseSQL=new NoteDataBaseSQL(this);

        databaseTripReference= FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid()).child("Trips");

//Floating
//        if(!Settings.canDrawOverlays(this)){
//            // ask for setting
//            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                    Uri.parse("package:" + getPackageName()));
//            startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
//        }





        ///


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent toAddNewTrip =new Intent(getApplicationContext(),NewTripActivity.class);
                startActivity(toAddNewTrip);

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
         drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.Drawer_open,R.string.Drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


      NavigationView navigationView = findViewById(R.id.nav_view);




        View headerView = navigationView.getHeaderView(0);
        TextView navEmail = (TextView) headerView.findViewById(R.id.navEmail);
        navEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());



        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment ,new HomeFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_home);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if( menuItem.getItemId() == R.id.setting){


                    firebaseWorks=new FirebaseWorks(getApplicationContext());


                      firebaseWorks.saveTripsToFireBase();





                  //  getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment ,new SettingFragment()).commit();


                }

                if( menuItem.getItemId() == R.id.history){


                    getSupportActionBar().setTitle("History");


                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment ,new HistoryFragment()).commit();

                }

                if( menuItem.getItemId() == R.id.nav_home){
                    getSupportActionBar().setTitle("Upcoming Trips");

                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment ,new HomeFragment()).commit();

                }


                if( menuItem.getItemId() == R.id.logout){

                    FirebaseAuth.getInstance().signOut();
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.remove("User").commit();
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    finish();
                    Toast.makeText(MainActivity.this,"YOU ARE LOGOUT" ,Toast.LENGTH_LONG).show();
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


        //Toast.makeText(getApplicationContext(),FirebaseAuth.getInstance().getUid(),Toast.LENGTH_LONG).show();






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

  if(item.getItemId()==R.id.action_settings){

Intent toHistoryMap=new Intent(getApplicationContext(),HistoryMap.class);
startActivity(toHistoryMap);
  }


        return super.onOptionsItemSelected(item);
    }

    //    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
            moveTaskToBack(true);
            finish();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (Settings.canDrawOverlays(this)) {
//
//
//
//                    Toast.makeText(getApplicationContext(),"We can now show alarms for you",Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getApplicationContext(),"We cannot  show alarms for you",Toast.LENGTH_LONG).show();
//                }
//            }
//        }
    }






}
