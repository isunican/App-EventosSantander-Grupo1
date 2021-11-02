package com.isunican.eventossantander.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;
import java.util.Set;

public interface ISharedPrefs {

    Set<String> getSelectedCategories();

    void setSelectedCategories(Set<String> categorias);

    void clearCategories();

}
