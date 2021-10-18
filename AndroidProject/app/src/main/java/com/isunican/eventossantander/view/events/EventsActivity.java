package com.isunican.eventossantander.view.events;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.isunican.eventossantander.R;
import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.presenter.events.EventsPresenter;
import com.isunican.eventossantander.view.eventsdetail.EventsDetailActivity;
import com.isunican.eventossantander.view.info.InfoActivity;

import java.util.List;

public class EventsActivity extends AppCompatActivity implements IEventsContract.View {

    private IEventsContract.Presenter presenter;
    private EditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new EventsPresenter(this);
    }

    @Override
    public void onEventsLoaded(List<Event> events) {
        EventArrayAdapter adapter;

        if (!inputSearch.getText().toString().equals("")) {
            String str = inputSearch.getText().toString();
            inputSearch.setText("");
            presenter.onKeywordsFilter(str);
            inputSearch.setText(str);
            return;
        }

        adapter = new EventArrayAdapter(EventsActivity.this, 0, events);
        ListView listView = findViewById(R.id.eventsListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> presenter.onEventClicked(position));
    }

    @Override
    public void onLoadError() {
        Toast.makeText(this, "Se ha producido un error al cargar los eventos", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadSuccess(int elementsLoaded, boolean showMessage) {
        if (showMessage) {
            String text = "";
            if (elementsLoaded == 0) {
                text = "No hay ningún evento relacionado con la búsqueda";
            } else {
                text = String.format("Cargados %d eventos", elementsLoaded);
            }
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void openEventDetails(Event event) {
        Intent intent = new Intent(this, EventsDetailActivity.class);
        intent.putExtra(EventsDetailActivity.INTENT_EVENT, event);
        startActivity(intent);
    }

    @Override
    public void openInfoView() {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }

    public IEventsContract.Presenter getPresenter() {
        return presenter;
    }

    /*
    Menu Handling
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                boolean showMsg = false;
                if (inputSearch.getText().toString().equals("")) {
                    showMsg = true;
                }
                presenter.onReloadClicked(showMsg);
                return true;
            case R.id.menu_info:
                presenter.onInfoClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onInternetConnectionFailure() {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle("Error de conectividad");
        alert.setMessage("Conéctate a una red Wi-Fi o móvil y pulse el botón de actualizar.");
        alert.setButton(DialogInterface.BUTTON_POSITIVE,"Actualizar", (dialog, which) ->
            presenter.onReloadClicked(true)
        );
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, "Cerrar", (dialogInterface, i) -> dialogInterface.dismiss());
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = alert.getWindow().getAttributes();

        wmlp.gravity = Gravity.BOTTOM;
        alert.show();
    }

    @Override
    public boolean hasInternetConnection() {

        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null) {
            return false;
        }else {return true;}
    }

    @Override
    public void onLoadingItems() {
        inputSearch = (EditText) findViewById(R.id.et_PalabrasClave);
        inputSearch.setOnKeyListener((v, keyCode, event) -> {
            // Si el evento es enter
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //Para esconder el teclado una vez el usuario pulse enter
                if (inputSearch.getText().toString().isEmpty()) {
                    return false;
                } else {
                    presenter.onKeywordsFilter(inputSearch.getText().toString());
                }
                return true;
            }
            return false;
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false); // Desactiva el title por defecto
            actionBar.setDisplayShowCustomEnabled(true); // Activa el title personalizable
            View customView = getLayoutInflater().inflate(R.layout.actionbar_title, null);
            TextView customTitle = (TextView) customView.findViewById(R.id.actionbarTitle); // Obtiene el textview del title

            // Establecemos un listener para cuando se pulsa el title
            customTitle.setOnClickListener(v -> {
                inputSearch.setText("");
                presenter.onReloadCachedEventsClicked();
            });
            // Aplicamos la vista personalizada
            actionBar.setCustomView(customView);
        }
    }
}