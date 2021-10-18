package com.isunican.eventossantander;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.test.espresso.IdlingRegistry;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.presenter.events.EventsPresenter;
import com.isunican.eventossantander.view.events.IEventsContract;

import org.junit.AfterClass;
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
    @BeforeClass
    public static void setUp() {
        EventsRepository.setLocalSource();
        mockView = mock(IEventsContract.View.class);
        sut = new EventsPresenter(mockView);
    }

    @AfterClass
    public static void clean() {
        EventsRepository.setOnlineSource();
    }

    @Test
    public void onKeyWordsFilterTest() {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        when(mockView.hasInternetConnection()).thenReturn(true);
        List<Event> lista;
        sut.onKeywordsFilter("Palacio de Festivales");
        lista = sut.getCachedEvents();
        assertEquals(lista.size(), 5);
        assertEquals(lista.get(0).getNombre(), "\"Entre nosotras\". Concierto");
        assertEquals(lista.get(1).getNombre(), "Sheku e Isata Kanneh-Mason, violonchelo y piano");
        assertEquals(lista.get(2).getNombre(), "Grigori Sokolov, piano");
        assertEquals(lista.get(3).getNombre(), "Video Mapping sobre la fachada sur del Palacio de Festivales");
        assertEquals(lista.get(4).getNombre(), "V Semana Internacional de Cine ");
    }
}