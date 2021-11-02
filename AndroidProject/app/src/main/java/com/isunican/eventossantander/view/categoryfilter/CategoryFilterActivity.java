package com.isunican.eventossantander.view.categoryfilter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.isunican.eventossantander.R;
import com.isunican.eventossantander.presenter.categoryfilter.CategoryPresenter;
import com.isunican.eventossantander.utils.AccessSharedPrefs;
import com.isunican.eventossantander.utils.ISharedPrefs;

import java.util.HashSet;
import java.util.Set;

public class CategoryFilterActivity extends AppCompatActivity implements ICategoryContract.View{

    private ICategoryContract.Presenter presenter;

    private CheckBox talleres_checkBox;
    private CheckBox artes_escenicas_checkBox;
    private CheckBox artes_plasticas_checkBox;
    private CheckBox infantil_checkBox;
    private CheckBox musica_checkBox;
    private CheckBox otros_checkBox;

    private Button limpiar_button;
    private Button aplicar_button;

    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ISharedPrefs sharedPref = new AccessSharedPrefs(getApplicationContext());
        presenter = new CategoryPresenter(this, sharedPref);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_filter);
        configItems();
    }

    private void configItems() {
        ActionBar actionBar = getSupportActionBar();
        talleres_checkBox = findViewById(R.id.talleres_checkBox);
        artes_escenicas_checkBox = findViewById(R.id.artesEscenicas_checkBox);
        artes_plasticas_checkBox = findViewById(R.id.artesPlasticas_checkBox);
        infantil_checkBox = findViewById(R.id.infantil_checkBox);
        musica_checkBox = findViewById(R.id.musica_checkBox);
        otros_checkBox = findViewById(R.id.otros_checkBox);

        if (presenter.getEstadoCheckBoxes(getString(R.string.talleres))) talleres_checkBox.setChecked(true);
        if (presenter.getEstadoCheckBoxes(getString(R.string.artes_escenicas))) artes_escenicas_checkBox.setChecked(true);
        if (presenter.getEstadoCheckBoxes(getString(R.string.artes_plasticas))) artes_plasticas_checkBox.setChecked(true);
        if (presenter.getEstadoCheckBoxes(getString(R.string.infantil))) infantil_checkBox.setChecked(true);
        if (presenter.getEstadoCheckBoxes(getString(R.string.musica))) musica_checkBox.setChecked(true);
        if (presenter.getEstadoCheckBoxes(getString(R.string.otros))) otros_checkBox.setChecked(true);

        limpiar_button = findViewById(R.id.categoryFilter_button_clear);
        aplicar_button = findViewById(R.id.categoryFilter_button_apply);

        aplicar_button.setOnClickListener( v->{
                    onAplicarClicked();
                }
        );

        limpiar_button.setOnClickListener( v-> {
            onLimpiarClicked();
        }
        );

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

    private void onLimpiarClicked() {
        Set<String> categorias = new HashSet<>();
        presenter.onAplicarFiltroCategoria(categorias);
        finish();
    }

    @Override
    public void onAplicarClicked(){
        Set<String> categorias = new HashSet<>();
        if(talleres_checkBox.isChecked()){
            categorias.add(getString(R.string.talleres));
        }
        if(artes_escenicas_checkBox.isChecked()){
            categorias.add(getString(R.string.artes_escenicas));
        }
        if(artes_plasticas_checkBox.isChecked()){
            categorias.add(getString(R.string.artes_plasticas));
        }
        if(musica_checkBox.isChecked()){
            categorias.add(getString(R.string.musica));
        }
        if(infantil_checkBox.isChecked()){
            categorias.add(getString(R.string.infantil));
        }
        if(otros_checkBox.isChecked()){
            categorias.add(getString(R.string.otros));
        }

        presenter.onAplicarFiltroCategoria(categorias);

        finish();

    }

    // Para cerrar la activity
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
