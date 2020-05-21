package com.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Movie>>, RecyclerViewAdapter.ListItemClickListener {
    public static final String LOG_TAG = MainActivity.class.getName();
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Movie> movies;
    private static final int MOVIE_LOADER_ID = 1;
    private String popularUrlString = "https://api.themoviedb.org/3/movie/popular?api_key=[API_KEY]";
    private String topRatedUrlString = "https://api.themoviedb.org/3/movie/top_rated?api_key=[API_KEY]";
    private SharedPreferences settings;
    SharedPreferences.Editor editor;
    private String MOVIE_REQUEST_URL;
    private TextView queryTextView;
    private ProgressBar loadingIndicator;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        queryTextView = findViewById(R.id.queryTypeTextView);
        loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);
        settings = getSharedPreferences("settings", MODE_PRIVATE);
        editor = settings.edit();
        MOVIE_REQUEST_URL = settings.getString("searchSettings", popularUrlString);
        if(MOVIE_REQUEST_URL.equals(popularUrlString)){
            queryTextView.setText("Popular Movies");
        }else if(MOVIE_REQUEST_URL.equals(topRatedUrlString)){
            queryTextView.setText("Top Rated Movies");
        }
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        //checking if there is internet connectivity
        if (networkInfo != null && networkInfo.isConnected()){
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.restartLoader(MOVIE_LOADER_ID,null,MainActivity.this);
        }
    }

    @NonNull
    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, @Nullable Bundle args) {
        return new MovieLoader(this,MOVIE_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        loadingIndicator.setVisibility(View.GONE);
        movies = data;
        recyclerViewAdapter = new RecyclerViewAdapter(data,this);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Movie>> loader) {
        recyclerView.setAdapter(null);
    }

    @Override
    public void onClickItemList(int clickItemIndex) {
        Intent intent = new Intent(MainActivity.this,DetailActivity.class);
        Movie movie = movies.get(clickItemIndex);
        intent.putExtra("Movie",movie);
        startActivity(intent);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.popularItem){
            recyclerView.setAdapter(null);
            loadingIndicator.setVisibility(View.VISIBLE);
            editor.putString("searchSettings",popularUrlString);
            MOVIE_REQUEST_URL = popularUrlString;
            queryTextView.setText("Popular Movies");
            editor.apply();
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            //checking if there is internet connectivity
            if (networkInfo != null && networkInfo.isConnected()){
                LoaderManager loaderManager = getSupportLoaderManager();
                loaderManager.restartLoader(MOVIE_LOADER_ID,null,MainActivity.this);
            }
        }else if(itemId == R.id.topRatedItem){
            recyclerView.setAdapter(null);
            loadingIndicator.setVisibility(View.VISIBLE);
            editor.putString("searchSettings",topRatedUrlString);
            MOVIE_REQUEST_URL = topRatedUrlString;
            queryTextView.setText("Top Rated Movies");
            editor.apply();
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            //checking if there is internet connectivity
            if (networkInfo != null && networkInfo.isConnected()){
                LoaderManager loaderManager = getSupportLoaderManager();
                loaderManager.restartLoader(MOVIE_LOADER_ID,null,MainActivity.this);
            }
        }
        return super.onOptionsItemSelected(item);


    }
}
