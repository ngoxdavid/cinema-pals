package com.example.davidngo.moviepal;

/**
 * App Name: Cinema Pals
 * Created by: David Ngo
 */

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * MovieDetails
 * - Shows user information about a specific movie (rating, description, title, image)
 */
public class MovieDetails extends AppCompatActivity {

    private String baseMovieQuery = "https://api.themoviedb.org/3/";
    private final String region = "US";
    private final String en_language = "&language=en-US";
    private String posterImage_Base_URL = "https://image.tmdb.org/t/p/";
    private String posterImageSize = "w500/";
    private static final String MOVIE_DB_API_KEY = "f42721cb192813385a8d52b749da6519";
    private static final String MOVIE_RATING_MAX = "10";
    private TextView movieName, movieDescription;
    private TinyDB tinyDB;
    private Movie selectedMovie;
    private ImageView moviePoster;
    private List<Movie> movies;
    private TextView movieRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        movies = new ArrayList<Movie>();

        //Get Selected Movie Object
        Movie movie = (Movie) getIntent().getSerializableExtra("movie");

        //Bind Views
        movieName = findViewById(R.id.movieName);
        movieDescription = findViewById(R.id.movieDescription);
        moviePoster = findViewById(R.id.moviePoster);
        movieRating = findViewById(R.id.movieRating);

        movieName.setText(movie.getTitle());

        //Build HTTP Request
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseMovieQuery)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        tinyDB = new TinyDB(getApplicationContext());

        //Call API to get movie of that movie title
        MovieClient client = retrofit.create(MovieClient.class);
        Call<MoviesResponse> call = client.getMovieByTitle(MOVIE_DB_API_KEY, en_language, region, movie.getTitle(), null);

        //Put call in queue
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                //Update list
                movies.add(response.body().getMovieResults().get(0));

                //Update UI
                selectedMovie = movies.get(0);
                movieName.setText(movies.get(0).getTitle());
                movieRating.setText("Rating: " + Double.toString(movies.get(0).getVoteAverage()) + "/" + MOVIE_RATING_MAX);
                movieDescription.setText(selectedMovie.getOverview());
                Picasso.with(getApplicationContext()).load(posterImage_Base_URL + posterImageSize + selectedMovie.getPosterPath()).into(moviePoster);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error API", Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MovieDetails.this, selectedMovie.getTitle() + " is added to your watchlist!", Toast.LENGTH_LONG).show();

                if (tinyDB.getListMovie("watchlist").isEmpty()) {
                    tinyDB.putListMovie("watchlist", (ArrayList<Movie>) movies);
                    tinyDB.putInt("watchCount", 1);
                } else {
                    List<Movie> movieObjects = tinyDB.getListMovie("watchlist");
                    movieObjects.add(selectedMovie);
                    tinyDB.putListMovie("watchlist", (ArrayList<Movie>) movieObjects);

                    int count = tinyDB.getInt("watchCount");
                    count = count + 1;
                    tinyDB.putInt("watchCount", count);
                }


            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
