package com.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.popularmovies.Movie.MovieReview;
import com.example.popularmovies.Movie.MovieTrailer;
import com.example.popularmovies.adapters.ReviewListViewAdapter;
import com.example.popularmovies.adapters.TrailerListViewAdapter;
import com.example.popularmovies.data.MovieContract;
import com.example.popularmovies.data.MovieDatabase;
import com.example.popularmovies.data.MovieEntry;
import com.example.popularmovies.loaders.MovieLoader;
import com.example.popularmovies.loaders.ReviewLoader;
import com.example.popularmovies.loaders.TrailerLoader;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

import static android.view.View.GONE;
import static com.example.popularmovies.data.MovieContract.REVIEW_LOADER_ID;
import static com.example.popularmovies.data.MovieContract.TRAILER_LOADER_ID;

public class DetailActivity extends AppCompatActivity{
    private String reviewFullURL = "";
    private String trailerFullURL = "";
    private ListView reviewListView;
    private ListView trailerListView;
    private ReviewListViewAdapter reviewListViewAdapter;
    private TrailerListViewAdapter trailerListViewAdapter;
    private TextView reviewEmptyTextView;
    private TextView trailerEmptyTextView;
    private ProgressBar reviewLoadingIndicator;
    private ProgressBar trailerLoadingIndicator;
    private ArrayList<MovieTrailer> movieTrailersArrayList;
    private ArrayList<MovieReview> movieReviewArrayList;
    private MovieDatabase movieDatabase;
    private Movie movie;
    private Boolean MOVIE_FAVOURITE = false;

    private LoaderManager.LoaderCallbacks<ArrayList<MovieReview>> movieReviewLoaderListener
            = new LoaderManager.LoaderCallbacks<ArrayList<MovieReview>>() {
        @NonNull
        @Override
        public Loader<ArrayList<MovieReview>> onCreateLoader(int id, @Nullable Bundle args) {
            return new ReviewLoader(DetailActivity.this,reviewFullURL);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<ArrayList<MovieReview>> loader, ArrayList<MovieReview> reviews) {
            reviewListViewAdapter.clear();
            if (reviews != null && !reviews.isEmpty()) {
                movieReviewArrayList = reviews;
                reviewListView.setVisibility(View.VISIBLE);
                reviewListViewAdapter.addAll(reviews);
                reviewListView.setAdapter(reviewListViewAdapter);
            }else if(reviews == null || reviews.isEmpty()){
                reviewEmptyTextView.setText(R.string.review_empty);
                reviewEmptyTextView.setVisibility(View.VISIBLE);
                reviewListView.setVisibility(GONE);
                reviewLoadingIndicator.setVisibility(GONE);
            }
            reviewLoadingIndicator.setVisibility(GONE);

        }

        @Override
        public void onLoaderReset(@NonNull Loader<ArrayList<MovieReview>> loader) {
            reviewListViewAdapter.clear();
        }
    };

    private LoaderManager.LoaderCallbacks<ArrayList<MovieTrailer>> movieTrailerLoaderListener
            = new LoaderManager.LoaderCallbacks<ArrayList<MovieTrailer>>() {
        @NonNull
        @Override
        public Loader<ArrayList<MovieTrailer>> onCreateLoader(int id, @Nullable Bundle args) {
            return new TrailerLoader(DetailActivity.this,trailerFullURL);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<ArrayList<MovieTrailer>> loader, ArrayList<MovieTrailer> trailers) {
            trailerListViewAdapter.clear();
            if (trailers != null && !trailers.isEmpty()) {
                movieTrailersArrayList = trailers;
                trailerListView.setVisibility(View.VISIBLE);
                trailerListViewAdapter.addAll(trailers);
                trailerListView.setAdapter(trailerListViewAdapter);
            }else if(trailers == null || trailers.isEmpty()){
                trailerEmptyTextView.setText(R.string.empty_trailers);
                trailerEmptyTextView.setVisibility(View.VISIBLE);
                trailerListView.setVisibility(GONE);
                trailerLoadingIndicator.setVisibility(GONE);
            }
            trailerLoadingIndicator.setVisibility(GONE);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<ArrayList<MovieTrailer>> loader) { trailerListViewAdapter.clear(); }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Initialize views
        ImageView posterImageView = findViewById(R.id.posterImageView);
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView synopsisTextView = findViewById(R.id.synopsisTextView);
        TextView ratingTextView = findViewById(R.id.ratingTextView);
        TextView releaseDateTextView = findViewById(R.id.releaseDateTextView);
        final ToggleButton favouriteToggleButton = findViewById(R.id.favouriteToggleButton);
        reviewListView = findViewById(R.id.reviewListView);
        reviewListViewAdapter = new ReviewListViewAdapter(this,R.layout.review_list_item,new ArrayList<MovieReview>());
        trailerListView = findViewById(R.id.trailerListView);
        trailerListViewAdapter = new TrailerListViewAdapter(this,R.layout.trailer_list_item,new ArrayList<MovieTrailer>());
        reviewEmptyTextView = findViewById(R.id.reviewEmptyView);
        trailerEmptyTextView = findViewById(R.id.trailerEmptyView);
        reviewLoadingIndicator = findViewById(R.id.reviewLoadingIndicator);
        trailerLoadingIndicator = findViewById(R.id.trailerLoadingIndicator);

        //Getting the intent and its extras
        Intent intent = getIntent();

        movie = (Movie) intent.getSerializableExtra("Movie");

        //Populating views in the activity
        titleTextView.setText(movie.getTitle());
        ratingTextView.setText(movie.getRating()+"/10");
        releaseDateTextView.setText("( " + reverseDate(movie.getDate()) +" )");
        MovieContract movieContract = new MovieContract();
        String poster = movie.getPoster();
        poster = poster.replaceAll(movieContract.posterBase, movieContract.posterNewBase);
        Picasso.get().load(Uri.parse(poster)).into(posterImageView);
        synopsisTextView.setText(movie.getSynopsis());

        //Forming the URLs for the trailers and reviews
        final String movieId = movie.getMovieId();
        reviewFullURL = movieContract.movieBaseUrl + movieId + movieContract.reviewExtension;
        trailerFullURL = movieContract.movieBaseUrl + movieId + movieContract.trailerExtension;

        //Setting the loading indicators to VISIBLE
        reviewLoadingIndicator.setVisibility(View.VISIBLE);
        trailerLoadingIndicator.setVisibility(View.VISIBLE);

        //Initializing the loaders
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        //checking if there is internet connectivity
        if (networkInfo != null && networkInfo.isConnected()){
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(REVIEW_LOADER_ID,null,movieReviewLoaderListener);
            loaderManager.initLoader(TRAILER_LOADER_ID,null,movieTrailerLoaderListener);
        }  else {
            // Otherwise, display error
            // First, hide loading indicators so error message will be visible
            reviewLoadingIndicator.setVisibility(GONE);
            trailerLoadingIndicator.setVisibility(GONE);
            reviewListView.setEmptyView(reviewEmptyTextView);
            trailerListView.setEmptyView(trailerEmptyTextView);
            reviewEmptyTextView.setVisibility(View.VISIBLE);
            trailerEmptyTextView.setVisibility(View.VISIBLE);
            reviewEmptyTextView.setText(R.string.no_internet);
            trailerEmptyTextView.setText(R.string.no_internet);
        }

        //ListViews onItemClickListeners
        reviewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieReview movieReview = movieReviewArrayList.get(position);
                String reviewStringURI = movieReview.getReviewURL();
                Uri reviewURI = Uri.parse(reviewStringURI);
                Intent reviewIntent = new Intent(Intent.ACTION_VIEW,reviewURI);
                if(reviewIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(reviewIntent);
                }else{
                    Toast.makeText(DetailActivity.this, "There is no app to open the review", Toast.LENGTH_SHORT).show();
                }
            }
        });
        trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieTrailer movieTrailer = movieTrailersArrayList.get(position);
                String trailerStringURI = movieTrailer.getTrailerURL();
                Uri trailerURI = Uri.parse(trailerStringURI);
                Intent trailerIntent = new Intent(Intent.ACTION_VIEW,trailerURI);
                if(trailerIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(trailerIntent);
                }else{
                    Toast.makeText(DetailActivity.this, "There is no app to open the trailer", Toast.LENGTH_SHORT).show();
                }


            }
        });


        //Handling Database-Related code
        movieDatabase = MovieDatabase.getInstance(getApplicationContext());

        //Handling the toggleButton
        MovieExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                MOVIE_FAVOURITE = QueryUtils.isMovieFavourite(DetailActivity.this, movieId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        favouriteToggleButton.setOnCheckedChangeListener(null);
                        //Checking if the movie is a favourite or not
                        if(MOVIE_FAVOURITE){
                            favouriteToggleButton.setChecked(true);
                        }else {
                            favouriteToggleButton.setChecked(false);
                        }
                        favouriteToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                String title = movie.getTitle();
                                String movieID = movie.getMovieId();
                                String poster = movie.getPoster();
                                String synopsis = movie.getSynopsis();
                                int rating = movie.getRating();
                                String date = movie.getDate();
                                final MovieEntry movieEntry = new MovieEntry(title,movieID,poster,synopsis,rating,date);
                                if (isChecked) {
                                    // The toggle is enabled
                                    //Inserting the movie in the favourites database
                                    MovieExecutors.getInstance().diskIO().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Insert the movie in the database in a backround thread
                                            movieDatabase.movieDAO().insertMovie(movieEntry);
                                        }
                                    });
                                } else{
                                    // The toggle is disabled
                                    //Deleting the movie from the favourites database
                                    MovieExecutors.getInstance().diskIO().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Deletes the movie from the database in the background thread
                                            movieDatabase.movieDAO().deleteMovie(movieEntry);
                                        }
                                    });
                                }
                                //End of onCheckedChanged()
                            }
                            //End of setOnCheckedChangeListener()
                        });
                        //End of run()
                    }
                    //End of runOnUiThread()
                });
                //End of run()
            }
            //End of execute()
        });
        //End of onCreate()
    }

    private String reverseDate(String date){
        String [] stringArray = date.split("-");
        String [] results =
                new String[stringArray.length];
        String result = "";
        for (int i = 0; i<stringArray.length; i++){
            results[i] =
                    stringArray[stringArray.length-i-1];
            if(!(i+1<stringArray.length)){
                result += results[i];
            }else{
                result+=results[i]+"-";
            }
    }
        return result;
    }
}
