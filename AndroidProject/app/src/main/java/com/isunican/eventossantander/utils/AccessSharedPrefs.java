package com.isunican.eventossantander.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AccessSharedPrefs implements ISharedPrefs{

    private static final String KEY_CATEGORIAS = "CATEGORIAS";

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

}
