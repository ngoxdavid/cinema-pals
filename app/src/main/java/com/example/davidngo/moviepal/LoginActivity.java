package com.example.davidngo.moviepal;

/**
 * App Name: Cinema Pals
 * Created by: David Ngo
 */

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {


    GoogleSignInClient mGoogleSignInClient;
    static int RC_SIGN_IN = 9000;
    SensorEventListener sensorEventListener;
    SensorManager sensorManager;
    Sensor light;
    LocationManager locationManager;
    Location location;
    TextView welcomeText;
    private DbHelper db;
    final public static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    boolean mLocationPermissionGranted;
    private String personName = "", personEmail = "", personId = "", personPhoto = "";
    private String city = "", state = "", postalcode = "";
    private TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        getLocationPermission();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);

        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        tinyDB = new TinyDB(getApplicationContext());

        //Create new Geocoder
        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            //Gets lat and long
            List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            tinyDB.putString("location", Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude()));

            //Uses geocoder to get city, state, and postal code
            if (addresses.size() > 0) {
                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
                postalcode = addresses.get(0).getPostalCode();
            }
        } catch (IOException e) {
            Log.d("test", "Can't find addresses");
        }


        boolean logStatus = tinyDB.getBoolean("logStatus");
        tinyDB.putInt("watchCount", 0);

        if (logStatus) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }


        welcomeText = (TextView) findViewById(R.id.welcomeText);

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (light != null) {
            Log.d("tester", "Light sensor detected!");
        }

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                int lightValue = (int) event.values[0];
                if (lightValue > 255) lightValue = 255;

                if (lightValue < 20) {
                    welcomeText.setText("Dim your phone, the movie is starting!");
                    welcomeText.setTextColor(Color.rgb(255, 255, 255));
                    welcomeText.setVisibility(TextView.VISIBLE);
                } else {
                    welcomeText.setVisibility(TextView.INVISIBLE);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        sensorManager.registerListener(sensorEventListener, light, SensorManager.SENSOR_DELAY_FASTEST);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(sensorEventListener);

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            if (account != null) {
                personName = account.getDisplayName();
                personEmail = account.getEmail();
                personId = account.getId();
                Uri photoUri = account.getPhotoUrl();

                if (photoUri != null)
                    personPhoto = photoUri.toString();

                Log.d("tester", personName);

                db = new DbHelper(this);
                if (db.insertProfileInfo(personId, personName, personEmail, personPhoto, city, state, postalcode)) {
                    Log.d("tester", "Db inserted");
                } else {
                    Log.d("tester", "db failed insert");
                }

                tinyDB = new TinyDB(getApplicationContext());
                tinyDB.putString("user_id", personId);
                tinyDB.putInt("tabPosition", 0);
                tinyDB.putBoolean("logStatus", true);

                db.close();
            }

            // Signed in successfully, show authenticated UI.
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("tester", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "Login failed!", Toast.LENGTH_SHORT).show();
        }
    }


    //Requests Location Permission from the User
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
