package com.guestlogix.travelercorekit.tasks;

import com.guestlogix.travelercorekit.utilities.InputStreamHelper;
import com.guestlogix.travelercorekit.utilities.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class NetworkTask extends Task {

    private Request request;
    private NetworkTaskError error;
    private ResponseHandler responseHandler;

    public interface Request {
        enum Method {
            GET, POST, PUT, DELETE, PATCH
        }

        Method getMethod();

        URL getURL() throws MalformedURLException;

        Map<String, String> getHeaders();

        void onProvidePayload(OutputStream stream);
    }

    public interface ResponseHandler {
        void onHandleResponse(InputStream stream) throws IOException;
        void onHandleError(InputStream stream);
    }

    public void setRequest(Request mRequest) {
        this.request = mRequest;
    }

    public NetworkTask(Request request, ResponseHandler responseHandler) {
        this.request = request;
        this.responseHandler = responseHandler;
    }

    public NetworkTaskError getError() {
        return error;
    }

    @Override
    public void execute() {
        // Some initial error handling
        if (request == null) {
            error = new NetworkTaskError(NetworkTaskError.Code.NO_REQUEST);
            finish();
            return;
        }

        URL url = null;
        try {
            url = request.getURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (url == null) {
            error = new NetworkTaskError(NetworkTaskError.Code.BAD_URL);
            finish();
            return;
        }

        String protocol = url.getProtocol();
        if (protocol == null || !(protocol.equalsIgnoreCase("http") || protocol.equalsIgnoreCase("https"))) {
            error = new NetworkTaskError(NetworkTaskError.Code.BAD_URL);
            finish();
            return;
        }

        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            // Headers

            Map<String, String> headers = request.getHeaders();

            if (headers != null) {
                for (Map.Entry<String, String> header :
                        headers.entrySet()) {

                    urlConnection.setRequestProperty(header.getKey(), header.getValue());
                }
            }

            urlConnection.setDoInput(true);

            // HTTP Method

            switch (request.getMethod()) {
                case GET:
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoOutput(false);
                    break;
                case PUT:
                    urlConnection.setRequestMethod("PUT");
                    urlConnection.setDoOutput(true);
                    request.onProvidePayload(urlConnection.getOutputStream());
                    break;
                case POST:
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);
                    request.onProvidePayload(urlConnection.getOutputStream());
                    break;
                case PATCH:
                    urlConnection.setRequestMethod("PATCH");
                    urlConnection.setDoOutput(true);
                    request.onProvidePayload(urlConnection.getOutputStream());
                    break;
                case DELETE:
                    urlConnection.setRequestMethod("DELETE");
                    urlConnection.setDoOutput(false);
                    break;
            }

            urlConnection.connect();

            // Cancellation check

            if (isCancelled()) {
                finish();
                return;
            }

            // Response

            int statusCode = urlConnection.getResponseCode();

            if (statusCode == 401) {
                error = new NetworkTaskError(NetworkTaskError.Code.UNAUTHORIZED);
            } else if (statusCode == 403) {
                error = new NetworkTaskError(NetworkTaskError.Code.FORBIDDEN);
            } else if (statusCode >= 500) {
                error = new NetworkTaskError(NetworkTaskError.Code.SERVER_ERROR,
                        InputStreamHelper.getStringFromInputStream(urlConnection.getInputStream()));
            } else {
                InputStream is = urlConnection.getInputStream();

                if (responseHandler != null) {
                    responseHandler.onHandleResponse(is);
                }

                is.close();
            }
        } catch (IOException e) {
            if (urlConnection != null) {
                responseHandler.onHandleError(urlConnection.getErrorStream());
            }
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        finish();
    }
}
