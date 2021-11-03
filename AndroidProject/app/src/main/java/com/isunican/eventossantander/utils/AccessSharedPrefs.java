package com.isunican.eventossantander.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class AccessSharedPrefs implements ISharedPrefs {

    private static final String KEY_CATEGORIAS = "CATEGORIAS";

    private final SharedPreferences sharedPref;
    private final SharedPreferences.Editor editor;

    public AccessSharedPrefs(Context c){
        sharedPref = c.getSharedPreferences(KEY_CATEGORIAS, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    @Override
    public Set<String> getSelectedCategories() {
        return sharedPref.getStringSet(KEY_CATEGORIAS, null);
    }

    @Override
    public void setSelectedCategories(Set<String> categorias) {
        editor.clear();
        editor.putStringSet(KEY_CATEGORIAS, categorias);
        editor.apply();
    }

    @Override
    public void clearCategories() {
        editor.clear();
        editor.apply();
    }

}
