package com.example.remindme2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.remindme2.Database.DatabaseAdapter;
import com.example.remindme2.POJOS.TripJC;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.Calendar;

public class EditTripActivity extends AppCompatActivity {
    String apiKey="AIzaSyDE7Cp07odjXREc8M4xenvYsQg-mBz5HSk";
    EditText startEt,endEt;
    EditText tripTitleEt;
    EditText dateEditText;
    EditText timeEditText;
    Button addTrip;
    DatePickerDialog datePicker;
    TimePickerDialog timePicker;
    //String tripType="one way";
    int day,month,yearm,hour,minutes;
    Calendar cldr;
    //will come from fire base auth
    //String userId="";
    String startPoint="";
    String endPoint="";
    double startLat;
    double startLong;
    double endLat;
    double endLong;
    DatabaseAdapter DB;
    TripJC recieviedTrip;
    String selectPlaceEt;

    int REQUEST_OVERLAY_PERMISSION=111;
    int AUTOCOMPLETE_REQUEST_CODE = 1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);
        DB=new DatabaseAdapter(getApplicationContext());
        Places.initialize(getApplicationContext(), apiKey);
        dateEditText=findViewById(R.id.dateEt);
        tripTitleEt=findViewById(R.id.tripTitleEt);
        timeEditText=findViewById(R.id.timeEt);
        startEt=findViewById(R.id.startEt);
        endEt=findViewById(R.id.endEt);

        Intent receivedIntent=getIntent();
       recieviedTrip = (TripJC) receivedIntent.getSerializableExtra("THETRIP");




        if(!Settings.canDrawOverlays(this)){
            // ask for setting
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
        }



        //////

        addTrip=findViewById(R.id.addTripBtn);


        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cldr= Calendar.getInstance();

                day = cldr.get(Calendar.DAY_OF_MONTH);
                month = cldr.get(Calendar.MONTH);
                yearm = cldr.get(Calendar.YEAR);

                datePicker = new DatePickerDialog(EditTripActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                day=dayOfMonth;
                                month=monthOfYear;
                                yearm=year;

                                dateEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, yearm, month, day);
                datePicker.show();
            }







        });

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cldr= Calendar.getInstance();

                hour = cldr.get(Calendar.HOUR_OF_DAY);
                minutes = cldr.get(Calendar.MINUTE);
                timePicker = new TimePickerDialog(EditTripActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                hour=sHour;
                                minutes=sMinute;
                                timeEditText.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, false);
                timePicker.show();
            }

        });







        addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBack=new Intent(getApplicationContext(),MainActivity.class);
                if(validateForm()){
                    TripJC trip=new TripJC(tripTitleEt.getText().toString(),startPoint,endPoint,day+"/"+month+"/"+yearm,hour+":"+minutes,recieviedTrip.getType(),startLong,startLat,endLong,endLat,"Upcoming",recieviedTrip.getUserId());
                    trip.setTripFId(recieviedTrip.getTripFId());
                trip.setId(recieviedTrip.getId());
                    //Toast.makeText(getApplicationContext(),"OK TESTAAT"+recieviedTrip.getId(),Toast.LENGTH_LONG).show();

                    if(  DB.editTrip(trip)!=-1){
                        cldr=Calendar.getInstance();
                        cldr.set(Calendar.YEAR,yearm);
                        cldr.set(Calendar.DAY_OF_MONTH,day);
                        cldr.set(Calendar.MONTH,month-1);
                        cldr.set(Calendar.HOUR_OF_DAY,hour);
                        cldr.set(Calendar.MINUTE,minutes);
                        cldr.set(Calendar.SECOND,0);
                        Log.i("alarm_id","t1");

                            startAlarm(cldr,recieviedTrip.getId());}
                    Toast.makeText(getApplicationContext(),"Updated Successfully !"+recieviedTrip.getId(),Toast.LENGTH_LONG).show();

                     finish();


                }



            }
        });


        //............

startEt.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        selectPlaceEt="s";
        StartAutoCompleteActivity();

    }
});

        endEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPlaceEt="e";
                StartAutoCompleteActivity();

            }
        });






    }


    @Override
    protected void onStart() {
        super.onStart();

        if(recieviedTrip!=null){
            tripTitleEt.setText(recieviedTrip.getName());
            dateEditText.setText(recieviedTrip.getDate());
            timeEditText.setText(recieviedTrip.getTime());
            startEt.setText(recieviedTrip.getStartPoint());
            endEt.setText(recieviedTrip.getEndPoint());

            String [] split=  recieviedTrip.getDate().split("/");
            day=Integer.parseInt(split[0]);
            month=Integer.parseInt(split[1]);
            yearm=Integer.parseInt(split[2]);
            String [] splitTime=  recieviedTrip.getTime().split(":");
            hour=Integer.parseInt(splitTime[0]);
            minutes=Integer.parseInt(splitTime[1]);
            startPoint=recieviedTrip.getStartPoint();
            endPoint=recieviedTrip.getEndPoint();
            startLat=recieviedTrip.getStartLatitude();
            startLong=recieviedTrip.getStartLongitude();
            endLat=recieviedTrip.getEndLatitude();
            endLong=recieviedTrip.getEndLongitude();




        }
    }

    public void StartAutoCompleteActivity() {
        // Toast.makeText(getApplicationContext(),"in startAutoComplete",Toast.LENGTH_LONG).show();
        Intent i = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                Arrays.asList(Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG))
                .setTypeFilter(TypeFilter.ADDRESS)
                .setCountries(Arrays.asList("EG"))
                .build(getApplicationContext());
        startActivityForResult(i,AUTOCOMPLETE_REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);

                if (selectPlaceEt.equals("s")){
                    startLat = place.getLatLng().latitude;
                    startLong = place.getLatLng().longitude;
                    startEt.setText(place.getName());
                    startPoint=place.getName();
                }
            if (selectPlaceEt.equals("e")){
                endLat = place.getLatLng().latitude;
                endLong = place.getLatLng().longitude;
                endEt.setText(place.getName());
                endPoint=place.getName();

                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("TAG", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
            }
        }








        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    //Toast.makeText(getApplicationContext(),"We can now show alarms for you",Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(getApplicationContext(),"We cannot  show alarms for you, If You want go to Setting to enable it",Toast.LENGTH_LONG).show();
                }
            }
        }
    }








    private void startAlarm(Calendar c,int tripId) {



        Intent serviceIntent = new Intent(this, AlarmSrevices.class);
        serviceIntent.putExtra("Cal",c);
        serviceIntent.putExtra("tripId",tripId);

        startService(serviceIntent);




    }

    private boolean validateForm() {
        boolean valid = true;

        String tripTitle = tripTitleEt.getText().toString();
        if (TextUtils.isEmpty(tripTitle)) {
            tripTitleEt.setError("Required.");
            valid = false;
        } else {
            tripTitleEt.setError(null);
        }

        String date = dateEditText.getText().toString();
        if (TextUtils.isEmpty(date)) {
            dateEditText.setError("Required.");
            valid = false;
        } else {
            dateEditText.setError(null);
        }
        String time = timeEditText.getText().toString();
        if (TextUtils.isEmpty(time)) {
            timeEditText.setError("Required.");
            valid = false;
        } else {
            timeEditText.setError(null);
        }
        String startPoint = startEt.getText().toString();
        if (TextUtils.isEmpty(startPoint)) {
            startEt.setError("Required.");
            valid = false;
        } else {
            startEt.setError(null);
        }


        String endPoint = endEt.getText().toString();
        if (TextUtils.isEmpty(endPoint)) {
            endEt.setError("Required.");
            valid = false;
        } else {
            endEt.setError(null);
        }





        return valid;
    }




//        Toast.makeText(getApplicationContext(),"OK2",Toast.LENGTH_LONG).show();
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlertReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
//
//        if (c.before(Calendar.getInstance())) {
//            c.add(Calendar.DATE, 1);
//            Toast.makeText(getApplicationContext(),"OK3"+Calendar.DATE,Toast.LENGTH_LONG).show();
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Toast.makeText(getApplicationContext(),"OK4"+c.getTimeInMillis(),Toast.LENGTH_LONG).show();
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
//        }
//    }



}
