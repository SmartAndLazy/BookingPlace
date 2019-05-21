package ru.nutscoon.mapsproject;

import android.app.Application;

import ru.nutscoon.mapsproject.di.ApplicationComponent;
import ru.nutscoon.mapsproject.di.ApplicationModule;
import ru.nutscoon.mapsproject.di.DaggerApplicationComponent;

public class App extends Application {

    public static ApplicationComponent getComponent() {
        return component;
    }

    private static ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        initDagger();
    }

    private void initDagger(){
        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }
}
