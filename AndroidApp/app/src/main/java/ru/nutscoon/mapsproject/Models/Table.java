package ru.nutscoon.mapsproject.Models;

public class Table {

    private String from;
    private String to;
    private int numberOfBuzyPlacement;
    private  int numberOfFreePlacement;


    public String getfromHours() {
        return from;
    }

    public String gettoHours() {
        return to;
    }

    public  int getCountFree(){return  numberOfBuzyPlacement;}

    public  int getCountBusy(){return  numberOfFreePlacement;}

}
