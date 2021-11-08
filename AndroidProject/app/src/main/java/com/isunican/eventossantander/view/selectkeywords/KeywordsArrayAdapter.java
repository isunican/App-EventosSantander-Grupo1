package com.isunican.eventossantander.view.selectkeywords;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.isunican.eventossantander.R;
import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.view.events.EventsActivity;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class KeywordsArrayAdapter extends ArrayAdapter<String> {

    private final List<String> keywords;

    public KeywordsArrayAdapter(@NonNull SelectKeywordsActivity activity, int resource, @NonNull List<String> objects) {
        super(activity, resource, objects);
        this.keywords = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String str = keywords.get(position);

        // Create item view
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.keywords_listview_item, null);

        // Link subviews
        TextView titleTxt = view.findViewById(R.id.item_keyword_name);
        Button btn = view.findViewById(R.id.item_btn_less);

        // Assign values to TextViews
        titleTxt.setText(str);

        return view;
    }
}
