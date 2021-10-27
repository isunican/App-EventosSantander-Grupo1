package com.isunican.eventossantander.presenter.eventsdetail;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.view.events.IEventsContract;
import com.isunican.eventossantander.view.eventsdetail.EventsDetailActivity;
import com.isunican.eventossantander.view.eventsdetail.IEventsDetailContract;
import com.isunican.eventossantander.view.favouriteevents.FavouriteEventsActivity;

import java.util.List;
import java.util.Map;

public class EventsDetailPresenter implements IEventsDetailContract.Presenter {

    private static final String KEY_FAVORITOS = "FAVORITOS";

    Context context;

    private Map<Event, String> favouriteEvents;
    private List<Event> favEventsById;

    private IEventsDetailContract.View view;

    private SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public EventsDetailPresenter(EventsDetailActivity view) {
        this.view = view;
        context = view.getApplicationContext();
        sharedPref = context.getSharedPreferences("KEY_FAVORITOS", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        loadFavouriteFromLocal();
    }

    @Override
    public void onFavouriteEventsClicked(Event event) {

    }

    public void loadFavouriteFromLocal(){
        String idLocal = sharedPref.getString(KEY_FAVORITOS, "");
        if (!idLocal.equals("")) {

        }
    }

}
