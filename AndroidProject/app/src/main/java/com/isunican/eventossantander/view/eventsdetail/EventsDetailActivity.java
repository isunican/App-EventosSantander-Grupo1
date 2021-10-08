package com.isunican.eventossantander.view.eventsdetail;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.isunican.eventossantander.R;
import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.view.events.EventArrayAdapter;
import com.squareup.picasso.Picasso;

public class EventsDetailActivity extends AppCompatActivity {

    public static final String INTENT_EVENT = "INTENT_EVENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_detail);

        ActionBar actBar = getSupportActionBar();

        actBar.setDisplayHomeAsUpEnabled(true); // Botón home en la barra superior
        actBar.setDisplayShowHomeEnabled(true);

        // Link to view elements
        TextView eventTitleText = findViewById(R.id.event_detail_title);
        TextView eventDateText = findViewById(R.id.event_detail_date);
        TextView eventDescripText = findViewById(R.id.event_detail_descrip);
        ImageView eventImage = findViewById(R.id.event_detail_Image);
        Button eventButton = findViewById(R.id.event_detail_MasInfo);

        //GoogleMap googleMap = ((MapView) findViewById(R.id.event_detail_mapView)).getMap();

        // Get Event from the intent that triggered this activity
        Event event = getIntent().getExtras().getParcelable(INTENT_EVENT);

        // Set information
        eventTitleText.setText(event.getNombre());
        eventDateText.setText(event.getFecha());
        eventDescripText.setText(Html.fromHtml(event.getDescripcion()));
        eventDescripText.setMovementMethod(LinkMovementMethod.getInstance());
        if (event.getImagen() != "" || event.getImagen() == null) {
            eventImage.setVisibility(View.INVISIBLE);//Si el evento no tiene imagen, no se muestra
        } else {
            Picasso.get().load(event.getImagen()).into(eventImage);
        }
        eventImage.setAdjustViewBounds(true);
        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getEnlace()));
                startActivity(browserIntent);
            }
        });
        //googleMap.addMarker(new MarkerOptions().position(new LatLng( event.getLatitud(), -event.getLongitud())).title("Marker"));

    }


}