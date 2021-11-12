package com.isunican.eventossantander.view.selectkeywords;

import java.util.List;

public interface ISelectKeywordsContract {

    interface Presenter {
        void onAplicarSelectKewords();
        void onLoadKewordsListFromLocal();
        void onDeleteKeyword(int keywordIndex);
        void onAddKeyword(String keyword);
        void onClearSelectKeywordsFilter();
        List<String> getKeywordsList();
        void onReloadKeywords();
    }

    interface View {
        void onKeywordsLoaded(List<String> keywords);
        void onAddKeywordToList();
        void onAddedKeywordSuccess(String keyword);
        void onDeletedKeywordSuccess(String keyword);
        void onAddKeywordExists(String keyword);
        void onDeleteKeywordFail();
        void onClearSelectKeywordsClicked();
        void onSpaceOnKeyWordExists();
    }
}
