package com.example.android.popularmovies.Data;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Movie;

import com.example.android.popularmovies.AMovie;
import com.example.android.popularmovies.DetailActivity;

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
        // Make the network request and
        // return a list of movie
        List <AMovie> movieList = QueryUtils.fetchMoviesData(mUrl);

        // The list should already be ready
        // movieList is ready but misses certain things


        // Set the list of movie on the
        // "movieList" static attribute of
        // the DetailActivity. This way, the
        // DetailActivity could use the list to
        // show details about a movie that was clicked
        DetailActivity.movieList = movieList;
        return movieList;

        // Pourquoi est ce qu'il n'y a pas assez de "movie" dans la liste ?
        // On dirait qu'il n'y a que 2 movie ?
    }
    // Obtain 2 things : How to make the card I want to make
    // Open dater

}