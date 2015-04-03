package com.getmebag.bag.androidspecific.prefs;

import android.content.SharedPreferences;

import com.google.gson.Gson;

public class CustomObjectPreference<T> {
    private final SharedPreferences preferences;
    private final String key;
    private final T defaultValue;

    public CustomObjectPreference(SharedPreferences preferences, String key) {
        this(preferences, key, null);
    }

    public CustomObjectPreference(SharedPreferences preferences, String key, T defaultValue) {
        this.preferences = preferences;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public Object get(Class c) {
        Gson gson = new Gson();
        String json = preferences.getString(key, null);
        return (gson.fromJson(json, c));
    }

    public boolean isSet() {
        return preferences.contains(key);
    }

    public void set(T value) {
        Gson gson = new Gson();
        String json = gson.toJson(value);
        preferences.edit().putString(key, json).apply();
    }

    public void delete() {
        preferences.edit().remove(key).apply();
    }
}
