package com.example.android.popularmovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.Data.MovieLoader;
import com.example.android.popularmovies.Data.QueryUtils;
import com.example.android.popularmovies.Fragment.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<AMovie>>{
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovietAdapter;

    private String baseUrl = "https://image.tmdb.org/t/p/original";

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private static final int FORECAST_LOADER_ID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_mainactivity);

        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setHasFixedSize(true);

        int spanCount = 3; // 3 columns
        int spacing = dpToPx(6); // 6dp
        boolean includeEdge = true;

        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        /* */
        mRecyclerView.setLayoutManager(layoutManager);

        /*
         * The MovieAdapter is responsible for linking our movie data with the Views that
         * will end up displaying our movie data.
         */
        mMovietAdapter = new MovieAdapter(this, new ArrayList<AMovie>());//new ArrayList<AMovie>());
        // How about putting a list inside the movie adapter ?
        // I don't know about that, really !
        //

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mMovietAdapter);

        /** Start the Loader */

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

    private int dpToPx(int dp) {
        float px = dp* this.getResources().getDisplayMetrics().density;
        return (int) px;
    }
    /** Get executed when the loader is initiated */
    @Override
    public Loader<List<AMovie>> onCreateLoader(int i, Bundle bundle) {

        // Make an Uri Builder with the Google Request Url as the base Uri
        Uri baseUri = Uri.parse(MOVIE_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Add 2 query parameter to the base Uri.
        // maxResults : determine the maximum number of elements
        // that should be downloaded from the API.
        // q: represent the book genre for the books that will be
        // downloaded from the API.

        uriBuilder.appendQueryParameter("maxResults", MAX_RESULT);
        uriBuilder.appendQueryParameter("q", mBookGenre.trim());

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
        /*mProgressSpinner.setVisibility(View.GONE);*/

        // Clear the adapter of the data of previous books
        mAdapter.clear();

        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.

        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    /** This will reset the previous created loader */
    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {

        // Create a new empty book list for the Adapter
        mAdapter.addAll(new ArrayList<Book>());
    }

}
