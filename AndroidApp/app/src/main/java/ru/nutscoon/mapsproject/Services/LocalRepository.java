package ru.nutscoon.mapsproject.Services;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

public class LocalRepository implements ILocalRepository {

    private SharedPreferences sPref;
    private Gson gson;

    public LocalRepository(SharedPreferences sharedPreferences){
        sPref = sharedPreferences;
        gson = new Gson();
    }

    @Override
    public <T> void saveValue(String key, T value) {
        String json = gson.toJson(value);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(key, json);
        editor.apply();
    }

    @Override
    public void remove(String key){
        SharedPreferences.Editor editor = sPref.edit();
        editor.remove(key);
        editor.apply();
    }

    @Override
    public <T> T getValue(String key, Class<T> type) {
        String json = sPref.getString(key, null);

        if(json == null){
            return null;
        }
        return  gson.fromJson(json, type);
    }
}
