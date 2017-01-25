/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Create a fake list of earthquake locations.
        ArrayList<Data> earthquakes = new ArrayList<Data>();
        earthquakes.add(new Data("7.2", "San Francisco", "Feb, 7, 2014"));
        earthquakes.add(new Data("7.2", "London", "Feb, 7, 2012"));
        earthquakes.add(new Data("7.2", "Tokyo" , "Feb, 7, 2013"));
        earthquakes.add(new Data("7.2", "Mexico City" , "Feb, 7, 2014"));
        earthquakes.add(new Data("7.2", "Moscow" , "Feb, 7, 1995"));
        earthquakes.add( new Data("7.2", "Rio de Janeiro", "Feb, 7, 2015"));
        earthquakes.add( new Data("7.2", "Paris", "Feb, 7, 2016"));

        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        CustomDataAdapter adapter = new CustomDataAdapter(this, earthquakes);

        earthquakeListView.setAdapter(adapter);
    }
}
