package com.isunican.eventossantander.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.isunican.eventossantander.model.Event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LocalEvents {

    private static final String KEY_LOCAL_EVENTS = "ALL_LOCAL_EVENTS";
    private static final String NUM_EVENTS = "NUM_EVENTS";

    private static final String[] eventsPartsNames = { "identificador",  "nombre", "nombreAlternativo", "categoria", "descripcion",
            "descripcionAlternativa", "fecha", "longitud", "latitud", "enlace", "enlaceAlternativo", "imagen"};

    public static List<Event> loadDataFromLocal (Context c) {
        SharedPreferences sharedPref = c.getSharedPreferences(KEY_LOCAL_EVENTS, Context.MODE_PRIVATE);
        int numEvents = sharedPref.getInt(NUM_EVENTS, 0);
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < numEvents; i++) {
            Event newEvent = new Event(sharedPref.getInt("evento" + i + eventsPartsNames[0], 0),
                    sharedPref.getString("evento" + i + eventsPartsNames[1], null),
                    sharedPref.getString("evento" + i + eventsPartsNames[2], null),
                    sharedPref.getString("evento" + i + eventsPartsNames[3], null),
                    sharedPref.getString("evento" + i + eventsPartsNames[4], null),
                    sharedPref.getString("evento" + i + eventsPartsNames[5], null),
                    sharedPref.getString("evento" + i + eventsPartsNames[6], null),
                    Double.parseDouble(sharedPref.getString("evento" + i + eventsPartsNames[7], null)),
                    Double.parseDouble(sharedPref.getString("evento" + i + eventsPartsNames[8], null)),
                    sharedPref.getString("evento" + i + eventsPartsNames[9], null),
                    sharedPref.getString("evento" + i + eventsPartsNames[10], null),
                    sharedPref.getString("evento" + i + eventsPartsNames[11], null)
            );
            events.add(newEvent);
        }
        return events;
    }

    public static void saveDataToLocal (Context c, List<Event> events) {
        SharedPreferences sharedPref = c.getSharedPreferences(KEY_LOCAL_EVENTS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit(); // Borramos todos los datos locales
        editor.clear();
        editor.apply();
        int counter = 0;
        for (Event e: events) {
            editor.putInt("evento" + counter + eventsPartsNames[0], e.getIdentificador());
            editor.putString("evento" + counter + eventsPartsNames[1], e.getNombre());
            editor.putString("evento" + counter + eventsPartsNames[2], e.getNombreAlternativo());
            editor.putString("evento" + counter + eventsPartsNames[3], e.getCategoria());
            editor.putString("evento" + counter + eventsPartsNames[4], e.getDescripcion());
            editor.putString("evento" + counter + eventsPartsNames[5], e.getDescripcionAlternativa());
            editor.putString("evento" + counter + eventsPartsNames[6], e.getFecha());
            editor.putString("evento" + counter + eventsPartsNames[7], String.valueOf(e.getLongitud()));
            editor.putString("evento" + counter + eventsPartsNames[8], String.valueOf(e.getLatitud()));
            editor.putString("evento" + counter + eventsPartsNames[9], e.getEnlace());
            editor.putString("evento" + counter + eventsPartsNames[10], e.getEnlaceAlternativo());
            editor.putString("evento" + counter + eventsPartsNames[11], e.getImagen());
            counter++;
        }
        editor.putInt(NUM_EVENTS, counter);
        editor.apply();
    }

    /*public static void saveDataToLocal (Context c, List<Event> events) {
        SharedPreferences sharedPref = c.getSharedPreferences(KEY_LOCAL_EVENTS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit(); // Borramos todos los datos locales
        editor.clear();
        editor.apply();
        int counter = 0;
        Set<String> eventoStrings;
        for (Event e: events) {
            eventoStrings = new HashSet<>();
            eventoStrings.add(String.valueOf(e.getIdentificador()));
            eventoStrings.add(e.getNombre());
            eventoStrings.add(e.getNombreAlternativo());
            eventoStrings.add(e.getCategoria());
            eventoStrings.add(e.getDescripcion());
            eventoStrings.add(e.getDescripcionAlternativa());
            eventoStrings.add(e.getFecha());
            eventoStrings.add(String.valueOf(e.getLongitud()));
            eventoStrings.add(String.valueOf(e.getLatitud()));
            eventoStrings.add(e.getEnlace());
            eventoStrings.add(e.getEnlaceAlternativo());
            eventoStrings.add(e.getImagen());
            editor.putStringSet("evento" + counter, eventoStrings);
            counter++;
        }
        editor.putInt(NUM_EVENTS, counter);
        editor.apply();
    }*/

}
