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

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Data>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String USGS_REQUEST_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query";

    private CustomDataAdapter adapter;
    private TextView noDataFound;
    private ProgressBar progressBar;
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        noDataFound = (TextView) findViewById(R.id.no_data_found);
        progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        final List<Data> earthquakes = new ArrayList<>();
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        adapter = new CustomDataAdapter(this, earthquakes);
        assert earthquakeListView != null;
        earthquakeListView.setAdapter(adapter);
        earthquakeListView.setEmptyView(noDataFound);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Data currentEarthquake = adapter.getItem(position);
                assert currentEarthquake != null;
                Uri parse = Uri.parse(currentEarthquake.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, parse);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();


        getLoaderManager().initLoader(1, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Data>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthQuakeAsyncLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Data>> loader, List<Data> data) {
        progressBar.setVisibility(View.GONE);

        adapter.clear();
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
        if (isConnected) {
            noDataFound.setText(R.string.no_earthquake_data_found);
        } else {
            noDataFound.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Data>> loader) {
        adapter.clear();
    }
}
