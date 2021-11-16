package com.isunican.eventossantander.androidTest.selectkeywords;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.isunican.eventossantander.androidTest.utils.Matchers.withListSize;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.anything;

import android.app.Application;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.isunican.eventossantander.R;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.utils.AccessSharedPrefs;
import com.isunican.eventossantander.view.events.EventsActivity;
import com.isunican.eventossantander.view.selectkeywords.SelectKeywordsActivity;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

public class SelectKeywordsActivityUITest {

    private static AccessSharedPrefs sharedPrefs;

    /**
     * Load known events json
     * https://personales.unican.es/rivasjm/resources/agenda_cultural.json
     */
    @BeforeClass
    public static void setUp() {
        EventsRepository.setLocalSource();
        IdlingRegistry.getInstance().register(EventsRepository.getIdlingResource());
        sharedPrefs = new AccessSharedPrefs(ApplicationProvider.getApplicationContext());
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
     * US422732-SeleccionarPalabrasClave-TestPlan
     * Test: agregar palabras clave seleccionadas
     * @author: Pablo Almohalla Gómez
     */
    @Test
    public void addKeywordIntoSelectedKeywordList() {

        // Accedo a la lista de palabras clave seleccionadas
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getContext());
        // Introducimos una espera de 1 segundo para que de tiempo a que aparezca el desplegable con la opción de Palabras Clave
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText(R.string.seleccionar_palabras_clave)).perform(click());

        // ** IVF.1a - Se añade una palabra clave que existe **
        onView(ViewMatchers.withId(R.id.et_SelectKeywords)).perform(typeText("Teatro"));
        onView(ViewMatchers.withId(R.id.btn_add_keyword)).perform(click());
        // Comprobamos que el tamaño de la lista es 1
        onView(withId(R.id.keywordsListView)).check(matches(withListSize(1)));
        onData(anything()).inAdapterView(withId(R.id.keywordsListView)).atPosition(0).onChildView(withId(R.id.item_keyword_name)).check(matches(withText("TEATRO")));

        // ** IVF.1b - Se pulsa el botón de aplicar **
        onView(ViewMatchers.withId(R.id.btn_apply_keywords_filter)).perform(click());
        // Comprobamos que el tamaño de la lista de eventos con el filtro aplicado es 32
        onView(withId(R.id.eventsListView)).check(matches(withListSize(32)));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0).onChildView(withId(R.id.item_event_title)).check(matches(withText("\"Lo que (no) se ve\", Cuartoymitad Teatro")));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(31).onChildView(withId(R.id.item_event_title)).check(matches(withText("Calle Cultura, huellas de Laura Irizabal")));

        // ** IVF.1c - Se pulsa el botón del menú Palabras Clave **
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getContext());
        // Introducimos una espera de 1 segundo para que de tiempo a que aparezca el desplegable con la opción de Palabras Clave
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText(R.string.seleccionar_palabras_clave)).perform(click());
        // Comprobamos que el tamaño de la lista es 1
        onView(withId(R.id.keywordsListView)).check(matches(withListSize(1)));
        onData(anything()).inAdapterView(withId(R.id.keywordsListView)).atPosition(0).onChildView(withId(R.id.item_keyword_name)).check(matches(withText("TEATRO")));

        // ** IVF.1d - Se añade otra palabra clave que existe **
        onView(ViewMatchers.withId(R.id.et_SelectKeywords)).perform(typeText("Surf"));
        onView(ViewMatchers.withId(R.id.btn_add_keyword)).perform(click());
        // Comprobamos que el tamaño de la lista es 2
        onView(withId(R.id.keywordsListView)).check(matches(withListSize(2)));
        onData(anything()).inAdapterView(withId(R.id.keywordsListView)).atPosition(0).onChildView(withId(R.id.item_keyword_name)).check(matches(withText("SURF")));

        // ** IVF.1e - Se añade una palabra clave vacía **
        onView(ViewMatchers.withId(R.id.et_SelectKeywords)).perform(typeText(""));
        onView(ViewMatchers.withId(R.id.btn_add_keyword)).perform(click());
        // Comprobamos que el tamaño de la lista es 2
        onView(withId(R.id.keywordsListView)).check(matches(withListSize(2)));
        onData(anything()).inAdapterView(withId(R.id.keywordsListView)).atPosition(0).onChildView(withId(R.id.item_keyword_name)).check(matches(withText("SURF")));

        // ** IVF.1f - Se añade una palabra clave con espacios **
        onView(ViewMatchers.withId(R.id.et_SelectKeywords)).perform(typeText("Teatro Online"));
        onView(ViewMatchers.withId(R.id.btn_add_keyword)).perform(click());
        // Comprobamos que el tamaño de la lista es 2
        onView(withId(R.id.keywordsListView)).check(matches(withListSize(2)));
        onData(anything()).inAdapterView(withId(R.id.keywordsListView)).atPosition(0).onChildView(withId(R.id.item_keyword_name)).check(matches(withText("SURF")));

        // ** IVF.1g - Se pulsa el botón de aplicar **
        onView(ViewMatchers.withId(R.id.btn_apply_keywords_filter)).perform(click());
        // Comprobamos que el tamaño de la lista de eventos con el filtro aplicado es 33
        onView(withId(R.id.eventsListView)).check(matches(withListSize(33)));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0).onChildView(withId(R.id.item_event_title)).check(matches(withText("The Sadies")));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(32).onChildView(withId(R.id.item_event_title)).check(matches(withText("Calle Cultura, huellas de Laura Irizabal")));

        // ** IVF.1h - Se pulsa el botón del menú Palabras Clave **
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getContext());
        // Introducimos una espera de 1 segundo para que de tiempo a que aparezca el desplegable con la opción de Palabras Clave
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText(R.string.seleccionar_palabras_clave)).perform(click());
        // Comprobamos que el tamaño de la lista es 2
        onView(withId(R.id.keywordsListView)).check(matches(withListSize(2)));
        onData(anything()).inAdapterView(withId(R.id.keywordsListView)).atPosition(0).onChildView(withId(R.id.item_keyword_name)).check(matches(withText("SURF")));

        // Vaciamos el sharedPreferences para la ejecución consecutiva de pruebas
        sharedPrefs.clearAllKeywords();
    }

    /**
     * US435007-EliminarPalabrasClave-TestPlan
     * Test: eliminar palabras clave seleccionadas
     * @author: Pablo Almohalla Gómez
     */
    @Test
    public void deleteKeywordFromSelectedKeywordList() {

        // Accedo a la lista de palabras clave seleccionadas
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getContext());
        // Introducimos una espera de 1 segundo para que de tiempo a que aparezca el desplegable con la opción de Palabras Clave
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText(R.string.seleccionar_palabras_clave)).perform(click());

        // Inicializamos la lista
        onView(ViewMatchers.withId(R.id.et_SelectKeywords)).perform(typeText("Teatro"));
        onView(ViewMatchers.withId(R.id.btn_add_keyword)).perform(click());
        onView(ViewMatchers.withId(R.id.et_SelectKeywords)).perform(typeText("Surf"));
        onView(ViewMatchers.withId(R.id.btn_add_keyword)).perform(click());
        // Comprobamos que el tamaño de la lista es 2
        onView(withId(R.id.keywordsListView)).check(matches(withListSize(2)));
        onData(anything()).inAdapterView(withId(R.id.keywordsListView)).atPosition(0).onChildView(withId(R.id.item_keyword_name)).check(matches(withText("SURF")));
        onData(anything()).inAdapterView(withId(R.id.keywordsListView)).atPosition(1).onChildView(withId(R.id.item_keyword_name)).check(matches(withText("TEATRO")));
        // Aplicamos los cambios
        onView(ViewMatchers.withId(R.id.btn_apply_keywords_filter)).perform(click());
        // Comprobamos que el tamaño de la lista de eventos con el filtro aplicado es 33
        onView(withId(R.id.eventsListView)).check(matches(withListSize(33)));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0).onChildView(withId(R.id.item_event_title)).check(matches(withText("The Sadies")));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(32).onChildView(withId(R.id.item_event_title)).check(matches(withText("Calle Cultura, huellas de Laura Irizabal")));
        // Entramos al menú de Palabras Clave
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getContext());
        // Introducimos una espera de 1 segundo para que de tiempo a que aparezca el desplegable con la opción de Palabras Clave
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText(R.string.seleccionar_palabras_clave)).perform(click());
        // Comprobamos que el tamaño de la lista es 2
        onView(withId(R.id.keywordsListView)).check(matches(withListSize(2)));
        onData(anything()).inAdapterView(withId(R.id.keywordsListView)).atPosition(0).onChildView(withId(R.id.item_keyword_name)).check(matches(withText("SURF")));

        // ** IVF.1a - Se elimina una palabra clave que existe **
        onData(anything()).inAdapterView(withId(R.id.keywordsListView)).atPosition(0).onChildView(withId(R.id.item_btn_delete_keyword)).perform(click());
        // Comprobamos que el tamaño de la lista es 1
        onView(withId(R.id.keywordsListView)).check(matches(withListSize(1)));
        onData(anything()).inAdapterView(withId(R.id.keywordsListView)).atPosition(0).onChildView(withId(R.id.item_keyword_name)).check(matches(withText("TEATRO")));

        // ** IVF.1b - Se pulsa el botón de aplicar **
        onView(ViewMatchers.withId(R.id.btn_apply_keywords_filter)).perform(click());
        // Comprobamos que el tamaño de la lista de eventos con el filtro aplicado es 32
        onView(withId(R.id.eventsListView)).check(matches(withListSize(32)));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0).onChildView(withId(R.id.item_event_title)).check(matches(withText("\"Lo que (no) se ve\", Cuartoymitad Teatro")));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(31).onChildView(withId(R.id.item_event_title)).check(matches(withText("Calle Cultura, huellas de Laura Irizabal")));

        // ** IVF.1c - Se pulsa el botón del menú Palabras Clave **
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getContext());
        // Introducimos una espera de 1 segundo para que de tiempo a que aparezca el desplegable con la opción de Palabras Clave
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText(R.string.seleccionar_palabras_clave)).perform(click());
        // Comprobamos que el tamaño de la lista es 1
        onView(withId(R.id.keywordsListView)).check(matches(withListSize(1)));
        onData(anything()).inAdapterView(withId(R.id.keywordsListView)).atPosition(0).onChildView(withId(R.id.item_keyword_name)).check(matches(withText("TEATRO")));

        // ** IVF.1d - Se pulsa el botón de limpiar **
        onView(ViewMatchers.withId(R.id.btn_clean_keywords)).perform(click());
        // Comprobamos que el tamaño de la lista de eventos con el filtro aplicado es 345
        onView(withId(R.id.eventsListView)).check(matches(withListSize(345)));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0).onChildView(withId(R.id.item_event_title)).check(matches(withText("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea")));
        onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(344).onChildView(withId(R.id.item_event_title)).check(matches(withText("Visiones Urbanas con ArteSantander 2021")));

        // Vaciamos el sharedPreferences para la ejecución consecutiva de pruebas
        sharedPrefs.clearAllKeywords();
    }

}
