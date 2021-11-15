package com.isunican.eventossantander.androidTest.categoryfilter;

import androidx.test.espresso.IdlingRegistry;

import com.isunican.eventossantander.model.EventsRepository;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CategoryFilterUITest {

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

    @Test
    public void categoryFilter() {
        //**IVF.1a **


        //**IVF.1b **


        //**IVF.1c **
    }

}
