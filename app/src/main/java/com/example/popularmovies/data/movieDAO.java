package com.example.popularmovies.data;

import com.example.popularmovies.Movie;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface movieDAO {

    @Query("SELECT * FROM movies")
    LiveData<List<MovieEntry>> loadMovies();

    @Insert
    void insertMovie(MovieEntry movieEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(MovieEntry movieEntry);

    @Delete
    int deleteMovie(MovieEntry movieEntry);

    @Query("SELECT * FROM movies WHERE movieID =  :movieId")
    MovieEntry loadMovieByMovieId(String movieId);



}
