package com.example.android.quakereport;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomDataAdapter extends ArrayAdapter<Data> {

    public CustomDataAdapter(Context context, ArrayList<Data> data) {
        super(context, 0, data);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listLayout = convertView;

        if (listLayout == null) {
            listLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_adapter_layout, null, false);
            ViewHoldler holdler = new ViewHoldler();
            holdler.meg = (TextView) listLayout.findViewById(R.id.lal_meg);
            holdler.location = (TextView) listLayout.findViewById(R.id.lal_location);
            holdler.date = (TextView) listLayout.findViewById(R.id.lal_date);
            listLayout.setTag(holdler);
        }
        Data data = getItem(position);
        ViewHoldler holdler = (ViewHoldler) listLayout.getTag();

        assert data != null;
        holdler.meg.setText(data.getMag());
        holdler.location.setText(data.getLocation());
        holdler.date.setText(data.getDate());

        return listLayout;
    }

    private static class ViewHoldler {
        TextView meg;
        TextView location;
        TextView date;
    }
}
