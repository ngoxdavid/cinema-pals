package com.example.davidngo.moviepal;

/**
 * App Name: Cinema Pals
 * Created by: David Ngo
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ShowtimesTab
 * - Fragment that handles the showtime tab
 * - Will show a list of theaters
 */
public class ShowtimesTab extends Fragment {

    private RecyclerView recycleView;
    private static final String baseTheaterQuery = "https://api.internationalshowtimes.com/v4/";
    private static final String ISHOWTIMES_API_KEY = "i0JaLmlkGURl9ZikUACwfBltGQdn3VkV";
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor editor;
    private TinyDB tinyDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefs = getContext().getSharedPreferences("myPreferences", 0);
        editor = mPrefs.edit();
        editor.putInt("count", 0);
        editor.commit();
    }

    /**
     * onViewCreated - make api once the view is created, and update adapter
     *
     * @param view               current view
     * @param savedInstanceState instance state
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind Views
        recycleView = (RecyclerView) getView().findViewById(R.id.theater_view);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleView.setHasFixedSize(true);

        recycleView.setAdapter(new TheaterAdapter(getActivity(), new ArrayList<Theater>()));

        //Create HTTP Get Request
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseTheaterQuery)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();


        tinyDB = new TinyDB(getContext());
        String location = tinyDB.getString("location");

        //Call API
        TheaterClient client = retrofit.create(TheaterClient.class);
        Call<TheatersResponse> call = client.getNearbyTheaters(ISHOWTIMES_API_KEY, location);

        //On Response, Grab List of theaters and set adapter
        call.enqueue(new Callback<TheatersResponse>() {
            @Override
            public void onResponse(Call<TheatersResponse> call, Response<TheatersResponse> response) {
                List<Theater> theaters;

                if (response.isSuccessful()) {
                    theaters = response.body().getTheatersResponse();
                    recycleView.setAdapter(new TheaterAdapter(getActivity(), theaters));
                    recycleView.getAdapter().notifyDataSetChanged();

                    DividerItemDecoration dividerItem = new DividerItemDecoration(recycleView.getContext(),
                            LinearLayoutManager.VERTICAL);

                    recycleView.addItemDecoration(dividerItem);
                    recycleView.setHasFixedSize(true);

                } else {
                    Toast.makeText(getActivity(), "Can't find response.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<TheatersResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_showtimes, container, false);
        return view;
    }

}
