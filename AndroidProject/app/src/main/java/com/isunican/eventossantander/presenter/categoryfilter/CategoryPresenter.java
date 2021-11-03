package com.isunican.eventossantander.presenter.categoryfilter;

import com.isunican.eventossantander.R;
import com.isunican.eventossantander.utils.ISharedPrefs;
import com.isunican.eventossantander.view.categoryfilter.CategoryFilterActivity;
import com.isunican.eventossantander.view.categoryfilter.ICategoryContract;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryPresenter implements ICategoryContract.Presenter {

    private ICategoryContract.View view;
    private ISharedPrefs sharedPrefs;

    public CategoryPresenter(CategoryFilterActivity categoryFilterActivity, ISharedPrefs sharedPrefs) {
        view = categoryFilterActivity;
        this.sharedPrefs = sharedPrefs;
    }

    @Override
    public void onAplicarFiltroCategoria(Set<String> categorias) {
        sharedPrefs.setSelectedCategories(categorias);
    }

    @Override
    public boolean getEstadoCheckBoxes(String checkBox){
        Set<String> categorias = sharedPrefs.getSelectedCategories();
        if (categorias != null && categorias.contains(checkBox)) {
            return true;
        }
        return false;
    }
}
