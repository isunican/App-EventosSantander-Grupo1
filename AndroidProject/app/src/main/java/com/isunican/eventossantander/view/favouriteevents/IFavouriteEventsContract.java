package com.isunican.eventossantander.view.favouriteevents;

import com.isunican.eventossantander.model.Event;

import java.util.List;

public interface IFavouriteEventsContract {

    public interface Presenter {

        void onEventClicked(int eventIndex);

        void onLoadFavouriteEvents();

        void onReloadFavourites();

    }

    public interface View {

        void onEventsLoaded(List<Event> events);

        void onLoadError();

        void onLoadSuccess(int elementsLoaded);

        void openEventDetails(Event event);

    }
}
