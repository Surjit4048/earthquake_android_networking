package com.example.android.quakereport;


import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomDataAdapter extends ArrayAdapter<Data> {

    public static final String LOCATION_OFFSET = " of ";

    public CustomDataAdapter(Context context, List<Data> data) {
        super(context, 0, data);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listLayout = convertView;

        if (listLayout == null) {
            listLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_adapter_layout, null, false);
            ViewHoldler holdler = new ViewHoldler();
            holdler.mag = (TextView) listLayout.findViewById(R.id.lal_meg);
            holdler.placeInCity = (TextView) listLayout.findViewById(R.id.lal_location_in_city);
            holdler.location = (TextView) listLayout.findViewById(R.id.lal_location_city);
            holdler.date = (TextView) listLayout.findViewById(R.id.lal_date);
            holdler.time = (TextView) listLayout.findViewById(R.id.lal_time);
            listLayout.setTag(holdler);
        }
        Data data = getItem(position);
        ViewHoldler holdler = (ViewHoldler) listLayout.getTag();

        assert data != null;
        String locationData = data.getLocation();
        String placeInCity;
        String location;

        if (locationData.contains(LOCATION_OFFSET)) {
            String parts[] = locationData.split(LOCATION_OFFSET);
            placeInCity = parts[0] + LOCATION_OFFSET;
            location = parts[1];
        } else {
            placeInCity = getContext().getResources().getString(R.string.near_the);
            location = locationData;
        }

        holdler.mag.setText(formatMagnitude(data.getMag()));
        holdler.placeInCity.setText(placeInCity);
        holdler.location.setText(location);
        Date dateObject = new Date(data.getTimeInMilliseconds());
        holdler.date.setText(formatDate(dateObject));
        holdler.time.setText(formatTime(dateObject));

        GradientDrawable magCircle = (GradientDrawable) holdler.mag.getBackground();
        int magnitudeColor = getMagnitudeColor(data.getMag());
        magCircle.setColor(magnitudeColor);

        return listLayout;
    }

    private int getMagnitudeColor(Double mag) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(mag);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }


    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private static class ViewHoldler {
        TextView mag;
        TextView placeInCity;
        TextView location;
        TextView date;
        TextView time;
    }
}
