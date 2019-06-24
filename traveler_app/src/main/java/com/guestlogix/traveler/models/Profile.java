package com.guestlogix.traveler.models;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonToken;
import androidx.annotation.NonNull;
import com.guestlogix.traveler.utils.SharedPrefsUtils;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.IOException;
import java.io.Serializable;

public class Profile implements Serializable {
    private String travelerId;
    private String externalId;
    private String firstName;
    private String lastName;
    private String email;

    public Profile(@NonNull String travelerId, @NonNull String externalId, @NonNull String firstName, @NonNull String lastName, @NonNull String email) {
        this.travelerId = travelerId;
        this.externalId = externalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getTravelerId() {
        return travelerId;
    }

    public String getExternalId() {
        return externalId;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static class ProfileObjectMappingFactory implements ObjectMappingFactory<Profile> {

        @Override
        public Profile instantiate(JsonReader reader) throws Exception {
            String travelerId = null;
            String externalId = null;
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
                    case "externalId":
                        externalId = reader.nextString();
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
            Assertion.eval(externalId != null);
            Assertion.eval(firstName != null);
            Assertion.eval(lastName != null);
            Assertion.eval(email != null);

            return new Profile(travelerId, externalId, firstName, lastName, email);
        }
    }

    public void save(Context context) {
        SharedPrefsUtils sharedPrefsUtils = SharedPrefsUtils.getInstance(context);

        sharedPrefsUtils.putString(SharedPrefsUtils.TRAVELER_ID, getTravelerId());
        sharedPrefsUtils.putString(SharedPrefsUtils.EXTERNAL_ID, getExternalId());
        sharedPrefsUtils.putString(SharedPrefsUtils.FIRST_NAME, getFirstName());
        sharedPrefsUtils.putString(SharedPrefsUtils.LAST_NAME, getLastName());
        sharedPrefsUtils.putString(SharedPrefsUtils.EMAIL, getEmail());
    }

    public static void remove(Context context) {
        SharedPrefsUtils sharedPrefsUtils = SharedPrefsUtils.getInstance(context);

        sharedPrefsUtils.removeString(SharedPrefsUtils.TRAVELER_ID);
        sharedPrefsUtils.removeString(SharedPrefsUtils.EXTERNAL_ID);
        sharedPrefsUtils.removeString(SharedPrefsUtils.FIRST_NAME);
        sharedPrefsUtils.removeString(SharedPrefsUtils.LAST_NAME);
        sharedPrefsUtils.removeString(SharedPrefsUtils.EMAIL);
    }

    public static Profile read(Context context) {
        SharedPrefsUtils sharedPrefsUtils = SharedPrefsUtils.getInstance(context);

        String travelerId = sharedPrefsUtils.getString(SharedPrefsUtils.TRAVELER_ID, null);
        String externalId = sharedPrefsUtils.getString(SharedPrefsUtils.EXTERNAL_ID, null);
        String firstName = sharedPrefsUtils.getString(SharedPrefsUtils.FIRST_NAME, null);
        String lastName = sharedPrefsUtils.getString(SharedPrefsUtils.LAST_NAME, null);
        String email = sharedPrefsUtils.getString(SharedPrefsUtils.EMAIL, null);
        if (email == null) {
            return null;
        }
        return new Profile(travelerId, externalId, firstName, lastName, email);
    }
}
