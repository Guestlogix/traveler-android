package com.guestlogix.traveler.models;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.traveler.utils.SharedPrefsUtils;
import com.guestlogix.travelercorekit.models.ObjectMappingError;
import com.guestlogix.travelercorekit.models.ObjectMappingErrorCode;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.IOException;
import java.io.Serializable;

public class Profile implements Serializable {

    private String travelerId;
    private String externalId;
    private String firstName;
    private String lastName;
    private String email;

    public Profile(String travelerId, String externalId, String firstName, String lastName, String email) {
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
        public Profile instantiate(JsonReader reader) throws ObjectMappingException {
            String key = "Profile";
            try {
                String travelerId = "";
                String externalId = "";
                String firstName = "";
                String lastName = "";
                String email = "";

                JsonToken token = reader.peek();
                if (JsonToken.NULL == token) {
                    reader.skipValue();
                    return null;
                }

                reader.beginObject();

                while (reader.hasNext()) {
                    key = reader.nextName();
                    switch (key) {
                        case "travelerId":
                            travelerId = JsonReaderHelper.readNonNullString(reader);
                            break;
                        case "externalId":
                            externalId = JsonReaderHelper.readNonNullString(reader);
                            break;
                        case "firstName":
                            firstName = JsonReaderHelper.readNonNullString(reader);
                            break;
                        case "lastName":
                            lastName = JsonReaderHelper.readNonNullString(reader);
                            break;
                        case "email":
                            email = JsonReaderHelper.readNonNullString(reader);
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                }

                reader.endObject();
                return new Profile(travelerId, externalId, firstName, lastName, email);
            } catch (IllegalArgumentException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, String.format(e.getMessage(), key)));
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            }
        }
    }

    public static void save(Context context, Profile profile) {
        SharedPrefsUtils sharedPrefsUtils = SharedPrefsUtils.getInstance(context);

        if (profile != null) {
            sharedPrefsUtils.putString(SharedPrefsUtils.TRAVELER_ID, profile.getTravelerId());
            sharedPrefsUtils.putString(SharedPrefsUtils.EXTERNAL_ID, profile.getExternalId());
            sharedPrefsUtils.putString(SharedPrefsUtils.FIRST_NAME, profile.getFirstName());
            sharedPrefsUtils.putString(SharedPrefsUtils.LAST_NAME, profile.getLastName());
            sharedPrefsUtils.putString(SharedPrefsUtils.EMAIL, profile.getEmail());
        } else {
            sharedPrefsUtils.removeString(SharedPrefsUtils.TRAVELER_ID);
            sharedPrefsUtils.removeString(SharedPrefsUtils.EXTERNAL_ID);
            sharedPrefsUtils.removeString(SharedPrefsUtils.FIRST_NAME);
            sharedPrefsUtils.removeString(SharedPrefsUtils.LAST_NAME);
            sharedPrefsUtils.removeString(SharedPrefsUtils.EMAIL);
        }
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
