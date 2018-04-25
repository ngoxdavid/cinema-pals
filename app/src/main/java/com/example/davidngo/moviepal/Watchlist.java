package com.example.davidngo.moviepal;

/**
 * App Name: Cinema Pals
 * Created by: David Ngo
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

/**
 * Watchlist Activity
 * - Displays a user's watchlist
 */
public class Watchlist extends AppCompatActivity {

    private List<Movie> movieWatchList;
    private TinyDB tinyDB;
    private RecyclerView recycleView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Gets the List<Movie> object from SharedPreferences
        tinyDB = new TinyDB(getApplicationContext());
        movieWatchList = tinyDB.getListMovie("watchlist");

        //Add List to Adapter and set adapter to the view
        recycleView = findViewById(R.id.movie_view);
        recycleView.setLayoutManager(new GridLayoutManager(this, 2));
        recycleView.setAdapter(new WatchListAdapter(getApplicationContext(), movieWatchList));
    }

}
