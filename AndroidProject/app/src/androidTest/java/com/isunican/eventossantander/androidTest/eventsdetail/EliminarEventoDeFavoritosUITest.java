package com.isunican.eventossantander.androidTest.eventsdetail;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.isunican.eventossantander.androidTest.utils.Matchers.withListSize;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.anything;

import android.view.View;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.isunican.eventossantander.R;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.view.events.EventsActivity;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

public class EliminarEventoDeFavoritosUITest {
    private View decorView;

    /**
     * Load known events json
     * https://personales.unican.es/rivasjm/resources/agenda_cultural.json
     */
    @BeforeClass
    public static void setUp() {
        EventsRepository.setLocalSource();
        IdlingRegistry.getInstance().register(EventsRepository.getIdlingResource());
    }

    @AfterClass
    public static void clean() {
        EventsRepository.setOnlineSource();
        IdlingRegistry.getInstance().unregister(EventsRepository.getIdlingResource());
    }

    @Rule
    public ActivityScenarioRule<EventsActivity> activityRule =
            new ActivityScenarioRule(EventsActivity.class);



    /**
     * Test de agregar a favoritos un evento
     */
    @Test
    public void eliminarEventoDeFavoritos() {
        // Precondición: se ha añadido el evento 4 a la lista
        // Selecciono el evento 4 de la lista
        onData(anything()).inAdapterView(ViewMatchers.withId(R.id.eventsListView)).atPosition(3).perform(click());
        // Selecciono el boton de añadir a favoritos
        onView(withId(R.id.event_detail_Favourite)).perform(scrollTo(), click());
        // Vuelvo atras
        pressBack();

        // Accedo a la lista de favoritos
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getContext());
        // Introducimos una espera de 1 segundo para que de tiempo a que aparezca el desplegable con la opción de Eventos Favoritos
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText(R.string.eventos_favoritos)).perform(click());

        // Compruebo que el tamaño de la lista es 1
        onView(withId(R.id.FavouriteEventsListView)).check(matches(withListSize(1)));
        onData(anything()).inAdapterView(withId(R.id.FavouriteEventsListView)).atPosition(0).onChildView(withId(R.id.item_event_title)).check(matches(withText("Gabinete de estampas virtual de la UC")));
        pressBack();

        // IVF 1.c Elimino el evento 4 de favoritos y compruebo si se rellena la estrella
        // Selecciono el evento 4 de la lista
        onData(anything()).inAdapterView(ViewMatchers.withId(R.id.eventsListView)).atPosition(3).perform(click());
        // Compruebo que la estrella está seleccionada (se muestra la estrella rellena)
        // Selecciono el boton de eliminar de favoritos
        onView(withId(R.id.event_detail_Favourite)).perform(scrollTo(), click());
        // Compruebo que la estrella no está seleccionada (se muestra la estrella vacía)
        onView(withId(R.id.event_detail_Favourite)).check(matches(withTagValue(equalTo(R.drawable.estrella))));
        // Vuelvo atras
        pressBack();

        // Accedo a la lista de favoritos
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getContext());
        // Introducimos una espera de 1 segundo para que de tiempo a que aparezca el desplegable con la opción de Eventos Favoritos
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText(R.string.eventos_favoritos)).perform(click());

        // Compruebo que el tamaño de la lista es 0 y no se muestra ningún evento
        onView(withId(R.id.FavouriteEventsListView)).check(matches(withListSize(0)));
        // Vuelvo atras
        pressBack();

    }

}
