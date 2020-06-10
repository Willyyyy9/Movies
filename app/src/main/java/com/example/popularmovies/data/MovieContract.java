package com.example.popularmovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

public class MovieContract {
    public String popularUrlString = "https://api.themoviedb.org/3/movie/popular?api_key={your_api_key}";
    public String topRatedUrlString = "https://api.themoviedb.org/3/movie/top_rated?api_key={your_api_key}";
    public String movieBaseUrl = "https://api.themoviedb.org/3/movie/";
    public String reviewExtension = "/reviews?api_key={your_api_key}";
    public String trailerExtension = "/videos?api_key={your_api_key}";
    public String posterBase = "https://image.tmdb.org/t/p/w185";
    public String posterNewBase = "https://image.tmdb.org/t/p/w500";
    public String youtubeBase = "https://youtube.com/watch?v=";
    public static final int MOVIE_LOADER_ID = 1;
    public static final int REVIEW_LOADER_ID = 100;
    public static final int TRAILER_LOADER_ID = 101;

    //Public Constructor
    public MovieContract() {
    }



}
