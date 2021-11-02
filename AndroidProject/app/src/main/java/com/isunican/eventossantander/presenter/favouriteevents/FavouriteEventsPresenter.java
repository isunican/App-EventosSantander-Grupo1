package com.isunican.eventossantander.presenter.favouriteevents;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.utils.ISharedPrefs;
import com.isunican.eventossantander.view.favouriteevents.FavouriteEventsActivity;
import com.isunican.eventossantander.view.favouriteevents.IFavouriteEventsContract;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavouriteEventsPresenter implements IFavouriteEventsContract.Presenter {

    private List<Event> favouriteEvents;
    private IFavouriteEventsContract.View view;

    private ISharedPrefs sharedPrefs;

    public FavouriteEventsPresenter(FavouriteEventsActivity view, ISharedPrefs sharedPrefs) {
        this.view = view;
        this.sharedPrefs = sharedPrefs;
        onLoadFavouriteEvents();
    }

    @Override
    public void onLoadFavouriteEvents() {

        Map<Integer, Event> totalEvents = createMapFromList(sharedPrefs.loadDataFromLocal());
        List<Integer> idsFav = sharedPrefs.loadFavouritesId();

        favouriteEvents = new ArrayList<>();

        for (Integer id: idsFav) {
            if (totalEvents.containsKey(id)) {
                favouriteEvents.add(totalEvents.get(id));
            }
        }

        view.onEventsLoaded(favouriteEvents);
        view.onLoadSuccess(favouriteEvents.size());

    }

    @Override
    public void onEventClicked(int eventIndex) {
        if (favouriteEvents != null && eventIndex < favouriteEvents.size()) {
            Event event = favouriteEvents.get(eventIndex);
            view.openEventDetails(event);
        }
    }

    private Map<Integer, Event> createMapFromList(List<Event> list){
        Map<Integer, Event> mapa = new HashMap<>();
        for (Event e: list) {
            mapa.put(e.getIdentificador(), e);
        }
        return mapa;
    }

    @Override
    public void onReloadFavourites(){
        onLoadFavouriteEvents();
    }
}
