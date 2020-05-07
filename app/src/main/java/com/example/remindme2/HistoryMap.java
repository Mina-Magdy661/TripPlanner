package com.example.remindme2;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;

import com.ahmadrosid.lib.drawroutemap.DrawRouteMaps;
import com.example.remindme2.Database.DatabaseAdapter;
import com.example.remindme2.POJOS.TripJC;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class HistoryMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseAdapter db;
    ArrayList<TripJC> historyTrips;
    private double startLat,startLong,endLat,endLong;
    private MarkerOptions originMarkerOption, desMarkerOption;
    private Marker originMarker,destMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        db=new DatabaseAdapter(getApplicationContext());
        historyTrips=new ArrayList<>();
        historyTrips=db.getTripsUpOnStatus("history", FirebaseAuth.getInstance().getUid());




        mapFragment.getMapAsync(this);








    }

    @Override
    protected void onStart() {
        super.onStart();

        db=new DatabaseAdapter(getApplicationContext());
        historyTrips=new ArrayList<>();
        historyTrips=db.getTripsUpOnStatus("history", FirebaseAuth.getInstance().getUid());




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (historyTrips != null) {
            DrawRouteMaps drawRouteMaps = DrawRouteMaps.getInstance(HistoryMap.this, "AIzaSyCiCvlY7AyLARQx3_M85nJm1jZPJNlfT3M");
            LatLngBounds bounds = null;
            for (int i = 0; i < historyTrips.size(); i++) {
                startLat = historyTrips.get(i).getStartLatitude();
                startLong = historyTrips.get(i).getStartLongitude();
                endLat = historyTrips.get(i).getEndLatitude();
                endLong = historyTrips.get(i).getEndLongitude();
                final String Name = historyTrips.get(i).getName();
                LatLng origin = new LatLng(startLat, startLong);
                LatLng destination = new LatLng(endLat, endLong);
                originMarkerOption = new MarkerOptions().position(new LatLng(startLat, startLong)).title(historyTrips.get(i).getStartPoint()+"("+Name+")");
                desMarkerOption = new MarkerOptions().position(new LatLng(endLat, endLong)).title(historyTrips.get(i).getEndPoint()+"("+Name+")");
                originMarker = mMap.addMarker(originMarkerOption);
                originMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                destMarker = mMap.addMarker(desMarkerOption);
                destMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                drawRouteMaps.draw(origin, destination, mMap);
                bounds = bounds.builder()
                        .include(origin)
                        .include(destination).build();
                Point displaySize = new Point();
                getWindowManager().getDefaultDisplay().getSize(displaySize);
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 150, 30));

            }
        }













    }
}
