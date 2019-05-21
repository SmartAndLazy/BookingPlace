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
    public <T> T getValue(String key, Class<T> type) {
        Log.e("!INFORMATION ABOUT JSON", key);
        String json = sPref.getString(key, null);
       // Log.e("!INFORMATION ABOUT JSON", json);

        if(json == null){
            return null;
        }
        Log.e("!INFORMATION ABOUT JSON", json);
        return  gson.fromJson(json, type);
    }
}
