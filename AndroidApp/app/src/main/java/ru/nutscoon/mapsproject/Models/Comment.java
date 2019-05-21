package ru.nutscoon.mapsproject.Models;

public class Comment {
    public Comment(int userId, int organizationId, String text, double rate) {
        this.userId = userId;
        this.organizationId = organizationId;
        this.text = text;
        this.rate = rate;
    }

    private int userId;
    private int organizationId;
    private String text;
    private double rate;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
