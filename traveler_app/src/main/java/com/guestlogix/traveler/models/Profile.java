package com.guestlogix.traveler.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.JsonReader;
import android.util.Log;
import androidx.annotation.NonNull;
import com.guestlogix.traveler.utils.SerializableUtils;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.*;

public class Profile implements Serializable {
    static private String SHARED_PREFS_KEY = "PROFILE_KEY";

    private String travelerId;
    private String firstName;
    private String lastName;
    private String email;

    public Profile(@NonNull String travelerId, @NonNull String firstName, @NonNull String lastName, @NonNull String email) {
        this.travelerId = travelerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getTravelerId() {
        return travelerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public static class ProfileObjectMappingFactory implements ObjectMappingFactory<Profile> {
        @Override
        public Profile instantiate(JsonReader reader) throws Exception {
            String travelerId = null;
            String firstName = null;
            String lastName = null;
            String email = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();
                switch (key) {
                    case "travelerId":
                        travelerId = reader.nextString();
                        break;
                    case "firstName":
                        firstName = reader.nextString();
                        break;
                    case "lastName":
                        lastName = reader.nextString();
                        break;
                    case "email":
                        email = reader.nextString();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(travelerId != null);
            Assertion.eval(firstName != null);
            Assertion.eval(lastName != null);
            Assertion.eval(email != null);

            return new Profile(travelerId, firstName, lastName, email);
        }
    }

    public void save(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        try {
            editor.putString(SHARED_PREFS_KEY, SerializableUtils.stringFromObject(this));
        } catch (IOException e) {
            Log.e("Profile", "Error saving Profile: " + e.toString());
            return;
        }

        editor.apply();
    }

    public static void clearStoredProfile(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SHARED_PREFS_KEY);
        editor.apply();
    }

    public static Profile storedProfile(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        String string = sharedPreferences.getString(SHARED_PREFS_KEY, null);

        if (string == null) {
            return null;
        }

        Profile storedProfile = null;

        try {
            return (Profile) SerializableUtils.objectFromString(string);
        } catch (Exception e) {
            Log.e("Profile", "Error reading Profile: " + e.toString());
            return null;
        }
    }
}
