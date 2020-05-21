package com.example.popularmovies;

import java.io.Serializable;

public class Movie implements Serializable {
    private String title;
    private String poster;
    private String synopsis;
    private int rating;
    private String date;

    public Movie(String title, String poster, String synopsis, int rating, String date) {
        this.title = title;
        this.poster = poster;
        this.synopsis = synopsis;
        this.rating = rating;
        this.date = date;
    }

    public String getTitle() { return title; }

    public String getPoster() { return poster; }

    public String getSynopsis() { return synopsis; }

    public int getRating() { return rating; }

    public String getDate() { return date; }
}
