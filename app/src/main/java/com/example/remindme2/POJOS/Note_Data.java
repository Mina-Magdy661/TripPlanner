package com.example.remindme2.POJOS;

public class Note_Data {

    long Id;
    String title ;
    String content ;
    int tripId;


    public Note_Data(long id, String title, String content, int tripId)  {
        Id = id;
        this.title = title;
        this.content = content;
        this.tripId = tripId;
    }


    public Note_Data( String title, String content,int tripId) {
        this.title = title;
        this.content = content;

        this.tripId = tripId;
    }

    public Note_Data() {

    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
