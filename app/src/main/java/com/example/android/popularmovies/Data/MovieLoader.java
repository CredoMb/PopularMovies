package com.example.android.popularmovies.Data;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Movie;

import com.example.android.popularmovies.AMovie;

import java.util.List;

public class MovieLoader extends AsyncTaskLoader<List<AMovie>> {

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
    public List<AMovie> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        return QueryUtils.fetchMoviesData(mUrl);
    }
    // Obtain 2 things : How to make the card I want to make
    // Open dater
}