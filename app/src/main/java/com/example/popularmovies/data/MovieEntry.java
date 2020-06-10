package com.example.popularmovies.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class MovieEntry {



    private String name;
    @NonNull
    @PrimaryKey
    private String movieID;
    private String poster;
    private String synopsis;
    private int rating;
    private String date;


    public MovieEntry(String name, String movieID, String poster, String synopsis, int rating, String date) {
        this.name = name;
        this.movieID = movieID;
        this.poster = poster;
        this.synopsis = synopsis;
        this.rating = rating;
        this.date = date;
    }





    public String getName() { return name; }

    public String getMovieID() { return movieID; }

    public String getPoster() { return poster; }

    public String getSynopsis() { return synopsis; }

    public int getRating() { return rating; }

    public String getDate() { return date; }
}
