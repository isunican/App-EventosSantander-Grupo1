package com.isunican.eventossantander;
// unican
import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.presenter.eventsdetail.EventsDetailPresenter;
import com.isunican.eventossantander.utils.ISharedPrefs;
import com.isunican.eventossantander.view.eventsdetail.EventsDetailActivity;
// junit
import org.junit.runner.RunWith;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertNull;
// roboelectric
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
// android
import android.os.Build;
// mokito
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})

public class EventsDetailPresenterTest {

    private static EventsDetailPresenter sut;
    @Mock
    private static EventsDetailActivity mockView;
    @Mock
    private static ISharedPrefs mockPref;
    @Mock
    private static Event mockEvent;


    /**
     * Inicializa los mocks y el sut
     */
    @Before
    public void setUp() {
        mockView = mock(EventsDetailActivity.class);
        mockEvent = mock(Event.class);
        mockPref = mock(ISharedPrefs.class);

        sut = new EventsDetailPresenter(mockView, mockPref);
    }

    /**
     * Prueba unitaria del metodo onFavouriteEventsClicked()
     * Se usa un identificador por defecto de 1234
     */
    @Test
    public void onFavouriteEventsClickedTest() {
        int idEventoPrueba = 1234;
        when(mockEvent.getIdentificador()).thenReturn(idEventoPrueba);

        // UGIC.1a Añadir un evento existente a la lista de favoritos
        sut.onFavouriteEventsClicked(mockEvent, false);
        verify(mockPref,times(1)).newFavouriteEvent(mockEvent.getIdentificador());

        // UGIC.1b Eliminar un evento existente a la lista de favoritos
        sut.onFavouriteEventsClicked(mockEvent, true);
        verify(mockPref,times(1)).deleteFavouriteEvent(mockEvent.getIdentificador());

        // UGIC.1c Añadir un evento nulo a la lista de favoritos

        sut.onFavouriteEventsClicked(null, false);
        assertNull(mockEvent);
        verify(mockPref, times(0)).newFavouriteEvent(anyInt());

        // UGIC.1d Eliminar un evento nulo a la lista de favoritos
        sut.onFavouriteEventsClicked(mockEvent, true);
        assertNull(mockEvent);
        verify(mockPref, times(0)).deleteFavouriteEvent(anyInt());

    }
}
