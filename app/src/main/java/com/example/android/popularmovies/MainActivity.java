package com.example.android.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.popularmovies.Data.MovieLoader;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<ArrayList<AMovie>> {

    private static final String TAG = MainActivity.class.getSimpleName();

    /** The Ids for the 2 Loaders to be used in
     * this activity. */
    private static final int MOVIE_LOADER_ID = 0;

    /** The Recycler and its Adapter*/
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    /**The variable that should contain the movie list*/

    public static ArrayList<AMovie> mMovieList;

    /**
     * Will be used as the base url and parameter will be
     */
    private String MOVIE_REQUEST_URL = "https://api.themoviedb.org/3/discover/movie?";

    /** The key used to save the movie list as
      a bundle. */
    public String MOVIE_LIST = "movie_list";

    /**
     * The progress Spinner
     */
    private ProgressBar mProgressSpinner;

    /**
     * Will be used as values for the mSort variable.
     * This will determine the order with which the movies
     * are displayed inside the Main Activity
     */
    private String BY_POPULARITY = "popularity.desc";
    private String BY_RATINGS = "vote_average.desc";

    /**
     * Will store the sorting option.
     * The popularity is the default
     * way of displaying the movies in the main
     * Activity.
     */
    private String mSortBy;

    /**
     * The group view that will contain the
     * empty state for a bad internet connection
     */

    private RelativeLayout emptyStateRl;

    /**
     * This is the textview present inside the
     * empty state's group view
     */

    private TextView mRefreshTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the movie list to make sure there
        // are no "null pointer" exceptions
        mMovieList = new ArrayList<AMovie>();

        // Verify if the movie list was saved as a bundle
        // and retrieve the data.
        if (savedInstanceState != null) {

            if (savedInstanceState.containsKey(MOVIE_LIST)) {
                mMovieList = savedInstanceState.getParcelableArrayList(MOVIE_LIST);

            }
        }

        // Store the entire emptyState view inside a variable
        emptyStateRl = (RelativeLayout) findViewById(R.id.empty_group_view);

        // Make the empty state invisible by default.
        // It will only become visible
        // if the API call is not successful.
        emptyStateRl.setVisibility(View.INVISIBLE);

        // The refresh textView from the empty State
        mRefreshTv = findViewById(R.id.refresh_tv);

        // Once clicked, the refresh textView will
        // trigger the Loader to start
        mRefreshTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoaderOrEmptyState(MOVIE_LOADER_ID);
            }
        });

        // Set the default movie sortage.
        // This will be used as a paramater inside the
        // query url, to classify movies based on their popularity
        mSortBy = BY_POPULARITY;

        // Remove the shadow under the app bar
        getSupportActionBar().setElevation(0);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_mainactivity);

        // The progress spinner to use for a good
        mProgressSpinner = (ProgressBar) findViewById(R.id.main_loading_spinner);

        // Create a new Grid Layout Manager
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setHasFixedSize(true);

        // Define the properties of the Layout Manager
        int spanCount = 3; // 3 columns
        int spacing = dpToPx(8); // 8dp
        boolean includeEdge = false;

        //This is needed to manage spacing between views
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        mRecyclerView.setLayoutManager(layoutManager);

        /*
         * The MovieAdapter is responsible for linking our movie data with the Recycler that
         * will end up displaying the data.
         */
        mMovieAdapter = new MovieAdapter(this, new ArrayList<AMovie>(), this);

        // Set the movie list as the data of the adapter
        mMovieAdapter.setMovieData(mMovieList);

        // Set the adapter onto its RecyclerView
        mRecyclerView.setAdapter(mMovieAdapter);

        // Start the Loader only if there's no element
        // inside our movie list. Otherwise,
        // remove the spinner from the screen.
        if(mMovieList.isEmpty()) {
            startLoaderOrEmptyState(MOVIE_LOADER_ID);
        }
        else {
            // Remove the progress from the screen.
            // As we already have data to display,
            // we will not need it.
            mProgressSpinner.setVisibility(View.GONE);
        }

    }

    /**
      * onSaveInstanceState will be called right after
      * the "onStop()" method of the life cycle.
      * Before stopping the activity, the app will
      * store the movie data so it can be retrieve
      * after a configuration change. This will avoid making
      * multiple API queries every time a config change (ex: device rotation) occurs. */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putParcelableArrayList(MOVIE_LIST,mMovieList);
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
     * Convert a dp value to a px value.
     * Will be used to set the margins between each views
     * of the gridLayout
     */

    private int dpToPx(int dp) {
        float px = dp * this.getResources().getDisplayMetrics().density;
        return (int) px;
    }

    /**
     * Execute certain task based on the internet connection status.
     * If the connection is on, initiate the loader
     * other wise, display the empty state view
     */

    private void startLoaderOrEmptyState(int loaderId) {
        // Check the status of the network, then either launch the Loader or
        // display the Empty State.

        // It's
        if (isNetworkConnected()) {
            getLoaderManager().initLoader(loaderId, null, MainActivity.this).forceLoad();
        } else {

            emptyStateRl.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Get executed when the loader is initiated
     */
    @Override
    public Loader<ArrayList<AMovie>> onCreateLoader(int i, Bundle bundle) {

        // Remove the empty state view
        emptyStateRl.setVisibility(View.GONE);

        // Set the visibility of the spinner.
        mProgressSpinner.setVisibility(View.VISIBLE);

        // The key can not appear on github as this is a public repo
        String API_KEY = "cd401ba98e50ce8bf913cdce912aa430";

        // Make an Uri Builder with the MOVIE_REQUEST_URL as the base Uri
        Uri baseUri = Uri.parse(MOVIE_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Add query parameters to the base Uri.
        // language : determine the movie languages
        // sort_by: represent the way movies should be shown
        // include_adult : determines weither or not we'll get adult movies from the API
        // include_video : determines weither or not we'll get Trailer videos of each movie we're getting from the API
        // page: indicate the # of page to download from the API

        uriBuilder.appendQueryParameter("api_key", API_KEY);
        uriBuilder.appendQueryParameter("language", "en-US");

        // Change this to vote average desc
        uriBuilder.appendQueryParameter("sort_by", mSortBy);
        uriBuilder.appendQueryParameter("include_adult", "true");
        uriBuilder.appendQueryParameter("include_video", "false");
        uriBuilder.appendQueryParameter("page", "1");

        // This will execute the network request needed to get the data
        // from the API and return the data to onLoadFinished
        return new MovieLoader(this, uriBuilder.toString());

    }

    /**
     * Get executed when the background thread finishes the work
     */
    @Override
    public void onLoadFinished(Loader<ArrayList<AMovie>> loader, ArrayList<AMovie> data) {

        mProgressSpinner.setVisibility(View.GONE);

        // Clear the adapter by setting an empty ArrayList
        mMovieAdapter.setMovieData(null);

        // Remove the emptyState view
        emptyStateRl.setVisibility(View.GONE);

        /* If there is a valid list of {@link AMovie}s, then add them to the adapter's
         data set. This will trigger the ListView to update. */

        if (data != null && !data.isEmpty()) {

            // Update the movie list of DetailActivity
            DetailActivity.mMovieList = data;

            // Set data onto the adapter
            mMovieList = data;
            mMovieAdapter.setMovieData(mMovieList);
        } else {

            /*If the list wasn't returned, then make sure
             * the internet connection is still and display
             * the emptystate view if necessary*/
            if (!isNetworkConnected()) {
                emptyStateRl.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * This will reset the previous created loader
     */
    @Override
    public void onLoaderReset(Loader<ArrayList<AMovie>> loader) {

        // Create a new empty Movie list for the Adapter
        mMovieList = new ArrayList<AMovie>();
        mMovieAdapter = new MovieAdapter(this, mMovieList, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        // If there's no internet connection display the emptystate view
        if (!isNetworkConnected()) {
            emptyStateRl.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(int position) {

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/activity_main_menu.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {

            // Respond to a click on the "Popularity" menu option
            case R.id.action_popularity:
                // Set the "mSortBy" parameter to "popularity.desc" on the url
                // used to query the API.
                // This way, the movies will be displayed
                // according to the sort preference.
                mSortBy = BY_POPULARITY;
                mMovieAdapter.setMovieData(new ArrayList<AMovie>());

                // Destroy the previous loader and start a new one
                getLoaderManager().destroyLoader(MOVIE_LOADER_ID);
                startLoaderOrEmptyState(MOVIE_LOADER_ID);

                return true;

            // Respond to a click on the "Ratings" menu option
            case R.id.action_ratings:
                // Make this to sort the movies by ratings

                // Set the "mSortBy" parameter to "vote_average.desc" on the url
                // used to query the API.
                // This way, the movies will be displayed
                // according to the sort preference.
                mSortBy = BY_RATINGS;

                // Destroy the previous loader and start a new one
                getLoaderManager().destroyLoader(MOVIE_LOADER_ID);
                startLoaderOrEmptyState(MOVIE_LOADER_ID);

                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
