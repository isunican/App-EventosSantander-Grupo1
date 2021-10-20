package com.isunican.eventossantander.androidTest.utils;

import android.view.View;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class Matchers {

    /**
     * Metodo para comprobar en los test de interfaz si una lista tiene un tamaño determinado. Para
     * utilizarlo se usa:
     *      onView(withId(R.id.id_de_la_lista)).check(matches(withListSize(tamaño_que_deberia_tener)))
     * @param size
     * @return Matcher<View>
     */
    public static Matcher<View> withListSize (final int size) {
        return new TypeSafeMatcher<View>() {
            @Override public boolean matchesSafely (final View view) {
                return ((ListView) view).getCount () == size;
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("ListView should have " + size + " items");
            }
        };
    }

}
