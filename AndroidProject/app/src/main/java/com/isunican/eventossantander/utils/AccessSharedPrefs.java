package com.isunican.eventossantander.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.isunican.eventossantander.model.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class AccessSharedPrefs implements ISharedPrefs{

    private static final String KEY_CATEGORIAS = "CATEGORIAS";
    private static final String KEY_LOCAL_EVENTS = "ALL_LOCAL_EVENTS";
    private static final String NUM_EVENTS = "NUM_EVENTS";
    private static final String KEY_FAVOURITE_EVENTS = "ALL_FAVOURITE_EVENTS";
    private static final String KEY_SELECT_KEYWORDS = "ALL_SELECT_KEYWORDS";


    private Context c;

    public AccessSharedPrefs(Context c){
        this.c = c;
    }

    // --------------------- Categorias -----------------------------
    @Override
    public Set<String> getSelectedCategories() {
        SharedPreferences sharedPref = c.getSharedPreferences(KEY_CATEGORIAS, Context.MODE_PRIVATE);
        return sharedPref.getStringSet(KEY_CATEGORIAS, null);
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

    // --------------------- Palabras Clave -----------------------------
    @Override
    public List<String> getSelectedKeywords() {
        SharedPreferences sharedPref = c.getSharedPreferences(KEY_SELECT_KEYWORDS, Context.MODE_PRIVATE);
        List<String> keywordsList = new ArrayList<>();
        String stringKW = sharedPref.getString(KEY_SELECT_KEYWORDS, null);
        if (stringKW!=null) {
            String[] keywords = stringKW.split(";");
            for (String kw : keywords) {
                if (!kw.isEmpty()) {
                    keywordsList.add(kw);
                }
            }
        }
        return keywordsList;
    }

    @Override
    public void setSelectedKeywords(List<String> listKeywords) {
        SharedPreferences sharedPref = c.getSharedPreferences(KEY_SELECT_KEYWORDS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        String stringKW =";";
        for(String keyword: listKeywords) {
            if (keyword != null) {
                stringKW = stringKW.concat(keyword + ";");
            }
        }
        editor.putString(KEY_SELECT_KEYWORDS, stringKW);
        editor.apply();
    }

    @Override
    public void clearAllKeywords() {
        SharedPreferences sharedPref = c.getSharedPreferences(KEY_SELECT_KEYWORDS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }

    // --------------------- ListaEventos -----------------------------
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

    // --------------------- Favoritos -----------------------------

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
        // Si el evento ya está en favoritos, no se añade
        if (checkFavouriteById(id)) return;
        // El evento tiene que existir en la lista de eventos
        if (!checkIfEventyExistById(id)) return;
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