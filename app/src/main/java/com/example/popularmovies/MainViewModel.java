package com.example.popularmovies;

import android.app.Application;

import com.example.popularmovies.data.MovieDatabase;
import com.example.popularmovies.data.MovieEntry;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {



    private LiveData<List<MovieEntry>> movies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        MovieDatabase database = MovieDatabase.getInstance(this.getApplication());
        movies = database.movieDAO().loadMovies();
    }

    public LiveData<List<MovieEntry>> getMovies() {
        return movies;
    }
}
