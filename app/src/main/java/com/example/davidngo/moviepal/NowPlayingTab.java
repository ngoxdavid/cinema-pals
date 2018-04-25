package com.example.davidngo.moviepal;

/**
 * App Name: Cinema Pals
 * Created by: David Ngo
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * NowPlayingTab
 * - shows user all movies playing in theaters nearby
 */
public class NowPlayingTab extends Fragment {

    private TextView nameTextView, email;
    private String baseMovieQuery = "https://api.themoviedb.org/3/";
    private RecyclerView recycleView;
    private final String region = "US";
    private final String en_language = "&language=en-US";
    private List<Movie> movies;
    private static final String MOVIE_DB_API_KEY = "f42721cb192813385a8d52b749da6519";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind Views
        nameTextView = getActivity().findViewById(R.id.name);
        email = getActivity().findViewById(R.id.email);

        movies = new ArrayList<>();


        recycleView = (RecyclerView) getView().findViewById(R.id.movie_view);
        recycleView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recycleView.setAdapter(new MovieAdapter(getActivity(), movies));

        //Use Retrofit to build the HTTP Get Request
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseMovieQuery)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        //Get the Client that will call the API Request
        MovieClient client = retrofit.create(MovieClient.class);
        //Call the API to get movies now playing in theaters
        Call<MoviesResponse> call = client.getNowPlaying(MOVIE_DB_API_KEY, en_language, region);

        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                //Add list to adapter, and set adapter.
                if (!movies.isEmpty()) {
                    movies.clear();
                }
                movies.addAll(response.body().getMovieResults());
                recycleView.setAdapter(new MovieAdapter(getActivity(), movies));
                recycleView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Error API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        movies.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_now_playing_movies, container, false);
        return view;
    }
}
