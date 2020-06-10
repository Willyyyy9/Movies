package com.example.popularmovies.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.popularmovies.Movie;
import com.example.popularmovies.Movie.MovieReview;
import com.example.popularmovies.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.content.ContentValues.TAG;

public class ReviewListViewAdapter extends ArrayAdapter<MovieReview> {
    private Context context;
    private int resource;

    public ReviewListViewAdapter(@NonNull Context context, int resource, ArrayList<MovieReview> reviews) {
        super(context, resource, reviews);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Getting data from data source
        String reviewAuthor = getItem(position).getReviewAuthor();

        //Inflating
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);

        //Initializing the TextViews
        TextView reviewAuthorTextView = convertView.findViewById(R.id.reviewAuthorTextView);

        //Populating the TextViews
        reviewAuthorTextView.setText(reviewAuthor);

        return convertView;
    }
}
