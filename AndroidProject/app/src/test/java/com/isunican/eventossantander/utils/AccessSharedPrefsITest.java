package com.isunican.eventossantander.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.os.Build;
import androidx.test.core.app.ApplicationProvider;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.presenter.events.EventsPresenter;
import com.isunican.eventossantander.view.events.IEventsContract;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})

public class AccessSharedPrefsITest {

    private static AccessSharedPrefs sut;
    private static EventsPresenter eventsPresenter;
    private static Phaser lock = EventsRepository.getLock();
    @Mock
    private static IEventsContract.View mockViewEvents;

    /**
     * Se incializan los mockups
     * Se cargan los eventos en json de
     * https://personales.unican.es/rivasjm/resources/agenda_cultural.json
     */
    @Before
    public void setUp() {
        EventsRepository.setLocalSource();
        Context context = ApplicationProvider.getApplicationContext();
        sut = new AccessSharedPrefs(context);
        mockViewEvents = mock(IEventsContract.View.class);
        when(mockViewEvents.hasInternetConnection()).thenReturn(true);
        eventsPresenter = new EventsPresenter(mockViewEvents, sut);
        eventsPresenter.loadData(true);
        lock.arriveAndAwaitAdvance();
    }

    @Test
    public void newFavouriteEventTest() {
        // Lista de favoritos
        List<Integer> listaFavs;

        // ** IGIC.2a - Se añade evento que existe y no esta a favoritos **
        int idEventoExiste = 44291;
        // Se comprueba el tamaño de la lista antes de intentar añadir el evento.
        listaFavs = sut.loadFavouritesId();
        assertEquals(0, listaFavs.size());
        // Se añade un evento que existe
        sut.newFavouriteEvent(idEventoExiste);
        listaFavs = sut.loadFavouritesId();
        assertEquals(idEventoExiste,listaFavs.get(0).intValue());
        assertEquals(1, listaFavs.size());

        // ** IGIC.2b - Se intenta añadir evento que no existe y no esta en favoritos **
        int idEventoInex = 99999;
        // Se comprueba el tamaño de la lista antes de intentar añadir el evento.
        listaFavs = sut.loadFavouritesId();
        assertEquals(1, listaFavs.size());
        sut.newFavouriteEvent(idEventoInex);
        listaFavs = sut.loadFavouritesId();
        assertFalse(listaFavs.contains(idEventoInex));
        // El tamaño no cambia porque no se debe de añadir el evento
        assertEquals(1, listaFavs.size());

        // ** IGIC.2c - Se añade evento que existe y ya esta a favoritos **
        // Se comprueba que el evento ya se ha añadido en el caso de uso IGIC.2a
        assertTrue(listaFavs.contains(idEventoExiste));
        // Se comprueba el tamaño de la lista antes de intentar añadir el evento.
        listaFavs = sut.loadFavouritesId();
        assertEquals(1, listaFavs.size());
        // Se intenta añadir un evento que ya esta en favoritos
        sut.newFavouriteEvent(idEventoExiste);
        // Se comprueba que el tamaño de la lista antes de añadir el evento  no debe cambiar
        listaFavs = sut.loadFavouritesId();
        assertEquals(1, listaFavs.size());
    }

    @Test
    public void deleteFavouriteEventTest() {
        // Lista de favoritos
        List<Integer> listaFavs;
        // Se comprueba que previamente la lista esta a cero
        listaFavs = sut.loadFavouritesId();
        assertEquals(0, listaFavs.size());
        // Se añade un evento a favoritos
        int idEventoExiste = 44291;
        sut.newFavouriteEvent(idEventoExiste);
        listaFavs = sut.loadFavouritesId();
        assertEquals(1, listaFavs.size());

        // IGIC.2a - Se quita evento que existe y ya esta a favoritos
        sut.deleteFavouriteEvent(idEventoExiste);
        listaFavs = sut.loadFavouritesId();
        assertFalse(listaFavs.contains(idEventoExiste));
        assertEquals(0, listaFavs.size());

        // IGIC.2b - Se intenta eliminar evento que no existe
        int idEventoInex = 99999;
        sut.deleteFavouriteEvent(idEventoInex);
        listaFavs = sut.loadFavouritesId();
        assertFalse(listaFavs.contains(idEventoInex));
        // El tamaño no cambia porque no se debe de eliminar el evento
        assertEquals(0,  listaFavs.size());

        // IGIC.2c - Se añade evento que existe y no esta a favoritos
        // Se comprueba que la lista esta vacia
        listaFavs = sut.loadFavouritesId();
        assertEquals(0, listaFavs.size());
        // Se elimina un evento que sabemos que existe y no esta en la lista
        sut.deleteFavouriteEvent(idEventoExiste);
        listaFavs = sut.loadFavouritesId();
        assertFalse(listaFavs.contains(idEventoExiste));
        // Se comprueba que la lista no cambia de tamaño
        assertEquals(0, listaFavs.size());
    }

    /**
     * US422732-SeleccionarPalabrasClave-TestPlan
     * Test: setSelectedKeywords(List<String> listKeywords) method
     * @author: Pablo Almohalla Gómez
     */
    @Test
    public void setSelectedKeywordsTest() {
        // Lista de palabras clave
        List<String> listaKeywords;

        // Se comprueba que previamente la lista esta a cero
        listaKeywords = sut.getSelectedKeywords();
        assertEquals(0, listaKeywords.size());

        // ** IGIC.1a - Se añade una palabra clave que existe **
        listaKeywords = new ArrayList<>();
        listaKeywords.add("Teatro");
        sut.setSelectedKeywords(listaKeywords);
        listaKeywords = sut.getSelectedKeywords();
        assertEquals(1, listaKeywords.size());

        // ** IGIC.1b - Se añade una palabra clave nula **
        listaKeywords = new ArrayList<>();
        listaKeywords.add(null);
        sut.setSelectedKeywords(listaKeywords);
        listaKeywords = sut.getSelectedKeywords();
        assertEquals(0, listaKeywords.size());

        // ** IGIC.1c - Se añade una palabra clave vacía **
        listaKeywords = new ArrayList<>();
        listaKeywords.add("");
        sut.setSelectedKeywords(listaKeywords);
        listaKeywords = sut.getSelectedKeywords();
        assertEquals(0, listaKeywords.size());

        // ** IGIC.1d - Se añaden dos palabras clave válidas **
        listaKeywords = new ArrayList<>();
        listaKeywords.add("Teatro");
        listaKeywords.add("Surf");
        sut.setSelectedKeywords(listaKeywords);
        listaKeywords = sut.getSelectedKeywords();
        assertEquals(2, listaKeywords.size());
    }
}
