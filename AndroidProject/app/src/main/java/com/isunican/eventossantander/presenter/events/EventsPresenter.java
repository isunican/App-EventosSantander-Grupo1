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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventsPresenter implements IEventsContract.Presenter {

    private final IEventsContract.View view;
    private List<Event> cachedEvents;
    private List<Event> copyAllEvents;

    private Map<Event, String> eventToStringMap;

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
                initEventToStringMap(copyAllEvents);
            }

            @Override
            public void onFailure() {
                view.onLoadError();
                cachedEvents = null;
                copyAllEvents = null;
            }
        });
    }

    private void initEventToStringMap(List<Event> copyAllEvents) {
        eventToStringMap = new HashMap<>();
        for (Event e: copyAllEvents) {
            eventToStringMap.put(e, e.toString().toLowerCase());
        }
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
    public void onKeywordsFilter(String search, boolean showMsg, boolean searchInCached) {
        List<Event> eventosFiltrados = new ArrayList<>();
        search = Normalizer.normalize(search, Normalizer.Form.NFD);
        search = search.replaceAll("[^\\p{ASCII}]", ""); // Para las tildes
        Pattern p = Pattern.compile(search, Pattern.CASE_INSENSITIVE);
        Matcher m;
        List<Event> eventList;
        if (searchInCached) {
            eventList = cachedEvents;
        } else {
            eventList = copyAllEvents;
        }
        for (Event e: eventList) {
            m = p.matcher(eventToStringMap.get(e));
            if (m.find()) {
                eventosFiltrados.add(e);
            }
        }
        cachedEvents = eventosFiltrados;
        view.onEventsLoaded(eventosFiltrados);
        view.onLoadSuccess(eventosFiltrados.size(), showMsg);
    }

    public List<Event> getCachedEvents() {
        return cachedEvents;
    }
}
