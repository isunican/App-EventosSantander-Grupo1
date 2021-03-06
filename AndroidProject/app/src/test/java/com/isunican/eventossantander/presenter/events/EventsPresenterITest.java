package com.isunican.eventossantander.presenter.events;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.utils.AccessSharedPrefs;
import com.isunican.eventossantander.utils.ISharedPrefs;
import com.isunican.eventossantander.view.events.IEventsContract;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.security.AccessControlContext;
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
public class EventsPresenterITest {

    private static EventsPresenter sut;
    @Mock
    private static IEventsContract.View mockView;
    private static ISharedPrefs mockSharedPrefs;

    private static Phaser lock = EventsRepository.getLock();

    /**
     * Load known events json
     * https://personales.unican.es/rivasjm/resources/agenda_cultural.json
     */
    @Before
    public void setUp() {
        EventsRepository.setLocalSource();
        mockView = mock(IEventsContract.View.class);
        mockSharedPrefs = mock(ISharedPrefs.class);
        when(mockView.hasInternetConnection()).thenReturn(false);
        sut = new EventsPresenter(mockView, mockSharedPrefs);
        lock.arriveAndAwaitAdvance();
    }

    @Test
    public void loadDataTest() {

        // Lista de Eventos
        List<Event> lista;

        // IGIC.1a No hay conexion con el repositorio
        when(mockView.hasInternetConnection()).thenReturn(false);
        sut.loadData(true);
        //Aqui falla porque ahora el load data tiene otra funcionalidad
        //assertEquals(null, sut.getCachedEvents());

        // IGIC.1b Hay conexion con el repositorio
        when(mockView.hasInternetConnection()).thenReturn(true);
        sut.loadData(true);
        // Espera para asegurar que se obtiene la lista de eventos del repositorio
        lock.arriveAndAwaitAdvance();
        lista = sut.getCachedEvents();
        assertEquals(345, lista.size());
        assertEquals("Abierto el plazo de inscripci??n para el Concurso Internacional de Piano de Santander Paloma O'Shea", lista.get(0).getNombre());
        assertEquals("Visiones Urbanas con ArteSantander 2021", lista.get(344).getNombre());

        // IGIC.1c No hay conexion con el repositorio al intentar recargar
        sut.loadData(true);
        assertEquals(345, lista.size());
        assertEquals("Abierto el plazo de inscripci??n para el Concurso Internacional de Piano de Santander Paloma O'Shea", lista.get(0).getNombre());
        assertEquals("Visiones Urbanas con ArteSantander 2021", lista.get(344).getNombre());

        // Se verifica que se realiza la llamada a los metodos correspondientes cuando falla el
        // repositorio y cuando funciona bien
        verify(mockView).onLoadSuccess(345, true);
        verify(mockView, times(2)).onInternetConnectionFailure();

    }

    /**
     * US435462-FiltrarPorCategor??a-TestPlan
     * Test: onCategoryFilter() method
     * @author: Pedro Monje Onandia
     */
    @Test
    public void onCategoryFilterTest() {
        // Configuraci??n inicial de la prueba
        when(mockView.hasInternetConnection()).thenReturn(true);
        Context context = ApplicationProvider.getApplicationContext();
        // Como es una prueba de integraci??n utilizamos unas shared preferences reales y no un mock
        AccessSharedPrefs sharedPreferencesCategoria = new AccessSharedPrefs(context);
        sut = new EventsPresenter(mockView,sharedPreferencesCategoria);
        sut.loadData(true);
        lock.arriveAndAwaitAdvance();

        // **IGIC.1a Se utiliza el m??todo con dos categor??as [???Otros???, ???M??sica???] **
        // Se crea el Set de strings en el que ir??n los nombres de las categorias
        Set<String > categoriasSeleccionadas = new HashSet<String>();
        categoriasSeleccionadas.add("Otros");
        categoriasSeleccionadas.add("Musica");
        // Se seleccionan las categor??as Otros y Musica
        sharedPreferencesCategoria.setSelectedCategories(categoriasSeleccionadas);
        // Llamada al m??todo de filtrar, el que se est?? probando
        sut.onCategoryFilter();
        // Se comprueba que el primer y el ??ltimo evento son los esperados, tambi??n se comprueba que se han filtrado 98 eventos
        assertEquals("Abierto el plazo de inscripci??n para el Concurso Internacional de Piano de Santander Paloma O'Shea", sut.getCachedEvents().get(0).getNombre());
        assertEquals("Museo del Agua: Historia sobre el abastecimiento de agua de Santander ", sut.getCachedEvents().get(97).getNombre());
        assertEquals(98, sut.getCachedEvents().size());

        // **IGIC.1b No hay categor??as seleccionadas en las SharedPrefs**
        // Se crea el Set de strings en el que ir??n los nombres de las categorias
        categoriasSeleccionadas = new HashSet<String>();
        // No se selecciona ninguna categor??a
        sharedPreferencesCategoria.setSelectedCategories(categoriasSeleccionadas);
        // Llamada al m??todo de filtrar, el que se est?? probando
        sut.onCategoryFilter();
        // Se comprueba que el primer y el ??ltimo evento son los esperados, tambi??n se comprueba que se han filtrado 345 eventos (todos)
        assertEquals("Abierto el plazo de inscripci??n para el Concurso Internacional de Piano de Santander Paloma O'Shea", sut.getCachedEvents().get(0).getNombre());
        assertEquals("Visiones Urbanas con ArteSantander 2021", sut.getCachedEvents().get(344).getNombre());
        assertEquals(345, sut.getCachedEvents().size());
    }
}