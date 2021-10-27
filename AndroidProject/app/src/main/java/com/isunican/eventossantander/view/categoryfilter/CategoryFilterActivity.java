package com.isunican.eventossantander.view.categoryfilter;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.isunican.eventossantander.R;
import com.isunican.eventossantander.presenter.events.EventsPresenter;

public class CategoryFilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_filter);

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
        customTitle.setText(R.string.filtrar_por_categoria); // Cambiamos el texto del title para saber que estamos en filtrar por categoria

        // Aplicamos la vista personalizada
        actionBar.setCustomView(customView);
    }

    // Para cerrar la activity
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
