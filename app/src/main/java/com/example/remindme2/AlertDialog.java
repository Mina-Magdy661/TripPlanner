package com.example.remindme2;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.remindme2.Database.DatabaseAdapter;
import com.example.remindme2.POJOS.TripJC;

public class AlertDialog extends AppCompatActivity implements View.OnClickListener {
    public static final String channelID = "channelID";


    public Button yesBtn, noBtn, latterBtn;
    Ringtone ringTone;
    Dialog dialog;
    TextView tripName, tripNotes ;
    DatabaseAdapter databaseAdapter;

    TripJC trip;
    int tripId;
    Vibrator vibrator;
    private NotificationManagerCompat notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        notificationManager = NotificationManagerCompat.from(this);
        databaseAdapter = new DatabaseAdapter(this);
        tripId = getIntent().getIntExtra("id_trip", 0);
        trip = databaseAdapter.getTripById(tripId);


        //tripName=findViewById(R.id.tripName);

//        Log.i("test","bbbbbbbb " + trip.getId());

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            ringTone = RingtoneManager.getRingtone(getApplicationContext(), notification);
            ringTone.play();
            vibrator = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_alert);

        initViews();



        yesBtn.setOnClickListener(this);
        noBtn.setOnClickListener(this);
        latterBtn.setOnClickListener(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
      //  tripName.setText(trip.getName());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_start:
                dialog.dismiss();
                ringTone.stop();
                Intent toFloatIntent = new Intent(this, FloatingService.class);
                toFloatIntent.putExtra("tripId", trip.getId());

                startService(toFloatIntent);


                String uri = "http://maps.google.com/maps?" + "&daddr=" + trip.getEndLatitude()
                        + "," + trip.getEndLongitude();
                Intent intent = new Intent(
                        Intent.ACTION_VIEW, Uri.parse(uri));

                startActivity(intent);
                vibrator.cancel();
               if( databaseAdapter.updateTripStatus(tripId,"Ended")) {
                   Toast.makeText(getApplicationContext(), "Your Trip Ended", Toast.LENGTH_LONG).show();
               }
                AlertDialog.this.finish();

                break;

            case R.id.btn_cancel:
                ringTone.stop();
                dialog.dismiss();
                vibrator.cancel();
                if( databaseAdapter.updateTripStatus(tripId,"Cancelled")) {
                    Toast.makeText(getApplicationContext(), "Your Trip Cancelled", Toast.LENGTH_LONG).show();
                }

                AlertDialog.this.finish();
                break;

            case R.id.btn_latter:
                ringTone.stop();
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Latter", Toast.LENGTH_LONG).show();

                Intent clickedNotifIntent = new Intent(this, NotificationReceiver.class);
                clickedNotifIntent.putExtra("NotifClickedFlag", 1);
                clickedNotifIntent.putExtra("tripObj", trip);
                PendingIntent clickedNotifPendingIntent = PendingIntent.getBroadcast(
                        this, 1, clickedNotifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel1 = new NotificationChannel(
                            channelID,
                            "Channel 1",
                            NotificationManager.IMPORTANCE_HIGH
                    );
                    channel1.setDescription("This is Channel 1");

                    NotificationChannel channel2 = new NotificationChannel(
                            channelID,
                            "Channel 2",
                            NotificationManager.IMPORTANCE_LOW
                    );
                    channel2.setDescription("This is Channel 2");

                    NotificationManager manager = getSystemService(NotificationManager.class);
                    manager.createNotificationChannel(channel1);
                    manager.createNotificationChannel(channel2);
                }

                Notification notification = new NotificationCompat.Builder(this, channelID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(trip.getName())
                        .setContentText("Your Trip To : "+trip.getEndPoint())
                        .setContentIntent(clickedNotifPendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setAutoCancel(false)
                        .setOngoing(true)
                        .build();

                notificationManager.notify(tripId, notification);


                vibrator.cancel();
                AlertDialog.this.finish();
                break;

            default:
                break;
        }
    }

    public void initViews() {
        tripName = (TextView) dialog.findViewById(R.id.remember);
        tripNotes = (TextView) dialog.findViewById(R.id.remember_notes);
        yesBtn = (Button) dialog.findViewById(R.id.btn_start);
        noBtn = (Button) dialog.findViewById(R.id.btn_cancel);
        latterBtn = (Button) dialog.findViewById(R.id.btn_latter);

    }
}

