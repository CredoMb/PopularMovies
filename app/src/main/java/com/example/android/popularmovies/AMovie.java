package com.example.android.popularmovies;

public class AMovie {

    // The id of the movie's image
    private String mImageUrl;

    // The movie's name
    private String mTitle;

    // The movie's year
    private String mYear;
    /**
     * Constructor
     */
    public AMovie (String title,String imageUrl,String year) {
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
    public String getYear() {return mYear;}

}
