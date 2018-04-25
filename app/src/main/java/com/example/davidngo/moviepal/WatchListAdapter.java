package com.example.davidngo.moviepal;

/**
 * App Name: Cinema Pals
 * Created by: David Ngo
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Watchlist Adapter
 * - gets data and binds to a recycle view
 * - allows user to go to movie details
 * - also allows a user to remove a movie from the overall watchlist
 */
public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.ViewHolder> {

    private Context context;
    private static List<Movie> movies;
    private String posterImage_Base_URL = "https://image.tmdb.org/t/p/";
    private String posterImageSize = "w500/";
    private static TinyDB tinyDB;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView poster;
        public View mView;
        public Button seenButton;

        public ViewHolder(View v) {
            super(v);

            poster = (ImageView) v.findViewById(R.id.posterImageView);
            mView = v;
            seenButton = v.findViewById(R.id.seenButton);

            //When a user clicks on a movie, it will take them to the movie details
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context mContext = view.getContext();
                    Intent intent = new Intent(mContext, MovieDetails.class);
                    intent.putExtra("movie", movies.get((int) view.getTag()));
                    mContext.startActivity(intent);
                }
            });


        }
    }


    public WatchListAdapter(Context context, List<Movie> values) {

        this.context = context;
        this.movies = values;
    }

    /**
     * Creates a ViewHolder
     *
     * @param parent   parent viewgroup
     * @param viewType viewType
     * @return ViewHolder of a movie in this case
     */
    @Override
    public WatchListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_watchlist, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    /**
     * When the data is binded to the View Holder, load poster image to the view
     *
     * @param holder   view holder
     * @param position position of movie in layout
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Load Image into ImageView using Picasso
        Picasso.with(context).load(posterImage_Base_URL + posterImageSize + movies.get(position).getPosterPath()).into(holder.poster);

        //Set Tag (Position)
        holder.mView.setTag(position);
        holder.seenButton.setTag(position);

        //When a user clicks on Mark As Seen Button
        //It will remove the movie from the watchlist
        //It will also update the user's watch count
        holder.seenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movies.remove((int) view.getTag());

                tinyDB = new TinyDB(view.getContext());
                List<Movie> movieWatchList = tinyDB.getListMovie("watchlist");
                movieWatchList.remove((int) view.getTag());
                tinyDB.putListMovie("watchlist", (ArrayList<Movie>) movieWatchList);
                int count = tinyDB.getInt("watchCount");
                count = count - 1;
                tinyDB.putInt("watchCount", count);
                notifyDataSetChanged();
            }
        });


    }

    /**
     * Invoked my Layout Manager , returns number of items in list
     *
     * @return count
     */
    @Override
    public int getItemCount() {
        return movies.size();
    }


}
