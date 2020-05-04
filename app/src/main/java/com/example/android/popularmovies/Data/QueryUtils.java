package com.example.android.popularmovies.Data;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.popularmovies.AMovie;
import com.example.android.popularmovies.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class QueryUtils {

    /**
     * Variable to store the JSON response for a USGS query
     */
    private static String JSON_RESPONSE;

    /**
     * Variable to store the Film Data Base Url
     */
    private static String THEMOVIEDB_REQUEST_URL = "https://image.tmdb.org/t/p/original";

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    /** The base Urls */
    private static String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/original";

    private static String MOVIE_BASE_REQUEST_URL = "https://api.themoviedb.org/3/movie/";

    /** the following 2 strings will be used to determine
     *  which information/json Object must be queried from the
     *  API. This will help to build the right Url*/

    public static String GET_CREDIT = "credit";
    public static String GET_EXTENDED_DETAILS ="details";

    // The key can not appear on github as this is a public repo

    private static String API_KEY ="cd401ba98e50ce8bf913cdce912aa430";
    /**
     * This is a sample json response to help us test the last function
     */
    private String SAMPLE_JSON_RESPONSE = theSampleJson();

    /** Will be used to get the year out of the "release_date" Json paramater*/

    private static int MOVIE_YEAR_START_INDEX = 0;
    private static int MOVIE_YEAR_END_INDEX = 4;

    /** This will contain the director of a given movie*/
    public static String mMovieDirector = "";

    /**This will contain the length of a given movie */
    public static String mMovieLength = "";

    /** Will be used wherever we don't have a date*/
    private static String NOT_SPECIFIED = "Not Specified";

    /**
     *
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {

        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */

    private static String makeHttpRequest(java.net.URL url) throws IOException {
        String jsonResponse = "";

        // Check if the url is null
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            // The internet connection is needed before executing the next line !
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect(); // Did the connexion lead to a successful transfert ? That's a good question !

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream(); // Check if the inputStream has a bad value, like null, right ?
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            //  Handle the exception
            Log.e(LOG_TAG, "Problem retrieving the Movie JSON results.", e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;

        // Returns the actual Json from the URL
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link AMovie} by Fetching data from the USGS server
     */

    public static ArrayList<AMovie> fetchMoviesData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            // Handle the IOException
            Log.e(LOG_TAG, "Problem retrieving the Movie JSON results.", e);
        }
        Log.w(LOG_TAG, "This is the \"fetchMovieData\" method");
        return extractMovies(jsonResponse);
    }

    /**
     * Return a list of {@link AMovie} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<AMovie> extractMovies(String jsonResponse) {

        // If the JSON string is empty or null, then return null.
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding Movies to
        ArrayList<AMovie> movies = new ArrayList<AMovie>();

        // Try to parse the jsonResponse. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Convert the string of Json received from the Movie DataBase API to a JSONObject
            // and JSONArray
            JSONObject JSONmovieObject = new JSONObject(jsonResponse);
            JSONArray JSONmovieArray = JSONmovieObject.optJSONArray("results");
            JSONObject JSONcurrentMovieObject;

            // Loop through the JSONmovieArray to extract informations about each movie
            for (int i = 0; i < JSONmovieArray.length(); i++) {

                JSONcurrentMovieObject = JSONmovieArray.optJSONObject(i);

                //Extract the movie Id
                int movieId = JSONmovieArray.optJSONObject(i).optInt("id");

                // Extract the path of the Poster Image for the movie positionned at the index "i"
                String posterPath = JSONmovieArray.optJSONObject(i).optString("poster_path");
                // Build the complete link to get the poster Image
                String posterCompletePath = IMAGE_BASE_URL + posterPath;
                // Extract the movie title's String
                String movieTitle = JSONmovieArray.optJSONObject(i).optString("title");
                // Extract the movie Year
                String fullReleaseDate = JSONmovieArray.optJSONObject(i).optString("release_date");
                String movieYear =  NOT_SPECIFIED;
                    // Make sure that the movie has a valid release date
                if(!TextUtils.isEmpty(fullReleaseDate)){
                     movieYear = fullReleaseDate.substring(MOVIE_YEAR_START_INDEX,MOVIE_YEAR_END_INDEX);
                }

                /** The following group of informations will only be used in the DetailActivity */

                String synopsis = JSONmovieArray.optJSONObject(i).optString("overview");
                    // Extract the path of the movie's backDrop Image for the movie positionned at the index "i"
                String backDropPath = JSONmovieArray.optJSONObject(i).optString("backdrop_path");
                    // Build the complete link to get the poster Image
                String backDropPathCompletePath = IMAGE_BASE_URL + backDropPath;

                    // Get the cast as an ArrayList of String and set the value of the "movieDirector"
                    // This will use the id of the movie to make an second API call and get back informations
                    // related to the movie credits
                ArrayList<String>movieCast  = new ArrayList<String>();
                    // get the rating of the movie
                float movieRating = (float) JSONmovieArray.optJSONObject(i).optDouble("vote_average"); // this is the vote_average and the vote_count

                movies.add(new AMovie(movieId,movieTitle,posterCompletePath,movieYear,
                        "",backDropPathCompletePath,synopsis,"",movieCast,movieRating));
                // The function goes into the "if" but the value doesn't change, why ?

            }

            }

         catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the movie JSON results", e);
        }
        return movies;
    }

    /**
     * This function will get the cast members from the API and return them as
     * a List of String. Also, it will set the values of mMovieLenght and mMovieDirector,
     * which will be used to build movie objects.
     */

    public static ArrayList<String> extractCastAndSetExtraDetails(int id) {

        // The list that will contain all the cast member of a movie
        ArrayList<String> cast = new ArrayList<String>();

        try {
            // Get the main Json object returned by the Request
            JSONObject movieCreditJsonObject = new JSONObject(MakeTheRightQuery(id,GET_CREDIT));

            // Get the extended details json from the API.
            // This json contains the information about the movie length
            JSONObject movieExtendedDetailJsonObject = new JSONObject(MakeTheRightQuery(id,GET_EXTENDED_DETAILS));

            // From the extended detail Json, get the movie length in minutes
            int lengthInMinute = movieExtendedDetailJsonObject.optInt("runtime");

            // Turn the movie lenght to the standardized format of "0h00",
            // for example, if a movie takes 123 minutes,
            // it will turned into "1h03"
            mMovieLength = formatMovieLength(lengthInMinute);

            // Get the Array of the entire movie cast
            JSONArray movieCastJsonArray = movieCreditJsonObject.optJSONArray("cast");
            // Get the Array of the movie crew
            JSONArray movieCrewJsonArray = movieCreditJsonObject.optJSONArray("crew");

            // Extract the cast of the movie.
            // Iterate through all the cast members inside of the movieCastJsonArray
            // and get their names
            for(int i =0; i< movieCastJsonArray.length();i++) {
                // What to do now ? I don't know !
                cast.add(movieCastJsonArray.optJSONObject(i).optString("name"));
            }

            // Iterate through the crew members
            // until we find the director of the movie
            for (int i = 0; i < movieCrewJsonArray.length(); i++) {

                //Log.e("the departement here","bijou " +movieCrewJsonArray.optJSONObject(i).optString("department"));
                // Inside the crew array, find the object that contains the
                // name of the director. Only one object has the name of the movie director
                if (movieCrewJsonArray.optJSONObject(i).optString("department").equals("Directing")
                        && movieCrewJsonArray.optJSONObject(i).optString("job").equals("Director") ){
                    // Extract the name of the director
                    // from the object that contains it
                    mMovieDirector = movieCrewJsonArray.optJSONObject(i).optString("name");

                    break;
                }
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the movie cast JSON results", e);

        }

        return cast;
    }

    /** This will be used to format the movie length with the
     * following type "0h00" instead of a number that represent */

    private static String formatMovieLength(int movieLength) {

        String formatedLength = "";

        // Format the movie length (duration), based on
        // the total length
        if (movieLength > 60 || movieLength == 60) {
            formatedLength = String.valueOf(movieLength / 60) + "h"
                    + String.format("%02d", movieLength % 60);
        } else {
            formatedLength = "0h" + String.format("%02d", movieLength);
        }

        return formatedLength;
    }

    /** Return the Json Response based on the information the information
     *  needed from the API */
    private static String MakeTheRightQuery(int id,String infoToGet) {

        String MovieUrlTextWithId = MOVIE_BASE_REQUEST_URL
                + String.valueOf(id);

        // If the user want to get the credit information,
        // add the "/credits" to the url text
        if(infoToGet == GET_CREDIT){
            MovieUrlTextWithId += "/credits";
        }


        // Create the variables that will hold
        // the Uri and the uri Builder needed
        // to construct the full link

        Uri movieInfoBaseUri;
        Uri.Builder uriBuilder;

        // Build the URI that will be use to get the credit of the movie
        // from the API
        movieInfoBaseUri = Uri.parse(MovieUrlTextWithId);
        uriBuilder = movieInfoBaseUri.buildUpon();

        // Add some relevant information to finalize the Uri
        uriBuilder.appendQueryParameter("api_key",API_KEY);

        // If the user want to get extended details of a movie,
        // instead of the credit, then add the following
        // parameter to the uriBuilder
        if (infoToGet == GET_EXTENDED_DETAILS) {
            uriBuilder.appendQueryParameter("language","en-US");
        }

        // Create URL object by using the uriBuilder
        URL url = createUrl(uriBuilder.toString());
        String movieJsonResponse ="";

        // Make the network request to the API
        // and get back the json.
        try {
            movieJsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            //  Handle the IOException
            Log.e(LOG_TAG, "Problem retrieving the targeted Movie JSON results.", e);
        }

        return movieJsonResponse;
    }


    /**
     * Returns a String that represent a Json Response from the Movie DataBase API
     */

    public static String theSampleJson() {
        return  "{\n" +
                "  \n" +
                "  \"page\": 1,\n" +
                "  \"total_results\": 10000,\n" +
                "  \"total_pages\": 500,\n" +
                "  \"results\": [\n" +
                "\n" +
                "    {\n" +
                "      \"popularity\": 318.742,\n" +
                "      \"vote_count\": 413,\n" +
                "      \"video\": false,\n" +
                "      \"poster_path\": \"/s8qRIwA0zDPbnRekeU0rDwWE7q7.jpg\",\n" +
                "      \"id\": 454626,\n" +
                "      \"adult\": false,\n" +
                "      \"backdrop_path\": \"/qonBhlm0UjuKX2sH7e73pnG0454.jpg\",\n" +
                "      \"original_language\": \"en\",\n" +
                "      \"original_title\": \"Sonic the Hedgehog\",\n" +
                "      \"genre_ids\": [\n" +
                "        28,\n" +
                "        35,\n" +
                "        878,\n" +
                "        10751\n" +
                "      ],\n" +
                "      \"title\": \"Sonic the Hedgehog\",\n" +
                "      \"vote_average\": 7,\n" +
                "      \"overview\": \"Based on the global blockbuster videogame franchise from Sega, Sonic the Hedgehog tells the story of the world’s speediest hedgehog as he embraces his new home on Earth. In this live-action adventure comedy, Sonic and his new best friend team up to defend the planet from the evil genius Dr. Robotnik and his plans for world domination.\",\n" +
                "      \"release_date\": \"2020-02-12\"\n" +
                "    }, \n" +
                "\n" +
                "    {\n" +
                "      \"popularity\": 70.379,\n" +
                "      \"vote_count\": 2057,\n" +
                "      \"video\": false,\n" +
                "      \"poster_path\": \"/7GsM4mtM0worCtIVeiQt28HieeN.jpg\",\n" +
                "      \"id\": 515001,\n" +
                "      \"adult\": false,\n" +
                "      \"backdrop_path\": \"/agoBZfL1q5G79SD0npArSlJn8BH.jpg\",\n" +
                "      \"original_language\": \"en\",\n" +
                "      \"original_title\": \"Jojo Rabbit\",\n" +
                "      \"genre_ids\": [\n" +
                "        35,\n" +
                "        18,\n" +
                "        10752\n" +
                "      ],\n" +
                "      \"title\": \"Jojo Rabbit\",\n" +
                "      \"vote_average\": 8.1,\n" +
                "      \"overview\": \"A World War II satire that follows a lonely German boy whose world view is turned upside down when he discovers his single mother is hiding a young Jewish girl in their attic. Aided only by his idiotic imaginary friend, Adolf Hitler, Jojo must confront his blind nationalism.\",\n" +
                "      \"release_date\": \"2019-10-18\"\n" +
                "    },\n" +
                "\n" +
                "    {\n" +
                "      \"popularity\": 65.006,\n" +
                "      \"vote_count\": 367,\n" +
                "      \"video\": false,\n" +
                "      \"poster_path\": \"/hj8pyoNnynGeJTAbl7jcLZO8Uhx.jpg\",\n" +
                "      \"id\": 522162,\n" +
                "      \"adult\": false,\n" +
                "      \"backdrop_path\": \"/xjP88DFPCQ81mbZHXFscyYtGskT.jpg\",\n" +
                "      \"original_language\": \"en\",\n" +
                "      \"original_title\": \"Midway\",\n" +
                "      \"genre_ids\": [\n" +
                "        28,\n" +
                "        18,\n" +
                "        36,\n" +
                "        10752\n" +
                "      ],\n" +
                "      \"title\": \"Midway\",\n" +
                "      \"vote_average\": 6.7,\n" +
                "      \"overview\": \"The story of the Battle of Midway, and the leaders and soldiers who used their instincts, fortitude and bravery to overcome massive odds.\",\n" +
                "      \"release_date\": \"2019-11-06\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"popularity\": 76.639,\n" +
                "      \"vote_count\": 52,\n" +
                "      \"video\": false,\n" +
                "      \"poster_path\": \"/i2FKQBcTbLUKGWgMRzt8nACJTIv.jpg\",\n" +
                "      \"id\": 505225,\n" +
                "      \"adult\": false,\n" +
                "      \"backdrop_path\": \"/lsgYcIbcoQeDZXsHYMOnkvk3sn0.jpg\",\n" +
                "      \"original_language\": \"en\",\n" +
                "      \"original_title\": \"The Last Thing He Wanted\",\n" +
                "      \"genre_ids\": [\n" +
                "        18,\n" +
                "        53\n" +
                "      ],\n" +
                "      \"title\": \"The Last Thing He Wanted\",\n" +
                "      \"vote_average\": 4.8,\n" +
                "      \"overview\": \"At the turning point of the Iran-Contra affair, Elena McMahon, a fearless investigative journalist covering the 1984 US presidential campaign, puts herself in danger when she abandons her assigned task in order to fulfill the last wish of her ailing father, a mysterious man whose past activities she barely knows…\",\n" +
                "      \"release_date\": \"2020-01-27\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"popularity\": 79.052,\n" +
                "      \"vote_count\": 8735,\n" +
                "      \"video\": false,\n" +
                "      \"poster_path\": \"/jpfkzbIXgKZqCZAkEkFH2VYF63s.jpg\",\n" +
                "      \"id\": 920,\n" +
                "      \"adult\": false,\n" +
                "      \"backdrop_path\": \"/a1MlbLBk5Sy6YvMbSuKfwGlDVlb.jpg\",\n" +
                "      \"original_language\": \"en\",\n" +
                "      \"original_title\": \"Cars\",\n" +
                "      \"genre_ids\": [\n" +
                "        12,\n" +
                "        16,\n" +
                "        35,\n" +
                "        10751\n" +
                "      ],\n" +
                "      \"title\": \"Cars\",\n" +
                "      \"vote_average\": 6.7,\n" +
                "      \"overview\": \"Lightning McQueen, a hotshot rookie race car driven to succeed, discovers that life is about the journey, not the finish line, when he finds himself unexpectedly detoured in the sleepy Route 66 town of Radiator Springs. On route across the country to the big Piston Cup Championship in California to compete against two seasoned pros, McQueen gets to know the town's offbeat characters.\",\n" +
                "      \"release_date\": \"2006-06-08\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"popularity\": 71.555,\n" +
                "      \"vote_count\": 135,\n" +
                "      \"video\": false,\n" +
                "      \"poster_path\": \"/hJ6YEbrjFvToa5c7IiUqILoB6Je.jpg\",\n" +
                "      \"id\": 552178,\n" +
                "      \"adult\": false,\n" +
                "      \"backdrop_path\": \"/4ZSlTfkHtgTTupCaLbseXQQzZha.jpg\",\n" +
                "      \"original_language\": \"en\",\n" +
                "      \"original_title\": \"Dark Waters\",\n" +
                "      \"genre_ids\": [\n" +
                "        18\n" +
                "      ],\n" +
                "      \"title\": \"Dark Waters\",\n" +
                "      \"vote_average\": 7.4,\n" +
                "      \"overview\": \"A tenacious attorney uncovers a dark secret that connects a growing number of unexplained deaths due to one of the world's largest corporations. In the process, he risks everything — his future, his family, and his own life — to expose the truth.\",\n" +
                "      \"release_date\": \"2019-11-22\"\n" +
                "    }\n" +
                "\n" +
                "]\n" +
                "\n" +
                "}";
    }
}
