package com.isunican.eventossantander.utils;


import com.isunican.eventossantander.model.Event;
import java.util.List;
import java.util.Set;

public interface ISharedPrefs {
    // Categorias
    Set<String> getSelectedCategories();
    void setSelectedCategories(Set<String> categorias);
    void clearCategories();

    // Palabras Clave
    List<String> getSelectedKeywords();
    void setSelectedKeywords(List<String> listKeywords);
    void clearAllKeywords();

    // Eventos
    List<Event> loadDataFromLocal ();
    void saveDataToLocal (List<Event> events);

    // Favoritos
    List<Integer> loadFavouritesId();
    void newFavouriteEvent(int id);
    void deleteFavouriteEvent(int id);
    boolean checkFavouriteById(int id);

}

