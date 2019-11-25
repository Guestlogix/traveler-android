package com.guestlogix.travelercorekit.tasks;

import android.util.Log;

import com.guestlogix.travelercorekit.BuildConfig;
import com.guestlogix.travelercorekit.utilities.InputStreamHelper;
import com.guestlogix.travelercorekit.utilities.Task;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class NetworkTask extends Task {

    private Route route;
    private Error error;
    private ResponseHandler responseHandler;

    public interface Route {
        enum Method {
            GET("GET", false),
            POST("POST", true),
            PUT("PUT", true),
            DELETE("DELETE", false),
            PATCH("PATCH", true);

            private String value;
            private boolean doOutput;

            Method(String value, Boolean doOutput) {
                this.value = value;
                this.doOutput = doOutput;
            }

            public String getValue() {
                return value;
            }

            public boolean isDoOutput() {
                return doOutput;
            }
        }

        Method getMethod();

        URL getURL() throws MalformedURLException;

        Map<String, String> getHeaders();

        Error transformError(NetworkTaskError error);

        void onProvidePayload(OutputStream stream);
    }

    public interface ResponseHandler {
        void onHandleResponse(InputStream stream) throws IOException;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public NetworkTask(Route route, ResponseHandler responseHandler) {
        this.route = route;
        this.responseHandler = responseHandler;
    }

    public Error getError() {
        return error;
    }

    @Override
    public void execute() {
        if (route == null) {
            this.error = new NetworkTaskError(NetworkTaskError.Code.NO_ROUTE);
            finish();
            return;
        }

        NetworkTaskError error = null;

        URL url = null;
        try {
            url = route.getURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (url == null) {
            this.error = new NetworkTaskError(NetworkTaskError.Code.BAD_URL);
            finish();
            return;
        }

        String protocol = url.getProtocol();
        if (protocol == null || !(protocol.equalsIgnoreCase("http") || protocol.equalsIgnoreCase("https"))) {
            this.error = new NetworkTaskError(NetworkTaskError.Code.BAD_URL);
            finish();
            return;
        }

        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            // Headers

            Map<String, String> headers = route.getHeaders();

            if (headers != null) {
                for (Map.Entry<String, String> header :
                        headers.entrySet()) {

                    urlConnection.setRequestProperty(header.getKey(), header.getValue());
                }
            }

            urlConnection.setDoInput(true);

            // HTTP Method

            urlConnection.setRequestMethod(route.getMethod().getValue());
            urlConnection.setDoOutput(route.getMethod().isDoOutput());

            if (BuildConfig.DEBUG) {
                logTheRequest(url, headers, route.getMethod());
            }

            if (route.getMethod().isDoOutput()) {
                route.onProvidePayload(urlConnection.getOutputStream());
            }

            urlConnection.connect();

            // Cancellation check

            if (isCancelled()) {
                finish();
                return;
            }

            // Response

            int statusCode = urlConnection.getResponseCode();

            if (BuildConfig.DEBUG) {
                logTheResponse(url, statusCode);
            }

            if (statusCode == 401) {
                error = new NetworkTaskError(NetworkTaskError.Code.UNAUTHORIZED);
            } else if (statusCode == 403) {
                error = new NetworkTaskError(NetworkTaskError.Code.FORBIDDEN);
            } else if (statusCode >= 500) {
                error = new NetworkTaskError(NetworkTaskError.Code.SERVER_ERROR,
                        InputStreamHelper.getStringFromInputStream(urlConnection.getErrorStream()));
            } else if (statusCode >= 400) {
                error = new NetworkTaskError(NetworkTaskError.Code.CLIENT_ERROR,
                        InputStreamHelper.getStringFromInputStream(urlConnection.getErrorStream()));
            } else {
                InputStream is = urlConnection.getInputStream();

                if (responseHandler != null) {
                    responseHandler.onHandleResponse(is);
                }

                is.close();
            }
        } catch (IOException e) {

            String errorMessage = null;

            if (urlConnection != null) {
                errorMessage = InputStreamHelper.getStringFromInputStream(urlConnection.getErrorStream());
            }

            error = new NetworkTaskError(NetworkTaskError.Code.CONNECTION_ERROR, errorMessage);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        if (error != null) {
            this.error = route.transformError(error);
        }

        finish();
    }

    private void logTheRequest(URL url, Map<String, String> headers, Route.Method method) {

        StringBuilder headersForLog = new StringBuilder();
        if (headers != null) {
            for (String key : headers.keySet()) {
                headersForLog.append(key + " : " + headers.get(key) + "\n");
            }
        }

        Log.d("NetworkTask",
                " \n------------------------\n--------API CALL--------\n------------------------" +
                        "\n URL: " + url.toString() + "(" + method.getValue() + ")" +
                        "\n HEADERS:" + ((headersForLog.toString().isEmpty()) ? " [NO HEADER]" : "\n" + headersForLog));
    }

    private void logTheResponse(URL url, int statusCode) {

        Log.d("NetworkTask",
                " \n------------------------\n------API RESPONSE------\n------------------------" +
                        "\n URL: " + url.toString() + "(" + route.getMethod().getValue() + ")" +
                        "\n RESPONSE CODE:" + statusCode);
    }

}
