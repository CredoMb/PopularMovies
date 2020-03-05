package com.example.android.popularmovies;

import java.util.ArrayList;
import java.util.List;

public class AMovie {

    /** Except the mPosterImagePath theses attributes
     * will be used on the MainActivity and the DetailActivity */

    // The id of the movie's image
    private String mPosterImagePath;
    // The movie's name
    private String mTitle;
    // The movie's year
    private String mYear;

    /** Theses other attributes will be used
     * inside the DetailActivity only*/
    // The synopsis of the movie
    private String mSynopsis;
    // The Director of the movie
    private String mMovieDirector;

    // The public allowed in this movie
    private String mAllowedPublic;

    // The lenght of the movie
    private String mMovieLenght;

    // A list that contains all the movie stars
    private List<String> mMovieStars;
    // The average rating of the movie
    private float mMovieRating;

    /**
     * Constructor with the 3 main attribute of the class
     */
    public AMovie(String title, String PosterImagePath,
                  String year) {
        mTitle = title;
        mPosterImagePath = PosterImagePath;
        mYear = year;
    }

    /**
     * Constructor with all the attributes of the class
     */
    public AMovie(String title, String posterImagePath,
                  String year,
                  String lenght,
                  String synopsis,
                  String movieDirector,
                  List<String> movieStars,
                  float movieRating) {

        this(title,posterImagePath,year);
        mMovieLenght = lenght;
        mSynopsis = synopsis;
        mMovieDirector = movieDirector;
        mMovieStars = movieStars;
        mMovieRating = movieRating;

    }

    /**
     * Defining getters for the "AMovie" Class
     */

    public String getPosterPath() {
        return mPosterImagePath;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getYear(){return mYear;}

    public String getSynopsis() {
        return mSynopsis;
    }

    public String getmMovieLenght() {return mMovieLenght;}

    public String getDirector() {
        return mMovieDirector;
    }

    public List<String> getMovieStars() {
        return mMovieStars;
    }

    public float getMovieRating() {
        // Divide the current rating by 2,
        // to have a rating out of 5
        return mMovieRating/2f;
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

    public void setMovieRating(float movieRating) {
        mMovieRating = movieRating;
    }
}
