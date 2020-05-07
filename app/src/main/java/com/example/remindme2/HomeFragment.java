package com.example.remindme2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.CheckInternetConnection;
import com.example.remindme2.Database.DatabaseAdapter;
import com.example.remindme2.Database.FirebaseWorks;
import com.example.remindme2.Database.NoteDataBaseSQL;
import com.example.remindme2.POJOS.Note_Data;
import com.example.remindme2.POJOS.TripJC;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

//EDit
public class HomeFragment extends Fragment {
FirebaseAuth auth;
    RecycleAdapter myRecycleAdapter ;
    RecyclerView myRecycle ;
    List<TripJC> list =new ArrayList<>() ;
    List<TripJC> listCanceled ;
    TripJC deleteItem = new TripJC();
    DatabaseAdapter db;
    //

    DatabaseReference databaseTripReference;
    NoteDataBaseSQL noteDataBaseSQL;
    DatabaseAdapter databaseAdapter;
    int passedTripsCounter=0;
    ArrayList<TripJC> tripsFromSqlite;
    ArrayList<Note_Data> notesFromSqlite;




    //




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        db=new DatabaseAdapter(getContext());
        myRecycle = v.findViewById(R.id.Recyle);
        myRecycle.setLayoutManager(new LinearLayoutManager(getContext()));







        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






    }




    @Override
    public void onStart() {
        super.onStart();



    int ii=  getActivity().getIntent().getIntExtra("FROMGETDATA",0);

//    Toast.makeText(getContext(),"FLAF"+ii,Toast.LENGTH_LONG).show();

             if(db.getTripsUpOnStatus("",FirebaseAuth.getInstance().getUid()).size()<=0&&ii!=1){
                 FirebaseWorks works=new FirebaseWorks(getContext());

                 works.GetDataFromFireBase();






             }

  //          Toast.makeText(getContext(), "Start", Toast.LENGTH_LONG).show();

            list = db.getTripsUpOnStatus("Upcoming", FirebaseAuth.getInstance().getUid());
            myRecycleAdapter = new RecycleAdapter(getActivity(), list);
            new ItemTouchHelper(itemtouchholder).attachToRecyclerView(myRecycle);
            myRecycle.setAdapter(myRecycleAdapter);
            myRecycleAdapter.notifyDataSetChanged();


            auth = FirebaseAuth.getInstance();

    //        Toast.makeText(getContext(), "HOME" + auth.getUid(), Toast.LENGTH_LONG).show();






  }

    ItemTouchHelper.SimpleCallback itemtouchholder = new ItemTouchHelper.SimpleCallback(0 , ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            switch (direction) {


                case ItemTouchHelper.LEFT:
                    list=db.getTripsUpOnStatus("Upcoming",FirebaseAuth.getInstance().getUid());
                    final int postion = viewHolder.getAdapterPosition();

                    new android.app.AlertDialog.Builder(getContext())
                            .setTitle("Delete Trip")
                            .setMessage("Are you sure you want to delete this Trip?")


                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {





                    myRecycleAdapter.notifyDataSetChanged();

                    deleteItem = list.get(postion);

                    if (deleteItem.getTripFId() == null) {
                        list.remove(postion);
                        myRecycleAdapter.notifyDataSetChanged();

                        if (db.deleteTrip(deleteItem.getId()) != -1) {

                            cancelAlarm(deleteItem.getId());
                            myRecycleAdapter.notifyDataSetChanged();
                        }
                        Snackbar.make(myRecycle, " Deleted  " + deleteItem.getName(), Snackbar.LENGTH_LONG).show();
                        list=db.getTripsUpOnStatus("Upcoming",FirebaseAuth.getInstance().getUid());
                        myRecycleAdapter = new RecycleAdapter(getActivity(), list);
                        myRecycle.setAdapter(myRecycleAdapter);
                        myRecycleAdapter.notifyDataSetChanged();

                                         }


                    else {


                        DatabaseReference tripDatabaseRef= FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid()).child("Trips").child(deleteItem.getTripFId());
                        tripDatabaseRef.removeValue();
                        //int postion2 = viewHolder.getAdapterPosition();

                        list.remove(postion);
                        myRecycleAdapter.notifyDataSetChanged();

                        if (db.deleteTrip(deleteItem.getId()) != -1) {

                            cancelAlarm(deleteItem.getId());
                            myRecycleAdapter.notifyDataSetChanged();
                        }
                        list=db.getTripsUpOnStatus("Upcoming",FirebaseAuth.getInstance().getUid());
                        myRecycleAdapter = new RecycleAdapter(getActivity(), list);
                        myRecycle.setAdapter(myRecycleAdapter);
                        myRecycleAdapter.notifyDataSetChanged();

                        Snackbar.make(myRecycle, " Cancel:" + deleteItem.getName(), Snackbar.LENGTH_LONG).show();




                    }

                                }
                            })


                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


                    break;

                case ItemTouchHelper.RIGHT:
//                    list=db.getTripsUpOnStatus("Upcoming",FirebaseAuth.getInstance().getUid());
//                    myRecycleAdapter.notifyDataSetChanged();
                    listCanceled = new ArrayList<>();

                    int postion1 = viewHolder.getAdapterPosition();
                    deleteItem = list.get(postion1);


                        list.remove(postion1);
                    myRecycleAdapter.notifyDataSetChanged();
                    if (db.updateTripStatus(deleteItem.getId(), "Cancelled")) {

                        cancelAlarm(deleteItem.getId());
                    }

                    Snackbar.make(myRecycle, " Cancel: " + deleteItem.getName(), Snackbar.LENGTH_LONG).show();
                    myRecycleAdapter.notifyDataSetChanged();





                    break;
            }




        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getActivity() ,R.color.colorDelete))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
                    .addSwipeRightActionIcon(R.drawable.cancel)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    private void cancelAlarm(int tripId) {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), tripId, intent, 0);

        alarmManager.cancel(pendingIntent);
        //Toast.makeText(getContext(),"AlCancled",Toast.LENGTH_LONG).show();

    }








    }
















