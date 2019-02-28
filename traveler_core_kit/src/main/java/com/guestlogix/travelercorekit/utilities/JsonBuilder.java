package com.guestlogix.travelercorekit.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class JsonBuilder {

    public static JSONObject buildJson(Object object) throws IllegalAccessException, JSONException {
        if (object == null) {
            return null;
        }

        if (object.getClass().getAnnotation(JsonObject.class) == null) {
            throw new IllegalArgumentException("Object " + object.getClass().getName() + " does not have @JsonObject annotation");
        }

        List<Field> fields = getFields(object);
        JSONObject jsonObject = new JSONObject();

        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = getName(field);

            // Primitive types
            if (field.getType() == int.class) {
                jsonObject.put(fieldName, field.getInt(object));
            } else if (field.getType() == double.class) {
                jsonObject.put(fieldName, field.getDouble(object));
            } else if (field.getType() == float.class) {
                jsonObject.put(fieldName, field.getFloat(object));
            } else if (field.getType() == long.class) {
                jsonObject.put(fieldName, field.getLong(object));
            } else if (field.getType() == boolean.class) {
                jsonObject.put(fieldName, field.getBoolean(object));
            } else if (field.getType() == char.class) {
                jsonObject.put(fieldName, field.getChar(object));
            } else if (field.getType() == short.class) {
                jsonObject.put(fieldName, field.getShort(object));
            } else if (field.getType() == byte.class) {
                jsonObject.put(fieldName, field.getByte(object));
            } else {
                Object o = field.get(object);

                if (field.getClass().isArray()) {
                    jsonObject.put(fieldName, buildJsonArray(o));
                } else if (o instanceof Number || o instanceof Boolean || o instanceof String || o instanceof Character) {
                    jsonObject.put(fieldName, o);
                } else {
                    // Regular object. Recursive call.
                    jsonObject.put(fieldName, buildJson(o));
                }
            }
        }

        return jsonObject;
    }

    public static JSONArray buildJsonArray(Object object) throws JSONException, IllegalAccessException {
        if (object == null) {
            return null;
        }

        if (object.getClass().isArray()) {
            JSONArray jsonArray = new JSONArray();

            // Handles copy of primitive values.
            if (object instanceof int[])
                for (int i : (int[]) object) jsonArray.put(i);
            else if (object instanceof double[])
                for (double i : (double[]) object) jsonArray.put(i);
            else if (object instanceof float[])
                for (float i : (float[]) object) jsonArray.put(i);
            else if (object instanceof long[])
                for (double i : (long[]) object) jsonArray.put(i);
            else if (object instanceof boolean[])
                for (boolean i : (boolean[]) object) jsonArray.put(i);
            else if (object instanceof char[])
                for (char i : (char[]) object) jsonArray.put(i);
            else if (object instanceof short[])
                for (short i : (short[]) object) jsonArray.put(i);
            else if (object instanceof byte[])
                for (byte i : (byte[]) object) jsonArray.put(i);
            else if (object instanceof String[])
                for (String i : (String[]) object) jsonArray.put(i);
            else if (object instanceof Number[])
                for (Number i : (Number[]) object) jsonArray.put(i);
            else if (object instanceof Boolean[])
                for (Boolean i : (Boolean[]) object) jsonArray.put(i);
            else {
                // More complex object;
                Object[] arr = (Object[]) object;

                for (Object item : arr) {
                    jsonArray.put(buildJson(item));
                }
            }

            return jsonArray;
        } else if (object instanceof Iterable) {
            JSONArray jsonArray = new JSONArray();

            for (Object o : (Iterable) object) {
                if (o instanceof Number || o instanceof Boolean || o instanceof Character || o instanceof String) {
                    jsonArray.put(o);
                } else {
                    jsonArray.put(buildJson(o));
                }
            }

            return jsonArray;
        }

        throw new IllegalArgumentException("Expected object " + object.getClass().toString() + " to be an array, but it is not");
    }

    // Returns all fields in the object which are to be JSONified
    private static List<Field> getFields(Object o) {
        List<Field> fields = new ArrayList<>();

        // Iterate through all super classes until it no longer has the JsonObject annotation.
        Class<?> current = o.getClass();
        while (current != null && current.getAnnotation(JsonObject.class) != null) {
            Field[] currFields = current.getDeclaredFields();

            for (Field field : currFields) {
                // Only add fields which have the JsonField annotation.
                if (field.getAnnotation(JsonField.class) != null) {
                    fields.add(field);
                }
            }

            current = current.getSuperclass();
        }

        return fields;
    }

    private static String getName(Field f) {
        JsonField j = f.getAnnotation(JsonField.class);

        if (j.name().isEmpty()) {
            return f.getName();
        } else {
            return j.name();
        }
    }
}
