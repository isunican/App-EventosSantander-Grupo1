package com.isunican.eventossantander.androidTest.categoryfilter;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.isunican.eventossantander.androidTest.utils.Matchers.withListSize;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.anything;

import android.view.View;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.isunican.eventossantander.R;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.view.events.EventsActivity;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

public class CategoryFilterUITest {

    private View decorView;

    @Rule
    public ActivityScenarioRule<EventsActivity> activityRule =
            new ActivityScenarioRule(EventsActivity.class);

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


    /**
     * US435462-FiltrarPorCategoría-TestPlan
     * @author: Pedro Monje Onandia
     */
    @Test
    public void categoryFilter() {
        //**IVF.1a Se seleccionan dos categorías [Otros, Música]**
        // Accedo al menu
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        // Pulso la opcion de filtrar por categoria
        onView(withText(R.string.filtrar_por_categoria)).perform(click());
        //Selecciono dos categorías y presiono aplicar
        onView(withId(R.id.otros_checkBox)).perform(click());
        onView(withId(R.id.musica_checkBox)).perform(click());
        onView(withId(R.id.categoryFilter_button_apply)).perform(click());
        // Idealmente Se comprueba que ha aparecido el toast
        //Compruebo si la lista que se ha filtrado tiene 98 eventos
        onView(withId(R.id.eventsListView)).check(matches(withListSize(98)));
        //Vuelvo a la lista de categorias y veo si están marcados los anteriormente pulsados
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withText(R.string.filtrar_por_categoria)).perform(click());
        onView(withId(R.id.otros_checkBox)).check(matches(isChecked()));
        onView(withId(R.id.musica_checkBox)).check(matches(isChecked()));

        //**IVF.1b Se presiona el boton de limpiar**
        onView(withId(R.id.categoryFilter_button_clear)).perform(click());
        // Idealmente Se comprueba que ha aparecido el toast
        // Se comprueba que se muestran todos los eventos
        onView(withId(R.id.eventsListView)).check(matches(withListSize(345)));
        //Vuelvo a la lista de favs y veo que no hay ninguna checkbox marcada
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withText(R.string.filtrar_por_categoria)).perform(click());
        onView(withId(R.id.artesEscenicas_checkBox)).check(matches(isNotChecked()));
        onView(withId(R.id.artesPlasticas_checkBox)).check(matches(isNotChecked()));
        onView(withId(R.id.talleres_checkBox)).check(matches(isNotChecked()));
        onView(withId(R.id.infantil_checkBox)).check(matches(isNotChecked()));
        onView(withId(R.id.otros_checkBox)).check(matches(isNotChecked()));
        onView(withId(R.id.musica_checkBox)).check(matches(isNotChecked()));

        //**IVF.1c No se selecciona ninguna categoría**
        onView(withId(R.id.categoryFilter_button_apply)).perform(click());
        // Idealmente Se comprueba que ha aparecido el toast
        // Se comprueba que se muestran todos los eventos
        onView(withId(R.id.eventsListView)).check(matches(withListSize(345)));
        //Vuelvo a la lista de favs y veo que no hay ninguna checkbox marcada
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withText(R.string.filtrar_por_categoria)).perform(click());
        onView(withId(R.id.artesEscenicas_checkBox)).check(matches(isNotChecked()));
        onView(withId(R.id.artesPlasticas_checkBox)).check(matches(isNotChecked()));
        onView(withId(R.id.talleres_checkBox)).check(matches(isNotChecked()));
        onView(withId(R.id.infantil_checkBox)).check(matches(isNotChecked()));
        onView(withId(R.id.otros_checkBox)).check(matches(isNotChecked()));
        onView(withId(R.id.musica_checkBox)).check(matches(isNotChecked()));
    }

}
