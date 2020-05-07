package com.example.remindme2.POJOS;

import java.io.Serializable;
import java.util.ArrayList;

public class TripJC implements Serializable {

    private int id ;
    private String Name ;
    private String StartPoint ;
    private String EndPoint ;
    private String date ;
    private String time ;
    private String type ;
    private double startLongitude ;
    private double startLatitude;
    private double endLongitude ;
    private double  endLatitude ;
    private String status ;
    private String userId;
    private  String tripFId=null;

    public String getTripFId() {
        return tripFId;
    }

    public void setTripFId(String tripFId) {
        this.tripFId = tripFId;
    }


    private ArrayList<Note_Data> noteList=new ArrayList<>();

    public ArrayList<Note_Data> getNoteList() {
        return noteList;
    }

    public void setNoteList(ArrayList<Note_Data> noteList) {
        this.noteList = noteList;
    }

    public TripJC(String name, String startPoint, String endPoint, String date, String time, String type, double startLongitude, double startLatitude, double endLongitude, double endLatitude, String status, String userId, ArrayList<Note_Data>noteList) {
        // this.id = id;
        Name = name;
        StartPoint = startPoint;
        EndPoint = endPoint;
        this.date = date;
        this.time = time;
        this.type = type;

        this.startLongitude = startLongitude;
        this.startLatitude = startLatitude;
        this.endLongitude = endLongitude;
        this.endLatitude = endLatitude;
        this.status=status;
        this.userId = userId;
        this.noteList=noteList;
    }




    public TripJC(){}


    public TripJC( String name, String startPoint, String endPoint, String date, String time, String type, double startLongitude, double startLatitude, double endLongitude, double endLatitude,String status, String userId) {
       // this.id = id;
        Name = name;
        StartPoint = startPoint;
        EndPoint = endPoint;
        this.date = date;
        this.time = time;
        this.type = type;

        this.startLongitude = startLongitude;
        this.startLatitude = startLatitude;
        this.endLongitude = endLongitude;
        this.endLatitude = endLatitude;
        this.status=status;
        this.userId = userId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStartPoint() {
        return StartPoint;
    }

    public void setStartPoint(String startPoint) {
        StartPoint = startPoint;
    }

    public String getEndPoint() {
        return EndPoint;
    }

    public void setEndPoint(String endPoint) {
        EndPoint = endPoint;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public double getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(double endLongitude) {
        this.endLongitude = endLongitude;
    }

    public double getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(double endLatitude) {
        this.endLatitude = endLatitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
