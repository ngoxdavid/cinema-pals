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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context context;
    private static List<Movie> movies;
    private String posterImage_Base_URL = "https://image.tmdb.org/t/p/";
    private String posterImageSize = "w500/";
    private int position;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title, description;
        public ImageView poster;
        public View mView;

        public ViewHolder(View v) {
            super(v);

            poster = (ImageView) v.findViewById(R.id.posterImageView);
            mView = v;

            //Start MovieDetails Activity, send the movie object in an intent
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

    public MovieAdapter(Context context) {

        this.context = context;

    }

    public MovieAdapter(Context context, List<Movie> values) {

        this.context = context;
        this.movies = values;
        setHasStableIds(true);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context)
                .inflate(R.layout.list_item_top_movies, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Picasso.with(context).load(posterImage_Base_URL + posterImageSize + movies.get(position).getPosterPath()).into(holder.poster);

        holder.mView.setTag(position);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (movies == null)
            return 0;
        else
            return movies.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
