package com.isunican.eventossantander.model.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventsAPIService {

    public enum Source {
        AYTO("http://datos.santander.es/api/rest/datasets/"),
        UNICAN("https://personales.unican.es/rivasjm/resources/");

        private final String url;

        private String getURL() {
            return url;
        }

        private Source(String url) {
            this.url = url;
        }
    }

    private static EventsAPI eventosService;

    public static EventsAPI getEventsServiceInstance() {
        return getEventsServiceInstance(Source.AYTO);
    }

    public static EventsAPI getEventsServiceInstance(Source source) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(source.getURL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            eventosService = retrofit.create(EventsAPI.class);
        return eventosService;
    }
}
