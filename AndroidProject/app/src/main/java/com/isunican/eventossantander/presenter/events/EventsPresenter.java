package com.isunican.eventossantander.presenter.events;

import android.annotation.SuppressLint;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.view.Listener;
import com.isunican.eventossantander.view.events.IEventsContract;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    //Tiene que ser publico por los test. No cambiar a private aunque lo diga el sonar
    public void loadData(boolean showMessage) {

        if (!view.hasInternetConnection()) {
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

    @SuppressLint("NewApi")
    @Override
    public void onKeywordsFilter(final String search, boolean showMsg, boolean searchInCached) {
        List<Event> eventosFiltrados = new ArrayList<>();
        List<Event> eventList;
        if (searchInCached) {
            eventList = cachedEvents;
        } else {
            eventList = copyAllEvents;
        }

        Arrays.stream(search.split(" ")).anyMatch(item -> search.contains(item));

        List<String> substrings = Arrays.asList(search.split(" "));
        for (Event e: eventList) {
            if (substrings.stream().allMatch(substring -> eventToStringMap.get(e).contains(substring))) {
                eventosFiltrados.add(e);
            }
        }

        cachedEvents = eventosFiltrados;
        view.onEventsLoaded(eventosFiltrados);
        view.onLoadSuccess(eventosFiltrados.size(), showMsg);
    }
    /**
     * Getter de la variable cachedEvents para poder ejecutar las pruebas unitarias.
     */
    public List<Event> getCachedEvents() {
        return cachedEvents;
    }
}
