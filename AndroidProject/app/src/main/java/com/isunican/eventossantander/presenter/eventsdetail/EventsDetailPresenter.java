package com.isunican.eventossantander.presenter.eventsdetail;

import android.content.Context;
import android.content.SharedPreferences;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.utils.LocalEvents;
import com.isunican.eventossantander.view.eventsdetail.EventsDetailActivity;
import com.isunican.eventossantander.view.eventsdetail.IEventsDetailContract;

import java.util.List;
import java.util.Map;

public class EventsDetailPresenter implements IEventsDetailContract.Presenter {

    private Context context;
    private IEventsDetailContract.View view;

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
