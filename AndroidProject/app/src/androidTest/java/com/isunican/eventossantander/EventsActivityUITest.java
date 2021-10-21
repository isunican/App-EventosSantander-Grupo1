package com.isunican.eventossantander;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.action.ViewActions.*;
import static com.isunican.eventossantander.androidTest.utils.Matchers.withListSize;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.anything;

import android.view.KeyEvent;
import android.view.View;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.view.events.EventsActivity;
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
        IdlingRegistry.getInstance().register(EventsRepository.getIdlingResource());
    }

    @AfterClass
    public static void clean() {
        EventsRepository.setOnlineSource();
        IdlingRegistry.getInstance().unregister(EventsRepository.getIdlingResource());
    }

    /**
     * Metodo para poder obtener el DecorView de la activity para poder comprobar los Toast
     */
    @Before
    public void setUp2(){
        activityRule.getScenario().onActivity(
                activity -> decorView = activity.getWindow().getDecorView()
        );
    }

    @Rule
    public ActivityScenarioRule<EventsActivity> activityRule =
            new ActivityScenarioRule(EventsActivity.class);


    /**
     * Test de filtrar por palabras clave
     */
    @Test
    public void vistaListaEventoBusquedaKeywords(){

        String text1 = "Palacio de festivales";
        String text2 = "Palabrarara";

        // IVF.1a Lista con 5 coincidencias
        // Se introduce el texto poco a poco para comprobar la funcionalidad de busqueda automatica
        onView(withId(R.id.et_PalabrasClave)).perform(typeText(text1.substring(0,4)));
        onView(withId(R.id.eventsListView)).check(matches(withListSize(28)));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0).onChildView(withId(R.id.item_event_title)).check(matches(withText("Programa Académico UIMP-2021")));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(27).onChildView(withId(R.id.item_event_title)).check(matches(withText("Huellas de Spy en Candina")));
        onView(withId(R.id.et_PalabrasClave)).perform(typeText(text1.substring(4,8)));
        onView(withId(R.id.eventsListView)).check(matches(withListSize(11)));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0).onChildView(withId(R.id.item_event_title)).check(matches(withText("Programa Académico UIMP-2021")));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(10).onChildView(withId(R.id.item_event_title)).check(matches(withText("V Semana Internacional de Cine ")));
        onView(withId(R.id.et_PalabrasClave)).perform(typeText(text1.substring(8,12)));
        onView(withId(R.id.eventsListView)).check(matches(withListSize(5)));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0).onChildView(withId(R.id.item_event_title)).check(matches(withText("\"Entre nosotras\". Concierto")));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(4).onChildView(withId(R.id.item_event_title)).check(matches(withText("V Semana Internacional de Cine ")));
        onView(withId(R.id.et_PalabrasClave)).perform(typeText(text1.substring(12,16)));
        onView(withId(R.id.eventsListView)).check(matches(withListSize(5)));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0).onChildView(withId(R.id.item_event_title)).check(matches(withText("\"Entre nosotras\". Concierto")));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(4).onChildView(withId(R.id.item_event_title)).check(matches(withText("V Semana Internacional de Cine ")));
        onView(withId(R.id.et_PalabrasClave)).perform(typeText(text1.substring(16,20)));
        onView(withId(R.id.eventsListView)).check(matches(withListSize(5)));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0).onChildView(withId(R.id.item_event_title)).check(matches(withText("\"Entre nosotras\". Concierto")));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(4).onChildView(withId(R.id.item_event_title)).check(matches(withText("V Semana Internacional de Cine ")));
        onView(withId(R.id.et_PalabrasClave)).perform(typeText(text1.substring(20,21)));

        // Se presiona la tecla ENTER para hacer la busqueda con el string completo
        onView(withId(R.id.et_PalabrasClave)).perform(pressKey(KeyEvent.KEYCODE_ENTER), closeSoftKeyboard());

        //Se comprueba que salta el toast
        onView(withText("Cargados 5 eventos")).inRoot(RootMatchers.withDecorView(not(decorView))).check(matches(isDisplayed()));
        // Se comprueba que el tamaño de la lista de eventos es 5
        onView(withId(R.id.eventsListView)).check(matches(withListSize(5)));
        // Se comprueban que los eventos son los correctos
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0).onChildView(withId(R.id.item_event_title)).check(matches(withText("\"Entre nosotras\". Concierto")));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(4).onChildView(withId(R.id.item_event_title)).check(matches(withText("V Semana Internacional de Cine ")));


        //IVF.1b
        // No se puede realizar ya que Espresso no soporta cambios en la conexion a internet


        // IVF.1c Lista Vacia
        // Se limpia el contenido de la busqueda anterior
        onView(withId(R.id.et_PalabrasClave)).perform(replaceText(""), closeSoftKeyboard());
        // Se introduce el texto poco a poco para comprobar la funcionalidad de busqueda automatica
        onView(withId(R.id.et_PalabrasClave)).perform(typeText(text2.substring(0,4)));
        onView(withId(R.id.eventsListView)).check(matches(withListSize(28)));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0).onChildView(withId(R.id.item_event_title)).check(matches(withText("Programa Académico UIMP-2021")));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(27).onChildView(withId(R.id.item_event_title)).check(matches(withText("Huellas de Spy en Candina")));
        onView(withId(R.id.et_PalabrasClave)).perform(typeText(text2.substring(4,8)));
        onView(withId(R.id.eventsListView)).check(matches(withListSize(0)));
        onView(withId(R.id.et_PalabrasClave)).perform(typeText(text2.substring(8,11)));
        onView(withId(R.id.et_PalabrasClave)).perform(pressKey(KeyEvent.KEYCODE_ENTER), closeSoftKeyboard());

        //Se comprueba que salta el toast
        onView(withText("No hay ningún evento relacionado con la búsqueda")).inRoot(RootMatchers.withDecorView(not(decorView))).check(matches(isDisplayed()));
        // Se comprueba que el tamaño de la lista de eventos es 0
        onView(withId(R.id.eventsListView)).check(matches(withListSize(0)));

        // IVF.1d Limpiar filtro y ver que se muestra la lista
        onView(withId(R.id.actionbarTitle)).perform(click());

        // Se comprueba que el tamaño de la lista de eventos es 0
        onView(withId(R.id.eventsListView)).check(matches(withListSize(345)));

        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0).onChildView(withId(R.id.item_event_title)).check(matches(withText("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea")));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(344).onChildView(withId(R.id.item_event_title)).check(matches(withText("Visiones Urbanas con ArteSantander 2021")));
    }
}
