package com.example.android.popularmovies.Data;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.popularmovies.AMovie;
import com.example.android.popularmovies.DetailActivity;

import java.util.ArrayList;
import java.util.List;

public class MovieLoader extends AsyncTaskLoader<ArrayList<AMovie>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = MovieLoader.class.getName();

    /**
     * Query Url
     **/
    private String mUrl;

    /** The constructor  */
    public MovieLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    /** This will help load the data */
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**This will cause the "QueryUtils.fetchMoviesData" to be executed on a background
     thread  */
    @Override
    public ArrayList<AMovie> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Make the network request and
        // return a list of movie
        ArrayList<AMovie> movieList = QueryUtils.fetchMoviesData(mUrl);

        // The list should already be ready
        // mMovieList is ready but misses certain things

        return movieList;

    }

}


