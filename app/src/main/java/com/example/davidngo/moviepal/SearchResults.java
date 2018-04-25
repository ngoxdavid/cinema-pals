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
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * SearchResults
 * - After a user enters a search, it will show a list of movies related to the input
 * - Uses the MovieAdapter for the Recycler View
 */
public class SearchResults extends AppCompatActivity {

    private TextView nameTextView, email;
    public static String TAG = "davidngo";
    private String baseMovieQuery = "https://api.themoviedb.org/3/";
    private RecyclerView recycleView;
    private final String region = "US";
    private final String en_language = "&language=en-US";
    private String movieToSearch;
    private static final String MOVIE_DB_API_KEY = "f42721cb192813385a8d52b749da6519";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get the user's input query (will be a movie title)
        movieToSearch = getIntent().getStringExtra("movieSearch");


        // Bind Views
        nameTextView = findViewById(R.id.name);
        email = findViewById(R.id.email);


        recycleView = (RecyclerView) findViewById(R.id.movie_view);
        recycleView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        //Build API Call
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseMovieQuery)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        //Call API requesting all movies with similar movie title (user input query)
        MovieClient client = retrofit.create(MovieClient.class);
        Call<MoviesResponse> call = client.getMovieByTitle(MOVIE_DB_API_KEY, en_language, region, movieToSearch, null);

        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movie> movies = response.body().getMovieResults();
                recycleView.setAdapter(new MovieAdapter(getApplicationContext(), movies));

            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error API", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
