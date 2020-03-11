package com.example.android.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Data.MovieLoader;
import com.example.android.popularmovies.Fragment.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<List<AMovie>> {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovietAdapter;

    private String baseUrlForImage = "https://image.tmdb.org/t/p/original";

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private static final int MOVIE_LOADER_ID = 0;

    private String MOVIE_REQUEST_URL = "https://api.themoviedb.org/3/discover/movie?";

    /**
     * The progress Spinner
     */
    private ProgressBar mProgressSpinner;

    /**
     * Will be used as values for the mSort variable.
     * This will determine the order with which the movies
     * are displayed inside the Main Activity
     * */
    private String BY_POPULARITY = "popularity.desc";
    private String BY_RATINGS = "vote_average.desc";

    /** Will store the sorting option.
     *  The popularity is the default
     *  way of displaying the movies in the main
     *  Activity. */
    private String mSortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        mSortBy = BY_RATINGS;

        // Hide the action bar to let more space for
        // the GridLayout
        //getSupportActionBar().hide();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_mainactivity);

        // The progress spinner to use for a good
        mProgressSpinner = (ProgressBar) findViewById(R.id.loading_spinner);

        // Remove the progress spinner to display the empty state view properly
        //mProgressSpinner.setVisibility(View.INVISIBLE);

        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setHasFixedSize(true);

        int spanCount = 3; // 3 columns
        int spacing = dpToPx(8); // 6dp
        boolean includeEdge = false;

        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        /* */
        mRecyclerView.setLayoutManager(layoutManager);

        /*
         * The MovieAdapter is responsible for linking our movie data with the Views that
         * will end up displaying our movie data.
         */
        mMovietAdapter = new MovieAdapter(this, new ArrayList<AMovie>(),this);//new ArrayList<AMovie>());
        // How about putting a list inside the movie adapter ?
        // I don't know about that, really !

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mMovietAdapter);

        /** Start the Loader */
        startLoaderOrEmptyState(0,0,0);

        /*final int spacing = getResources().getDimensionPixelSize(R.dimen.recycler_spacing) / 2;

        // apply spacing
        mRecyclerView.setPadding(spacing, spacing, spacing, spacing);
        mRecyclerView.setClipToPadding(false);
        mRecyclerView.setClipChildren(false);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(spacing, spacing, spacing, spacing);
            }
        });*/
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

    /** Convert a dp value to a px value.
     * Will be used to set the margins between each views
     * of the gridLayout*/

    private int dpToPx(int dp) {
        float px = dp* this.getResources().getDisplayMetrics().density;
        return (int) px;
    }

    /**
     * Execute certain task based on the internet connection status.
     * If the connection is on, initiate the loader
     * other wise, display the empty state view
     */

    private void startLoaderOrEmptyState(int loaderId,int emptyStateImageId, int emptyStateTitleId) {

        // Check the status of the network, then either launch the Loader or
        // display the Empty State

        if (isNetworkConnected()) {
            getLoaderManager().initLoader(MOVIE_LOADER_ID + loaderId, null, MainActivity.this).forceLoad();
        } else {

            mProgressSpinner.setVisibility(View.GONE);
            Toast.makeText(this,"No Internet connexion",Toast.LENGTH_LONG).show();

            // Make a refresh button
            /*

            // Remove the progress spinner
            mProgressSpinner.setVisibility(View.GONE);

            // Fill the empty state view with its resources,
            // one image and 2 strings
            fillEmptyStateView(emptyStateImageId,
                    emptyStateTitleId,
                    emptyStateSubtitleId);*/
        }
    }

    /** Get executed when the loader is initiated */
    @Override
    public Loader<List<AMovie>> onCreateLoader(int i, Bundle bundle) {

        // Set the visibility of the spinner.

        // The key can not appear on github as this is a public repo
        String API_KEY = "";

        // Make an Uri Builder with the Google Request Url as the base Uri
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


        Log.e("The sort in loader",mSortBy);

        // This will execute the network request needed to get the data
        // from the API and return the data to onLoadFinished
        return new MovieLoader(this, uriBuilder.toString());
    }

    /** Get executed when the background thread finishes the work */
    @Override
    public void onLoadFinished(Loader<List<AMovie>> loader, List<AMovie> data) {

        // Set empty state view with the content of the "no result" state
        // This will explain to the user that no result where found for the
        // key word he entered
        /*fillEmptyStateView(R.drawable.empty_state_search,
                R.string.no_results_title,
                R.string.no_results_subtitle);*/

        // Hide the loading spinner
        mProgressSpinner.setVisibility(View.GONE);

        // Clear the adapter by setting an empty ArrayList
        mMovietAdapter.setMovieData(new ArrayList<AMovie>());

        /** If there is a valid list of {@link AMovie}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.*/

        if (data != null && !data.isEmpty()) {

            mProgressSpinner.setVisibility(View.GONE);
            mMovietAdapter.setMovieData(data);
        }
    }

    /** This will reset the previous created loader */
    @Override
    public void onLoaderReset(Loader<List<AMovie>> loader) {

        // Create a new empty Movie list for the Adapter
        mMovietAdapter =  new MovieAdapter(this,new ArrayList<AMovie>(),this);
    }

    @Override
    public void onClick(int position) {
        Toast.makeText(this,"was clicked",Toast.LENGTH_LONG).show();
        Log.e("The click happened","yeah");
        Intent intent = new Intent(MainActivity.this,DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POSITION,position);
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
                // Set the "" parameter to "" on the url
                // used to query the API.
                // This way, the movies will be displayed
                // according to the sort preference.
                mSortBy = BY_POPULARITY;

                Log.e("sort option in method",BY_POPULARITY);
                startLoaderOrEmptyState(1, 0, 0);

                return true;
            // Respond to a click on the "Ratings" menu option
            case R.id.action_ratings:
                // Do nothing for now
                mSortBy = BY_RATINGS;
                Toast.makeText(this,"Ratings was clicked",Toast.LENGTH_LONG).show();
                startLoaderOrEmptyState(2, 0, 0);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
