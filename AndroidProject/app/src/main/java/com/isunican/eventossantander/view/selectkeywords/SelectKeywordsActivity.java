package com.isunican.eventossantander.view.selectkeywords;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.isunican.eventossantander.R;
import com.isunican.eventossantander.view.events.EventArrayAdapter;
import com.isunican.eventossantander.view.events.EventsActivity;

import java.util.ArrayList;
import java.util.List;

public class SelectKeywordsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_keywords);
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
        customTitle.setText(R.string.seleccionar_palabras_clave); // Cambiamos el texto del title para saber que estamos en seleccionar palabras clave

        // Aplicamos la vista personalizada
        actionBar.setCustomView(customView);

        //TODO: ELIMINAR, ES UNA PRUEBA
        List<String> strs = new ArrayList<>();
        strs.add("FURBO");
        strs.add("TENIS");
        KeywordsArrayAdapter adapter;

        adapter = new KeywordsArrayAdapter(SelectKeywordsActivity.this, 0, strs);
        ListView listView = findViewById(R.id.keywordsListView);
        listView.setAdapter(adapter);
    }

    // Para cerrar la activity
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
