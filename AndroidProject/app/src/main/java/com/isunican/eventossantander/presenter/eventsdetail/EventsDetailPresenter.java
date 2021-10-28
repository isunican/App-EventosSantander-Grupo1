package com.isunican.eventossantander.presenter.eventsdetail;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.utils.LocalEvents;
import com.isunican.eventossantander.view.events.IEventsContract;
import com.isunican.eventossantander.view.eventsdetail.EventsDetailActivity;
import com.isunican.eventossantander.view.eventsdetail.IEventsDetailContract;
import com.isunican.eventossantander.view.favouriteevents.FavouriteEventsActivity;

import java.util.List;
import java.util.Map;

public class EventsDetailPresenter implements IEventsDetailContract.Presenter {

    private static final String KEY_FAVORITOS = "FAVORITOS";

    private Context context;

    private Map<Event, String> favouriteEvents;
    private List<Event> favEventsById;

    private IEventsDetailContract.View view;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public EventsDetailPresenter(EventsDetailActivity view) {
        this.view = view;
        context = view.getApplicationContext();
    }

    @Override
    public void onFavouriteEventsClicked(Event event, boolean eliminar) {
        if (eliminar){
            LocalEvents.deleteFavouriteEvent(context, event.getIdentificador());
        }else{
            LocalEvents.newFavouriteEvent(context, event.getIdentificador());
        }
    }

}
