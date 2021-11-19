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

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.isunican.eventossantander.R;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.view.events.EventsActivity;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

public class AnhadirEventoAFavoritosUITest {
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
    public void anhadirEventoAFavoritos() {
        // IVF 1.a Anhado el evento 4 a favoritos y compruebo si se rellena la estrella
        // Selecciono el evento 4 de la lista
        onData(anything()).inAdapterView(ViewMatchers.withId(R.id.eventsListView)).atPosition(3).perform(click());
        // Compruebo que la estrella no esta seleccionada (se muestra la estrella vacia)
        onView(withId(R.id.event_detail_Favourite)).check(matches(withTagValue(equalTo(R.drawable.estrella))));
        // Selecciono el boton de añadir a favoritos
        onView(withId(R.id.event_detail_Favourite)).perform(scrollTo(), click());
        // Compruebo que la estrella esta seleccionada (se muestra la estrella rellena)
        onView(withId(R.id.event_detail_Favourite)).check(matches(withTagValue(equalTo(R.drawable.estrella_rellena))));
        // Vuelvo atras
        pressBack();

        // Accedo a la lista de favoritos
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withText(R.string.eventos_favoritos)).perform(click());

        // Compruebo que el tamaño de la lista es 1
        onView(withId(R.id.FavouriteEventsListView)).check(matches(withListSize(1)));
        onData(anything()).inAdapterView(withId(R.id.FavouriteEventsListView)).atPosition(0).onChildView(withId(R.id.item_event_title)).check(matches(withText("Gabinete de estampas virtual de la UC")));
        pressBack();


        // IVF 1.b Anhado el evento 10 a favoritos y compruebo si se rellena la estrella
        // Selecciono el evento 10 de la lista
        onData(anything()).inAdapterView(ViewMatchers.withId(R.id.eventsListView)).atPosition(9).perform(click());
        // Compruebo que la estrella no esta seleccionada (se muestra la estrella vacia)
        onView(withId(R.id.event_detail_Favourite)).check(matches(withTagValue(equalTo(R.drawable.estrella))));
        // Selecciono el boton de añadir a favoritos
        onView(withId(R.id.event_detail_Favourite)).perform(scrollTo(), click());
        // Compruebo que la estrella esta seleccionada (se muestra la estrella rellena)
        onView(withId(R.id.event_detail_Favourite)).check(matches(withTagValue(equalTo(R.drawable.estrella_rellena))));
        // Vuelvo atras
        pressBack();

        // Accedo a la lista de favoritos
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withText(R.string.eventos_favoritos)).perform(click());

        // Compruebo que se anadido el evento a la lista por encima del anterior
        onView(withId(R.id.FavouriteEventsListView)).check(matches(withListSize(2)));
        onData(anything()).inAdapterView(withId(R.id.FavouriteEventsListView)).atPosition(0).onChildView(withId(R.id.item_event_title)).check(matches(withText("Arte Postal")));
        onData(anything()).inAdapterView(withId(R.id.FavouriteEventsListView)).atPosition(1).onChildView(withId(R.id.item_event_title)).check(matches(withText("Gabinete de estampas virtual de la UC")));
        pressBack();

        // Vacío sharedPrefs
        // Selecciono el evento 4 de la lista
        onData(anything()).inAdapterView(ViewMatchers.withId(R.id.eventsListView)).atPosition(3).perform(click());
        // Selecciono el boton de eliminar de favorito
        onView(withId(R.id.event_detail_Favourite)).perform(scrollTo(), click());
        // Vuelvo atras y voy a la lista de favoritos
        pressBack();
        // Selecciono el evento 10 de la lista
        onData(anything()).inAdapterView(ViewMatchers.withId(R.id.eventsListView)).atPosition(9).perform(click());
        // Selecciono el boton de eliminar de favorito
        onView(withId(R.id.event_detail_Favourite)).perform(scrollTo(), click());
        // Vuelvo atras
        pressBack();
    }

}
