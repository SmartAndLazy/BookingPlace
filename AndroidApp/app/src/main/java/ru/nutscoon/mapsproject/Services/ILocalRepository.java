package ru.nutscoon.mapsproject.Services;

public interface ILocalRepository {
    <T> void saveValue(String key, T value);
    <T> T getValue(String key, Class<T> type);
    void remove(String key);
}
