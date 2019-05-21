package ru.nutscoon.mapsproject.Models;

import java.util.Date;

public class BookingInformation {

    public BookingInformation(int orgId, Date date, String time, String clientName, String clientSurname, String clientPhone) {
        this.orgId = orgId;
        this.date = date;
        this.time = time;
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        this.clientPhone = clientPhone;
    }

    private int orgId;
    private Date date;
    private String time;
    private String clientName;
    private String clientSurname;
    private String clientPhone;
}
