package com.example.android.popularmovies;

import java.util.ArrayList;
import java.util.List;

public class AMovie {

    // The id of the movie's image
    private String mImageUrl;

    // The movie's name
    private String mTitle;

    // The movie's year
    private String mYear;

    // The synopsis of the movie
    private String mSynopsis;
    // The director of the movie
    private String mMovieDirector;
    // A list that contains all the movie stars
    private List<String> mMovieStars;
    // The average rating of the movie
    private float mMovieRating;


    /**
     * Constructor with the 3 main attribute of the class
     */
    public AMovie(String title, String imageUrl, String year) {
        mTitle = title;
        mImageUrl = imageUrl;
        mYear = year;
    }

    /**
     * Defining getters for the "AMovie" Class
     */

    public String getImageId() {
        return mImageUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getYear(){return mYear;}

    public String getSynopsis() {
        return mSynopsis;
    }

    public String getMovieDirector() {
        return mMovieDirector;
    }

    public List<String> getMovieStars() {
        return mMovieStars;
    }

    public float getMovieRating() {
        return mMovieRating;
    }

    /**
     * Defining setters for certain attributes of the "AMovie" class.
     * We will define setters only for a certain attributes.
     */
    public void setSynopsis(String synopsis) {
        mSynopsis = synopsis;
    }

    public void setMovieStars(List<String> movieStars) {

        // Initialise the list before putting values in
        mMovieStars = new ArrayList<String>();

        for (int i = 0; i < movieStars.size(); i++) {
            mMovieStars.set(i, movieStars.get(i));
        }
    }

    public void setMovieDirector(String movieDirector) {
        mMovieDirector = movieDirector;
    }

    public void setMovieRating(float movieRating) {
        mMovieRating = movieRating;
    }
}
