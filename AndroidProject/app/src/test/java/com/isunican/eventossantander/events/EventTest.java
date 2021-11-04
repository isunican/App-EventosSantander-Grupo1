package com.isunican.eventossantander.events;

import com.isunican.eventossantander.model.Event;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventTest {

    @Test
    public void testEventCreation() {
        Event event = new Event();
        event.setDescripcionAlternativa("desc alt");
        event.setDescripcion("desc");
        assertEquals("desc alt", event.getDescripcionAlternativa());
    }
}
