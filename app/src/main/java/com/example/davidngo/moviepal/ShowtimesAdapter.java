package com.example.davidngo.moviepal;

/**
 * App Name: Cinema Pals
 * Created by: David Ngo
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;


/**
 * ShowtimesAdapter
 * - recyclerView custom adapter
 * - binds movies, showtimes to a list of a specific theater
 */
public class ShowtimesAdapter extends RecyclerView.Adapter<ShowtimesAdapter.ViewHolder> {

    private Context context;
    private List<Theater> theaters;
    private static List<Movie> movies;
    private List<Showtime> showtimes;
    HashMap<Integer, String> movieHashMap;
    HashMap<Integer, Theater> theaterHashMap;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor editor;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView theaterName, movieName, movieTime;
        ImageButton moviePoster;
        private ImageButton askButton;
        public View mView;


        public ViewHolder(View v) {
            super(v);
            this.theaterName = (TextView) v.findViewById(R.id.theaterName);
            this.movieName = v.findViewById(R.id.movieName);
            this.movieTime = v.findViewById(R.id.movieTime);
            this.moviePoster = v.findViewById(R.id.moviePoster);
            this.askButton = v.findViewById(R.id.askButton);
            this.mView = v;

            //If a user clicks on the movie poster, they can see its details
            moviePoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context mContext = view.getContext();
                    Intent intent = new Intent(mContext, MovieDetails.class);
                    intent.putExtra("movie", movies.get((int) view.getTag()));
                    mContext.startActivity(intent);
                }
            });

            //Ask Button will copy the movie title, theater name, and available showtimes to a plaintext body
            //An app chooser will appear and allow the user to send the message to another person using any app
            //It will ask their pal on a movie date!
            askButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String shareBody = "Hey there! Would you like to watch " +
                            movieName.getText() + " with me?\nIt is playing at " +
                            theaterName.getText() + ". \nThe showtimes are: " + movieTime.getText();
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Want to watch a movie?");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    view.getContext().startActivity(Intent.createChooser(sharingIntent, "Share with"));

                }
            });
        }

        //If the available movie is full or not playing at the theater
        //Hide the card view
        public void setVisibility(boolean isVisible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (isVisible) {
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);
            } else {
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }
    }


    public ShowtimesAdapter(Context context, List<Theater> theaters, List<Movie> movies, List<Showtime> showtimes,
                            HashMap<Integer, String> movieHashMap, HashMap<Integer, Theater> theaterHashMap) {

        this.context = context;
        this.theaters = theaters;
        this.movies = movies;
        this.showtimes = showtimes;
        this.movieHashMap = movieHashMap;

        setHasStableIds(true);

        this.theaterHashMap = theaterHashMap;

        if (context != null) {
            mPrefs = context.getApplicationContext().getSharedPreferences("myPreferences", 0);
            editor = mPrefs.edit();
        }

    }

    //Create new view
    @Override
    public ShowtimesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_showtimes, parent, false);


        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    //Replace content of view when view is binded
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        int theaterID_Key = showtimes.get(position).getCinemaID();
        int c = mPrefs.getInt("count", 0);
        int b = theaterHashMap.get(theaterID_Key).getMoviesPlaying().size();


        //Makes sure that there are the correct amount of movies per theater
        if (c == b - 1) {
            editor.putInt("count", 0);
            editor.commit();
        }

        String s = getMovieShowtimes(showtimes, theaters.get(0).getMoviesPlaying().get(c).getId(), theaterID_Key);

        //No showtimes = Hide View
        if (s == "") {
            holder.setVisibility(false);
        } else {

            //Update UI
            holder.theaterName.setText(theaters.get(0).getName());
            holder.movieName.setText(theaters.get(0).getMoviesPlaying().get(c).getTitle());
            holder.movieTime.setText(s);

            Picasso.with(context).load(theaterHashMap.get(theaterID_Key).getMoviesPlaying().get(c).getPoster_image()).into(holder.moviePoster);
        }

        if (c + 1 < b) {
            editor.putInt("count", c + 1);
            editor.commit();
        }

        holder.moviePoster.setTag(position);
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

    /**
     * getMovieShowtimes - creates and returns a string consisting of available showtimes of
     * a specific movie
     *
     * @param showtimes list of showtimes
     * @param movieID   id of a movie
     * @param theaterID id of a theater
     * @return
     */
    public String getMovieShowtimes(List<Showtime> showtimes, int movieID, int theaterID) {

        String allShowtimesStr = "";
        String AMorPM = "AM";

        for (int i = 0; i < showtimes.size(); i++) {
            if (showtimes.get(i).getCinemaID() == theaterID && showtimes.get(i).getMovieID() == movieID) {
                String[] s = showtimes.get(i).getStartTime().split("T|-");
                String temp = s[3];

                String[] temp2 = temp.split(":");

                int hr = Integer.parseInt(temp2[0]);

                if (hr > 12) {
                    hr = hr - 12;
                    AMorPM = "PM";
                }
                if (hr == 12) {
                    AMorPM = "PM";
                }

                allShowtimesStr = allShowtimesStr + Integer.toString(hr) + ":" + temp2[1] + AMorPM + "  ";
            }
        }

        return allShowtimesStr;
    }

}
