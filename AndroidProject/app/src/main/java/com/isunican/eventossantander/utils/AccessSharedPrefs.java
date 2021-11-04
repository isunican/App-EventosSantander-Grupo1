package com.isunican.eventossantander.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.isunican.eventossantander.model.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AccessSharedPrefs implements ISharedPrefs{

    private static final String KEY_CATEGORIAS = "CATEGORIAS";
    private static final String KEY_LOCAL_EVENTS = "ALL_LOCAL_EVENTS";
    private static final String NUM_EVENTS = "NUM_EVENTS";
    private static final String KEY_FAVOURITE_EVENTS = "ALL_FAVOURITE_EVENTS";

    private Context c;

    public AccessSharedPrefs(Context c){
        this.c = c;
    }

    @Override
    public Set<String> getSelectedCategories() {
        SharedPreferences sharedPref = c.getSharedPreferences(KEY_CATEGORIAS, Context.MODE_PRIVATE);
        Set<String> categorias = sharedPref.getStringSet(KEY_CATEGORIAS, null);
        return categorias;
    }

    @Override
    public void setSelectedCategories(Set<String> categorias) {
        SharedPreferences sharedPref = c.getSharedPreferences(KEY_CATEGORIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.putStringSet(KEY_CATEGORIAS, categorias);
        editor.apply();
    }

    @Override
    public void clearCategories() {
        SharedPreferences sharedPref = c.getSharedPreferences(KEY_CATEGORIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    public List<Event> loadDataFromLocal () {
        SharedPreferences sharedPref = c.getSharedPreferences(KEY_LOCAL_EVENTS, Context.MODE_PRIVATE);
        int numEvents = sharedPref.getInt(NUM_EVENTS, 0);
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < numEvents; i++) {
            events.add(Event.fromJSON(sharedPref.getString("evento" + i, null)));
        }
        return events;
    }

    @Override
    public void saveDataToLocal (List<Event> events) {
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

    @Override
    public List<Integer> loadFavouritesId() {
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
        Collections.reverse(events);
        return events;
    }

    @Override
    public void newFavouriteEvent(int id) {
        if (checkFavouriteById(id)) return;
        if (checkIfEventyExistById(id)) return;
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

    @Override
    public void deleteFavouriteEvent(int id) {
        SharedPreferences sharedPref = c.getSharedPreferences(KEY_FAVOURITE_EVENTS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String str = sharedPref.getString(KEY_FAVOURITE_EVENTS, null);
        if (str != null) {
            String[] ids = str.split(";");
            StringBuilder newStr = new StringBuilder();
            for (String i: ids) {
                if (!i.equals(String.valueOf(id)) && !i.isEmpty()) {
                    newStr.append(";").append(i);
                }
            }
            editor.clear();
            editor.putString(KEY_FAVOURITE_EVENTS, newStr.toString());
            editor.apply();
        }
    }

    @Override
    public boolean checkFavouriteById(int id) {
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

    private boolean checkIfEventyExistById(int id){
        List<Event> eventos = loadDataFromLocal();
        for (Event e: eventos) {
            if (e.getIdentificador() == id){
                return true;
            }
        }
        return false;
    }

}