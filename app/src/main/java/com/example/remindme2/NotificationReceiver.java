package com.example.remindme2;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.remindme2.Database.DatabaseAdapter;
import com.example.remindme2.POJOS.TripJC;

public class NotificationReceiver extends BroadcastReceiver {

    DatabaseAdapter databaseAdapter;
    int notificationClickedFlag, notificationCancelledFlag;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("test","here in receiver ");

        databaseAdapter = new DatabaseAdapter(context);
        TripJC trip = (TripJC) intent.getSerializableExtra("tripObj");

//        notificationClickedFlag = intent.getIntExtra("NotifClickedFlag", 0);
//        notificationCancelledFlag = intent.getIntExtra("NotifCancelledFlag", 0);
//        Log.i("test", " clicked" + notificationClickedFlag + "");
//        Log.i("test", " cancelled" + notificationCancelledFlag + "");

        //if(notificationClickedFlag == 1) {
            Intent alertAgain = new Intent(context,AlertDialog.class);
            alertAgain.putExtra("id_trip",trip.getId());
            alertAgain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(alertAgain);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(trip.getId());
           // databaseAdapter.updateTripStatus(trip.getId(), "Ended");
            Log.i("test", "clicked notif");
       // }

//        else if(notificationCancelledFlag == 2){
//            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            manager.cancel(trip.getId());
//            databaseAdapter.updateTripStatus(trip.getId(), "cancelled");
//            Log.i("test", "delete notif");
//        }
//
//        else {
//            Log.i("test", "nothing");
//        }

    }
}
