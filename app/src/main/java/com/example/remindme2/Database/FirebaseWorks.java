package com.example.remindme2.Database;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.remindme2.AlarmSrevices;
import com.example.remindme2.Main2Activity;
import com.example.remindme2.POJOS.Note_Data;
import com.example.remindme2.POJOS.TripJC;
import com.example.remindme2.weGetData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class FirebaseWorks {

    Context context;
    DatabaseReference databaseTripReference;
    NoteDataBaseSQL noteDataBaseSQL;
    DatabaseAdapter databaseAdapter;
    int passedTripsCounter=0;
    ArrayList<TripJC> tripsFromSqlite;
    ArrayList<Note_Data> notesFromSqlite;

    public FirebaseWorks(Context context)
    {
        this.context=context;

        databaseAdapter=new DatabaseAdapter(context);
        noteDataBaseSQL=new NoteDataBaseSQL(context);

        databaseTripReference= FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid()).child("Trips");
       tripsFromSqlite= new ArrayList<>();
       notesFromSqlite=new ArrayList<>();

    }


    public void saveTripsToFireBase(){


        //tripsFromSqlite.clear();
        tripsFromSqlite=databaseAdapter.getTripsUpOnStatus("",FirebaseAuth.getInstance().getUid());
        //Toast.makeText(context,"Added"+tripsFromSqlite.size()+"",Toast.LENGTH_LONG).show();


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


   public void GetDataFromFireBase(){
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
                     //   Toast.makeText(context,"Edit"+currentOne.getName(),Toast.LENGTH_LONG).show();
                        databaseAdapter.editTrip(currentOne);

                    }

                    else {
                        //databaseAdapter.insertTrip(currentOne);
                      //  Toast.makeText(context,"Add"+currentOne.getName(),Toast.LENGTH_LONG).show();
                        checkAndiInsert(currentOne);
                    }

//notes
                    if(currentOne.getNoteList().size()>0){
                        for(int j=0;j<currentOne.getNoteList().size();j++){

                            noteDataBaseSQL.addNote(currentOne.getNoteList().get(j));

                        }}


                }
                if(passedTripsCounter>0){
                   // Toast.makeText(context,"passedTripsCounter",Toast.LENGTH_LONG).show();
                    //builder.setMessage("You have "+ passedTripsCounter+ "passed Trips we marked them as Ended ")

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(context,"Something went wrong Please Check Your Connection !",Toast.LENGTH_LONG).show();

            }
        });

  Intent i =new Intent(context, weGetData.class);
  i.putExtra("FROMGETDATA",1);
  context.startActivity(i);

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
            //Toast.makeText(context,"Befor"+passedTripsCounter,Toast.LENGTH_LONG).show();

            passedTrip.setStatus("Ended");

            databaseAdapter.insertTrip(passedTrip);

        }else {

            //Toast.makeText(context,"After",Toast.LENGTH_LONG).show();
            int insertedTripId = (int)  databaseAdapter.insertTrip(passedTrip);
            startAlarm(cldr,insertedTripId);

        }

    }

    private void startAlarm(Calendar c, int tripId) {

        Intent serviceIntent = new Intent(context, AlarmSrevices.class);
        serviceIntent.putExtra("Cal",c);
        serviceIntent.putExtra("tripId",tripId);
        context.startService(serviceIntent);

    }

}
