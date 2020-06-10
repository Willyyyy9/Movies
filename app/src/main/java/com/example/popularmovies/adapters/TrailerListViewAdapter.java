package com.example.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.popularmovies.Movie;
import com.example.popularmovies.Movie.MovieTrailer;
import com.example.popularmovies.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TrailerListViewAdapter extends ArrayAdapter<MovieTrailer> {
    private Context context;
    private int resource;


    public TrailerListViewAdapter(@NonNull Context context, int resource, ArrayList<MovieTrailer> movieTrailers) {
        super(context, resource, movieTrailers);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Getting data that needs to be displayed
        String trailerName = getItem(position).getTrailerName();
        String trailerSite = getItem(position).getTrailerSite();

        //Inflating the listView
        LayoutInflater inflater =LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);

        //Intializing TextViews
        TextView trailerNameTextView = convertView.findViewById(R.id.trailerTitleTextView);
        TextView trailerSiteTextView = convertView.findViewById(R.id.trailerSiteTextView);

        //Populating data into TextViews
        trailerNameTextView.setText(trailerName);
        trailerSiteTextView.setText(trailerSite);

        return convertView;
    }
}
