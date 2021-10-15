package com.isunican.eventossantander.presenter.events;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.isunican.eventossantander.R;
import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.view.Listener;
import com.isunican.eventossantander.view.events.IEventsContract;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class EventsPresenter implements IEventsContract.Presenter {

    private final IEventsContract.View view;
    private List<Event> cachedEvents;

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
            }

            @Override
            public void onFailure() {
                view.onLoadError();
                cachedEvents = null;
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
        view.onEventsLoaded(cachedEvents);
        view.onLoadSuccess(cachedEvents.size(), false);
    }

    @Override
    public void onInfoClicked() {
        view.openInfoView();
    }

    @Override
    public void filtrarPorPalabrasClave(String busqueda) {
        List<Event> eventosFiltrados = new ArrayList<Event>();
        busqueda = Normalizer.normalize(busqueda, Normalizer.Form.NFD);
        busqueda = busqueda.replaceAll("[^\\p{ASCII}]", ""); // Para las tildes
        for (Event e: cachedEvents) {
            if (e.getNombre().toLowerCase().contains(busqueda) || e.getDescripcion().toLowerCase().contains(busqueda) || e.getCategoria().toLowerCase().contains(busqueda) ||
                    e.getNombreAlternativo().toLowerCase().contains(busqueda) || e.getDescripcionAlternativa().toLowerCase().contains(busqueda)) {
                eventosFiltrados.add(e);
            }
        }
        view.onEventsLoaded(eventosFiltrados);
        view.onLoadSuccess(eventosFiltrados.size(), true);
    }
}
