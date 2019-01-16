package com.guestlogix.travelercorekit.task;

import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.error.TravelerErrorCode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NetworkTaskTest {

    @Mock
    NetworkTask.Request request;

    @Mock
    NetworkTask.ResponseHandler handler;

    private NetworkTask task;
    private TravelerError error;
    private final String PROTOCOL = "https";

    @Before
    public void setup() {
        task = new NetworkTask(request, handler);
    }

    @Test
    public void execute_nullRequest_error() {
        task.setRequest(null);

        task.execute();
        error = task.getError();


        assertNotNull(error);
        assertEquals(TravelerErrorCode.NO_REQUEST, error.getCode());
    }

    @Test
    public void execute_malformedUrl_error() throws MalformedURLException {
        when(request.getURL()).thenThrow(MalformedURLException.class);

        task.execute();
        error = task.getError();

        assertNotNull(error);
        assertEquals(TravelerErrorCode.BAD_URL, error.getCode());
    }

    @Test
    public void execute_nullUrl_error() throws MalformedURLException {
        doReturn(null).when(request).getURL();

        task.execute();
        error = task.getError();

        assertNotNull(error);
        assertEquals(TravelerErrorCode.BAD_URL, error.getCode());
    }

    @Test
    public void execute_nullProtocol_error() throws MalformedURLException {
        URL url = mock(URL.class);
        doReturn(url).when(request).getURL();
        doReturn(null).when(url).getProtocol();


        task.execute();
        error = task.getError();

        assertNotNull(error);
        assertEquals(TravelerErrorCode.BAD_URL, error.getCode());
    }

    @Test
    public void execute_invalidProtocol_error() throws MalformedURLException {
        URL url = mock(URL.class);
        doReturn(url).when(request).getURL();
        doReturn("something else").when(url).getProtocol();

        task.execute();
        error = task.getError();

        assertNotNull(error);
        assertEquals(TravelerErrorCode.BAD_URL, error.getCode());
    }

    @Test
    public void execute_openConnectionException_error() throws IOException {
        URL url = mock(URL.class);
        doReturn(url).when(request).getURL();
        doReturn(PROTOCOL).when(url).getProtocol();
        doThrow(IOException.class).when(url).openConnection();

        task.execute();
        error = task.getError();

        assertNotNull(error);
        assertEquals(TravelerErrorCode.CONNECTION_ERROR, error.getCode());
    }

    @Test
    public void executeSetup_nullHeadersGET_error() throws IOException {
        URL url = mock(URL.class);
        HttpURLConnection conn = mock(HttpURLConnection.class);
        doReturn(url).when(request).getURL();
        doReturn(PROTOCOL).when(url).getProtocol();
        doReturn(conn).when(url).openConnection();
        doReturn(NetworkTask.Request.Method.GET).when(request).getMethod();
        doReturn(null).when(request).getHeaders();
        doReturn(401).when(conn).getResponseCode();

        task.execute();
        error = task.getError();

        assertNotNull(error);
        verify(conn, never()).setRequestProperty(any(), any());
        verify(conn).setRequestMethod("GET");
        verify(conn).setDoOutput(false);
        verify(conn).setUseCaches(true);
        verify(conn).connect();
        verify(request, never()).onProvidePayload(any());

        assertEquals(TravelerErrorCode.UNAUTHORIZED, error.getCode());
    }
    @Test
    public void execute_headersPUT_error() throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("Hello", "World");
        URL url = mock(URL.class);
        HttpURLConnection conn = mock(HttpURLConnection.class);
        doReturn(url).when(request).getURL();
        doReturn(PROTOCOL).when(url).getProtocol();
        doReturn(conn).when(url).openConnection();
        doReturn(NetworkTask.Request.Method.PUT).when(request).getMethod();
        doReturn(map).when(request).getHeaders();
        doReturn(403).when(conn).getResponseCode();

        task.execute();
        error = task.getError();

        assertNotNull(error);
        verify(conn, times(1)).setRequestProperty("Hello", "World");
        verify(conn).setRequestMethod("PUT");
        verify(conn).setDoOutput(true);
        verify(conn).setUseCaches(true);
        verify(conn).connect();
        verify(request, times(1)).onProvidePayload(any());

        assertEquals(TravelerErrorCode.FORBIDDEN, error.getCode());
    }

    @Test
    public void execute_post500_error() throws IOException {
        URL url = mock(URL.class);
        HttpURLConnection conn = mock(HttpURLConnection.class);
        doReturn(url).when(request).getURL();
        doReturn(PROTOCOL).when(url).getProtocol();
        doReturn(conn).when(url).openConnection();
        doReturn(NetworkTask.Request.Method.POST).when(request).getMethod();
        doReturn(500).when(conn).getResponseCode();

        task.execute();
        error = task.getError();

        assertNotNull(error);
        verify(conn).setRequestMethod("POST");
        verify(conn).setDoOutput(true);
        verify(conn).setUseCaches(true);
        verify(conn).connect();
        verify(request, times(1)).onProvidePayload(any());

        assertEquals(TravelerErrorCode.SERVER_ERROR, error.getCode());
    }

    @Test
    public void execute_patch200_noError() throws IOException {
        URL url = mock(URL.class);
        HttpURLConnection conn = mock(HttpURLConnection.class);
        InputStream is = mock(InputStream.class);
        doReturn(url).when(request).getURL();
        doReturn(PROTOCOL).when(url).getProtocol();
        doReturn(conn).when(url).openConnection();
        doReturn(NetworkTask.Request.Method.PATCH).when(request).getMethod();
        doReturn(200).when(conn).getResponseCode();
        doReturn(is).when(conn).getInputStream();

        task.execute();
        error = task.getError();

        assertNull(error);
        verify(conn).setRequestMethod("PATCH");
        verify(conn).setDoOutput(true);
        verify(conn).setUseCaches(true);
        verify(conn).connect();
        verify(request, times(1)).onProvidePayload(any());
        verify(conn, times(1)).getInputStream();
        verify(handler).onHandleResponse(is);
        verify(is).close();
    }

    @Test
    public void execute_deleteResponseCodeException_noError() throws IOException {
        URL url = mock(URL.class);
        HttpURLConnection conn = mock(HttpURLConnection.class);
        InputStream is = mock(InputStream.class);
        doReturn(url).when(request).getURL();
        doReturn(PROTOCOL).when(url).getProtocol();
        doReturn(conn).when(url).openConnection();
        doReturn(NetworkTask.Request.Method.DELETE).when(request).getMethod();
        doThrow(IOException.class).when(conn).getResponseCode();

        task.execute();
        error = task.getError();

        assertNotNull(error);
        verify(conn).setRequestMethod("DELETE");
        verify(conn).setDoOutput(false);
        verify(conn).setUseCaches(true);
        verify(conn).connect();
        verify(conn, never()).getInputStream();
        verify(handler, never()).onHandleResponse(is);
        verify(is, never()).close();

        assertEquals(TravelerErrorCode.CONNECTION_ERROR, error.getCode());
    }

}