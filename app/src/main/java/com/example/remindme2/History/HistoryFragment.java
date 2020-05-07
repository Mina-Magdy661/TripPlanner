package com.example.remindme2.History;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remindme2.AlertReceiver;
import com.example.remindme2.Database.DatabaseAdapter;
import com.example.remindme2.Database.FirebaseWorks;
import com.example.remindme2.Database.NoteDataBaseSQL;
import com.example.remindme2.HistoryRecyclerAdapter;
import com.example.remindme2.POJOS.Note_Data;
import com.example.remindme2.POJOS.TripJC;
import com.example.remindme2.R;
import com.example.remindme2.RecycleAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class HistoryFragment extends Fragment {

    FirebaseAuth auth;
    HistoryRecyclerAdapter myRecycleAdapter ;
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

        View v = inflater.inflate(R.layout.fragment_gallery, container, false);

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



       // Toast.makeText(getContext(), "his" + FirebaseAuth.getInstance().getUid(), Toast.LENGTH_LONG).show();

        list = db.getTripsUpOnStatus("history", FirebaseAuth.getInstance().getUid());
        myRecycleAdapter = new HistoryRecyclerAdapter(getActivity(), list);
        //new ItemTouchHelper(itemtouchholder).attachToRecyclerView(myRecycle);
        myRecycle.setAdapter(myRecycleAdapter);
        myRecycleAdapter.notifyDataSetChanged();


        //auth = FirebaseAuth.getInstance();
        //Toast.makeText(getContext(), "HOME" + auth.getUid(), Toast.LENGTH_LONG).show();






    }









}
















