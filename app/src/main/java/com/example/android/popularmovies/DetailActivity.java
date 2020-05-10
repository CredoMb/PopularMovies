package com.example.android.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Data.GlideHelperClass;
import com.example.android.popularmovies.Data.QueryUtils;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Typeface.BOLD;

public class DetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<String>> {

    private static final String TAG = DetailActivity.class.getSimpleName();

    /**
     * The following variables will store
     * all the views of the DetailActivity
     */
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

    /**
     * The Id for the Loader that should extract
     * the cast and details about the movie
     */
    private static final int EXTRACT_CAST_LOADER_ID = 1;

    private ProgressBar mProgressSpinner;

    /**
     * Will store the groupview of the empty state
     */
    private RelativeLayout mEmptyStateRl;

    /**
     * The Refresh button of the empty state in the
     * DetailActivity
     */
    private Button mEmptyStateRefreshButton;

    // The key to get the extra date our of the intent
    public static String EXTRA_POSITION = "movie position";

    //The default position of a movie inside the movie list
    private int DEFAULT_POSITION = -1;

    // Will store the position of the
    // clicked Movie
    int mPosition;

    // Will store the movie that was clicked on
    AMovie mClickedMovie;

    // Will store the list of movies gotten from
    // the network request made inside the MovieLoader
    public static ArrayList<AMovie> mMovieList;

    // The key used to save the movie list as
    // a bundle.
    public String MOVIE_LIST = "movie_list";

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

        //Retrieve the ArrayList that was saved as bundle
        if (!(savedInstanceState == null)) {

            if (savedInstanceState.containsKey(MOVIE_LIST)) {
                mMovieList = savedInstanceState.getParcelableArrayList(MOVIE_LIST);
            }
        }

        // Receive the intent from the Main Activity
        Intent intent = getIntent();

        // If the intent is null, display a toast with an error message
        if (intent == null) {
            closeOnError();
        }

        // Get the extra associated with the intent.
        // This extra represent the mPosition of the movie
        // that was clicked on
        mPosition = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);

        if (mPosition == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        // Verify if the List of movie is not empty
        if (mMovieList == null) {
            // movie data unavailable
            closeOnError();
            return;
        }

        // Stores the EmptyState RelativeLayout
        mEmptyStateRl = (RelativeLayout) findViewById(R.id.detail_empty_group_view);

        // Before Laying out all the views
        // Make sure that there'a an internet connection.
        // If so, the empty state will be made invisible,
        // else, it will appear as the first layout of the DetailActivity
        if (isNetworkConnected()) {
            mEmptyStateRl.setVisibility(View.GONE);
        }

        // Stores the progress spinner of the layout
        mProgressSpinner = (ProgressBar) findViewById(R.id.detail_loading_spinner);
        mProgressSpinner.setVisibility(View.VISIBLE);

        // Stores the refresh button of the emtpy state view
        mEmptyStateRefreshButton = (Button) findViewById(R.id.detail_refresh_tv);

        // Set an onclick listener to define the bejabiour of the
        // refresh button of the empoty state view
        mEmptyStateRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmptyStateRl.setVisibility(View.GONE);
                startLoaderOrEmptyState(EXTRACT_CAST_LOADER_ID);
            }
        });

        // Get the movie at "mPosition"
        mClickedMovie = mMovieList.get(mPosition);

        // When
        //  The Back Drop Image of the movie ok
        mBackDropIv = (ImageView) findViewById(R.id.backdrop_image);
        GlideHelperClass glideHelper = new GlideHelperClass(this,
                mClickedMovie.getBackDropImagePath(),
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
        mMovieTitleTv.setText(mClickedMovie.getTitle());

        // the release date's TextView ok
        mMovieYearTv = (TextView) findViewById(R.id.release_year_tv);
        mMovieYearTv.setText(mClickedMovie.getYear());

        // the TextView with the movie Synopsis
        mMovieSynopsisTv = (TextView) findViewById(R.id.movie_synopsis_tv);
        mMovieSynopsisTv.append(mClickedMovie.getSynopsis());
        makeTheTitleBold(mMovieSynopsisTv, SYNOPSIS_TEXT_LAST_INDEX);

        /** The length, director and stars values
         will also be set inside "onLoadFinished"
         after the API Query is completed.*/

        // The TextView that contains the movie's length.
        mMovieLenghtTv = (TextView) findViewById(R.id.movie_length_tv);
        // Set the MovieLength onto it view
        mMovieLenghtTv.setText(mClickedMovie.getmMovieLenght());

        // the TextView with the movie's Director Name
        mMovieDirectorTv = (TextView) findViewById(R.id.movie_director_tv);
        // Set the MovieDirector onto it view
        mMovieDirectorTv.append(mClickedMovie.getDirector());
        // Will only make the "Director" label bold
        makeTheTitleBold(mMovieDirectorTv, DIRECTOR_TEXT_LAST_INDEX);

        // the TextView that contains the list of the movie stars
        mMovieStarsTv = (TextView) findViewById(R.id.movie_stars_tv);
        // This will append the four main starts of the movie
        // inside the "stars" TextView
        if (mClickedMovie.getMovieStars().size() > FOUR_STARS) {
            appendStarNames(mClickedMovie, mMovieStarsTv, FOUR_STARS);
        } else {
            appendStarNames(mClickedMovie, mMovieStarsTv, mClickedMovie.getMovieStars().size());
        }
        // Will only make the "main stars" label bold
        makeTheTitleBold(mMovieStarsTv, MAIN_STARS_TEXT_LAST_INDEX);


        // the rating Bar for the movie ratings
        mMovieRatingBar = (RatingBar) findViewById(R.id.rating_bar);
        mMovieRatingBar.setRating(mClickedMovie.getMovieRating());

        // If the movie already has a Length, Director
        // and a list of Stars, don't run the Loader.
        // This will avoid making API calls everytime a
        // movie is clicked. Instead the Activity will
        // the data that has been already cached
        if (TextUtils.isEmpty(mClickedMovie.getmMovieLenght())
                && TextUtils.isEmpty(mClickedMovie.getDirector())
                && mClickedMovie.getMovieStars().size() == 0) {

            // Start the Loader to update the clickedMovie.
            startLoaderOrEmptyState(EXTRACT_CAST_LOADER_ID);

            // If the movie possess all the necessary attribute,
            // then there's no need to display neither the spinner
            // nor the Empty State View
        } else {
            mProgressSpinner.setVisibility(View.GONE);
            mEmptyStateRl.setVisibility(View.GONE);
        }

        /*Override the back button so that it looks like
         * an up button. It will go back the Parent Activity
         * and not exit the app*/

        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_LIST, mMovieList);
        super.onSaveInstanceState(outState);
    }

    /**
     * Method to Check the Network connection and return true or false
     * based on the network connection state
     */
    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    /**
     * The loader call backs
     */

    @Override
    public Loader<ArrayList<String>> onCreateLoader(int loaderId, Bundle bundle) {

        /**
         * Check the Loader Id
         */
        switch (loaderId) {

            case EXTRACT_CAST_LOADER_ID:

                // Set the visibility of the spinner.
                mProgressSpinner.setVisibility(View.VISIBLE);

                // Create a Loader that should execute the
                // API calls to get the remaining infos.
                AsyncTaskLoader<ArrayList<String>> movieDetailsLoader =
                        new AsyncTaskLoader<ArrayList<String>>(this) {
                            @Override
                            public ArrayList<String> loadInBackground() {
                                // We will get the cast of the clicked movie,
                                // by calling the method QueryUtils.extractCastAndSetExtraDetails().
                                // This method makes 2 API calls, so it should to be
                                // executed in a background thread.

                                ArrayList<String> clickedMovieCast = QueryUtils.extractCastAndSetExtraDetails(
                                        mClickedMovie.getMovieId());

                                return clickedMovieCast;
                            }
                        };

                return movieDetailsLoader;

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);

        }
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<String>> loader, ArrayList<String> clickedMovieCast) {

        mProgressSpinner.setVisibility(View.GONE);

        // mMovieDirector and mMovieLength are 2 global variables located in
        // QueryUtils. Those 2 variables were updated
        // when we called extractCastAndSetExtraDetails().
        // This way, we can easily set the director and the length of the
        // movie into our clickedMovie.

        String movieDirector = QueryUtils.mMovieDirector;
        String movieLength = QueryUtils.mMovieLength;

        // After getting those 3 main informations, which are
        // the cast the director and the length,
        // we will update the clickedMovie.
        mClickedMovie.setMovieStars(clickedMovieCast);
        mClickedMovie.setMovieDirector(movieDirector);
        mClickedMovie.setMovieLength(movieLength);

        // Update the movie list of the DetailActivity.
        mMovieList.set(mPosition, mClickedMovie);

        // Set the MovieLength onto it view
        mMovieLenghtTv.setText(mClickedMovie.getmMovieLenght());

        // Set the MovieDirector onto it view
        mMovieDirectorTv.append(mClickedMovie.getDirector());

        // This will append the four main starts of the movie
        // inside the "stars" TextView.
        if (mClickedMovie.getMovieStars().size() > FOUR_STARS) {
            appendStarNames(mClickedMovie, mMovieStarsTv, FOUR_STARS);
        } else {
            appendStarNames(mClickedMovie, mMovieStarsTv, mClickedMovie.getMovieStars().size());
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<String>> loader) {
        //
    }

    /**
     * Execute certain task based on the internet connection status.
     * If the connection is on, initiate the loader
     * other wise, display the empty state view
     */

    private void startLoaderOrEmptyState(int loaderId) {
        // Check the status of the network, then either launch the Loader or
        // display the Empty State

        if (isNetworkConnected()) {
            getLoaderManager().initLoader(loaderId, null, DetailActivity.this).forceLoad();
        } else {
            mEmptyStateRl.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // When the home button is pressed, take the user back to the MainActivity
        // android.R.id.home represents the up button, the one located at the app bar

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            // onBackPressed(); this will make the
            // up button behave like the back button

            // When we press the backButton, save
            // the movie list as a parcel using "onSaveInstanceState".
            // This will help keep the list updated even after the
            // DetailActivity is no longer visible.
            onSaveInstanceState(new Bundle());
        }
        return super.onOptionsItemSelected(item);
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

    /**
     * Will be used to make a small set of the textView become bold
     */
    private void makeTheTitleBold(TextView textView, int end) {

        String originalText = textView.getText().toString();

        SpannableString spannableString = new SpannableString(originalText);
        StyleSpan styleSpanBold = new StyleSpan(BOLD);

        spannableString.setSpan(styleSpanBold, 0, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }
}