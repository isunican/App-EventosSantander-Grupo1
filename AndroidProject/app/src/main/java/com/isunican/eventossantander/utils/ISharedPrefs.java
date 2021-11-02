package com.isunican.eventossantander.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.isunican.eventossantander.model.Event;

import java.util.List;
import java.util.Set;

public interface ISharedPrefs {

    Set<String> getSelectedCategories();

    void setSelectedCategories(Set<String> categorias);

    void clearCategories();

    List<Event> loadDataFromLocal ();

    void saveDataToLocal (List<Event> events);

    List<Integer> loadFavouritesId();

    void newFavouriteEvent(int id);

    void deleteFavouriteEvent(int id);

    boolean checkFavouriteById(int id);

}