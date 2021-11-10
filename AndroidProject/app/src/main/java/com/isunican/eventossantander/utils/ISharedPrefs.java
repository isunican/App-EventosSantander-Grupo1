package com.isunican.eventossantander.utils;


import com.isunican.eventossantander.model.Event;
import java.util.List;
import java.util.Set;

public interface ISharedPrefs {

    Set<String> getSelectedCategories();

    void setSelectedCategories(Set<String> categorias);

    void clearCategories();

    List<String> getSelectedKeywords();
    void newKeyword(String keyword);
    void deleteKeyword(String keyword);
    void clearSelectedKeywords();



    List<Event> loadDataFromLocal ();

    void saveDataToLocal (List<Event> events);

    List<Integer> loadFavouritesId();

    void newFavouriteEvent(int id);

    void deleteFavouriteEvent(int id);

    boolean checkFavouriteById(int id);

}

