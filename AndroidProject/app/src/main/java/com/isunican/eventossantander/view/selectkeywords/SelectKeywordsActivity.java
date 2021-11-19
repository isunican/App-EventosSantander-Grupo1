package com.isunican.eventossantander.view.selectkeywords;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.isunican.eventossantander.R;
import com.isunican.eventossantander.presenter.selectkeywords.SelectKeywordsPresenter;
import com.isunican.eventossantander.utils.AccessSharedPrefs;
import com.isunican.eventossantander.utils.ISharedPrefs;

import java.util.List;


public class SelectKeywordsActivity extends AppCompatActivity implements ISelectKeywordsContract.View {

    private Toast msgToast;
    EditText inputKeyword;
    private List<String> keywordsList;
    private ISelectKeywordsContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_keywords);
        ISharedPrefs sharedPrefs = new AccessSharedPrefs(getApplicationContext());
        presenter = new SelectKeywordsPresenter(this, sharedPrefs);
        ListView listView = findViewById(R.id.keywordsListView);
        configItems();
        keywordsList = presenter.getKeywordsList();
        listView.setAdapter(new KeywordsArrayAdapter(this,R.layout.keywords_listview_item, keywordsList));
    }

    private void configItems() {
        ActionBar actionBar = getSupportActionBar();
        Button limpiarButton = findViewById(R.id.btn_clean_keywords);
        Button aplicarButton = findViewById(R.id.btn_apply_keywords_filter);
        Button andadeKeyord = findViewById(R.id.btn_add_keyword);
        inputKeyword = findViewById(R.id.et_SelectKeywords);

        aplicarButton.setOnClickListener(v-> onAplicarClicked());
        limpiarButton.setOnClickListener(v-> onClearSelectKeywordsClicked());
        andadeKeyord.setOnClickListener(v-> onAddKeywordToList());

        actionBar.setDisplayHomeAsUpEnabled(true); // Botón home en la barra superior
        actionBar.setDisplayShowHomeEnabled(true);

        actionBar.setDisplayShowTitleEnabled(false); // Desactiva el title por defecto
        actionBar.setDisplayShowCustomEnabled(true); // Activa el title personalizable
        View customView = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        TextView customTitle = customView.findViewById(R.id.actionbarTitle); // Obtiene el textview del title
        customTitle.setText(R.string.seleccionar_palabras_clave); // Cambiamos el texto del title para saber que estamos en seleccionar palabras clave

        // Aplicamos la vista personalizada
        actionBar.setCustomView(customView);
    }

    // Para cerrar la activity
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    /**
     * Metodo que carga las palabras clave en la vista
     * //  lista de palabras clave a cargar.
     */
    @Override
    public void onKeywordsLoaded(List<String> list) {
        keywordsList = list;
        KeywordsArrayAdapter adapter = new KeywordsArrayAdapter(SelectKeywordsActivity.this, 0, keywordsList);
        ListView listView = findViewById(R.id.keywordsListView);
        listView.setAdapter(adapter);
    }

    /**
     * Metodo de la vista que añade una palabra clave al filtro al dar al boton de añadir
     */
    @Override
    public void onAddKeywordToList() {
        inputKeyword = findViewById(R.id.et_SelectKeywords);
        presenter.onAddKeyword(inputKeyword.getText().toString());
        inputKeyword.setText("");
    }

    /**
     * Metodo de la vista que muestra un mensaje al añadir una palabra clave correctamente a la lista.
     */
    @Override
    public void onAddedKeywordSuccess(String keyword) {
        if (msgToast != null) {
            msgToast.cancel();
        }
        msgToast = Toast.makeText(this,
                String.format("Se ha añadido la palabra %s a la lista de palabras clave.", keyword.toUpperCase()), Toast.LENGTH_SHORT);
        if(msgToast != null) {
            msgToast.show();
        }
    }

    /**
     * Metodo de la vista que muestra un toast en caso de producirse un error al intentar añadir una palabra clave
     * a la lista.
     */
    @Override
    public void onAddKeywordExists(String keyword) {
        if (msgToast != null) {
            msgToast.cancel();
        }
        msgToast = Toast.makeText(this, String.format("La palabra %s ya está en la lista.",
                keyword.toUpperCase()), Toast.LENGTH_SHORT);
        if(msgToast != null) {
            msgToast.show();
        }
    }
    /**
     * Metodo de la vista que muestra un mensaje al eliminar una palabra clave correctamente del filtro.
     * @param keyword palabra clave añadida.
     */
    public void onDeletedKeywordSuccess(String keyword) {
        if (msgToast != null) {
            msgToast.cancel();
        }
        msgToast = Toast.makeText(this, String.format("Se ha eliminado la palabra clave %s",
                keyword.toUpperCase()), Toast.LENGTH_SHORT);
        if(msgToast != null) {
            msgToast.show();
        }
    }
    /**
     * Metodo de la vista que muestra un toast en caso de producirse un error al intentar eliminar una palabra
     * clave del filtro.
     */
    @Override
    public void onDeleteKeywordFail() {
        if (msgToast != null) {
            msgToast.cancel();
        }
        msgToast  = Toast.makeText(this,
                "No se ha podido eliminar la palabra clave del filtro.",
                Toast.LENGTH_SHORT);
        if(msgToast != null) {
            msgToast.show();
        }
    }

    @Override
    public void onSpaceOnKeyWordExists() {
        if (msgToast != null) {
            msgToast.cancel();
        }
        msgToast  = Toast.makeText(this,
                "No se permiten palabras clave con espacios.",
                Toast.LENGTH_SHORT);
        if(msgToast != null) {
            msgToast.show();
        }
    }

    /**
     * Metodo de la vista que aplica el filtro por palabras clave.
     */
    private void onAplicarClicked() {
        presenter.onAplicarSelectKeywords();
        finish();
    }

    /**
     * Metodo de la vista que desactiva el filtro por palabras clave.
     */
    @Override
    public void onClearSelectKeywordsClicked() {
        presenter.onClearSelectKeywordsFilter();
        finish();
        if (msgToast != null) {
            msgToast.cancel();
        }
        msgToast = Toast.makeText(this,
                "Se ha limpiado el filtro por palabras clave.",
                Toast.LENGTH_SHORT);
        if(msgToast != null) {
            msgToast.show();
        }
    }

    @Override
    protected void onRestart() {
        presenter.onReloadKeywords();
        super.onRestart();
    }
    public ISelectKeywordsContract.Presenter getPresenter() {
        return presenter;
    }
}
