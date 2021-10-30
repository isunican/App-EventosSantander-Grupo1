package com.isunican.eventossantander.view.favouriteevents;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.isunican.eventossantander.R;
import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.presenter.favouriteevents.FavouriteEventsPresenter;
import com.isunican.eventossantander.view.eventsdetail.EventsDetailActivity;

import java.util.List;

public class FavouriteEventsActivity extends AppCompatActivity implements IFavouriteEventsContract.View{

    private Toast msgToast;
    private IFavouriteEventsContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_events);
        presenter = new FavouriteEventsPresenter(this);
        configItems();
    }

    private void configItems() {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true); // Botón home en la barra superior
        actionBar.setDisplayShowHomeEnabled(true);

        actionBar.setDisplayShowTitleEnabled(false); // Desactiva el title por defecto
        actionBar.setDisplayShowCustomEnabled(true); // Activa el title personalizable
        View customView = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        TextView customTitle = (TextView) customView.findViewById(R.id.actionbarTitle); // Obtiene el textview del title
        customTitle.setText(R.string.eventos_favoritos); // Cambiamos el texto del title para saber que estamos en favoritos

        // Aplicamos la vista personalizada
        actionBar.setCustomView(customView);
    }

    // Para cerrar la activity
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onEventsLoaded(List<Event> events) {
        FavouriteEventArrayAdapter adapter;

        adapter = new FavouriteEventArrayAdapter(FavouriteEventsActivity.this, 0, events);
        ListView listView = findViewById(R.id.FavouriteEventsListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> presenter.onEventClicked(position));
    }

    @Override
    public void onLoadError() {
        if (msgToast != null) {
            msgToast.cancel();
        }
        msgToast = Toast.makeText(this, "Se ha producido un error al cargar los eventos favoritos", Toast.LENGTH_SHORT);
        msgToast.show();
    }

    @Override
    public void onLoadSuccess(int elementsLoaded) {
        if (msgToast != null) {
            msgToast.cancel();
        }
        msgToast = Toast.makeText(this, String.format("Cargados %d eventos favoritos", elementsLoaded), Toast.LENGTH_SHORT);

        if(msgToast != null) {
            msgToast.show();
        }
    }

    @Override
    public void openEventDetails(Event event) {
        Intent intent = new Intent(this, EventsDetailActivity.class);
        intent.putExtra(EventsDetailActivity.INTENT_EVENT, event);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        presenter.onReloadFavourites();
        super.onRestart();
    }

}
