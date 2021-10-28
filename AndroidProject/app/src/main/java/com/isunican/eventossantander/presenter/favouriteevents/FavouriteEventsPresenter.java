package com.isunican.eventossantander.presenter.favouriteevents;

import android.content.Context;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.utils.LocalEvents;
import com.isunican.eventossantander.view.favouriteevents.FavouriteEventsActivity;
import com.isunican.eventossantander.view.favouriteevents.IFavouriteEventsContract;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavouriteEventsPresenter implements IFavouriteEventsContract.Presenter {

    private List<Event> favouriteEvents;
    private IFavouriteEventsContract.View view;

    private Context context;

    public FavouriteEventsPresenter(FavouriteEventsActivity view) {
        this.view = view;
        context = view.getApplicationContext();
        onLoadFavouriteEvents();
    }

    @Override
    public void onLoadFavouriteEvents() {

        Map<Integer, Event> totalEvents = createMapFromList(LocalEvents.loadDataFromLocal(context));
        List<Integer> idsFav = LocalEvents.loadFavouritesId(context);

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
