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

    /**
     * Load known events json
     * https://personales.unican.es/rivasjm/resources/agenda_cultural.json
     */
    @Before
    public void setUp() {
        EventsRepository.setLocalSource();
        mockView = mock(IEventsContract.View.class);
        when(mockView.hasInternetConnection()).thenReturn(true);
        sut = new EventsPresenter(mockView);
    }

    @Test
    public void onKeyWordsFilterTest() {
        // Espera para asegurar que se obtiene correctamente el OpenData
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Lista de Eventos
        List<Event> lista;

        // UGIC.1a Lista con 4 Coincidencias
        sut.onKeywordsFilter("Palacio de Festivales");
        lista = sut.getCachedEvents();
        assertEquals(lista.size(), 5);
        assertEquals(lista.get(0).getNombre(), "\"Entre nosotras\". Concierto");
        assertEquals(lista.get(4).getNombre(), "V Semana Internacional de Cine ");

        // UGIC.1b Lista Completa
        sut.onKeywordsFilter("");
        lista = sut.getCachedEvents();
        assertEquals(lista.size(), 345);
        assertEquals(lista.get(0).getNombre(), "Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea");
        assertEquals(lista.get(344).getNombre(), "Visiones Urbanas con ArteSantander 2021");

        //UGIC.1c Lista Vacia
        sut.onKeywordsFilter("PalabraRara");
        lista = sut.getCachedEvents();
        assertEquals(lista.size(), 0);

        // Se comprueba 4 veces ya que tambien se utiliza ese metodo en loadData de la creacion del sut
        verify(mockView, times(4)).onLoadSuccess(anyInt(), anyBoolean());
    }
}