package com.isunican.eventossantander.presenter.selectkeywords;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Build;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.utils.ISharedPrefs;
import com.isunican.eventossantander.view.selectkeywords.SelectKeywordsActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
public class SelectKeywordsPresenterTest {

    private static SelectKeywordsPresenter sut;
    @Mock
    private static SelectKeywordsActivity mockView;
    @Mock
    private static ISharedPrefs mockPreferences;

    /**
     * Inicializa los mocks y el sut
     */
    @Before
    public void setUp() {
        mockView = mock(SelectKeywordsActivity.class);
        mockPreferences = mock(ISharedPrefs.class);

        // Inicializamos la lista
        List<String> listaKW = new ArrayList<>();
        listaKW.add("Teatro");
        listaKW.add("Surf");
        listaKW.add("Online");
        listaKW.add("Sardinero");
        listaKW.add("Santander");
        when(mockPreferences.getSelectedKeywords()).thenReturn(listaKW);

        sut = new SelectKeywordsPresenter(mockView, mockPreferences);
    }

    /**
     * US435007-EliminarPalabrasClave-TestPlan
     * Test: onDeleteKeyword(int keywordIndex) method
     * @author: Pablo Almohalla Gómez
     */
    @Test
    public void onDeleteKeywordTest() {

        // ** UGIC.1a - Elimina el primer elemento de la lista **
        sut.onDeleteKeyword(0);
        assertEquals(4, sut.getKeywordsList().size());

        // ** UGIC.1b Eliminar el último elemento de la lista **
        sut.onDeleteKeyword(3);
        assertEquals(3, sut.getKeywordsList().size());

        // ** UGIC.1c Eliminar el elemento del medio de la lista **
        sut.onDeleteKeyword(1);
        assertEquals(2, sut.getKeywordsList().size());

        // ** UGIC.1d Eliminar un elemento negativo de la lista **
        sut.onDeleteKeyword(-1);
        assertEquals(2, sut.getKeywordsList().size());

        // ** UGIC.1e Eliminar un elemento más grande que la lista **
        sut.onDeleteKeyword(5);
        assertEquals(2, sut.getKeywordsList().size());


        // En el verify comprobamos que el método onDeletedKeywordSuccess() se ha invocado 3 veces
        verify(mockView, times(3)).onDeletedKeywordSuccess(anyString());

        // En el verify comprobamos que el método onKeywordsLoaded() se ha invocado 4 veces (1 de onLoadKewordsListFromLocal y 3 del test)
        verify(mockView, times(4)).onKeywordsLoaded(anyList());

        // En el verify comprobamos que el método onDeleteKeywordFail() se ha invocado 2 veces
        verify(mockView, times(2)).onDeleteKeywordFail();
    }

}
