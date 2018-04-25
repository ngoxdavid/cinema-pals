package com.example.davidngo.moviepal;

/**
 * App Name: Cinema Pals
 * Created by: David Ngo
 */

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

/**
 * ShowtimeDetails
 * - shows a list of movies and their available showtimes for that theater
 */
public class ShowtimeDetails extends AppCompatActivity {
    private List<Theater> theaters;
    private List<Movie> movies;
    private List<Showtime> showtimes;
    private static final String ISHOWTIMES_API_KEY = "i0JaLmlkGURl9ZikUACwfBltGQdn3VkV";
    private static final String baseTheaterQuery = "https://api.internationalshowtimes.com/v4/";
    private TinyDB tinyDB;
    private HashMap<Integer, String> movieHashMap;
    private HashMap<Integer, Theater> theaterHashMap;
    private RecyclerView recycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtime_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        showtimes = new ArrayList<Showtime>();
        movies = new ArrayList<Movie>();

        Theater currentTheater = (Theater) getIntent().getSerializableExtra("theater");

        getSupportActionBar().setTitle(currentTheater.getName());

        //Create HTTP Request
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseTheaterQuery)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        tinyDB = new TinyDB(getApplicationContext());
        String location = tinyDB.getString("location");

        //Call the API for the nearest showtimes of the day
        ShowtimesClient client = retrofit.create(ShowtimesClient.class);
        Call<ShowtimesResponse> call = client.getNearbyShowtimes(ISHOWTIMES_API_KEY, location, "2017-12-10", "2017-12-12", null, Integer.toString(currentTheater.getId()), "movies,cinemas");

        recycleView = (RecyclerView) findViewById(R.id.theater_view);
        recycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        call.enqueue(new Callback<ShowtimesResponse>() {
            @Override
            public void onResponse(Call<ShowtimesResponse> call, Response<ShowtimesResponse> response) {

                theaters = response.body().getTheaters();
                movies = response.body().getMovies();
                showtimes = response.body().getShowtimes();

                //Hash map to map Movie ID to Movie Name
                movieHashMap = new HashMap<Integer, String>();
                //Hashmap to map Theater ID to Theater Name
                theaterHashMap = new HashMap<Integer, Theater>();

                for (int i = 0; i < movies.size(); i++) {
                    movieHashMap.put(movies.get(i).getId(), movies.get(i).getTitle());
                }

                for (int i = 0; i < theaters.size(); i++) {
                    theaterHashMap.put(theaters.get(i).getId(), theaters.get(i));
                    theaters.get(i).setMoviesPlaying(movies);
                }

                //Set and update Adapter
                recycleView.setAdapter(new ShowtimesAdapter(getApplicationContext(), theaters, movies, showtimes, movieHashMap, theaterHashMap));
                recycleView.setHasFixedSize(true);
            }

            @Override
            public void onFailure(Call<ShowtimesResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error API", Toast.LENGTH_SHORT).show();
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}

