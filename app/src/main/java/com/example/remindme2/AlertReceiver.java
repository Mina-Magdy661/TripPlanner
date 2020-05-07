package com.example.remindme2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.remindme2.Database.DatabaseAdapter;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseAdapter databaseAdapter=new DatabaseAdapter(context);
        //Toast.makeText(context,"Alarm Notification",Toast.LENGTH_LONG).show();
       int aId= intent.getIntExtra("TRIPID",0);
        //Toast.makeText(context,"Alarm Notification"+aId,Toast.LENGTH_LONG).show();
        //databaseAdapter.getTripById(aId);
       // Toast.makeText(context,"TripName"+databaseAdapter.getTripById(aId).getName(),Toast.LENGTH_LONG).show();

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, nb.build());

        Intent i =new Intent(context,AlertDialog.class);
        //context.startActivity(i);
         i.putExtra("id_trip",aId);
        //i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(i);



    }
}
