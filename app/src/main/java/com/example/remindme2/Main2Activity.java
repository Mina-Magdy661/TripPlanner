package com.example.remindme2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.CheckInternetConnection;
import com.example.remindme2.Database.DatabaseAdapter;
import com.example.remindme2.Database.NoteDataBaseSQL;
import com.example.remindme2.POJOS.Note_Data;
import com.example.remindme2.POJOS.TripJC;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class Main2Activity extends AppCompatActivity{
    int AUTOCOMPLETE_REQUEST_CODE = 1;

    DatabaseReference databaseTripReference;
    NoteDataBaseSQL noteDataBaseSQL;
    DatabaseAdapter databaseAdapter;
    int passedTripsCounter=0;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button addTrips=findViewById(R.id.addTrips);
        Button grips=findViewById(R.id.getTrips);


CheckInternetConnection cIC=new CheckInternetConnection(getApplicationContext());

Boolean ch=cIC.isNetworkAvailable();

if(ch){

    Toast.makeText(getApplicationContext(),"HEY CON",Toast.LENGTH_LONG).show();
}
else {

    Toast.makeText(getApplicationContext(),"HEY NOT",Toast.LENGTH_LONG).show();

}



        Toast.makeText(getApplicationContext(),FirebaseAuth.getInstance().getUid(),Toast.LENGTH_LONG).show();
        databaseAdapter=new DatabaseAdapter(this);
        noteDataBaseSQL=new NoteDataBaseSQL(this);

        databaseTripReference=FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid()).child("Trips");

//        for(int i=0;i<10;i++){
//
//            TripJC tripJC = new TripJC("TestName"+i, "testStartP", "testEndP", "testDate", "Time", "testtype", 12.666, 13.77, 14.88, 15.99, "Upcoming", FirebaseAuth.getInstance().getUid());
//
//            Toast.makeText(this,"Added",Toast.LENGTH_LONG).show();
//            databaseAdapter.insertTrip(tripJC);
//
//
//        }



 addTrips.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {

         saveTripsToFireBase();

     }
 });
 grips.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
        // GetDataFromFireBase();


         finish();



     }
 });

    }


    public void saveTripsToFireBase(){

        ArrayList<TripJC> tripsFromSqlite=new ArrayList<>();
        ArrayList<Note_Data> notesFromSqlite=new ArrayList<>();
        tripsFromSqlite=databaseAdapter.getTripsUpOnStatus("all",FirebaseAuth.getInstance().getUid());
        //Toast.makeText(getApplicationContext(),"Added"+tripsFromSqlite.size()+"",Toast.LENGTH_LONG).show();


        for(int i =0; i<tripsFromSqlite.size();i++) {
           notesFromSqlite.clear();
            String tripKey =null;

          if(tripsFromSqlite.get(i).getTripFId()==null){
           tripKey = databaseTripReference.push().getKey();
              tripsFromSqlite.get(i).setTripFId(tripKey);
              databaseAdapter.editTrip(tripsFromSqlite.get(i));
          }
          else {

              tripKey=tripsFromSqlite.get(i).getTripFId();
          }
            notesFromSqlite=(ArrayList<Note_Data>) noteDataBaseSQL.getAllNote(tripsFromSqlite.get(i).getId());

           tripsFromSqlite.get(i).setNoteList(notesFromSqlite);
                    databaseTripReference.child(tripKey).setValue(tripsFromSqlite.get(i));

        }


    }


    void GetDataFromFireBase(){
        final ArrayList<TripJC> tripsFromFireBase=new ArrayList<>();

        databaseTripReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot tripsSnapShot:dataSnapshot.getChildren()){

                    TripJC recievedTrip=tripsSnapShot.getValue(TripJC.class);

                    tripsFromFireBase.add(recievedTrip);




                }


                for(int i=0;i<tripsFromFireBase.size();i++){


                    TripJC currentOne=tripsFromFireBase.get(i);

                   if (databaseAdapter.checkExsistance(currentOne.getTripFId())){
                       Toast.makeText(getApplicationContext(),"Edit"+currentOne.getName(),Toast.LENGTH_LONG).show();

                       databaseAdapter.editTrip(currentOne);

                    }

                   else {

                       //databaseAdapter.insertTrip(currentOne);
                       Toast.makeText(getApplicationContext(),"Add"+currentOne.getName(),Toast.LENGTH_LONG).show();

                       checkAndiInsert(currentOne);

                   }






//notes
                   if(currentOne.getNoteList().size()>0){
                    for(int j=0;j<currentOne.getNoteList().size();j++){

                        noteDataBaseSQL.addNote(currentOne.getNoteList().get(j));




                    }}


                }
                if(passedTripsCounter>0){
                    Toast.makeText(getApplicationContext(),"passedTripsCounter",Toast.LENGTH_LONG).show();
                    //builder.setMessage("You have "+ passedTripsCounter+ "passed Trips we marked them as Ended ")




                }







            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {



                Toast.makeText(getApplicationContext(),"Something went wrong Please Check Your Connection !",Toast.LENGTH_LONG).show();

            }
        });






    }


    public void checkAndiInsert(TripJC passedTrip){

        Calendar cldr=Calendar.getInstance();
        String [] split=  passedTrip.getDate().split("/");
        int day=Integer.parseInt(split[0]);
        int month=Integer.parseInt(split[1])-1;
        int yearm=Integer.parseInt(split[2]);
        String [] splitTime=  passedTrip.getTime().split(":");
        int hour=Integer.parseInt(splitTime[0]);
        int  minutes=Integer.parseInt(splitTime[1]);
        cldr.set(Calendar.YEAR,yearm);
        cldr.set(Calendar.DAY_OF_MONTH,day);
        cldr.set(Calendar.MONTH,month);
        cldr.set(Calendar.HOUR_OF_DAY,hour);
        cldr.set(Calendar.MINUTE,minutes);
        cldr.set(Calendar.SECOND,0);

        if (cldr.before(Calendar.getInstance()) && passedTrip.getStatus().equalsIgnoreCase("Upcoming")){

            passedTripsCounter++;
            Toast.makeText(getApplicationContext(),"Befor"+passedTripsCounter,Toast.LENGTH_LONG).show();

              passedTrip.setStatus("Ended");

              databaseAdapter.insertTrip(passedTrip);




        }else {

            Toast.makeText(getApplicationContext(),"After",Toast.LENGTH_LONG).show();

            int insertedTripId = (int)  databaseAdapter.insertTrip(passedTrip);
            startAlarm(cldr,insertedTripId);



        }







    }

    private void startAlarm(Calendar c, int tripId) {

        Intent serviceIntent = new Intent(this, AlarmSrevices.class);
        serviceIntent.putExtra("Cal",c);
        serviceIntent.putExtra("tripId",tripId);
        startService(serviceIntent);


    }






}
