package com.example.davidngo.moviepal;

/**
 * App Name: Cinema Pals
 * Created by: David Ngo
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.EditText;


public class DbHelper extends SQLiteOpenHelper {

    private static final String PROFILE_TBL_NAME = "profile";

    /**
     * Constructor of DbHelper
     *
     * @param context the context of the current state of an object/application
     */
    public DbHelper(Context context) {
        super(context, "CinemaPalDatabase", null, 1);
    }

    /**
     * Creates the database for the first time
     *
     * @param db database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createProfileTBL = "CREATE TABLE " + PROFILE_TBL_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id TEXT, " +
                "name TEXT, " +
                "email TEXT, " +
                "profile_picture TEXT, " +
                "city TEXT, " +
                "state TEXT, " +
                "postal_code TEXT);";

        //Create profile table
        db.execSQL(createProfileTBL);
    }

    /**
     * Writes the user's profile data into the profile table
     *
     * @param id         the user's Google account ID
     * @param name       the user's name
     * @param email      the user's email
     * @param picture    the user's profile picture
     * @param city       the user's current city
     * @param state      the user's current state
     * @param postalcode the user's current postal code
     * @return true if successful insert, false if failed
     */
    public boolean insertProfileInfo(String id, String name, String email, String picture, String city, String state, String postalcode) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //Put information into content values
        values.put("user_id", id);
        values.put("name", name);
        values.put("email", email);
        values.put("profile_picture", picture);
        values.put("city", city);
        values.put("state", state);
        values.put("postal_code", postalcode);

        //Insert info into the database
        long result = database.insert(PROFILE_TBL_NAME, null, values);

        //Returns false if failed, true if successful
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Reads the User's Profile Data from the Database
     *
     * @param id the user's Google ID
     * @return
     */
    public Cursor getProfileData(String id) {

        SQLiteDatabase db = this.getWritableDatabase();

        //SQL Query to find user's profile info
        String query = "SELECT name, email, profile_picture, city, state, postal_code FROM " + PROFILE_TBL_NAME + " WHERE user_id = '" + id + "'";

        //Return Cursor (pointer) to query results
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Updates the user's profile data in the database
     *
     * @param name       the user's name
     * @param email      the user's email
     * @param city       the user's city
     * @param state      the user's state
     * @param postalcode the user's postal code (zipcode)
     * @param id         the user's Google id
     * @return true if update is a success, false if failed
     */
    public boolean updateProfile(String name, String email, String city, String state, String postalcode, String id) {
        SQLiteDatabase database = this.getWritableDatabase();

        //Put profile info into content values
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("city", city);
        values.put("state", state);
        values.put("postal_code", postalcode);

        //Update database where user_id = id
        return database.update(PROFILE_TBL_NAME, values, "user_id='" + id + "'", null) > 0;
    }

    /**
     * Delete the user's data from the database (deleting their Cinema Pal account)
     *
     * @param id the user's Google ID
     * @return true if the deletion has been successful, false if failed
     */
    public boolean deleteProfile(String id) {
        SQLiteDatabase database = this.getWritableDatabase();

        //Delete row from database of a user
        return database.delete(PROFILE_TBL_NAME, "user_id='" + id + "'", null) > 0;
    }


    /**
     * Upgrades the databsae from the old version to the newer version
     *
     * @param db         the database to be upgraded
     * @param oldVersion the old database version
     * @param newVersion the new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop old database
        db.execSQL("DROP IF TABLE EXISTS " + PROFILE_TBL_NAME);
        //Create new database
        onCreate(db);
    }
}
