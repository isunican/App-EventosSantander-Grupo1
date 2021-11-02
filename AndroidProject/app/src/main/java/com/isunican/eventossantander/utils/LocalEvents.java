package com.isunican.eventossantander.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.isunican.eventossantander.model.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalEvents {

    private static final String KEY_LOCAL_EVENTS = "ALL_LOCAL_EVENTS";
    private static final String NUM_EVENTS = "NUM_EVENTS";
    private static final String KEY_FAVOURITE_EVENTS = "ALL_FAVOURITE_EVENTS";

    public static List<Event> loadDataFromLocal (Context c) {
        SharedPreferences sharedPref = c.getSharedPreferences(KEY_LOCAL_EVENTS, Context.MODE_PRIVATE);
        int numEvents = sharedPref.getInt(NUM_EVENTS, 0);
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < numEvents; i++) {
            events.add(Event.fromJSON(sharedPref.getString("evento" + i, null)));
        }
        return events;
    }

    public static void saveDataToLocal (Context c, List<Event> events) {
        SharedPreferences sharedPref = c.getSharedPreferences(KEY_LOCAL_EVENTS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit(); // Borramos todos los datos locales
        editor.clear();
        int counter = 0;
        for (Event e: events) {
            editor.putString("evento" + counter, e.toJSON());
            counter++;
        }
        editor.putInt(NUM_EVENTS, counter);
        editor.apply();
    }

    public static List<Integer> loadFavouritesId(Context c) {
        SharedPreferences sharedPref = c.getSharedPreferences(KEY_FAVOURITE_EVENTS, Context.MODE_PRIVATE);
        List<Integer> events = new ArrayList<>();
        String str = sharedPref.getString(KEY_FAVOURITE_EVENTS, null);
        if (str!=null) {
            String[] ids = str.split(";");
            for (String id : ids) {
                if (!id.isEmpty()) {
                    events.add(Integer.parseInt(id));
                }
            }
        }
        Collections.sort(events, Collections.reverseOrder());
        return events;
    }

    public static void newFavouriteEvent(Context c, int id) {
        SharedPreferences sharedPref = c.getSharedPreferences(KEY_FAVOURITE_EVENTS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String ids = sharedPref.getString(KEY_FAVOURITE_EVENTS, null);
        if(ids == null){
            ids = "";
        }
        editor.clear();
        ids += ";" + id;
        editor.putString(KEY_FAVOURITE_EVENTS, ids);
        editor.apply();
    }

    public static void deleteFavouriteEvent(Context c, int id) {
        SharedPreferences sharedPref = c.getSharedPreferences(KEY_FAVOURITE_EVENTS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String str = sharedPref.getString(KEY_FAVOURITE_EVENTS, null);
        if (str != null) {
            String[] ids = str.split(";");
            String newStr = "";
            for (String i: ids) {
                if (!i.equals(String.valueOf(id)) && !i.isEmpty()) {
                    newStr += ";" + i;
                }
            }
            editor.clear();
            editor.putString(KEY_FAVOURITE_EVENTS, newStr);
            editor.apply();
        }
    }

    public static boolean checkFavouriteById(Context c, int id) {
        SharedPreferences sharedPref = c.getSharedPreferences(KEY_FAVOURITE_EVENTS, Context.MODE_PRIVATE);
        String str = sharedPref.getString(KEY_FAVOURITE_EVENTS, null);
        if (str!=null) {
            String[] ids = str.split(";");
            for (String i: ids) {
                if (i.equals(String.valueOf(id))) {
                    return true;
                }
            }
        }
        return false;
    }

}
