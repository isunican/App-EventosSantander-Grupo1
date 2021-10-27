package com.isunican.eventossantander.view.events;

import com.isunican.eventossantander.model.Event;

import java.util.List;

public interface IEventsContract {

    public interface Presenter {

        void onEventClicked(int eventIndex);

        void onReloadClicked(boolean showMessage);

        void onReloadCachedEventsClicked();

        void onInfoClicked();

        void onKeywordsFilter(String busqueda, boolean showMsg, boolean searchInCached);

        void onCategoryFilterClicked();
    }

    public interface View {

        void onEventsLoaded(List<Event> events);

        void onLoadError();

        void onLoadSuccess(int elementsLoaded, boolean showMessage);

        void openEventDetails(Event event);

        void openInfoView();

        void openCategoryFilterView();

        void onInternetConnectionFailure();

        boolean hasInternetConnection();

        void onLoadingItems();
    }
}
