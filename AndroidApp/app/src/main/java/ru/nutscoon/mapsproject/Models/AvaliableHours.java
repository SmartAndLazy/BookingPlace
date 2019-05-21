package ru.nutscoon.mapsproject.Models;

public class AvaliableHours {

    private int id;

    private String startTime;
    private String endTime;
    private  int summaryNumberOfPlace;
    private  Table[] bookingPlaceInfo;

    public int getId() {
        return id;
    }

    public String getstartTime() {
        return startTime;
    }

    public String getendTime() {
        return endTime;
    }

    public int getsummaryNumberOfPlace() {  return summaryNumberOfPlace;   }

    public   Table[] getbookingPlaceInfo() {
        return bookingPlaceInfo;
    }



}
