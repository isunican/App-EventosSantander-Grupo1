package com.isunican.eventossantander.presenter.events;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.utils.LocalEvents;
import com.isunican.eventossantander.view.Listener;
import com.isunican.eventossantander.view.events.IEventsContract;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventsPresenter implements IEventsContract.Presenter {

    private final IEventsContract.View view;
    private final Context context;
    private List<Event> cachedEvents;
    private List<Event> copyAllEvents;

    private Map<Event, String> eventToStringMap;
    private Map<Integer, Event> eventsByIdMap;

    public EventsPresenter(IEventsContract.View view) {
        this.view = view;
        context = view.getContext();
        configItems();
        loadData(true);
    }

    public void configItems() {
        view.onLoadingItems();
    }

    //Tiene que ser publico por los test. No cambiar a private aunque lo diga el sonar
    public void loadData(boolean showMessage) {

        if (!view.hasInternetConnection()) {
            List<Event> evLocal = loadLocalData();
            view.onEventsLoaded(evLocal);
            view.onLoadSuccess(evLocal.size(), showMessage);
            cachedEvents = evLocal;
            copyAllEvents = evLocal;
            initEventToStringMap(copyAllEvents);
            view.onInternetConnectionFailure();
            creaFavoritos();
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
                saveData(cachedEvents);
                creaFavoritos();
            }

            @Override
            public void onFailure() {
                view.onLoadError();
                cachedEvents = null;
                copyAllEvents = null;
            }
        });
    }

    private List<Event> loadLocalData() {
        return LocalEvents.loadDataFromLocal(view.getContext());
    }

    private void saveData(List<Event> cachedEvents) {
        LocalEvents.saveDataToLocal(view.getContext(), cachedEvents);
    }

    private void initEventToStringMap(List<Event> copyAllEvents) {
        eventToStringMap = new HashMap<>();
        eventsByIdMap = new HashMap<>();
        for (Event e: copyAllEvents) {
            eventToStringMap.put(e, e.toString().toLowerCase());
            eventsByIdMap.put(e.getIdentificador(), e);
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
    public void onFavouriteEventsClicked() {
        view.openFavouriteEventsView();
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
    /**
     * Getter de la variable cachedEvents para poder ejecutar las pruebas unitarias.
     */
    public List<Event> getCachedEvents() {
        return cachedEvents;
    }

    /**
     * Metodo para crear los favoritos al inicio de la app
     */
    private void creaFavoritos() {
        List<Integer> idsFavoritos = LocalEvents.loadFavouritesId(context);
        for (Integer id: idsFavoritos) {
            if(eventsByIdMap.containsKey(id)){
                Event e = eventsByIdMap.get(id);
                LocalEvents.newFavouriteEvent(context, e.getIdentificador());
            }
        }
    }
}
