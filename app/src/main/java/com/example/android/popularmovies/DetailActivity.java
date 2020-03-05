package com.example.android.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Data.GlideHelperClass;

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

    private int FOUR_STARS = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Log.e("the list of movies",movieList.get(0).getTitle());
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

        // From the XML layout file inflate the view that will be used
        // on the Detail Activity of any given movie

        // Get the movie at "position"

        AMovie clickedMovie =  movieList.get(position);

        //  the Back Drop Image of the movie ok
        mBackDropIv = (ImageView) findViewById(R.id.backdrop_image);
        GlideHelperClass glideHelper = new GlideHelperClass(this,
                "https://image.tmdb.org/t/p/original/qonBhlm0UjuKX2sH7e73pnG0454.jpg",
                R.drawable.ic_launcher_background,
                mBackDropIv);

        glideHelper.loadImage();

        /** The trailer will not be part of the this app version.
         *  The next version will be capable of playing the movie trailer*/
        // the Play Icon on the movie BackDrop Image
        mPlayIcon = (ImageView) findViewById(R.id.play_trailer_iv);
        // the "Play Trailer" overlapping the Image BackDrop Image View ok
        mPlayTrailerTv = (TextView) findViewById(R.id.play_trailer_tv);

        // the TextView containing the movie's title ok
        mMovieTitleTv = (TextView) findViewById(R.id.movie_title_tv_detail);
        mMovieTitleTv.setText(clickedMovie.getTitle());

        // the release date's TextView ok
        mMovieYearTv = (TextView) findViewById(R.id.release_year_tv);
        mMovieYearTv.setText(clickedMovie.getYear());

/*
        // the TextView that contains the type of public allowed to view this movie ok
        mMoviePublicTv = (TextView) findViewById(R.id.public_allowed_tv);
*/

        // the TextView with the movie Synopsis
        mMovieSynopsisTv = (TextView) findViewById(R.id.movie_synopsis_tv);
        mMovieSynopsisTv.setText(clickedMovie.getSynopsis());

        // the TextView that contains the movie's length
        mMovieLenghtTv = (TextView) findViewById(R.id.movie_length_tv);
        mMovieLenghtTv.setText(clickedMovie.getmMovieLenght());

        // the TextView with the movie's Director Name
        mMovieDirectorTv = (TextView) findViewById(R.id.movie_director_tv);
        mMovieDirectorTv.setText(clickedMovie.getDirector());

        // the TextView that contains the list of the movie stars
        mMovieStarsTv = (TextView) findViewById(R.id.movie_stars_tv);
        // This will append the four main starts of the movie
        // inside the "stars" TextView
        for(int i=0 ; i < FOUR_STARS; i++) {
            mMovieStarsTv.append(clickedMovie.getMovieStars().get(i) + ", ");
        }

        // the rating Bar for the movie ratings
        mMovieRatingBar = (RatingBar) findViewById(R.id.rating_bar);
        mMovieRatingBar.setRating(clickedMovie.getMovieRating());
    }


    private void closeOnError(){
        finish();
        Toast.makeText(this, "Movie Data is not available", Toast.LENGTH_SHORT).show();
    }
}

// /s8qRIwA0zDPbnRekeU0rDwWE7q7.jpg