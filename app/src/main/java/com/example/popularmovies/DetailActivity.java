package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class DetailActivity extends AppCompatActivity{
    private ImageView posterImageView;
    private TextView titleTextView;
    private TextView synopsisTextView;
    private TextView ratingTextView;
    private TextView releaseDateTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        posterImageView = findViewById(R.id.posterImageView);
        titleTextView = findViewById(R.id.titleTextView);
        synopsisTextView = findViewById(R.id.synopsisTextView);
        ratingTextView = findViewById(R.id.ratingTextView);
        releaseDateTextView = findViewById(R.id.releaseDateTextView);
        Intent intent = getIntent();
        Movie movie = (Movie) intent.getSerializableExtra("Movie");
        String poster = movie.getPoster();
        String posterBase = "https://image.tmdb.org/t/p/w185";
        String posterNewBase = "https://image.tmdb.org/t/p/w500";
        poster = poster.replaceAll(posterBase,posterNewBase);
        Picasso.get().load(Uri.parse(poster)).into(posterImageView);
        titleTextView.setText(movie.getTitle());
        synopsisTextView.setText(movie.getSynopsis());
        ratingTextView.setText(Integer.toString(movie.getRating())+"/10");
        releaseDateTextView.setText("( " + reverseDate(movie.getDate()) +" )");


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
