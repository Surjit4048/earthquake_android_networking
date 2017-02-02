package com.example.android.quakereport;


import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;

public class EarthQuakeAsyncLoader extends AsyncTaskLoader<List<Data>> {
    private String url;

    public EarthQuakeAsyncLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public List<Data> loadInBackground() {
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        return QueryUtils.makaConnectionAndFetchData(url);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

}
