package com.isunican.eventossantander.view.selectkeywords;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.isunican.eventossantander.R;

import java.util.List;

public class KeywordsArrayAdapter extends ArrayAdapter<String> {

    private final List<String> keywords;
    ISelectKeywordsContract.Presenter presenter;

    public KeywordsArrayAdapter(@NonNull SelectKeywordsActivity activity, int resource, @NonNull List<String> objects) {
        super(activity, resource, objects);
        this.keywords = objects;
        presenter = activity.getPresenter();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String str = keywords.get(position);

        // Create item view
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.keywords_listview_item, null);

        // Link subviews
        TextView keyword = view.findViewById(R.id.item_keyword_name);
        Button deleteKeyorkdBtn = view.findViewById(R.id.item_btn_delete_keyword);
        // Assign values to TextViews
        keyword.setText(str);
        keyword.setTag(R.id.et_SelectKeywords);

        // Assign values to Buttons
        deleteKeyorkdBtn.setTag(R.id.item_btn_delete_keyword);
        deleteKeyorkdBtn.setOnClickListener(v -> presenter.onDeleteKeyword(position));
        return view;
    }

}
