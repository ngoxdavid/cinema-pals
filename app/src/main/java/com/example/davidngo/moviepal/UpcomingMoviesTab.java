package com.example.davidngo.moviepal;

/**
 * App Name: Cinema Pals
 * Created by: David Ngo
 */

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * UpcomingMoviesTab
 * - Displays movies coming soon
 * -
 */
public class UpcomingMoviesTab extends Fragment {

    private TextView nameTextView, email;
    private String baseMovieQuery = "https://api.themoviedb.org/3/";
    private RecyclerView recycleView;
    private final String region = "US";
    private final String en_language = "&language=en-US";
    private static final String MOVIE_DB_API_KEY = "f42721cb192813385a8d52b749da6519";
    private List<Movie> emptyMovieList;

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

        emptyMovieList = new ArrayList<>();


        recycleView = (RecyclerView) getView().findViewById(R.id.movie_view);
        recycleView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recycleView.setAdapter(new MovieAdapter(getActivity(), emptyMovieList));

        //Retrofit to build an API request
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseMovieQuery)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        //Send a GET request to the API
        MovieClient client = retrofit.create(MovieClient.class);
        Call<MoviesResponse> call = client.getUpcoming(MOVIE_DB_API_KEY, en_language, region);

        //Puts the call in a queue
        call.enqueue(new Callback<MoviesResponse>() {
            //When the API responds, the repsonse is saved and added to the adapter/updated
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (!emptyMovieList.isEmpty()) {
                    emptyMovieList.clear();
                }
                emptyMovieList.addAll(response.body().getMovieResults());
                recycleView.setAdapter(new MovieAdapter(getActivity(), emptyMovieList));
                recycleView.getAdapter().notifyDataSetChanged();
                Log.d("tester", "hi i got response");
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
        emptyMovieList.clear();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the view
        View view = inflater.inflate(R.layout.tab_upcoming_movies, container, false);
        return view;
    }
}
