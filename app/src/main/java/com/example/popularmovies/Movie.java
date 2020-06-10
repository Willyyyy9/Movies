package com.example.popularmovies;

import java.io.Serializable;

public class Movie implements Serializable {
    private String title;
    private String movieId;
    private String poster;
    private String synopsis;
    private int rating;
    private String date;



    public Movie(String title, String movieId, String poster, String synopsis, int rating, String date) {
        this.title = title;
        this.movieId = movieId;
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

    public String getMovieId() { return movieId; }


    public static class MovieTrailer{
        private String trailerName;
        private String trailerURL;
        private String trailerSite;

        public MovieTrailer(String trailerName, String trailerURL, String trailerSite) {
            this.trailerName = trailerName;
            this.trailerURL = trailerURL;
            this.trailerSite = trailerSite;
        }

        public String getTrailerName() {
            return trailerName;
        }

        public String getTrailerURL() {
            return trailerURL;
        }

        public String getTrailerSite() {
            return trailerSite;
        }
    }

    public static class MovieReview{
        private String reviewAuthor;
        private String reviewURL;

        public MovieReview(String reviewAuthor, String reviewURL) {
            this.reviewAuthor = reviewAuthor;
            this.reviewURL = reviewURL;
        }

        public String getReviewAuthor() {
            return reviewAuthor;
        }

        public String getReviewURL() {
            return reviewURL;
        }
    }
}
