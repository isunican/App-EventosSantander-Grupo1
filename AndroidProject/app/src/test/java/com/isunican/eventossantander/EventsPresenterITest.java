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
    }

    @Test
    public void loadDataTest() {

        // Lista de Eventos
        List<Event> lista;

        // IGIC.1a No hay conexion con el repositorio
        when(mockView.hasInternetConnection()).thenReturn(false);
        sut.loadData(true);
        assertEquals(sut.getCachedEvents(), null);

        // IGIC.1b Hay conexion con el repositorio
        when(mockView.hasInternetConnection()).thenReturn(true);
        sut.loadData(true);
        // Espera para asegurar que se obtiene la lista de eventos del repositorio
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lista = sut.getCachedEvents();
        assertEquals(lista.size(), 345);
        assertEquals(lista.get(0).getNombre(), "Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea");
        assertEquals(lista.get(344).getNombre(), "Visiones Urbanas con ArteSantander 2021");

        // IGIC.1c No hay conexion con el repositorio al intentar recargar
        sut.loadData(true);
        assertEquals(lista.size(), 345);
        assertEquals(lista.get(0).getNombre(), "Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea");
        assertEquals(lista.get(344).getNombre(), "Visiones Urbanas con ArteSantander 2021");

        // Se verifica que se realiza la llamada a los metodos correspondientes cuando falla el
        // repositorio y cuando funciona bien
        verify(mockView).onLoadSuccess(345, true);
        verify(mockView, times(2)).onInternetConnectionFailure();

    }
}