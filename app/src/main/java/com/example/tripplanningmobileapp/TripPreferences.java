package com.example.tripplanningmobileapp;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
public class TripPreferences {
    private static final String PREF_NAME = "TripPreferences";
    private static final String KEY_TRIPS = "trips";
    private static final String KEY_NEXT_ID = "next_id";
    private SharedPreferences sharedPreferences;
    private Gson gson;
    public TripPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }
    public void saveTrips(ArrayList<Trip> trips) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(trips);
        editor.putString(KEY_TRIPS, json);
        editor.apply();
    }
    public ArrayList<Trip> loadTrips() {
        String json = sharedPreferences.getString(KEY_TRIPS, null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<Trip>>() {}.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>();
    }
    public void saveNextId(int nextId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_NEXT_ID, nextId);
        editor.apply();
    }
    public int loadNextId() {
        return sharedPreferences.getInt(KEY_NEXT_ID, 1);
    }
    public void clearAll() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}