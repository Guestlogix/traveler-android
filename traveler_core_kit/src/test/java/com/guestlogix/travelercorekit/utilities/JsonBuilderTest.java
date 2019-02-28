package com.guestlogix.travelercorekit.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JsonBuilderTest {
    Parent t;

    @Before
    public void setup() {
        t = new Parent();
        t.booleanField = true;
        t.id = "dasdas";
        t.charField = 'c';
        t.longField = 1l;
        t.intField = 2;
        t.doubleField = 3.;
        t.floatField = 4f;
    }

    @Test
    public void buildJson() throws JSONException, IllegalAccessException {
        Child a = new Child();
        a.i = 3;
        a.intField = 4;
        Parent b = a;
        JSONObject jsonObject = JsonBuilder.buildJson(b);

        assertNotNull(jsonObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildJsonArray_shouldFailArrayCheck() throws JSONException, IllegalAccessException {
        JSONArray jsonObject = JsonBuilder.buildJsonArray(t);

        fail();
    }

    @Test
    public void buildJsonArray_shouldWorkWithObjects() throws JSONException, IllegalAccessException {
        Parent[] arr = new Parent[]{t, t};
        JSONArray jsonObject = JsonBuilder.buildJsonArray(arr);

        assertNotNull(jsonObject);
    }

    @Test
    public void buildJsonArray_shouldWorkWithPrimitives() throws JSONException, IllegalAccessException {
        int[] arr = new int[]{1, 2, 3};
        JSONArray jsonObject = JsonBuilder.buildJsonArray(arr);

        assertNotNull(jsonObject);
    }

    @Test
    public void buildJsonArray_shouldWorkWithPrimitiveObjects() throws JSONException, IllegalAccessException {
        Integer[] arr = new Integer[]{1, 2, 3};
        JSONArray jsonObject = JsonBuilder.buildJsonArray(arr);

        assertNotNull(jsonObject);
    }

    @Test
    public void buildJsonArray_shouldWorkWithLists() throws JSONException, IllegalAccessException {
        List<Integer> arr = new ArrayList<>();
        arr.add(1);
        arr.add(2);
        arr.add(3);
        JSONArray jsonObject = JsonBuilder.buildJsonArray(arr);

        assertNotNull(jsonObject);
    }

    @Test
    public void buildJsonArray_shouldWorkWithListsOfObject() throws JSONException, IllegalAccessException {
        List<Parent> arr = new ArrayList<>();
        arr.add(t);
        arr.add(t);
        Child c = new Child();
        c.i = 3;
        c.booleanField = false;
        arr.add(c);
        JSONArray jsonObject = JsonBuilder.buildJsonArray(arr);

        assertNotNull(jsonObject);
    }

    @JsonObject
    private class Parent {
        @JsonField
        public int intField;
        @JsonField
        public float floatField;
        public double doubleField;
        @JsonField
        public long longField;
        @JsonField
        public boolean booleanField;
        @JsonField
        public char charField;
        @JsonField
        private String id;
    }

    @JsonObject
    private class Child extends Parent {
        @JsonField
        int i = 1;
    }
}