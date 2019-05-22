package ru.nutscoon.mapsproject.Models;


public class BookingInformation {

    public BookingInformation(int orgId, String date, String time, String clientName, String clientSurname, String clientPhone) {
        this.organizationId = orgId;
        this.date = date;
        this.fromTime = time;
        this.name = clientName;
        this.surname = clientSurname;
        this.phone = clientPhone;
        this.numberOfTables = 1;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public String getDate() {
        return date;
    }

    public String getFromTime() {
        return fromTime;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhone() {
        return phone;
    }

    public int getNumberOfTables() {
        return numberOfTables;
    }

    private int organizationId;
    private String date;
    private String fromTime;
    private String name;
    private String surname;
    private String phone;
    private int numberOfTables;
}
