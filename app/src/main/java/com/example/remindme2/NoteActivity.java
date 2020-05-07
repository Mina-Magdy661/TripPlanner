package com.example.remindme2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remindme2.Database.NoteDataBaseSQL;
import com.example.remindme2.POJOS.Note_Data;
import com.example.remindme2.POJOS.Note_Data;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    RecyclerView recyclerView ;
    Toolbar toolbar ;
    NoteAdapter adapter ;
    List<Note_Data> list;
    Intent tripIDFromRecycleView ;
    int KeyTripIDFromRecycleView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        recyclerView = findViewById(R.id.listOfNote);


    }

    @Override
    protected void onStart() {
        super.onStart();
        tripIDFromRecycleView = getIntent();
        KeyTripIDFromRecycleView = tripIDFromRecycleView.getIntExtra("tripID",0);
        Log.i("TRIPID",""+KeyTripIDFromRecycleView);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("YOU NOTE");

        NoteDataBaseSQL db =  new NoteDataBaseSQL(this);

        Intent getDataFromDelete = getIntent();
        int KeyGetDataFromDelete = getDataFromDelete.getIntExtra("KeyOfTripIdDelete", 0);

        if(KeyGetDataFromDelete == 0  ) {
            list = db.getAllNote(KeyTripIDFromRecycleView);
        }
        else {

            list = db.getAllNote(KeyGetDataFromDelete);
        }



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoteAdapter( this , list );
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.add){

            //Toast.makeText(NoteActivity.this , "TRIP_ID" + KeyTripIDFromRecycleView , Toast.LENGTH_LONG).show();

            Intent goAddNote = new Intent(NoteActivity.this,AddNotes.class);
            Log.i("TRIPID","TO ADD"+KeyTripIDFromRecycleView);

            goAddNote.putExtra("tripID", KeyTripIDFromRecycleView);
            startActivity(goAddNote);
        }
        return super.onOptionsItemSelected(item);
    }


}
