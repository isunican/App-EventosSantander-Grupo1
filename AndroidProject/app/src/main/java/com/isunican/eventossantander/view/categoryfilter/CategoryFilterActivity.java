package com.isunican.eventossantander.view.categoryfilter;

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

    private CheckBox talleresCheckBox;
    private CheckBox artesEscenicasCheckBox;
    private CheckBox artesPlasticasCheckBox;
    private CheckBox infantilCheckBox;
    private CheckBox musicaCheckBox;
    private CheckBox otrosCheckBox;

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
        talleresCheckBox = findViewById(R.id.talleres_checkBox);
        artesEscenicasCheckBox = findViewById(R.id.artesEscenicas_checkBox);
        artesPlasticasCheckBox = findViewById(R.id.artesPlasticas_checkBox);
        infantilCheckBox = findViewById(R.id.infantil_checkBox);
        musicaCheckBox = findViewById(R.id.musica_checkBox);
        otrosCheckBox = findViewById(R.id.otros_checkBox);

        if (presenter.getEstadoCheckBoxes(getString(R.string.talleres))) talleresCheckBox.setChecked(true);
        if (presenter.getEstadoCheckBoxes(getString(R.string.artes_escenicas))) artesEscenicasCheckBox.setChecked(true);
        if (presenter.getEstadoCheckBoxes(getString(R.string.artes_plasticas))) artesPlasticasCheckBox.setChecked(true);
        if (presenter.getEstadoCheckBoxes(getString(R.string.infantil))) infantilCheckBox.setChecked(true);
        if (presenter.getEstadoCheckBoxes(getString(R.string.musica))) musicaCheckBox.setChecked(true);
        if (presenter.getEstadoCheckBoxes(getString(R.string.otros))) otrosCheckBox.setChecked(true);

        Button limpiarButton = findViewById(R.id.categoryFilter_button_clear);
        Button aplicarButton = findViewById(R.id.categoryFilter_button_apply);

        aplicarButton.setOnClickListener(v-> onAplicarClicked());

        limpiarButton.setOnClickListener(v-> onLimpiarClicked());

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

    private void onAplicarClicked(){
        Set<String> categorias = new HashSet<>();
        if(talleresCheckBox.isChecked()){
            categorias.add(getString(R.string.talleres));
        }
        if(artesEscenicasCheckBox.isChecked()){
            categorias.add(getString(R.string.artes_escenicas));
        }
        if(artesPlasticasCheckBox.isChecked()){
            categorias.add(getString(R.string.artes_plasticas));
        }
        if(musicaCheckBox.isChecked()){
            categorias.add(getString(R.string.musica));
        }
        if(infantilCheckBox.isChecked()){
            categorias.add(getString(R.string.infantil));
        }
        if(otrosCheckBox.isChecked()){
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
