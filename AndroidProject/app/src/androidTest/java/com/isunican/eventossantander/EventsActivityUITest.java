package com.isunican.eventossantander;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.action.ViewActions.*;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;

import android.view.KeyEvent;
import android.view.View;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.view.events.EventsActivity;
import com.isunican.eventossantander.view.events.IEventsContract;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

public class EventsActivityUITest {

    private View decorView;

    /**
     * Load known events json
     * https://personales.unican.es/rivasjm/resources/agenda_cultural.json
     */
    @BeforeClass
    public static void setUp() {
        EventsRepository.setLocalSource();
    }

    @Before
    public void setUp2(){
        activityRule.getScenario().onActivity(
                activity -> decorView = activity.getWindow().getDecorView()
        );
    }

    @Rule
    public ActivityScenarioRule<EventsActivity> activityRule =
            new ActivityScenarioRule(EventsActivity.class);


    @Test
    public void vistaListaEventoBusquedaKeywords(){

        // IVF.1a Lista con 5 coincidencias
        onView(withId(R.id.et_PalabrasClave)).perform(typeText("Palacio de Festivales"),
                    pressKey(KeyEvent.KEYCODE_ENTER),closeSoftKeyboard());

        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0).onChildView(withId(R.id.item_event_title)).check(matches(withText("\"Entre nosotras\". Concierto")));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(4).onChildView(withId(R.id.item_event_title)).check(matches(withText("V Semana Internacional de Cine ")));


        //IVF.1b
        // No se puede realizar ya que Espresso no soporta comprobar la conexion a internet
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.et_PalabrasClave)).perform(replaceText(""), closeSoftKeyboard());

        // IVF.1c
        onView(withId(R.id.et_PalabrasClave)).perform(typeText("PalabraRara"),
                pressKey(KeyEvent.KEYCODE_ENTER),closeSoftKeyboard());

        onView(withText("No hay ningún evento relacionado con la búsqueda")).inRoot(RootMatchers.withDecorView(not(decorView))).check(matches(isDisplayed()));

        //assertThat(onView(withId(R.id.et_PalabrasClave)), hasSize(0));
        //onView(withId(R.id.eventsListView)).check(matches(nullValue()));

        // IVF.1d Volver y ver que se muestra la lista
        onView(withId(R.id.actionbarTitle)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0).onChildView(withId(R.id.item_event_title)).check(matches(withText("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea")));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(344).onChildView(withId(R.id.item_event_title)).check(matches(withText("Visiones Urbanas con ArteSantander 2021")));

    }
}
