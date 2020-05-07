package com.example.remindme2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.remindme2.Database.NoteDataBaseSQL;
import com.example.remindme2.POJOS.Note_Data;

import java.util.ArrayList;

public class NotesListActivity extends AppCompatActivity {


    ListView listView;
    ArrayList<Note_Data> noteList;
    ArrayList<String> noteContent;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        listView=findViewById(R.id.notesList);


        NoteDataBaseSQL noteDataBaseSQL=new NoteDataBaseSQL(this);
        noteList=new ArrayList<>();
        noteContent=new ArrayList<>();



        int tripId =getIntent().getIntExtra("TRIPID",0);

       // Toast.makeText(getApplicationContext(),"TripId"+tripId,Toast.LENGTH_LONG).show();

        noteList= (ArrayList<Note_Data>) noteDataBaseSQL.getAllNote(tripId);


for(int i=0;i<noteList.size();i++){


    noteContent.add(noteList.get(i).getContent());
}

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1,noteContent );

if(noteList.size()>0) {

    listView.setAdapter(adapter);
}








    }

    @Override
    protected void onStart() {
        super.onStart();


        adapter.notifyDataSetChanged();



    }
}
