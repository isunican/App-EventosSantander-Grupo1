package com.isunican.eventossantander.view.eventsdetail;

import com.isunican.eventossantander.model.Event;

import java.util.List;

public interface IEventsDetailContract {

    public interface Presenter {

        void onFavouriteEventsClicked(Event event);
    }

    public interface View {

        void onFavouriteEventsView();
    }
}
