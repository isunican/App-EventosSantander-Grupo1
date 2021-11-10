package com.isunican.eventossantander.presenter.selectkeywords;

import com.isunican.eventossantander.utils.ISharedPrefs;
import com.isunican.eventossantander.view.selectkeywords.ISelectKeywordsContract;
import com.isunican.eventossantander.view.selectkeywords.SelectKeywordsActivity;

import java.util.Set;


public class SelectKeywordsPresenter implements ISelectKeywordsContract.Presenter {

    private ISelectKeywordsContract.View view;
    private ISharedPrefs sharedPrefs;

    public SelectKeywordsPresenter(SelectKeywordsActivity selectKeywordsActivity, ISharedPrefs sharedPrefs) {
        view = selectKeywordsActivity;
        this.sharedPrefs = sharedPrefs;
    }

    // TODO:
    private boolean checkKewyord(String keyword) {
        return false;
    }

    @Override
    public void onAplicarSelectKewords(Set<String> palabrasClave) {

    }
}
