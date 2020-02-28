package com.example.android.popularmovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.Data.QueryUtils;
import com.example.android.popularmovies.Fragment.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {
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

    private int dpToPx(int dp) {
        float px = dp* this.getResources().getDisplayMetrics().density;
        return (int) px;
    }
}
