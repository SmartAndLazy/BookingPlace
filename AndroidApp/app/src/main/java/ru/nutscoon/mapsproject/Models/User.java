package ru.nutscoon.mapsproject.Models;

public class User {
    private int id;
    private String passwordHash;
    private String name;
    private String surname;
    private String phone;

    public User(String name, String surname, String phone, String passwordHash) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.passwordHash = passwordHash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
