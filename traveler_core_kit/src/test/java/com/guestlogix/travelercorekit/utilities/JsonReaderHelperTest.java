package com.guestlogix.travelercorekit.utilities;

import android.util.JsonReader;
import android.util.JsonToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JsonReaderHelperTest {

    @Mock
    private JsonReader reader;

    @Test
    public void readString() throws IOException {
        String expected = "test";
        when(reader.peek()).thenReturn(JsonToken.STRING);
        when(reader.nextString()).thenReturn(expected);
        spy(reader).skipValue();

        String result = JsonReaderHelper.readString(reader);

        verify(reader, never()).skipValue();
        assertEquals(expected, result);
    }

    @Test
    public void readString_null() throws IOException {
        String expected = null;
        when(reader.peek()).thenReturn(JsonToken.NULL);
        spy(reader).skipValue();

        String result = JsonReaderHelper.readString(reader);

        verify(reader, times(1)).skipValue();
        verify(reader, never()).nextString();
        assertEquals(expected, result);
    }


    @Test
    public void readBoolean() throws IOException {
        Boolean expected = false;
        when(reader.peek()).thenReturn(JsonToken.STRING);
        when(reader.nextBoolean()).thenReturn(expected);
        spy(reader).skipValue();

        Boolean result = JsonReaderHelper.readBoolean(reader);

        verify(reader, never()).skipValue();
        assertEquals(expected, result);
    }

    @Test
    public void readBoolean_null() throws IOException {
        Boolean expected = null;
        when(reader.peek()).thenReturn(JsonToken.NULL);
        spy(reader).skipValue();

        Boolean result = JsonReaderHelper.readBoolean(reader);

        verify(reader, times(1)).skipValue();
        verify(reader, never()).nextBoolean();
        assertEquals(expected, result);
    }

    @Test
    public void readInteger() throws IOException {
        Integer expected = 1234;
        when(reader.peek()).thenReturn(JsonToken.NUMBER);
        when(reader.nextInt()).thenReturn(expected);
        spy(reader).skipValue();

        Integer result = JsonReaderHelper.readInteger(reader);

        verify(reader, never()).skipValue();
        assertEquals(expected, result);
    }

    @Test
    public void readInteger_null() throws IOException {
        Integer expected = null;
        when(reader.peek()).thenReturn(JsonToken.NULL);
        spy(reader).skipValue();

        Integer result = JsonReaderHelper.readInteger(reader);

        verify(reader, times(1)).skipValue();
        verify(reader, never()).nextInt();
        assertEquals(expected, result);
    }

    @Test
    public void readLong() throws IOException {
        Long expected = 1234L;
        when(reader.peek()).thenReturn(JsonToken.NUMBER);
        when(reader.nextLong()).thenReturn(expected);
        spy(reader).skipValue();

        Long result = JsonReaderHelper.readLong(reader);

        verify(reader, never()).skipValue();
        assertEquals(expected, result);
    }

    @Test
    public void readLong_null() throws IOException {
        Long expected = null;
        when(reader.peek()).thenReturn(JsonToken.NULL);
        spy(reader).skipValue();

        Long result = JsonReaderHelper.readLong(reader);

        verify(reader, times(1)).skipValue();
        verify(reader, never()).nextLong();
        assertEquals(expected, result);
    }

    @Test
    public void readDouble() throws IOException {
        Double expected = 1234.2;
        when(reader.peek()).thenReturn(JsonToken.NUMBER);
        when(reader.nextDouble()).thenReturn(expected);
        spy(reader).skipValue();

        Double result = JsonReaderHelper.readDouble(reader);

        verify(reader, never()).skipValue();
        assertEquals(expected, result);
    }

    @Test
    public void readDouble_null() throws IOException {
        Double expected = null;
        when(reader.peek()).thenReturn(JsonToken.NULL);
        spy(reader).skipValue();

        Double result = JsonReaderHelper.readDouble(reader);

        verify(reader, times(1)).skipValue();
        verify(reader, never()).nextDouble();
        assertEquals(expected, result);
    }
}