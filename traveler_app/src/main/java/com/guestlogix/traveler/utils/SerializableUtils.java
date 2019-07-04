package com.guestlogix.traveler.utils;

import android.util.Base64;

import java.io.*;

public class SerializableUtils {
    static public String stringFromObject(Object object) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(stream);
        outputStream.writeObject(object);
        outputStream.close();
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }

    static public Object objectFromString(String string) throws IOException, ClassNotFoundException {
        byte[] data = Base64.decode(string, Base64.DEFAULT);
        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(data));
        Object object = inputStream.readObject();
        inputStream.close();
        return object;
    }
}
