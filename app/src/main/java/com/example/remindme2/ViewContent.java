package com.example.remindme2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.remindme2.Database.NoteDataBaseSQL;
import com.example.remindme2.POJOS.Note_Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ViewContent extends AppCompatActivity {

    TextView txtEditdNote ;
    NoteDataBaseSQL db ;
    Note_Data note ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note_content);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent i = getIntent();
        long id = i.getLongExtra("ID", 5);
        final int tripID = i.getIntExtra("tripID" , 0);

        db  = new NoteDataBaseSQL(this);
         note = db.getNote(id);

        txtEditdNote = findViewById(R.id.txtEditOfote);
        txtEditdNote.setText(note.getContent());



   getSupportActionBar().setTitle(note.getTitle());


         Toast.makeText(ViewContent.this , "Title ->"  + note.getTitle() , Toast.LENGTH_LONG).show();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


             db.deleteNote(note.getId());
//             Intent i = new Intent(getApplicationContext(),NoteActivity.class);
//             i.putExtra("KeyOfTripIdDelete" , tripID);
//             startActivity(i);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_note, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.editDetails) {

            Intent i = new Intent(ViewContent.this, EditNote.class);
            i.putExtra("ID", note.getId());
            i.putExtra("tripId", note.getTripId());
            startActivity(i);

         }

        return super.onOptionsItemSelected(item);
    }

//

}
