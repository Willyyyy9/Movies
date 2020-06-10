package com.example.popularmovies;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.popularmovies.Movie.MovieReview;
import com.example.popularmovies.Movie.MovieTrailer;
import com.example.popularmovies.data.MovieContract;
import com.example.popularmovies.data.MovieDatabase;
import com.example.popularmovies.data.MovieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


import androidx.lifecycle.LiveData;

import static com.example.popularmovies.MainActivity.LOG_TAG;

public class QueryUtils {

    private static ArrayList<String> titles = new ArrayList<>();
    private static ArrayList<String> movieIDs = new ArrayList<>();
    private static ArrayList<String> posters = new ArrayList<>();
    private static ArrayList<String> synopsises = new ArrayList<>();
    private static ArrayList<Integer> ratings = new ArrayList<>();
    private static ArrayList<String> dates = new ArrayList<>();
    private static ArrayList<ArrayList<String>> trailers = new ArrayList<>();
    private static ArrayList<ArrayList<String>> reviews = new ArrayList<>();

    private QueryUtils() { }

    private static URL createUrl(String stringUrl){
        //Converts String to URL
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        //Makes the HTTPConnection
        String jsonResponse = "";
        if(url == null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                Log.e(LOG_TAG, "Error response code: " + url);
            }
        }catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        //Forms the JSON Object retrieved from the connection
        StringBuilder stringBuilder = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line!=null){
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }

    public static ArrayList<Movie> extractDataFromJSON(String moviesJSON){
        //Putting data to ArrayList of Movies
        MovieContract movieContract = new MovieContract();
        ArrayList<Movie> movies = new ArrayList<>();
        try{
            JSONObject root = new JSONObject(moviesJSON);
            JSONArray results = root.getJSONArray("results");
            for(int i = 0; i < results.length(); i++){
                JSONObject result = results.getJSONObject(i);
                String title = result.getString("title");
                String movieID = String.valueOf(result.getInt("id"));
                String posterPath = result.getString("poster_path");
                String poster = movieContract.posterBase+posterPath;
                String synopsis = result.getString("overview");
                int rating = result.getInt("vote_average");
                String releaseDate = result.getString("release_date");
                movies.add(new Movie(title,movieID,poster,synopsis,rating,releaseDate));
            }
        }catch(JSONException e){
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }
        return movies;
    }

    public static ArrayList<MovieTrailer> extractTrailersFromJSON(String trailerJSON){
        //Putting data to ArrayList of Trailers
        MovieContract movieContract = new MovieContract();
        ArrayList<MovieTrailer> trailers = new ArrayList<>();
        try{
            JSONObject root = new JSONObject(trailerJSON);
            JSONArray results = root.getJSONArray("results");
            for(int i = 0; i<results.length(); i++){
                JSONObject result = results.getJSONObject(i);
                String name = result.getString("name");
                String key = result.getString("key");
                String youtubePath = movieContract.youtubeBase+key;
                String site  = result.getString("site");
                trailers.add(new MovieTrailer(name,youtubePath,site));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return trailers;
    }

    public static ArrayList<MovieReview> extractReviewsFromJSON(String reviewJSON){
        //Putting data into ArrayList of Reviews
        ArrayList<MovieReview> reviews = new ArrayList<>();
        try{
            JSONObject root = new JSONObject(reviewJSON);
            JSONArray results = root.getJSONArray("results");
            for(int i = 0; i<results.length(); i++){
                JSONObject result = results.getJSONObject(i);
                String author = result.getString("author");
                String url = result.getString("url");
                reviews.add(new MovieReview(author,url));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return reviews;
    }



    public static ArrayList<Movie> fetchMovieListData(String requestURL){
        //Gets movies from JSON API
        URL url = createUrl(requestURL);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        return extractDataFromJSON(jsonResponse);
    }

    public static ArrayList<MovieTrailer> fetchMovieTrailerListData(String requestURL){
        //Gets trailers from JSON API
        URL url = createUrl(requestURL);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        return extractTrailersFromJSON(jsonResponse);
    }

    public static ArrayList<MovieReview> fetchMovieReviewListData(String requestURL){
        //Gets reviews from JSON API
        URL url = createUrl(requestURL);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        return extractReviewsFromJSON(jsonResponse);
    }



    public static boolean isMovieFavourite(Context context, String movieId) {
        //Checking if a movie is in the favourites or not.
         MovieDatabase movieDatabase = MovieDatabase.getInstance(context);
         MovieEntry movieEntry = movieDatabase.movieDAO().loadMovieByMovieId(movieId);
         if(movieEntry != null){
             return true;
         }else {
             return false;
         }

    }





}
