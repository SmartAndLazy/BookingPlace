package ru.nutscoon.mapsproject.Services;

import ru.nutscoon.mapsproject.Models.User;

public class UserService implements IUserService {
    @Override
    public boolean isUserLogin() {
        return false;
    }

    @Override
    public User getUser() {
        return null;
    }

    @Override
    public void setUserLoggedIn(User user) {

    }
}
