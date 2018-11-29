package com.guestlogix.network;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class NetworkManager {

    public NetworkManager() {
    }

    public static void execute(NetworkTask_ task) {

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper(task.getJsonObjectMapperCallback(), task.getmFact());

        String testResponse = "{\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkZXZpY2VJZCI6ImQwNTFhZGQ2LTU4NDAtNDgxMy1hYTc2LTQ1YmIwNDRhNzc2MiIsImNvbXBhbnlJZCI6IjMzNGNjYTZkLTk4ZGQtNDAyOS1hMzI2LTg2ZDUzNjUyOGEwYiIsImV4cCI6MTU0NDIxNjQ2OSwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo1NzQwNyIsImF1ZCI6Imh0dHA6Ly9sb2NhbGhvc3Q6NTc0MDcifQ.MXNF8fQ2yD-ep2EXuelnRQMBur3k-uLA3ws0Qx1GMiQ\",\n" +
                "    \"created\": \"2018-11-23T21:01:09.4970791Z\",\n" +
                "    \"expires\": \"2018-12-07T21:01:09.4970791Z\"\n" +
                "}";

        InputStream stream = new ByteArrayInputStream(testResponse.getBytes(Charset.defaultCharset()));

        jsonObjectMapper.onSuccess(stream);
    }
}
