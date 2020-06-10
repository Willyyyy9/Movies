package com.example.popularmovies.loaders;

import android.content.Context;

import com.example.popularmovies.Movie;
import com.example.popularmovies.Movie.MovieReview;
import com.example.popularmovies.QueryUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class ReviewLoader extends AsyncTaskLoader {
    private String url;

    public ReviewLoader(@NonNull Context context, String url) {
        super(context);
        this.url = url;
    }

    @Nullable
    @Override
    public Object loadInBackground() {
        if (url == null) {
            return null;
        }
        return QueryUtils.fetchMovieReviewListData(url);
    }

    @Override
    protected void onStartLoading() { forceLoad(); }

}
