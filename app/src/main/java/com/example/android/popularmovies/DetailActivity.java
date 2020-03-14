package com.example.android.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Data.GlideHelperClass;

import java.util.List;

import static android.graphics.Typeface.BOLD;

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

    // The key to get the extra date our of the intent
    public static String EXTRA_POSITION = "movie position";

    //The default position of a movie inside the movie list
    private int DEFAULT_POSITION = -1;

    // Will store the list of movies gotten from
    // the network request made inside the MovieLoader
    public static List<AMovie> movieList;

    // The maximum number of movie stars to
    // display on the detail activity
    private final int FOUR_STARS = 4;

    // The last index of the word "Director".
    // Will be used to make that text chunk bold
    private final int DIRECTOR_TEXT_LAST_INDEX = 8;

    // The last index of the word "Synopsis"
    // Will be used to make that text chunk bold
    private final int SYNOPSIS_TEXT_LAST_INDEX = 8;

    // The last index of the string "Main Stars"
    // Will be used to make that text chunk bold
    private final int MAIN_STARS_TEXT_LAST_INDEX = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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

        // From the XML layout file inflate the view that will be used
        // on the Detail Activity of any given movie

        // Get the movie at "position"
        AMovie clickedMovie = movieList.get(position);

        //  the Back Drop Image of the movie ok
        mBackDropIv = (ImageView) findViewById(R.id.backdrop_image);
        GlideHelperClass glideHelper = new GlideHelperClass(this,
                clickedMovie.getBackDropImagePath(),
                R.drawable.placeholder_image,
                mBackDropIv);

        glideHelper.loadImage();

        /** The trailer will not be part of the this app version.
         *  The next version will be capable of playing the movie trailer*/
        // the Play Icon on the movie BackDrop Image
        /*
        mPlayIcon = (ImageView) findViewById(R.id.play_trailer_iv);
        // the "Play Trailer" overlapping the Image BackDrop Image View ok
        mPlayTrailerTv = (TextView) findViewById(R.id.play_trailer_tv);
        */

        // the TextView containing the movie's title ok
        mMovieTitleTv = (TextView) findViewById(R.id.movie_title_tv_detail);
        mMovieTitleTv.setText(clickedMovie.getTitle());

        // the release date's TextView ok
        mMovieYearTv = (TextView) findViewById(R.id.release_year_tv);
        mMovieYearTv.setText(clickedMovie.getYear());

        // the TextView with the movie Synopsis
        mMovieSynopsisTv = (TextView) findViewById(R.id.movie_synopsis_tv);
        mMovieSynopsisTv.append(clickedMovie.getSynopsis());
        makeTheTitleBold(mMovieSynopsisTv, SYNOPSIS_TEXT_LAST_INDEX);

        // the TextView that contains the movie's length
        mMovieLenghtTv = (TextView) findViewById(R.id.movie_length_tv);
        mMovieLenghtTv.setText(clickedMovie.getmMovieLenght());

        // the TextView with the movie's Director Name
        mMovieDirectorTv = (TextView) findViewById(R.id.movie_director_tv);
        mMovieDirectorTv.append(clickedMovie.getDirector());
        makeTheTitleBold(mMovieDirectorTv, DIRECTOR_TEXT_LAST_INDEX);

        // the TextView that contains the list of the movie stars
        mMovieStarsTv = (TextView) findViewById(R.id.movie_stars_tv);
        // This will append the four main starts of the movie
        // inside the "stars" TextView
        if (clickedMovie.getMovieStars().size() > FOUR_STARS) {
            appendStarNames(clickedMovie, mMovieStarsTv, FOUR_STARS);
        } else {
            appendStarNames(clickedMovie, mMovieStarsTv, clickedMovie.getMovieStars().size());
        }
        makeTheTitleBold(mMovieStarsTv, MAIN_STARS_TEXT_LAST_INDEX);

        // the rating Bar for the movie ratings
        mMovieRatingBar = (RatingBar) findViewById(R.id.rating_bar);
        mMovieRatingBar.setRating(clickedMovie.getMovieRating());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Movie Data is not available", Toast.LENGTH_LONG).show();
    }

    private void appendStarNames(AMovie clickedMovie, TextView tv, int maxStars) {

        for (int i = 0; i < maxStars; i++) {
            tv.append(clickedMovie.getMovieStars().get(i));

            if (i < maxStars - 1) {
                tv.append(", ");
            }
        }
    }

    /** Will be used to make a small set of the textView become bold */
    private void makeTheTitleBold(TextView textView, int end) {

        String originalText = textView.getText().toString();

        SpannableString spannableString = new SpannableString(originalText);
        StyleSpan styleSpanBold = new StyleSpan(BOLD);

        spannableString.setSpan(styleSpanBold, 0, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }
}