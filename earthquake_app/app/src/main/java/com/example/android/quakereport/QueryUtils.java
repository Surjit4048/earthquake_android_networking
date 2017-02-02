package com.example.android.quakereport;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    public static final String TAG = "FailedFetchData";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Data} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Data> extractEarthquakes(String fetchedData) {

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Data> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            JSONObject rootObj = new JSONObject(fetchedData);
            JSONArray featureArray = rootObj.getJSONArray("features");

            for (int i = 0; i < featureArray.length(); i++) {
                JSONObject singleFeature = featureArray.getJSONObject(i);
                JSONObject properties = singleFeature.getJSONObject("properties");
                Double mag = properties.optDouble("mag");
                String place = properties.optString("place");
                long time = properties.optLong("time");
                String url = properties.optString("url");
                earthquakes.add(new Data(mag, place, time, url));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }


    public static List<Data> makaConnectionAndFetchData(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        URL createdUrl = creatURL(url);

        if (createdUrl != null) {
            return fetchData(createdUrl);
        } else {
            return null;
        }

    }

    private static List<Data> fetchData(URL createdUrl) {

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) createdUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                return extractEarthquakes(readInputStream(inputStream));
            } else {
                Log.e(TAG, "fetchData: " + httpURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder result = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                result.append(line);
                line = reader.readLine();
            }
        }
        return result.toString();
    }

    private static URL creatURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
