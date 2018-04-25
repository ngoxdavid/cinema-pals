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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by David Ngo on 12/4/2017.
 */

/**
 * TheaterAdapter
 * - binds the list of theaters to a RecyclerView
 */
public class TheaterAdapter extends RecyclerView.Adapter<TheaterAdapter.ViewHolder> {

    private Context context;
    private static List<Theater> theaters;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor editor;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView theaterName, theaterAddress;
        private View mView;


        public ViewHolder(View v) {
            super(v);
            this.theaterName = (TextView) v.findViewById(R.id.theaterName);
            this.theaterAddress = (TextView) v.findViewById(R.id.theaterAddress);

            this.mView = v;

            //When a theater is clicked, send user to showtime details
            //Also send list of theaters in an intent to the ShowtimeDetails activity
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context mContext = view.getContext();
                    Intent intent = new Intent(mContext, ShowtimeDetails.class);
                    intent.putExtra("theater", theaters.get((int) view.getTag()));
                    mContext.startActivity(intent);
                }
            });
        }

    }


    /**
     * TheaterAdapter constructer
     *
     * @param context  context of app
     * @param theaters list of theaters nearby
     */
    public TheaterAdapter(Context context, List<Theater> theaters) {

        this.context = context;
        this.theaters = theaters;

        setHasStableIds(true);

        if (context != null) {
            mPrefs = context.getApplicationContext().getSharedPreferences("myPreferences", 0);
            editor = mPrefs.edit();
        }

    }


    @Override
    public TheaterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_theaters, parent, false);


        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Update UI
        holder.theaterName.setText(theaters.get(position).getName());
        holder.theaterAddress.setText(theaters.get(position).getLocation().getAddress().getAddr());
        holder.mView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return theaters.size();
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
