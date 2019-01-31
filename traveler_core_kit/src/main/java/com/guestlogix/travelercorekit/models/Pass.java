package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.network.ArrayMappingFactory;
import com.guestlogix.travelercorekit.network.ObjectMappingException;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

import java.io.IOException;
import java.util.List;

public class Pass {
    private String id;
    private String name;
    private String description;
    private Integer maxQuantity;
    private Double price;
    private List<Question> questions;

    public Pass(String id, String name, String description, Integer maxQuantity, Double price, List<Question> questions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxQuantity = maxQuantity;
        this.price = price;
        this.questions = questions;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public static class PassObjectMappingFactory implements ObjectMappingFactory<Pass> {

        @Override
        public Pass instantiate(JsonReader reader) throws ObjectMappingException, IOException {
            return readPass(reader);
        }

        private static Pass readPass(JsonReader reader) throws IOException, ObjectMappingException {
            String id = "";
            String name = "";
            String description = "";
            Integer maxQuantity = 0;
            Double price = 0.0;
            List<Question> questions = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "id":
                        id = JsonReaderHelper.readString(reader);
                        break;
                    case "name":
                        name = JsonReaderHelper.readString(reader);
                        break;
                    case "description":
                        description = JsonReaderHelper.readString(reader);
                        break;
                    case "price":
                        price = JsonReaderHelper.readDouble(reader);
                        break;
                    case "maximumQuantity":
                        maxQuantity = JsonReaderHelper.readInteger(reader);
                        break;
                    case "questions":
                        ArrayMappingFactory<Question> factory = new ArrayMappingFactory<>(new Question.QuestionObjectMappingFactory());
                        questions = factory.instantiate(reader);
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            return new Pass(id, name, description, maxQuantity, price, questions);
        }
    }
}
