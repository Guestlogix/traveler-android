package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;


public class Token {
    private String value;

    public Token(@NonNull String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String token) {
        this.value = token;
    }

    public static class AuthTokenObjectMappingFactory implements ObjectMappingFactory<Token> {
        @Override
        public Token instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String value = jsonObject.getString("token");

            Assertion.eval(value != null);

            return new Token(value);
        }
    }
}
