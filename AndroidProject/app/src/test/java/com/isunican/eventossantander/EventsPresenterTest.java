package com.isunican.eventossantander;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.test.espresso.IdlingRegistry;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.presenter.events.EventsPresenter;
import com.isunican.eventossantander.view.events.IEventsContract;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.*;

import android.os.Build;

import java.util.List;
import java.util.concurrent.Phaser;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
public class EventsPresenterTest {

    private static EventsPresenter sut;
    @Mock
    private static IEventsContract.View mockView;

    private static Phaser lock = EventsRepository.getLock();

    /**
     * Load known events json
     * https://personales.unican.es/rivasjm/resources/agenda_cultural.json
     * Creacion del mock de la vista y asignacion a la clase sut
     */
    @Before
    public void setUp() {
        EventsRepository.setLocalSource();
        mockView = mock(IEventsContract.View.class);
        when(mockView.hasInternetConnection()).thenReturn(true);
        sut = new EventsPresenter(mockView);
        lock.arriveAndAwaitAdvance();
    }

    /**
     * Test del metodo onKeyWordsFilter
     * (Buscar por palabras clave)
     */
    @Test
    public void onKeywordsFilterTest() {

        // Lista de Eventos
        List<Event> lista;

        // UGIC.1a Lista con 4 Coincidencias
        sut.onKeywordsFilter("Palacio de Festivales", false, false);
        lista = sut.getCachedEvents();
        assertEquals(5, lista.size());
        assertEquals("\"Entre nosotras\". Concierto", lista.get(0).getNombre());
        assertEquals("V Semana Internacional de Cine ", lista.get(4).getNombre());

        // UGIC.1b Lista Completa
        sut.onKeywordsFilter("", false, false);
        lista = sut.getCachedEvents();
        assertEquals(345, lista.size());
        assertEquals("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea", lista.get(0).getNombre());
        assertEquals("Visiones Urbanas con ArteSantander 2021", lista.get(344).getNombre());

        //UGIC.1c Lista Vacia
        sut.onKeywordsFilter("PalabraRara", false, false);
        lista = sut.getCachedEvents();
        assertEquals(0, lista.size());

        // Se comprueba 4 veces ya que tambien se utiliza ese metodo en loadData de la creacion del sut
        verify(mockView, times(4)).onLoadSuccess(anyInt(), anyBoolean());
    }
}