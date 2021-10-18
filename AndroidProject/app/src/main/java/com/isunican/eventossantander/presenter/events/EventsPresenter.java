package com.isunican.eventossantander.presenter.events;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.view.Listener;
import com.isunican.eventossantander.view.events.IEventsContract;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class EventsPresenter implements IEventsContract.Presenter {

    private final IEventsContract.View view;
    private List<Event> cachedEvents;
    private List<Event> copyAllEvents;

    public EventsPresenter(IEventsContract.View view) {
        this.view = view;
        configItems();
        loadData(true);
    }

    public void configItems() {
        view.onLoadingItems();
    }

    private void loadData(boolean showMessage) {

        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null) {
            view.onInternetConnectionFailure();
            return;
        }

        EventsRepository.getEvents(new Listener<List<Event>>() {
            @Override
            public void onSuccess(List<Event> data) {
                view.onEventsLoaded(data);
                view.onLoadSuccess(data.size(), showMessage);
                cachedEvents = data;
                copyAllEvents = data;
            }

            @Override
            public void onFailure() {
                view.onLoadError();
                cachedEvents = null;
                copyAllEvents = null;
            }
        });
    }

    @Override
    public void onEventClicked(int eventIndex) {
        if (cachedEvents != null && eventIndex < cachedEvents.size()) {
            Event event = cachedEvents.get(eventIndex);
            view.openEventDetails(event);
        }
    }

    @Override
    public void onReloadClicked(boolean showMessage) {
        loadData(showMessage);
    }

    @Override
    public void onReloadCachedEventsClicked() {
        cachedEvents = copyAllEvents;
        view.onEventsLoaded(copyAllEvents);
        view.onLoadSuccess(copyAllEvents.size(), false);
    }

    @Override
    public void onInfoClicked() {
        view.openInfoView();
    }

    @Override
    public void onKeywordsFilter(String search) {
        List<Event> eventosFiltrados = new ArrayList<>();
        search = Normalizer.normalize(search, Normalizer.Form.NFD);
        search = search.replaceAll("[^\\p{ASCII}]", ""); // Para las tildes
        for (Event e: copyAllEvents) {
            if (e.getNombre().toLowerCase().contains(search.toLowerCase()) || e.getDescripcion().toLowerCase().contains(search.toLowerCase()) || e.getCategoria().toLowerCase().contains(search.toLowerCase()) ||
                    e.getNombreAlternativo().toLowerCase().contains(search.toLowerCase()) || e.getDescripcionAlternativa().toLowerCase().contains(search.toLowerCase())) {
                eventosFiltrados.add(e);
            }
        }
        cachedEvents = eventosFiltrados;
        view.onEventsLoaded(eventosFiltrados);
        view.onLoadSuccess(eventosFiltrados.size(), true);
    }

    public List<Event> getCachedEvents() {
        return cachedEvents;
    }
}
