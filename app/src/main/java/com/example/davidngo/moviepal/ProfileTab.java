package com.example.davidngo.moviepal;

/**
 * App Name: Cinema Pals
 * Created by: David Ngo
 */

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Profile Tab
 * - Shows user profile information
 * - allows user to edit profile
 * - allows user to go to their watchlist
 */
public class ProfileTab extends Fragment {

    private TextView nameTextView, emailTextView, locationTextView;
    private TinyDB tinyDB;
    private DbHelper dbHelper;
    private String name, email, photo, city, state, postalcode;
    private ImageView profilePicture;
    private PopupWindow popUpInfo;
    private EditText newName, newEmail, newCity, newState, newZipCode;
    private View mView;
    private TextView watchCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Bind Views
        mView = view;
        nameTextView = view.findViewById(R.id.name);
        emailTextView = view.findViewById(R.id.email);
        locationTextView = view.findViewById(R.id.location);
        profilePicture = view.findViewById(R.id.profilePicture);
        watchCount = view.findViewById(R.id.watchlistNum);
        dbHelper = new DbHelper(getContext());

        tinyDB = new TinyDB(getContext());
        //Get user's watchlist count
        watchCount.setText("Movies Left to Watch: " + Integer.toString(tinyDB.getInt("watchCount")));

        //Get user's profile data
        Cursor data = dbHelper.getProfileData(tinyDB.getString("user_id"));

        while (data.moveToNext()) {
            name = data.getString(0);
            email = data.getString(1);
            photo = data.getString(2);
            city = data.getString(3);
            state = data.getString(4);
            postalcode = data.getString(5);
        }

        //Update Profile UI
        nameTextView.setText(name);
        emailTextView.setText(email);
        locationTextView.setText(city + ", " + state + " " + postalcode);

        //Load profile image
        Picasso.with(getContext())
                .load(Uri.parse(photo))
                .resize(150, 150)
                .into(profilePicture);


        data.close();
        dbHelper.close();

        Button watchlistButton = getActivity().findViewById(R.id.watchlistButton);
        Button editProfileButton = getActivity().findViewById(R.id.editInfo);


        //Show a message when user touches their picture
        profilePicture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Toast.makeText(getContext(), "You look like a person who likes to watch movies!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        //Send user to watchlist activity
        watchlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Watchlist.class);
                getActivity().startActivity(intent);
            }
        });

        //Opens popup window that allows user to change their data
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp(mView);

            }
        });
    }


    public void popUp(View view) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        ViewGroup container = (ViewGroup) inflater.inflate(R.layout.pop_up_edit_profile, null);
        LinearLayout linearL = (LinearLayout) view.findViewById(R.id.profileLinearLayout);
        popUpInfo = new PopupWindow(container, 800, 1000, true);
        popUpInfo.showAtLocation(linearL, Gravity.CENTER, 0, 0);

        //Get Views
        Button updateButton = (Button) popUpInfo.getContentView().findViewById(R.id.updateButton);
        Button cancelButton = (Button) popUpInfo.getContentView().findViewById(R.id.cancelButton);
        newName = popUpInfo.getContentView().findViewById(R.id.editName);
        newEmail = popUpInfo.getContentView().findViewById(R.id.editEmail);
        newCity = popUpInfo.getContentView().findViewById(R.id.editCity);
        newState = popUpInfo.getContentView().findViewById(R.id.editState);
        newZipCode = popUpInfo.getContentView().findViewById(R.id.editZip);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper = new DbHelper(getContext());
                tinyDB = new TinyDB(getContext());

                //Update Profile in Database
                dbHelper.updateProfile(newName.getText().toString().trim(), newEmail.getText().toString().trim(),
                        newCity.getText().toString().trim(), newState.getText().toString().trim(),
                        newZipCode.getText().toString().trim(), tinyDB.getString("user_id"));

                //Update UI
                nameTextView.setText(newName.getText().toString().trim());
                emailTextView.setText(newEmail.getText().toString().trim());
                locationTextView.setText(newCity.getText().toString().trim() + ", "
                        + newState.getText().toString().trim()
                        + " " + newZipCode.getText().toString().trim());

                dbHelper.close();
                popUpInfo.dismiss();
            }
        });


        //Cancel - don't do anything, dismiss popup window
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpInfo.dismiss();
            }
        });


    }

}
