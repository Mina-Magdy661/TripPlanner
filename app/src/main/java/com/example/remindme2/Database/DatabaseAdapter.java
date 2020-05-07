package com.example.remindme2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.remindme2.POJOS.TripJC;

import java.util.ArrayList;

public class DatabaseAdapter {
    private Context context;
    static DataBaseHelper dbHelper;
    private boolean englishFlag = true;
    public DatabaseAdapter(Context _context){
        dbHelper = new DataBaseHelper(_context);
        context = _context;

    }

    public long insertTrip( TripJC trip){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(DataBaseHelper.TRIPID, trip.getId());
        contentValues.put(DataBaseHelper.TRIPNAME, trip.getName());
        contentValues.put(DataBaseHelper.STARTPOINT, trip.getStartPoint());
        contentValues.put(DataBaseHelper.ENDPOINT, trip.getEndPoint());
        contentValues.put(DataBaseHelper.DATE, trip.getDate());
        contentValues.put(DataBaseHelper.TIME, trip.getTime());
        contentValues.put(DataBaseHelper.TYPE, trip.getType());
        contentValues.put(DataBaseHelper.STARTLONG, trip.getStartLongitude());
        contentValues.put(DataBaseHelper.STARTLAT, trip.getStartLatitude());
        contentValues.put(DataBaseHelper.ENDLONG, trip.getEndLongitude());
        contentValues.put(DataBaseHelper.ENDLAT, trip.getEndLatitude());
        contentValues.put(DataBaseHelper.STATUS, trip.getStatus());
        contentValues.put(DataBaseHelper.USERID, trip.getUserId());
        contentValues.put(DataBaseHelper.FID,trip.getTripFId());



        long id = db.insert(DataBaseHelper.TRIP_TABLE_NAME, null, contentValues);

        return id;
    }

    public void  insertListOfTrips(ArrayList<TripJC> receivedTrips){


        for(int i=0;i<receivedTrips.size();i++) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            //contentValues.put(DataBaseHelper.TRIPID, trip.getId());
            contentValues.put(DataBaseHelper.TRIPNAME, receivedTrips.get(i).getName());
            contentValues.put(DataBaseHelper.STARTPOINT, receivedTrips.get(i).getStartPoint());
            contentValues.put(DataBaseHelper.ENDPOINT, receivedTrips.get(i).getEndPoint());
            contentValues.put(DataBaseHelper.DATE, receivedTrips.get(i).getDate());
            contentValues.put(DataBaseHelper.TIME, receivedTrips.get(i).getTime());
            contentValues.put(DataBaseHelper.TYPE, receivedTrips.get(i).getType());
            contentValues.put(DataBaseHelper.STARTLONG, receivedTrips.get(i).getStartLongitude());
            contentValues.put(DataBaseHelper.STARTLAT, receivedTrips.get(i).getStartLatitude());
            contentValues.put(DataBaseHelper.ENDLONG, receivedTrips.get(i).getEndLongitude());
            contentValues.put(DataBaseHelper.ENDLAT, receivedTrips.get(i).getEndLatitude());
            contentValues.put(DataBaseHelper.STATUS, receivedTrips.get(i).getStatus());
            contentValues.put(DataBaseHelper.USERID, receivedTrips.get(i).getUserId());
            contentValues.put(DataBaseHelper.FID,receivedTrips.get(i).getTripFId());

            long id = db.insert(DataBaseHelper.TRIP_TABLE_NAME, null, contentValues);

            //set Alarms Again

        }
    }


    public int editTrip(TripJC trip){

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DataBaseHelper.TRIPNAME, trip.getName());

        contentValues.put(DataBaseHelper.STARTPOINT, trip.getStartPoint());
        contentValues.put(DataBaseHelper.ENDPOINT, trip.getEndPoint());
        contentValues.put(DataBaseHelper.DATE, trip.getDate());
        contentValues.put(DataBaseHelper.TIME, trip.getTime());
        contentValues.put(DataBaseHelper.TYPE,trip.getType());
        contentValues.put(DataBaseHelper.STARTLONG,trip.getStartLongitude());
        contentValues.put(DataBaseHelper.STARTLAT, trip.getStartLatitude());
        contentValues.put(DataBaseHelper.ENDLONG, trip.getEndLongitude());
        contentValues.put(DataBaseHelper.ENDLAT, trip.getEndLatitude());
        contentValues.put(DataBaseHelper.STATUS, trip.getStatus());
        contentValues.put(DataBaseHelper.USERID, trip.getUserId());
        contentValues.put(DataBaseHelper.FID,trip.getTripFId());

        return  db.update(DataBaseHelper.TRIP_TABLE_NAME , contentValues , DataBaseHelper.TRIPID + "=?" , new String[] {String.valueOf(trip.getId())} );

    }





///
public  boolean checkExsistance(String tripFId){

    Cursor c;
    SQLiteDatabase db = dbHelper.getReadableDatabase();
    String[] columns = {

            DataBaseHelper.FID
    };
    //query (String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
    c = db.query(DataBaseHelper.TRIP_TABLE_NAME, columns, columns[0]+ "=?", new String[]{String.valueOf(tripFId)}, null, null, null);
    if(c.getCount()>0) return true;
   return false;
}



////////////////////////////////////////////////////
    public  TripJC getTripById(int tripId){

         TripJC trip=new TripJC();
        Cursor c;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {DataBaseHelper.TRIPID,
                DataBaseHelper.TRIPNAME,
                DataBaseHelper.STARTPOINT,
                DataBaseHelper.ENDPOINT,
                DataBaseHelper.DATE,
                DataBaseHelper.TIME,
                DataBaseHelper.TYPE,
                DataBaseHelper.STARTLONG,
                DataBaseHelper.STARTLAT,
                DataBaseHelper.ENDLONG,
                DataBaseHelper.ENDLAT,
                DataBaseHelper.STATUS,
                DataBaseHelper.USERID,
                DataBaseHelper.FID
        };
        //query (String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
        c = db.query(DataBaseHelper.TRIP_TABLE_NAME, columns, columns[0]+ "=?", new String[]{String.valueOf(tripId)}, null, null, null);

        while ( c.moveToNext()) {

            //msg= c.getString(2);

            int tId=c.getInt(0);
            String tName=c.getString(1);
            String tStartP=c.getString(2);
            String tEndP=c.getString(3);
            String tDate=c.getString(4);
            String tTime=c.getString(5);
            String tType=c.getString(6);
            double tStartL=c.getDouble(7);
            double tStartLat=c.getDouble(8);
            double tEndL=c.getDouble(9);
            double tEndLat=c.getDouble(10);
            String tStatus=c.getString(11);
            String uId=c.getString(12);
            String fId=c.getString(13);

            trip.setId(tId);
            trip.setName(tName);
            trip.setStartPoint(tStartP);
            trip.setEndPoint(tEndP);
            trip.setDate(tDate);
            trip.setTime(tTime);
            trip.setType(tType);
            trip.setStartLongitude(tStartL);
            trip.setStartLatitude(tStartLat);
            trip.setEndLongitude(tEndL);
            trip.setEndLatitude(tEndLat);
            trip.setStatus(tStatus);
            trip.setUserId(uId);
            trip.setTripFId(fId);




        }
        return trip;
    }

    public ArrayList<TripJC> getTripsUpOnStatus(String queryStatus,String userId){

             ArrayList<TripJC> tripsList=new ArrayList<>();
            TripJC trip=null;
            Cursor c=null;
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String[] columns = {DataBaseHelper.TRIPID,
                    DataBaseHelper.TRIPNAME,
                    DataBaseHelper.STARTPOINT,
                    DataBaseHelper.ENDPOINT,
                    DataBaseHelper.DATE,
                    DataBaseHelper.TIME,
                    DataBaseHelper.TYPE,
                    DataBaseHelper.STARTLONG,
                    DataBaseHelper.STARTLAT,
                    DataBaseHelper.ENDLONG,
                    DataBaseHelper.ENDLAT,
                    DataBaseHelper.STATUS,
                    DataBaseHelper.USERID,
                    DataBaseHelper.FID
            };
            //query (String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)

           if(queryStatus.equalsIgnoreCase("history")) {
//Toast.makeText(context,"HIS",Toast.LENGTH_LONG).show();



               c = db.query(DataBaseHelper.TRIP_TABLE_NAME, columns, "("+columns[11] + "=? OR "+ columns[11] +"= ?) AND "+columns[12]+" = ?", new String[]{"Ended","Cancelled",userId}, null, null, null);





           }
        if(queryStatus.equalsIgnoreCase("Upcoming")){
               c = db.query(DataBaseHelper.TRIP_TABLE_NAME, columns, columns[11] + "=? AND "+ columns[12] +"= ? ", new String[]{"Upcoming",userId}, null, null, null);
                            }

        if(queryStatus.equalsIgnoreCase("")){


            c = db.query(DataBaseHelper.TRIP_TABLE_NAME, columns, columns[12]+"=?", new String[]{userId}, null, null, null);


        }



            while ( c.moveToNext()) {

                //msg= c.getString(2);
                    trip =new TripJC();
                int tId=c.getInt(0);
                String tName=c.getString(1);
                String tStartP=c.getString(2);
                String tEndP=c.getString(3);
                String tDate=c.getString(4);
                String tTime=c.getString(5);
                String tType=c.getString(6);
                double tStartL=c.getDouble(7);
                double tStartLat=c.getDouble(8);
                double tEndL=c.getDouble(9);
                double tEndLat=c.getDouble(10);
                String tStatus=c.getString(11);
                String uId=c.getString(12);
                String fId=c.getString(13);

                trip.setId(tId);
                trip.setName(tName);
                trip.setStartPoint(tStartP);
                trip.setEndPoint(tEndP);
                trip.setDate(tDate);
                trip.setTime(tTime);
                trip.setType(tType);
                trip.setStartLongitude(tStartL);
                trip.setStartLatitude(tStartLat);
                trip.setEndLongitude(tEndL);
                trip.setEndLatitude(tEndLat);
                trip.setStatus(tStatus);
                trip.setUserId(uId);

                trip.setTripFId(fId);
                tripsList.add(trip);




            }
//        Log.i("TRIP",String.valueOf(trip.getId()));
            return tripsList;
        }











    public boolean updateTripStatus(int tripId, String status){


        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.STATUS,status);
        Log.i("TESTTTT",String.valueOf(status));
        db.update(DataBaseHelper.TRIP_TABLE_NAME, contentValues, "_id = ?",new String[] { String.valueOf(tripId) });
        return true;



    }

    public int deleteTrip(int tripId){

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DataBaseHelper.TRIP_TABLE_NAME, "_id = ?",new String[] { String.valueOf(tripId) });

    }



    static class DataBaseHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 2;
        private static final String DATABASE_NAME = "REMINDER.db";
        private static final String TRIP_TABLE_NAME = "TRIPS";
        private static final String TRIPID = "_id";
        private static final String TRIPNAME = "TRIPNAME";
        private static final String STARTPOINT = "STARTPOINT";
        private static final String ENDPOINT = "ENDPOINT";
        private static final String DATE = "DATE";
        private static final String TIME = "TIME";
        private static final String TYPE = "TYPE";
        private static final String STARTLONG = "STARTLONG";
        private static final String STARTLAT = "STARTLAT";
        private static final String ENDLONG = "ENDLONG";
        private static final String ENDLAT = "ENDLAT";
        private static final String STATUS = "STATUS";
        private static final String USERID = "USERID";
        private static final String FID = "FID";







        private static final String CREATE_TRIP_TABLE = "CREATE TABLE " + TRIP_TABLE_NAME + " (" +TRIPID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                TRIPNAME +" TEXT, " + STARTPOINT + " TEXT," + ENDPOINT + " TEXT," + DATE +" TEXT," + TIME + " TEXT," + TYPE + " TEXT," + STARTLONG + " REAL," +
                STARTLAT + " REAL," + ENDLONG + " REAL," + ENDLAT + " REAL," + STATUS + " TEXT," + USERID + " TEXT," + FID
                + " TEXT" + ")";



        private String DROP_TRIP_TABLE = "DROP TABLE IF EXISTS " + TRIP_TABLE_NAME ;


        public DataBaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(CREATE_TRIP_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub

            db.execSQL(DROP_TRIP_TABLE);
            onCreate(db);

        }
    }


}
