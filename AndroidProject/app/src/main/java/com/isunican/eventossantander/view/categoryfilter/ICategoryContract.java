package com.isunican.eventossantander.view.categoryfilter;

import java.util.List;
import java.util.Set;

public interface ICategoryContract {

    public interface Presenter {

        void onAplicarFiltroCategoria(Set<String> categorias);

        boolean getEstadoCheckBoxes(String checkBox);

    }

    public interface View {

        void onAplicarClicked();

    }


}
