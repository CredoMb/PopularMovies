package com.example.android.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private ImageView mBackDropIv;
    private ImageView mPlayIcon;
    private TextView mPlayTrailerTv;
    private TextView mMovieTitleTv;
    private TextView mMovieYearTv;
    private TextView mMoviePublicTv;
    private TextView mMovieLenghtTv;
    private RatingBar mMovieRatingBar;
    private TextView mMovieSynopsisTv;
    private TextView mMovieStarsTv;
    private TextView mMovieDirectorTv;

    // The key for the extra in the intent
    public static String EXTRA_POSITION ="movie position";

    //The default position of a movie inside the movie list
    private int DEFAULT_POSITION = -1;

    // Will store the list of movies gotten from
    // the network request made inside the MovieLoader
    public static List<AMovie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // From the XML layout file inflate the view that will be used
        // on the Detail Activity of any given movie

            //  the Back Drop Image of the movie ok
        mBackDropIv = (ImageView) findViewById(R.id.backdrop_image);
            // the Play Icon on the movie BackDrop Image
        mPlayIcon = (ImageView) findViewById(R.id.play_trailer_iv);
            // the "Play Trailer" overlapping the Image BackDrop Image View ok
        mPlayTrailerTv = (TextView) findViewById(R.id.play_trailer_tv);
            // the TextView containing the movie's title ok
        mMovieTitleTv = (TextView) findViewById(R.id.title_text_view);
            // the release date's TextView ok
        mMovieYearTv = (TextView) findViewById(R.id.release_year_tv);
            // the TextView that contains the type of public allowed to view this movie ok
        mMoviePublicTv = (TextView) findViewById(R.id.public_allowed_tv);
            // the TextView with the movie Synopsis
        mMovieSynopsisTv = (TextView) findViewById(R.id.movie_synopsis_tv);
            // the TextView that contains the movie's length
        mMovieLenghtTv = (TextView) findViewById(R.id.movie_length_tv);
            // the TextView with the movie's Director Name
        mMovieDirectorTv = (TextView) findViewById(R.id.movie_director_tv);
            // the TextView that contains the list of the movie stars
        mMovieStarsTv = (TextView) findViewById(R.id.movie_stars_tv);
            // the rating Bar for the movie ratings
        mMovieRatingBar = (RatingBar) findViewById(R.id.rating_bar);


        // Receive the intent from the Main Activity
        Intent intent = getIntent();
        // If the intent is null, display a toast with an error message
        if (intent == null) {
            closeOnError();
        }

        // Get the extra associated with the intent.
        // This extra represent the position of the movie
        // that was clicked on
        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        // Get the array that contains the Json for all the sandwiches
        // present on the screen
        if (movieList == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }
        // How about getting the json again ?
        // How to create a String ressource
    }
    private void closeOnError(){
        finish();
        Toast.makeText(this, "Movie Data is not available", Toast.LENGTH_SHORT).show();
    }
}

// /s8qRIwA0zDPbnRekeU0rDwWE7q7.jpg