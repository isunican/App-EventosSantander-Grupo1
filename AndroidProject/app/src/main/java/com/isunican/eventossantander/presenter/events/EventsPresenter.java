package com.isunican.eventossantander.presenter.events;

import android.util.Log;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.utils.ISharedPrefs;
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
    private List<Event> cachedEvents;
    private List<Event> copyAllEvents;

    private Map<Event, String> eventToStringMap;

    private ISharedPrefs sharedPrefs;

    public EventsPresenter(IEventsContract.View view, ISharedPrefs sharedPrefs) {
        this.view = view;
        this.sharedPrefs = sharedPrefs;
        configItems();
        loadData(true);
    }

    public void configItems() {
        view.onLoadingItems();
    }

    //Tiene que ser publico por los test. No cambiar a private aunque lo diga el sonar
    public void loadData(boolean showMessage) {

        sharedPrefs.clearCategories();

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

    @Override
    public void onCategoryFilter() {
        Set<String> categorias = sharedPrefs.getSelectedCategories();
        List<Event> eventosFiltrados = new ArrayList<>();
        List<Event> eventList;
        eventList = copyAllEvents;
        String categoriaI;
        String eventoI;
        if (categorias != null && !categorias.isEmpty()) {
            for (String s : categorias) {
                categoriaI = Normalizer.normalize(s, Normalizer.Form.NFD);
                categoriaI = categoriaI.replaceAll("[^\\p{ASCII}]", "");
                categoriaI = categoriaI.toLowerCase();
                for (Event e : eventList) {
                    eventoI = Normalizer.normalize(e.getCategoria(), Normalizer.Form.NFD);
                    eventoI = eventoI.replaceAll("[^\\p{ASCII}]", "");
                    eventoI = eventoI.toLowerCase();
                    if (categoriaI.equals(eventoI)) eventosFiltrados.add(e);
                }
            }
            cachedEvents = eventosFiltrados;
        } else{
            eventosFiltrados = eventList;
            cachedEvents = copyAllEvents;
        }
        view.onEventsLoaded(eventosFiltrados);
        view.onLoadSuccess(eventosFiltrados.size(), true);
    }

    @Override
    public void onCategoryFilterClicked() {
        view.openCategoryFilterView();
    }

    /**
     * Getter de la variable cachedEvents para poder ejecutar las pruebas unitarias.
     */
    public List<Event> getCachedEvents() {
        return cachedEvents;
    }
}
