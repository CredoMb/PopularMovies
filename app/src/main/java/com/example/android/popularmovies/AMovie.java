package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class AMovie implements Parcelable {

    /** Except the mPosterImagePath theses attributes
     * will be used on the MainActivity and the DetailActivity */

    private int mMovieId;
    // The id of the movie's image
    private String mPosterImagePath;
    // The movie's name
    private String mTitle;
    // The movie's year
    private String mYear;

    /** Theses other attributes will be used
     * inside the DetailActivity only */
    private String mBackDropImagePath;
    // The synopsis of the movie
    private String mSynopsis;
    // The Director of the movie
    private String mMovieDirector;

    // The public allowed in this movie
    private String mAllowedPublic;

    // The lenght of the movie
    private String mMovieLenght;

    // A list that contains all the movie stars
    private ArrayList<String> mMovieStars;
    // The average rating of the movie
    private float mMovieRating;

    /**
     * Constructor with the 3 main attribute of the class
     */
    public AMovie(int movieId, String title, String PosterImagePath,
                  String year) {
        mMovieId = movieId;
        mTitle = title;
        mPosterImagePath = PosterImagePath;
        mYear = year;
    }

    /**
     * Constructor with all the attributes of the class
     */
    public AMovie(int movieId, String title,
                  String posterImagePath,
                  String year,
                  String lenght,
                  String backDropImagePath,
                  String synopsis,
                  String movieDirector,
                  ArrayList<String> movieStars,
                  float movieRating) {

        this(movieId,title,posterImagePath,year);
        mMovieLenght = lenght;
        mBackDropImagePath = backDropImagePath;
        mSynopsis = synopsis;
        mMovieDirector = movieDirector;
        mMovieStars = movieStars;
        mMovieRating = movieRating;

    }

    /** Private constructor to be used
     *  by the createFromParcel method of
     *  Parcelable Creator. Will create the object from the
     *  Parcel*/

    private AMovie (Parcel in) {

        this(in.readInt(), // The id
                in.readString(), // The title
                in.readString(), // The poster Image Path
                in.readString(), // The year
                in.readString(), // The movie Length
                in.readString(), // The backdrop image Path
                in.readString(), // The synopsis
                in.readString(), // The Movie Director
                in.readArrayList(String.class.getClassLoader()), // Star list
                in.readFloat()); // The movie rating

    }

    /**Overriding this method is mandatory in
     * order to implement the "Parcelable" class
     * */

    @Override
    public int describeContents() {
        return 0;
    }

    /**Will be used to turn the object into a parcel.*/
    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(mMovieId); // Id
        parcel.writeString(mTitle); // Title
        parcel.writeString(mPosterImagePath); // Poster Image Path
        parcel.writeString(mYear); // Year
        parcel.writeString(mMovieLenght); // Movie Length
        parcel.writeString(mBackDropImagePath); // Backdrop Image Path
        parcel.writeString(mSynopsis); // Synopsis
        parcel.writeString(mMovieDirector); // Movie Director
        parcel.writeList(mMovieStars); // Movie stars
        parcel.writeFloat(mMovieRating); // Movie's rating
    }

    /** Will help us generate an instance of
     *  "AMovie" from a Parcel,
     *  by using the constructor "AMovie (Parcel in)". */

    public final Parcelable.Creator<AMovie> CREATOR = new Parcelable.Creator<AMovie>() {
        @Override
        public AMovie createFromParcel(Parcel parcel) {
            return new AMovie(parcel);
        }

        @Override
        public AMovie[] newArray(int i) {
            return new AMovie[i];
        }

    };

    /**
     * Defining getters for the "AMovie" Class
     */
    public int getMovieId() {
        return mMovieId;
    }

    public String getPosterPath() {
        return mPosterImagePath;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getYear(){return mYear;}

    public String getBackDropImagePath(){return mBackDropImagePath;}

    public String getSynopsis() {
        return mSynopsis;
    }

    public String getmMovieLenght() {return mMovieLenght;}

    public String getDirector() {
        return mMovieDirector;
    }

    public ArrayList<String> getMovieStars() {
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

    public void setMovieDirector(String director) {
        mMovieDirector = director;
    }

    public void setMovieLength(String movieLenght) {
        mMovieLenght = movieLenght;
    }

    public void setMovieStars(ArrayList<String> movieStars) {
        mMovieStars = movieStars;
    }

    // Won't be used in this app version
    /*public void setSynopsis(String synopsis) {
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
    }*/

}
