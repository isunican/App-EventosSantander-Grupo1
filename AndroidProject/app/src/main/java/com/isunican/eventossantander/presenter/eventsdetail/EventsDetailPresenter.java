package com.isunican.eventossantander.presenter.eventsdetail;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.utils.ISharedPrefs;
import com.isunican.eventossantander.view.eventsdetail.EventsDetailActivity;
import com.isunican.eventossantander.view.eventsdetail.IEventsDetailContract;

public class EventsDetailPresenter implements IEventsDetailContract.Presenter {

    private ISharedPrefs sharedPrefs;
    private IEventsDetailContract.View view;

    public EventsDetailPresenter(EventsDetailActivity view, ISharedPrefs sharedPrefs) {
        this.view = view;
        this.sharedPrefs = sharedPrefs;
    }

    @Override
    public void onFavouriteEventsClicked(Event event, boolean eliminar) {
        if (eliminar){
            sharedPrefs.deleteFavouriteEvent(event.getIdentificador());
        }else{
            sharedPrefs.newFavouriteEvent(event.getIdentificador());
        }
    }

}
