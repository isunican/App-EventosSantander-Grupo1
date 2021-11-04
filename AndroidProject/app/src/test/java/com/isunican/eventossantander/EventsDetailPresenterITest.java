package com.isunican.eventossantander;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.os.Build;

// unican
import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.presenter.events.EventsPresenter;
import com.isunican.eventossantander.presenter.eventsdetail.EventsDetailPresenter;
import com.isunican.eventossantander.utils.ISharedPrefs;
import com.isunican.eventossantander.view.events.IEventsContract;
import com.isunican.eventossantander.view.eventsdetail.EventsDetailActivity;
// junit
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
// java
import java.util.List;
import java.util.concurrent.Phaser;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
public class EventsDetailPresenterITest {

    private static EventsDetailPresenter sut;
    @Mock
    private static EventsDetailActivity mockView;
    @Mock
    private static IEventsContract.View mockView2;
    @Mock
    private static ISharedPrefs mockPref;
    @Mock
    private static Event mockEvent;
    @Mock
    private static EventsPresenter eventsPresenter;

    private static Phaser lock = EventsRepository.getLock();

    /**
     * Se incializan los mockups
     * Se cargan los eventos en json de
     * https://personales.unican.es/rivasjm/resources/agenda_cultural.json
     */
    @Before
    public void setUp() {
        EventsRepository.setLocalSource();
        mockView = mock(EventsDetailActivity.class);
//        mockView2 = mock(IEventsContract.View.class);
//        mockEvent = mock(Event.class);
//        mockPref = new ISharedPrefs();
        eventsPresenter = new EventsPresenter(mockView2, mockPref); // preguntar si se puede o hay que hacer mock
        lock.arriveAndAwaitAdvance();
        sut = new EventsDetailPresenter(mockView, mockPref);
    }
    @Test
    public void onFavouriteEventsClickedITest() {
        // Lista de favoritos
        List<Integer> listaFavs;
        //Lista de eventos
        List<Event> listaEventos;
        when(mockView2.hasInternetConnection()).thenReturn(true);
        eventsPresenter.loadData(true);
        // Espera para asegurar que se obtiene la lista de eventos del repositorio
        lock.arriveAndAwaitAdvance();
        listaEventos = eventsPresenter.getCachedEvents();
        assertEquals(345,listaEventos.size());
        assertEquals("Gabinete de estampas virtual de la UC", listaEventos.get(3).getNombre());

        when(mockEvent.getIdentificador()).thenReturn(listaEventos.get(3).getIdentificador());
        when(mockEvent.getNombre()).thenReturn(listaEventos.get(3).getNombre());
        assertEquals("Gabinete de estampas virtual de la UC", mockEvent.getNombre());
        assertEquals(44291, mockEvent.getIdentificador());

        // Se comprueba que previamente la lista esta a cero
        listaFavs = mockPref.loadFavouritesId();
        assertEquals(0, listaFavs.size());
        // IGIC.1a Se añade un evento a la lista de favoritos
        sut.onFavouriteEventsClicked(mockEvent, false);
        listaFavs = mockPref.loadFavouritesId();
        assertEquals(mockEvent.getIdentificador(), listaFavs.get(0).intValue());
        assertEquals(1, listaFavs.size());



//        assertEquals(1,listaFavs.size());

        // IGIC.1b Se elimina un evento a la lista de favoritos
        // IGIC.1c Se elimina un evento a la lista de favoritos
    }
}
