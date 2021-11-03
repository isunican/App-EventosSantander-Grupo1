package com.isunican.eventossantander.view.events;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.isunican.eventossantander.R;
import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.presenter.events.EventsPresenter;
import com.isunican.eventossantander.utils.AccessSharedPrefs;
import com.isunican.eventossantander.utils.ISharedPrefs;
import com.isunican.eventossantander.view.categoryfilter.CategoryFilterActivity;
import com.isunican.eventossantander.view.eventsdetail.EventsDetailActivity;
import com.isunican.eventossantander.view.info.InfoActivity;

import java.util.List;

public class EventsActivity extends AppCompatActivity implements IEventsContract.View {

    private IEventsContract.Presenter presenter;
    private EditText inputSearch;

    private Toast msgToast;

    private boolean categoryFilter = false;
    ISharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPrefs = new AccessSharedPrefs(getApplicationContext());
        presenter = new EventsPresenter(this, sharedPrefs);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(categoryFilter) presenter.onCategoryFilter();
        categoryFilter = false;
    }

    @Override
    public void onEventsLoaded(List<Event> events) {
        EventArrayAdapter adapter;

        adapter = new EventArrayAdapter(EventsActivity.this, 0, events);
        ListView listView = findViewById(R.id.categorysListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> presenter.onEventClicked(position));
    }

    @Override
    public void onLoadError() {
        if (msgToast != null) {
            msgToast.cancel();
        }
        msgToast = Toast.makeText(this, "Se ha producido un error al cargar los eventos", Toast.LENGTH_SHORT);
        msgToast.show();
    }

    @Override
    public void onLoadSuccess(int elementsLoaded, boolean showMessage) {
        if (msgToast != null) {
            msgToast.cancel();
        }
        if (elementsLoaded == 0) {
            msgToast = Toast.makeText(this, "No hay ningún evento relacionado con la búsqueda", Toast.LENGTH_SHORT);
        } else if (showMessage) {
            msgToast = Toast.makeText(this, String.format("Cargados %d eventos", elementsLoaded), Toast.LENGTH_SHORT);
        }
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
    public void openInfoView() {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }

    @Override
    public void openCategoryFilterView() {
        categoryFilter = true;
        Intent intent = new Intent(this, CategoryFilterActivity.class);
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
            case R.id.menu_filtrarCategoria:
                presenter.onCategoryFilterClicked();
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

        return info != null;
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
                    presenter.onKeywordsFilter(inputSearch.getText().toString(), true, false);
                }
                return true;
            }
            return false;
        });

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public final void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public final void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.onKeywordsFilter(s.toString(), false, false);
            }

            @Override
            public final void afterTextChanged(Editable s) { }

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