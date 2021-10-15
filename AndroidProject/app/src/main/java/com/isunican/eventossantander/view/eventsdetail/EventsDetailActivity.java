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

        // Get Event from the intent that triggered this activity
        Event event = getIntent().getExtras().getParcelable(INTENT_EVENT);

        // Set information
        eventTitleText.setText(event.getNombre());
        eventDateText.setText(event.getFecha());
        eventDescripText.setText(Html.fromHtml(event.getDescripcion()));
        eventDescripText.setMovementMethod(LinkMovementMethod.getInstance());
        if (event.getImagen().equals("") || event.getImagen() == null) {
            eventImage.setVisibility(View.INVISIBLE);//Si el evento no tiene imagen, no se muestra
        } else {
            Picasso.get().load(event.getImagen()).into(eventImage);
        }
        eventImage.setAdjustViewBounds(true);
        eventButton.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getEnlace()));
            startActivity(browserIntent);
        });

    }

    // Para cerrar la activity
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}