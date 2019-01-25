package com.guestlogix.travelercorekit.task;

import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.error.TravelerErrorCode;
import com.guestlogix.travelercorekit.utilities.InputStreamHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class NetworkTask extends Task {

    private Request mRequest;
    private TravelerError mError;
    private ResponseHandler mResponseHandler;

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
    }

    public void setRequest(Request mRequest) {
        this.mRequest = mRequest;
    }

    public NetworkTask(Request request, ResponseHandler responseHandler) {
        mRequest = request;
        mResponseHandler = responseHandler;
    }

    public TravelerError getError() {
        return mError;
    }

    @Override
    public void execute() {
        // Some initial error handling
        if (mRequest == null) {
            mError = new TravelerError(TravelerErrorCode.NO_REQUEST, null);
            finish();
            return;
        }

        URL url = null;
        try {
            url = mRequest.getURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (url == null) {
            mError = new TravelerError(TravelerErrorCode.BAD_URL, null);
            finish();
            return;
        }

        String protocol = url.getProtocol();
        if (protocol == null || !(protocol.equalsIgnoreCase("http") || protocol.equalsIgnoreCase("https"))) {
            mError = new TravelerError(TravelerErrorCode.BAD_URL, null);
            finish();
            return;
        }

        HttpURLConnection urlConnection = null;

        try {
            // Configure the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            setupConnection(urlConnection);

            // Open connection
            urlConnection.connect();

            // Process connection
            processConnection(urlConnection);
        } catch (IOException e) {

            String errorMessage = null;

            if (urlConnection != null) {
                errorMessage = InputStreamHelper.getStringFromInputStream(urlConnection.getErrorStream());
            }

            mError = new TravelerError(TravelerErrorCode.CONNECTION_ERROR, errorMessage);
        }
        finish();
    }

    private void setupConnection(HttpURLConnection conn) throws IOException {
        setHeaders(conn);
        setMethod(conn);
    }

    private void setHeaders(HttpURLConnection urlConnection) {
        Map<String, String> headers = mRequest.getHeaders();

        if (headers != null) {
            for (Map.Entry<String, String> header :
                    headers.entrySet()) {

                urlConnection.setRequestProperty(header.getKey(), header.getValue());
            }
        }
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
    }

    private void setMethod(HttpURLConnection urlConnection) throws IOException {
        switch (mRequest.getMethod()) {
            case GET:
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(false);
                break;
            case PUT:
                urlConnection.setRequestMethod("PUT");
                urlConnection.setDoOutput(true);
                mRequest.onProvidePayload(urlConnection.getOutputStream());
                break;
            case POST:
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                mRequest.onProvidePayload(urlConnection.getOutputStream());
                break;
            case PATCH:
                urlConnection.setRequestMethod("PATCH");
                urlConnection.setDoOutput(true);
                mRequest.onProvidePayload(urlConnection.getOutputStream());
                break;
            case DELETE:
                urlConnection.setRequestMethod("DELETE");
                urlConnection.setDoOutput(false);
                break;
        }
    }

    private void processConnection(HttpURLConnection urlConnection) throws IOException {
        int statusCode = urlConnection.getResponseCode();

        if (statusCode == 401) {
            mError = new TravelerError(TravelerErrorCode.UNAUTHORIZED,
                    InputStreamHelper.getStringFromInputStream(urlConnection.getInputStream()));
        } else if (statusCode == 403) {
            mError = new TravelerError(TravelerErrorCode.FORBIDDEN, null);
        } else if (statusCode >= 500) {
            mError = new TravelerError(TravelerErrorCode.SERVER_ERROR,
                    InputStreamHelper.getStringFromInputStream(urlConnection.getInputStream()));
        } else {
            InputStream is = urlConnection.getInputStream();

            if (mResponseHandler != null) {
                mResponseHandler.onHandleResponse(is);
            }
            is.close();
        }
    }
}
