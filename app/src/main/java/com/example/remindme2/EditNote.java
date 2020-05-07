package com.example.remindme2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.Calendar;

public class EditNote extends AppCompatActivity {

    EditText editTitle, editDetails;
    Toolbar toolbar;
    Calendar calendar;
    String todayDay, currentTime;
    NoteDataBaseSQL db ;
    Note_Data note ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Intent i = getIntent();
        long id = i.getLongExtra("ID", 0 );

         db = new NoteDataBaseSQL(this);
         note = db.getNote(id);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(note.getTitle());



        editTitle = findViewById(R.id.editTitle);
        editDetails = findViewById(R.id.editDetails);

        editDetails.setText(note.getContent());
        editTitle.setText(note.getTitle());

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

        calendar = Calendar.getInstance();
        todayDay = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
        currentTime = pad(calendar.get(Calendar.HOUR)) + ":" + pad(calendar.get(Calendar.MINUTE));
    }

    private String pad(int i) {

        if (i < 10) {
            return "0" + i;
        }

        return String.valueOf(i);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.save) {

            //Toast.makeText(EditNote.this, "SAVE", Toast.LENGTH_LONG).show();

            note.setTitle(editTitle.getText().toString());
            note.setContent(editDetails.getText().toString());
            long id = db.editNote(note);

            if(id==note.getId()){
                Toast.makeText(EditNote.this , "UPDATE" , Toast.LENGTH_LONG).show();

            }
            else{

                Toast.makeText(EditNote.this , "NOT UPDATED" , Toast.LENGTH_LONG).show();
            }

         //   Intent i = new Intent(getApplicationContext() , ViewContent.class);
//            i.putExtra("ID", note.getId());
//            i.putExtra("tripID", note.getTripId());
//            startActivity(i);

//            Intent tripIdFromNoteActivity = getIntent();
//            int KeytTripIdFromNoteActivity = tripIdFromNoteActivity.getIntExtra("tripID", 0);


            finish();
//
        }

        if (item.getItemId() == R.id.delete) {

            Toast.makeText(EditNote.this, "Data Not Saved", Toast.LENGTH_LONG).show();
            super.onBackPressed();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

}

