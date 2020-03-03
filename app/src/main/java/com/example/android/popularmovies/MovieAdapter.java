package com.example.android.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.example.android.popularmovies.Data.GlideHelperClass;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private String ImagebaseUrl = "https://image.tmdb.org/t/p/original";
    private List <AMovie> mMovieData = new ArrayList<AMovie>();
    private Context mContext;

    /**Constructor of the adaptor */
    public MovieAdapter(Context context, List<AMovie> movieData) {

        mContext = context;
        mMovieData = movieData;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder {

        // TODO 1: Here, create variables reference to the item_layout

        public ImageView mMovieThumbnailIv;
        public TextView mMovietitleTv;
        public TextView mMovieyearTv;

        MovieAdapterViewHolder(View view) {
            super(view);
            // TODO 3: FindView By Id to get the views and store them into the variables

            /*mMovieTitleTv = (TextView) view.findViewById(); */
            // Get the ImageView from the list item layout
              mMovieThumbnailIv = (ImageView) view.findViewById(R.id.movieThumbnail_Iv);
                mMovietitleTv = (TextView) view.findViewById(R.id.title_text_view);
                mMovieyearTv = (TextView) view.findViewById(R.id.year_text_view);

              // What to do now ? Why not setting the content here ?
           // mMovieThumbnailIv.setImageResource(R.drawable.ic_launcher_background);

            /*mMovieTitleTv.setText(currentMovie.getTitle());
            mMovieThumbnailIv.setImageResource(currentMovie.getImage());
            */
        }
        // TODO 2 :
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

        // Work at work + extra time !

    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the movie
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param movieAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder movieAdapterViewHolder, int position) {

       // movieAdapterViewHolder.mMovieThumbnailIv.setImageURI(Uri.parse(ImagebaseUrl));
        AMovie currentMovie = mMovieData.get(position);
        //String posterFullLink =
        /*RequestBuilder<R.drawable> errorImage ;
        errorImage.load(R.drawable.movie_poster);*/

        GlideHelperClass glideHelper = new GlideHelperClass(mContext,
                currentMovie.getPosterPath(),
                R.drawable.ic_launcher_background,
                movieAdapterViewHolder.mMovieThumbnailIv);
        //Log.e("the path",ImagebaseUrl + currentMovie.getPosterPath());

        glideHelper.loadImage();

        /*
            GlideApp.with(mContext)
                    .load("http://via.placeholder.com/300.png")
                    .into(movieAdapterViewHolder.mMovieThumbnailIv);*/



        movieAdapterViewHolder.mMovietitleTv.setText(currentMovie.getTitle());
        movieAdapterViewHolder.mMovieyearTv.setText(currentMovie.getYear());
        // iV.setImageResource(R.drawable.ic_launcher_foreground);
        // movieAdapterViewHolder.mMovieThumbnailIv.getContext()
        int id = R.drawable.ic_launcher_background;

        /*Picasso.with(iV.getContext())
                .load(R.drawable.ic_launcher_background); */

         // currentMovie.getTitle()
         // movieAdapterViewHolder.mMovieTitleTv.setText(currentMovie.getTitle());
         // currentMovie.getImageId()
         // movieAdapterViewHolder.mMovieThumbnailIv.setImageResource(R.drawable.ic_launcher_foreground);

          // Either the context Is bad or the load doesn't work !
        // Tester le Query Utils avec le Sample Json, bitch !

        // Genérer l'image à la position donnée puis
        // Why is this considered as a null object ? Really, I don't know ! I don't know !

        /*Picasso.with(iV.getContext())
                .load(R.drawable.ic_launcher_background);*/

                // Either the context or the ImageView is not correct ! Bitch !

                //.load(currentMovie.getImageId())
                //.error(R.drawable.error)
                //.placeholder(R.drawable.sandwich_placeholder)

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
