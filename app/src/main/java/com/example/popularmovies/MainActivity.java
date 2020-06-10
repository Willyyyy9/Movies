package com.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.popularmovies.adapters.RecyclerViewAdapter;
import com.example.popularmovies.data.MovieContract;
import com.example.popularmovies.data.MovieDatabase;
import com.example.popularmovies.data.MovieEntry;
import com.example.popularmovies.loaders.MovieLoader;
import java.util.ArrayList;
import java.util.List;

import static com.example.popularmovies.data.MovieContract.MOVIE_LOADER_ID;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Movie>>,
        RecyclerViewAdapter.ListItemClickListener {

    private RecyclerView recyclerView;
    private TextView queryTextView;
    private ProgressBar loadingIndicator;
    private ArrayList<Movie> movies = new ArrayList<>();
    private String MOVIE_REQUEST_URL;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private MovieContract movieContract;
    public static final String LOG_TAG = MainActivity.class.getName();







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Setting the RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing Views
        queryTextView = findViewById(R.id.queryTypeTextView);
        loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);
        movieContract = new MovieContract();


        //Accessing and retrieving preferences

        settings = getSharedPreferences(getString(R.string.settings_key), MODE_PRIVATE);
        editor = settings.edit();
        MOVIE_REQUEST_URL = settings.getString(getString(R.string.Url_key),movieContract.popularUrlString);
        if(MOVIE_REQUEST_URL.equals(movieContract.popularUrlString)){
            queryTextView.setText(R.string.Popular_title);
        }else if(MOVIE_REQUEST_URL.equals(movieContract.topRatedUrlString)){
            queryTextView.setText(R.string.Top_Rated_title);
        }else{
            queryTextView.setText(getString(R.string.favourites));
        }

        //If the url is either top rated or popular
        if (MOVIE_REQUEST_URL.equals(movieContract.popularUrlString) || MOVIE_REQUEST_URL.equals(movieContract.topRatedUrlString)){
            //Checking if there is internet connectivity
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()){
                LoaderManager loaderManager = getSupportLoaderManager();
                loaderManager.restartLoader(MOVIE_LOADER_ID,null,MainActivity.this);
            }else{
                //Showing a toast if there isn't a connection
                Toast.makeText(this, R.string.No_Internet_Connection, Toast.LENGTH_SHORT).show();
                loadingIndicator.setVisibility(View.GONE);
            }
            //if the url is "" which means that it is a database query
        }else {
            loadingIndicator.setVisibility(View.GONE);
            setupMoviesFromDatabase(recyclerView);
        }
    }

    @NonNull
    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, @Nullable Bundle args) {
        return new MovieLoader(this,MOVIE_REQUEST_URL);
    }



    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        //Handling the results when it comes
        loadingIndicator.setVisibility(View.GONE);
        movies = data;
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(data, this);
        recyclerView.setAdapter(recyclerViewAdapter);
        //Destroying the loader to avoid loading it when back is clicked
        getSupportLoaderManager().destroyLoader(loader.getId());
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Movie>> loader) {
        recyclerView.setAdapter(null);
    }

    @Override
    public void onClickItemList(int clickItemIndex) {
        //Handling the ItemClickListener of the recyclerView
        Intent intent = new Intent(MainActivity.this,DetailActivity.class);
        Movie movie = movies.get(clickItemIndex);
        intent.putExtra("Movie",movie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflating OptionsMenu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.popularItem){
            //In case the user clicks on popular movies in optionMenu
            recyclerView.setAdapter(null);
            loadingIndicator.setVisibility(View.VISIBLE);
            queryTextView.setText(getString(R.string.Popular_title));

            //Setting the preferences
            MOVIE_REQUEST_URL = movieContract.popularUrlString;
            editor.putString(getString(R.string.Url_key),movieContract.popularUrlString);
            editor.apply();

            //Loading the loader with the popular movies
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            //Checking if there is internet connectivity
            if (networkInfo != null && networkInfo.isConnected()){
                LoaderManager loaderManager = getSupportLoaderManager();
                loaderManager.restartLoader(MOVIE_LOADER_ID,null,MainActivity.this);
            }else{
                //Showing a toast if there isn't a connection
                Toast.makeText(this, R.string.No_Internet_Connection, Toast.LENGTH_SHORT).show();
                loadingIndicator.setVisibility(View.GONE);
            }

        } else if(itemId == R.id.topRatedItem){
            //In case the user clicks on top rated movies in optionMenu
            recyclerView.setAdapter(null);
            loadingIndicator.setVisibility(View.VISIBLE);
            queryTextView.setText(getString(R.string.Top_Rated_title));

            //Setting the preferences
            MOVIE_REQUEST_URL = movieContract.topRatedUrlString;
            editor.putString(getString(R.string.Url_key),movieContract.topRatedUrlString);
            editor.apply();

            //Loading the loader with the top rated url
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            //checking if there is internet connectivity
            if (networkInfo != null && networkInfo.isConnected()){
                LoaderManager loaderManager = getSupportLoaderManager();
                loaderManager.restartLoader(MOVIE_LOADER_ID,null,MainActivity.this);
            }else{
                //Showing a toast if there isn't a connection
                Toast.makeText(this, R.string.No_Internet_Connection, Toast.LENGTH_SHORT).show();
                loadingIndicator.setVisibility(View.GONE);
            }

        } else if(itemId == R.id.favouritesItem){
            //if the user click on favourites

            recyclerView.setAdapter(null);
            loadingIndicator.setVisibility(View.VISIBLE);
            queryTextView.setText(getString(R.string.favourites));

            //setting the preferences
            MOVIE_REQUEST_URL = "";
            editor.putString(getString(R.string.Url_key),"");
            editor.apply();

            //Querying the favourite movies from the database and populating the recyclerView with it
            setupMoviesFromDatabase(recyclerView);
            loadingIndicator.setVisibility(View.GONE);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupMoviesFromDatabase( final RecyclerView recyclerView){
        //Using ViewModel to limit unnecessary database queries
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        recyclerView.setAdapter(null);
        mainViewModel.getMovies().observe(this, new Observer<List<MovieEntry>>() {

            @Override
            public void onChanged(List<MovieEntry> movieEntries) {
                if(movies.size() > 0){
                    movies.clear();
                }
                if(MOVIE_REQUEST_URL.equals("")){
                    for(int i = 0; i<movieEntries.size(); i++) {
                        MovieEntry movieEntryFromDatabase = movieEntries.get(i);
                        movies.add(new Movie(movieEntryFromDatabase.getName(), movieEntryFromDatabase.getMovieID(),
                                movieEntryFromDatabase.getPoster(), movieEntryFromDatabase.getSynopsis(),
                                movieEntryFromDatabase.getRating(), movieEntryFromDatabase.getDate()));
                    }
                    RecyclerViewAdapter favouritesRecyclerView = new RecyclerViewAdapter(movies,MainActivity.this);
                    recyclerView.setAdapter(favouritesRecyclerView);
                }
            }
        });
    }
}

