package com.example.popularmovies;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class MovieLoader extends AsyncTaskLoader {
    private String url;

    public MovieLoader(@NonNull Context context, String url) {
        super(context);
        this.url = url;
    }

    @Nullable
    @Override
    public Object loadInBackground() {
        if (url == null) {
            return null;
        }
        ArrayList<Movie> result = QueryUtils.fetchMovieListData(url);
        return result;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
