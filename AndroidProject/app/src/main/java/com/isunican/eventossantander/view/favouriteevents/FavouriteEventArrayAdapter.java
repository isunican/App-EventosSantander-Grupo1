package com.isunican.eventossantander.view.favouriteevents;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.isunican.eventossantander.R;
import com.isunican.eventossantander.model.Event;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class FavouriteEventArrayAdapter extends ArrayAdapter<Event> {

    private final List<Event> events;

    public FavouriteEventArrayAdapter(@NonNull FavouriteEventsActivity activity, int resource, @NonNull List<Event> objects) {
        super(activity, resource, objects);
        this.events = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = events.get(position);

        // Create item view
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.events_listview_item, null);

        // Link subviews
        TextView titleTxt = view.findViewById(R.id.item_keyword_name);
        TextView dateTxt = view.findViewById(R.id.item_event_date);
        ImageView imageTxt = view.findViewById(R.id.item_event_image);

        // Assign values to TextViews
        titleTxt.setText(event.getNombre());
        dateTxt.setText(event.getFecha());

        // Assign image
        imageTxt.setImageResource(getImageIdForEvent(event));

        return view;
    }

    /**
     * Determines the image resource id that must be used as the icon for a given event.
     * @param event
     * @return the image resource id for the event
     */
    private int getImageIdForEvent(Event event) {
        int id = getContext().getResources().getIdentifier(
                getNormalizedCategory(event),
                "drawable",
                getContext().getPackageName());

        // fallback image in case of unrecognized category
        if (id == 0) {
            id = getContext().getResources().getIdentifier(
                    "otros",
                    "drawable",
                    getContext().getPackageName());
        }
        return id;
    }

    private static String getNormalizedCategory(Event event) {
        return StringUtils.deleteWhitespace(
                StringUtils.stripAccents(event.getCategoria()))
                .toLowerCase();
    }
}
