package com.guestlogix.task;

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
    private Error mError;
    private ResponseHandler mResponseHandler;

    public enum ErrorCode {
        BAD_URL, CONNECTION_ERROR, NO_REQUEST, FORBIDDEN, UNAUTHORIZED, SERVER_ERROR
    }

    public interface Request {
        enum Method {
            GET, POST, PUT, DELETE, PATCH
        }

        Method getMethod();

        URL getURL() throws MalformedURLException;

        HashMap<String, String> getHeaders();

        void onProvidePayload(OutputStream stream);
    }

    public interface ResponseHandler {
        void onHandleResponse(InputStream stream) throws IOException;
    }

    public class NetworkTaskError extends Error {
        private ErrorCode mCode;
        private String mMessage;

        NetworkTaskError(ErrorCode code, String message) {
            mCode = code;
            mMessage = message;
        }

        public String toString() {
            return mCode + mMessage;
        }
    }

    public NetworkTask(Request request, ResponseHandler responseHandler) {
        mRequest = request;
        mResponseHandler = responseHandler;
    }

    public Error getError() {
        return mError;
    }

    @Override
    public void execute() {
        // Some initial error handling

        if (mRequest == null) {
            mError = new NetworkTaskError(ErrorCode.NO_REQUEST, null);
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
            mError = new NetworkTaskError(ErrorCode.BAD_URL, null);
            finish();
            return;
        }

        String protocol = url.getProtocol();

        if (protocol == null || (!protocol.toLowerCase().equals("http") && !protocol.toLowerCase().equals("https"))) {
            mError = new NetworkTaskError(ErrorCode.BAD_URL, null);
            finish();
            return;
        }

        // Open Connection

        HttpURLConnection urlConnection = null;

        try {
            // Configure the connection

            urlConnection = (HttpURLConnection) url.openConnection();

            Map<String, String> headers = mRequest.getHeaders();

            if (headers != null) {
                for (Map.Entry<String, String> header :
                        headers.entrySet()) {

                    urlConnection.setRequestProperty(header.getKey(), header.getValue());
                }
            }

            urlConnection.setUseCaches(true);

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


            // Open connection

            urlConnection.connect();

            int statusCode = urlConnection.getResponseCode();

            if (statusCode == 401) {
                mError = new NetworkTaskError(ErrorCode.UNAUTHORIZED,
                        InputStreamHelper.getStringFromInputStream(urlConnection.getInputStream()));
            } else if (statusCode == 403) {
                mError = new NetworkTaskError(ErrorCode.FORBIDDEN, null);
            } else if (statusCode >= 500) {
                mError = new NetworkTaskError(ErrorCode.SERVER_ERROR,
                        InputStreamHelper.getStringFromInputStream(urlConnection.getInputStream()));
            } else {
                InputStream is = urlConnection.getInputStream();

                if (mResponseHandler != null) {
                    mResponseHandler.onHandleResponse(is);
                }

                is.close();
            }


            finish();

        } catch (IOException e) {

            String errorMessage = null;

            if (urlConnection != null) {
                errorMessage = InputStreamHelper.getStringFromInputStream(urlConnection.getErrorStream());
            }

            mError = new NetworkTaskError(ErrorCode.CONNECTION_ERROR, errorMessage);
            finish();
        } finally {
//            if (null != urlConnection) {
            //urlConnection.disconnect();
//            }
        }
    }
}
