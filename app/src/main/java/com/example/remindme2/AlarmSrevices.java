package com.example.remindme2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class AlarmSrevices  extends Service {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    public static final String ALARM_PREF = "ALARM_PREF" ;
    public static final String ALARM_ID = "ALARM_ID";
    SharedPreferences sharedpreferences;



    Calendar c;
    int alarm_id=0;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        String input = intent.getStringExtra("inputExtra");
//
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                0, notificationIntent, 0);
//
//        Notification notification = new NotificationCompat.Builder(this, channelID)
//                .setContentTitle("Example Service")
//                .setContentText(input)
//                .setSmallIcon(R.drawable.ic_grid_on_black_24dp)
//                .setContentIntent(pendingIntent)
//                .build();
//
//
//
//
//        startForeground(1,notification );
        c= (Calendar) intent.getSerializableExtra("Cal");
        alarm_id=intent.getIntExtra("tripId",0);
        Log.i("alarm_id",String.valueOf(alarm_id)+"-1");

       // Toast.makeText(getApplicationContext(),"OK2",Toast.LENGTH_LONG).show();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent AlertRecieverIntent = new Intent(this, AlertReceiver.class);
        AlertRecieverIntent.putExtra("TRIPID",alarm_id);
//        if(getCurrentIdFromShared()==0) {
//            alarm_id=0;
//            putCurrentIdToShared(1);
//            Log.i("alarm_id",String.valueOf(alarm_id)+"1");
//
//        }
//        else{
//
////            alarm_id=getCurrentIdFromShared()+1;
////            putCurrentIdToShared(alarm_id);
//            Log.i("alarm_id",String.valueOf(alarm_id)+"2");
//
//
//        }
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, alarm_id, AlertRecieverIntent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
           // Toast.makeText(getApplicationContext(),"OK3"+Calendar.DATE,Toast.LENGTH_LONG).show();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
           // Toast.makeText(getApplicationContext(),"OK4"+c.getTimeInMillis(),Toast.LENGTH_LONG).show();
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent2);
        }


        //do heavy work on a background thread
        //stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



//    public  void putCurrentIdToShared(int id){
//        sharedpreferences = getSharedPreferences(ALARM_PREF, Context.MODE_PRIVATE);
//
//
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//
//        editor.putInt(ALARM_ID,id);
//        editor.commit();
//
//
//    }
//    public  int getCurrentIdFromShared(){
//
//        sharedpreferences
//                = getSharedPreferences(ALARM_PREF,
//                MODE_PRIVATE);
//
//        int a = sharedpreferences.getInt(ALARM_ID, 0);
//
//        return a;
//    }
}