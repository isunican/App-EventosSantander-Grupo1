package com.isunican.eventossantander.presenter.selectkeywords;


import com.isunican.eventossantander.utils.ISharedPrefs;
import com.isunican.eventossantander.view.selectkeywords.ISelectKeywordsContract;
import com.isunican.eventossantander.view.selectkeywords.SelectKeywordsActivity;

import java.text.Normalizer;
import java.util.List;


public class SelectKeywordsPresenter implements ISelectKeywordsContract.Presenter {

    private final ISelectKeywordsContract.View view;
    private final ISharedPrefs sharedPrefs;
    private List<String> keywordsList;
    private static final String ASCII = "[^\\p{ASCII}]";

    public SelectKeywordsPresenter(SelectKeywordsActivity view, ISharedPrefs sharedPrefs) {
        this.view = view;
        this.sharedPrefs = sharedPrefs;
        onLoadKewordsListFromLocal();
    }

    @Override
    public void onLoadKewordsListFromLocal() {
        keywordsList= sharedPrefs.getSelectedKeywords();
        view.onKeywordsLoaded(keywordsList);
    }

    @Override
    public void onReloadKeywords(){
        onLoadKewordsListFromLocal();
    }


    @Override
    public void onDeleteKeyword(int keywordIndex) {
        if (keywordsList != null && keywordIndex < keywordsList.size()) {
            String keyword = keywordsList.get(keywordIndex);
            // Se comprueba que la palabra a borrar exista
            if(keywordExists(keyword)){
                keywordsList.remove(keywordIndex);
                view.onDeletedKeywordSuccess(keyword);
                view.onKeywordsLoaded(keywordsList);
            }
        } else {
            view.onDeleteKeywordFail();
        }
    }

    /**
     * Metodo que añade una palabra clave a la lista local de palabras
     * @param keyword palabra a añadir a la lista
     */
    @Override
    public void onAddKeyword(String keyword) {
        if(keyword.equals("")){
            return;
        }
        if (keyword.contains(" ")) {
            view.onSpaceOnKeyWordExists();
            return;
        }
        if (!keywordExists(keyword)) {
            keywordsList.add(0, keyword.toUpperCase());
            view.onKeywordsLoaded(keywordsList);
            view.onAddedKeywordSuccess(keyword);
        }
        else view.onAddKeywordExists(keyword);
    }

    /**
     * Metodo que elimina todas las palabras clave y
     */
    @Override
    public void onClearSelectKeywordsFilter() {
        keywordsList.clear();
        sharedPrefs.clearAllKeywords();
    }

    @Override
    public void onAplicarSelectKewords() {
        sharedPrefs.setSelectedKeywords(keywordsList);
    }

    public List<String> getKeywordsList(){
        return keywordsList;
    }

    /**
     * Metodo interno para comprobar si una palabra ya existe en la lista
     * @param newKeyword palabra clave a anadir a la lista
     * @return true si existe en la lista
     *         false si no
     */
    private boolean keywordExists(String newKeyword){
        String kwListaNormaliz;
        // Se normaliza el String nuevo
        String newKWNormaliz = Normalizer.normalize(newKeyword, Normalizer.Form.NFD);
        newKWNormaliz = newKWNormaliz.toLowerCase().replaceAll(ASCII, "");

        for (String keyword: keywordsList) {
            kwListaNormaliz = Normalizer.normalize(keyword, Normalizer.Form.NFD);
            kwListaNormaliz = kwListaNormaliz.toLowerCase().replaceAll(ASCII, "");
            if(kwListaNormaliz.equals(newKWNormaliz)) return true;
        }
        return false;
    }

}
