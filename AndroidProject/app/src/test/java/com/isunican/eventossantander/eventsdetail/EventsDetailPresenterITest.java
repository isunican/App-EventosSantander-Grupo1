package com.isunican.eventossantander.eventsdetail;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
// Android
import androidx.test.core.app.ApplicationProvider;
import android.content.Context;
import android.os.Build;
// unican
import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.presenter.events.EventsPresenter;
import com.isunican.eventossantander.presenter.eventsdetail.EventsDetailPresenter;
import com.isunican.eventossantander.utils.AccessSharedPrefs;
import com.isunican.eventossantander.view.events.IEventsContract;
import com.isunican.eventossantander.view.eventsdetail.EventsDetailActivity;
// junit
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
// java
import java.util.List;
import java.util.concurrent.Phaser;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
public class EventsDetailPresenterITest {

    // Mocks
    @Mock
    private static EventsDetailActivity mockView;
    @Mock
    private static IEventsContract.View mockViewEvents;

    private static AccessSharedPrefs preferences;

    private static EventsDetailPresenter sut;
    private static EventsPresenter eventsPresenter;
    private static Phaser lock = EventsRepository.getLock();

    /**
     * Se incializan los mockups
     * Se cargan los eventos en json de
     * https://personales.unican.es/rivasjm/resources/agenda_cultural.json
     */
    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        EventsRepository.setLocalSource();
        mockView = mock(EventsDetailActivity.class);
        mockViewEvents = mock(IEventsContract.View.class);
        when(mockViewEvents.hasInternetConnection()).thenReturn(true);
        preferences = new AccessSharedPrefs(context);
        eventsPresenter = new EventsPresenter(mockViewEvents, preferences);
        eventsPresenter.loadData(true);
        lock.arriveAndAwaitAdvance();
        sut = new EventsDetailPresenter(mockView, preferences);
    }
    @Test
    public void onFavouriteEventsClickedITest() {
        // Lista de favoritos
        List<Integer> listaFavs;
        //Lista de eventos
        List<Event> listaEventos;
        // Evento
        Event evento;
        // Se cargan los eventos que se han descargado en el setUp()
        listaEventos = eventsPresenter.getCachedEvents();
        evento = listaEventos.get(3);

       // Se comprueba que previamente la lista esta a cero
        listaFavs = preferences.loadFavouritesId();
        assertEquals(0, listaFavs.size());
        // *** IGIC.1a Se añade un evento a la lista de favoritos ***
        sut.onFavouriteEventsClicked(evento, false);
        listaFavs = preferences.loadFavouritesId();
        assertTrue(listaFavs.contains(evento.getIdentificador()));
        assertEquals(1, listaFavs.size());

        // *** IGIC.1b Se elimina un evento a la lista de favoritos ***
        sut.onFavouriteEventsClicked(evento, true);
        listaFavs = preferences.loadFavouritesId();
        assertFalse(listaFavs.contains(evento.getIdentificador()));
        assertEquals(0, listaFavs.size());

    }
}
