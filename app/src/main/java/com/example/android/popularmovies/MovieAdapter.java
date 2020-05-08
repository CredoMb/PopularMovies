package com.example.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.Data.GlideHelperClass;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private List<AMovie> mMovieData = new ArrayList<AMovie>();
    private Context mContext;

    public interface MovieAdapterOnClickHandler {
        void onClick(int postion);
    }

    final private MovieAdapterOnClickHandler mClickHandler;

    /**
     * Constructor of the adaptor
     */
    public MovieAdapter(Context context, List<AMovie> movieData,
                        MovieAdapterOnClickHandler clickHandler) {

        mContext = context;
        mMovieData = movieData;
        mClickHandler = clickHandler;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mMovieThumbnailIv;

        public TextView mMovietitleTv;
        public TextView mMovieyearTv;

        MovieAdapterViewHolder(View view) {
            super(view);

            // Get the ImageView from the list item layout
            mMovieThumbnailIv = (ImageView) view.findViewById(R.id.movieThumbnail_Iv);

              mMovietitleTv = (TextView) view.findViewById(R.id.movie_title_textview_main);
              mMovieyearTv = (TextView) view.findViewById(R.id.movie_year_textview_main);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);


    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the movie
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param movieAdapterViewHolder The ViewHolder which should be updated to represent the
     *                               contents of the item at the given position in the data set.
     * @param position               The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder movieAdapterViewHolder, int position) {

        // movieAdapterViewHolder.mMovieThumbnailIv.setImageURI(Uri.parse(ImagebaseUrl));
        AMovie currentMovie = mMovieData.get(position);

        GlideHelperClass glideHelper = new GlideHelperClass(mContext,
                currentMovie.getPosterPath(),
                R.drawable.placeholder_image,
                movieAdapterViewHolder.mMovieThumbnailIv);

        // This will load the image, from the API to the
        // image view
        glideHelper.loadImage();

        movieAdapterViewHolder.mMovietitleTv.setText(currentMovie.getTitle());
        movieAdapterViewHolder.mMovieyearTv.setText(currentMovie.getYear());

    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our list of movies
     */
    @Override
    public int getItemCount() {
        if (mMovieData == null) return 0;
        return mMovieData.size();
    }

    /**
     * This method is used to set the movies on a MovieAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new MovieAdapter to display it.
     *
     * @param movieData The new movie data to be displayed.
     */
    public void setMovieData(List<AMovie> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}
