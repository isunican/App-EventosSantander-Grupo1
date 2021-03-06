package com.isunican.eventossantander.presenter.events;

import static org.junit.Assert.assertEquals;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.utils.ISharedPrefs;
import com.isunican.eventossantander.view.events.IEventsContract;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.*;

import android.os.Build;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private static ISharedPrefs mockSharedPrefs;

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
        mockSharedPrefs = mock(ISharedPrefs.class);
        when(mockView.hasInternetConnection()).thenReturn(true);
        sut = new EventsPresenter(mockView, mockSharedPrefs);
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
        assertEquals("Abierto el plazo de inscripci??n para el Concurso Internacional de Piano de Santander Paloma O'Shea", lista.get(0).getNombre());
        assertEquals("Visiones Urbanas con ArteSantander 2021", lista.get(344).getNombre());

        //UGIC.1c Lista Vacia
        sut.onKeywordsFilter("PalabraRara", false, false);
        lista = sut.getCachedEvents();
        assertEquals(0, lista.size());

        // Se comprueba 4 veces ya que tambien se utiliza ese metodo en loadData de la creacion del sut
        verify(mockView, times(4)).onLoadSuccess(anyInt(), anyBoolean());
    }

    /**
     * US422732-SeleccionarPalabrasClave-TestPlan
     * Test: onSelectKeywordFilter() method
     * @author: Pablo Almohalla G??mez
     */
    @Test
    public void onSelectKeywordFilterTest() {

        // Lista de eventos
        List<Event> listaEvents;

        // Lista de palabras clave
        List<String> listaKeywords;

        // ** UGIC.1a - Se a??ade una lista de palabras clave nula **
        when(mockSharedPrefs.getSelectedKeywords()).thenReturn(null);
        sut.onSelectKeywordFilter();
        listaEvents = sut.getCachedEvents();
        assertEquals(345, listaEvents.size());
        assertEquals("Abierto el plazo de inscripci??n para el Concurso Internacional de Piano de Santander Paloma O'Shea", listaEvents.get(0).getNombre());
        assertEquals("Visiones Urbanas con ArteSantander 2021", listaEvents.get(344).getNombre());

        // ** UGIC.1b - Se a??ade una lista con una palabras clave existente **
        listaKeywords = new ArrayList<>();
        listaKeywords.add("Teatro");
        when(mockSharedPrefs.getSelectedKeywords()).thenReturn(listaKeywords);
        sut.onSelectKeywordFilter();
        listaEvents = sut.getCachedEvents();
        assertEquals(32, listaEvents.size());
        assertEquals("\"Lo que (no) se ve\", Cuartoymitad Teatro", listaEvents.get(0).getNombre());
        assertEquals("Calle Cultura, huellas de Laura Irizabal", listaEvents.get(31).getNombre());

        // ** UGIC.1c - Se a??ade una lista con una palabras clave no existente **
        listaKeywords = new ArrayList<>();
        listaKeywords.add("Esternocleidomastoideo");
        when(mockSharedPrefs.getSelectedKeywords()).thenReturn(listaKeywords);
        sut.onSelectKeywordFilter();
        listaEvents = sut.getCachedEvents();
        assertEquals(0, listaEvents.size());

        // ** UGIC.1d - Se a??ade una lista con dos palabras clave existentes **
        listaKeywords = new ArrayList<>();
        listaKeywords.add("Teatro");
        listaKeywords.add("Surf");
        when(mockSharedPrefs.getSelectedKeywords()).thenReturn(listaKeywords);
        sut.onSelectKeywordFilter();
        listaEvents = sut.getCachedEvents();
        assertEquals(33, listaEvents.size());
        assertEquals("The Sadies", listaEvents.get(0).getNombre());
        assertEquals("Calle Cultura, huellas de Laura Irizabal", listaEvents.get(32).getNombre());

        // En el verify comprobamos que el m??todo onEventsLoaded() se ha invocado 4 veces (1 del sut y 3 del test)
        verify(mockView, times(4)).onEventsLoaded(anyList());

        // En el verify comprobamos que el m??todo onLoadSuccess() se ha invocado 4 veces (1 del sut y 3 del test)
        verify(mockView, times(4)).onLoadSuccess(anyInt(), anyBoolean());

        // En el verify comprobamos que el m??todo getSelectedKeywords() se ha invocado 4 veces
        verify(mockSharedPrefs, times(4)).getSelectedKeywords();
    }

    /**
     * US435462-FiltrarPorCategor??a-TestPlan
     * Test: onCategoryFilter() method
     * @author: Pedro Monje Onandia
     */
    @Test
    public void onCategoryFilterTest() {
        // **UGIC.1a Se utiliza el m??todo con dos categor??as [???Otros???, ???M??sica???] **
        // Se crea el Set de strings en el que ir??n los nombres de las categor??as
        Set<String > categoriasSeleccionadas = new HashSet<String>();
        // Se introducen las categor??as en el Set
        categoriasSeleccionadas.add("Otros");
        categoriasSeleccionadas.add("Musica");
        // Se mockea el m??todo que devuelve las categor??as
        when(mockSharedPrefs.getSelectedCategories()).thenReturn(categoriasSeleccionadas);
        sut.onCategoryFilter(); //Se llama al m??todo de filtrar (m??todo que se est?? probando)
        // Se comprueba que el primer y el ??ltimo evento son los esperados, tambi??n se comprueba que se han filtrado 98 eventos
        assertEquals("Abierto el plazo de inscripci??n para el Concurso Internacional de Piano de Santander Paloma O'Shea", sut.getCachedEvents().get(0).getNombre());
        assertEquals("Museo del Agua: Historia sobre el abastecimiento de agua de Santander ", sut.getCachedEvents().get(97).getNombre());
        assertEquals(98, sut.getCachedEvents().size());

        // **UGIC.1b No se seleccionan categor??as en las SharedPrefs**
        // Se crea el Set de strings en el que ir??n los nombres de las categorias
        categoriasSeleccionadas = new HashSet<String>();
        // Se mockea el m??todo que devuelve las categor??as
        when(mockSharedPrefs.getSelectedCategories()).thenReturn(categoriasSeleccionadas);
        sut.onCategoryFilter();// Se llama al m??todo de filtrar (m??todo que se est?? probando)
        // Se comprueba que el primer y el ??ltimo evento son los esperados, tambi??n se comprueba que se han filtrado 345 eventos, es decir, todos
        assertEquals("Abierto el plazo de inscripci??n para el Concurso Internacional de Piano de Santander Paloma O'Shea", sut.getCachedEvents().get(0).getNombre());
        assertEquals("Visiones Urbanas con ArteSantander 2021", sut.getCachedEvents().get(344).getNombre());
        assertEquals(345, sut.getCachedEvents().size());
    }

}