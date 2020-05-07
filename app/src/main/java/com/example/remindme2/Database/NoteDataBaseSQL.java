package com.example.remindme2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.remindme2.POJOS.Note_Data;

import java.util.ArrayList;
import java.util.List;

public class NoteDataBaseSQL extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NOTE.db";
    private static final String DATABASE_TABLE = "NOTETABLE";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";

    private static final String KEY_TRIP_ID = "tripId";



    public NoteDataBaseSQL(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + DATABASE_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_TITLE + " TEXT," +
                KEY_CONTENT + " TEXT," +
                KEY_TRIP_ID +" INT"+ ")";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion >= newVersion)
            return;
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    public long addNote(Note_Data note) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_TITLE, note.getTitle());
        contentValues.put(KEY_CONTENT, note.getContent());

        contentValues.put(KEY_TRIP_ID,note.getTripId());

        long id = db.insert(DATABASE_TABLE, null, contentValues);

        Log.i("MM", "ID ADD   " + id);

        return id;
    }


    public  Note_Data getNote(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{
                        KEY_ID, KEY_TITLE, KEY_CONTENT,KEY_TRIP_ID}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
            Note_Data note = new Note_Data(cursor.getLong(0), cursor.getString(1),
                    cursor.getString(2),cursor.getInt(3));

            return note;
    }


    public List<Note_Data> getAllNote(int tripid){



        SQLiteDatabase db =  this.getReadableDatabase();
        List<Note_Data> allNote = new ArrayList<>();

        String query = "SELECT * FROM " + DATABASE_TABLE  + " WHERE " + KEY_TRIP_ID + " = " + tripid;
        Cursor cursor = db.rawQuery(query , null);

        if(cursor.moveToFirst()){

            do{

                Note_Data note = new Note_Data();

                note.setId(cursor.getLong(0));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setTripId(cursor.getInt(3));

//                Log.i("MM", "Time   " + note.getTime());
//                Log.i("MM", "TITLE   " + note.getTitle());
//                Log.i("MM", "Date   " + note.getDate());

                Log.i("MM", " GET ID   " + note.getId());

                allNote.add(note);

            } while (cursor.moveToNext());

        }

        return allNote;

    }

    public int editNote(Note_Data note) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_TITLE, note.getTitle());
        contentValues.put(KEY_CONTENT, note.getContent());

        contentValues.put(KEY_TRIP_ID,note.getTripId());

        return  db.update(DATABASE_TABLE , contentValues ,KEY_ID + "=?" , new String[] {String.valueOf(note.getId())} );

    }


    public  void deleteNote(long id){

        SQLiteDatabase db =  this.getWritableDatabase();
        db.delete(DATABASE_TABLE , KEY_ID + "=?" , new String[]{String.valueOf(id)});
        db.close();

    }
}



