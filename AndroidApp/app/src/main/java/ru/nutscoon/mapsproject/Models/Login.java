package ru.nutscoon.mapsproject.Models;

public class Login {

    public Login(String phone, String passwordHash) {
        this.phone = phone;
        this.passwordHash = passwordHash;
    }

    private String phone;
    private String passwordHash;
}
