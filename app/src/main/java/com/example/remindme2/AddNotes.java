package com.example.remindme2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.remindme2.Database.NoteDataBaseSQL;
import com.example.remindme2.POJOS.Note_Data;

public class AddNotes extends AppCompatActivity {

    EditText editTitle, editDetails;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("New Note");


        editTitle = findViewById(R.id.editTitle);
        editDetails = findViewById(R.id.editDetails);

        editTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() != 0) {
                    getSupportActionBar().setTitle(s);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private String pad(int i) {

        if (i < 10) {
            return "0" + i;
        }

        return String.valueOf(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.save) {

            Toast.makeText(AddNotes.this, "SAVE", Toast.LENGTH_LONG).show();

            Intent tripIdFromNoteActivity = getIntent();
            int KeytTripIdFromNoteActivity = tripIdFromNoteActivity.getIntExtra("tripID", 0);
            Log.i("TRIPID","IN ADD"+KeytTripIdFromNoteActivity);

            Note_Data note = new Note_Data(editTitle.getText().toString(), editDetails.getText().toString(),  KeytTripIdFromNoteActivity);
            NoteDataBaseSQL db = new NoteDataBaseSQL(AddNotes.this);
            db.addNote(note);

            finish();

        }

        if (item.getItemId() == R.id.delete) {

            Toast.makeText(AddNotes.this, "Data Not Saved", Toast.LENGTH_LONG).show();
           // super.onBackPressed();
            finish();

        }

        return super.onOptionsItemSelected(item);
    }

//    private void goToBack() {
////
//        Intent tripIDFromNodeActivity = getIntent();
//        int KeyTripIDFromNodeActivity =  tripIDFromNodeActivity.getIntExtra("tripID",0);
//
//        Intent i = new Intent(AddNotes.this, NoteActivity.class);
//        i.putExtra("tripID", KeyTripIDFromNodeActivity);
//        finish();
//        startActivity(i);
//
//    }


}