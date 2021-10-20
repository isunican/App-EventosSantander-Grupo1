package com.isunican.eventossantander;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Build;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.presenter.events.EventsPresenter;
import com.isunican.eventossantander.view.events.IEventsContract;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;
import java.util.concurrent.Phaser;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
public class EventsPresenterITest {

    private static EventsPresenter sut;
    @Mock
    private static IEventsContract.View mockView;

    private static Phaser lock = EventsRepository.getLock();

    /**
     * Load known events json
     * https://personales.unican.es/rivasjm/resources/agenda_cultural.json
     */
    @Before
    public void setUp() {
        EventsRepository.setLocalSource();
        mockView = mock(IEventsContract.View.class);
        when(mockView.hasInternetConnection()).thenReturn(false);
        sut = new EventsPresenter(mockView);
        lock.arriveAndAwaitAdvance();
    }

    @Test
    public void loadDataTest() {

        // Lista de Eventos
        List<Event> lista;

        // IGIC.1a No hay conexion con el repositorio
        when(mockView.hasInternetConnection()).thenReturn(false);
        sut.loadData(true);
        assertEquals(null, sut.getCachedEvents());

        // IGIC.1b Hay conexion con el repositorio
        when(mockView.hasInternetConnection()).thenReturn(true);
        sut.loadData(true);
        // Espera para asegurar que se obtiene la lista de eventos del repositorio
        lock.arriveAndAwaitAdvance();
        lista = sut.getCachedEvents();
        assertEquals(345, lista.size());
        assertEquals("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea", lista.get(0).getNombre());
        assertEquals("Visiones Urbanas con ArteSantander 2021", lista.get(344).getNombre());

        // IGIC.1c No hay conexion con el repositorio al intentar recargar
        sut.loadData(true);
        assertEquals(345, lista.size());
        assertEquals("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea", lista.get(0).getNombre());
        assertEquals("Visiones Urbanas con ArteSantander 2021", lista.get(344).getNombre());

        // Se verifica que se realiza la llamada a los metodos correspondientes cuando falla el
        // repositorio y cuando funciona bien
        verify(mockView).onLoadSuccess(345, true);
        verify(mockView, times(2)).onInternetConnectionFailure();

    }
}