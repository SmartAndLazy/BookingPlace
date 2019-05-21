package ru.nutscoon.mapsproject.Models;

public class UserRating {
    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public double getRate() {
        return rate;
    }

    public String getText() {
        return text;
    }

    public String getPhone() {
        return phone;
    }

    private int userId;
    private String name;
    private String surname;
    private double rate;
    private String text;
    private String phone;
}
