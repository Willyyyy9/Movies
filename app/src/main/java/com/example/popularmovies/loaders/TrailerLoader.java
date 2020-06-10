package com.example.popularmovies.loaders;

import android.content.Context;

import com.example.popularmovies.Movie;
import com.example.popularmovies.Movie.MovieTrailer;
import com.example.popularmovies.QueryUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class TrailerLoader extends AsyncTaskLoader {
    private String url;

    public TrailerLoader(@NonNull Context context, String url) {
        super(context);
        this.url = url;
    }

    @Nullable
    @Override
    public ArrayList<MovieTrailer> loadInBackground() {
        if (url == null) {
            return null;
        }
        return QueryUtils.fetchMovieTrailerListData(url);
    }

    @Override
    protected void onStartLoading() { forceLoad(); }
}
