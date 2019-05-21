package ru.nutscoon.mapsproject.Services;

import ru.nutscoon.mapsproject.Models.User;

public interface IUserService {
    boolean isUserLogin();
    User getUser();
    void setUserLoggedIn(User user);
}
