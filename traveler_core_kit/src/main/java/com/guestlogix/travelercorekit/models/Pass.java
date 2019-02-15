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
    private Price price;
    private List<Question> questions;

    public Pass(String id, String name, String description, Integer maxQuantity, Price price, List<Question> questions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxQuantity = maxQuantity;
        this.price = price;
        this.questions = questions;
    }

    public Integer getMaxQuantity() {
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

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public static class PassObjectMappingFactory implements ObjectMappingFactory<Pass> {

        /**
         * Passes a json reader object into a Pass model. Does not guarantee the correctness of the resulting object.
         *
         * @param reader to read from.
         * @return Pass model.
         * @throws ObjectMappingException if mapping fails.
         * @throws IOException            if mapping fails.
         */
        @Override
        public Pass instantiate(JsonReader reader) throws ObjectMappingException, IOException {
            return readPass(reader);
        }

        private static Pass readPass(JsonReader reader) throws IOException, ObjectMappingException {
            String id = "";
            String name = "";
            String description = "";
            Integer maxQuantity = 0;
            Price price = null;
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
                        ObjectMappingFactory<Price> p = new Price.PriceObjectMappingFactory();
                        price = p.instantiate(reader);
                        break;
                    case "maximumQuantity":
                        maxQuantity = JsonReaderHelper.readInteger(reader);
                        break;
                    case "questions":
                        ArrayMappingFactory<Question> factory = new ArrayMappingFactory<>(new Question.QuestionObjectMappingFactory());
                        questions = factory.instantiate(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            return new Pass(id, name, description, maxQuantity, price, questions);
        }
    }
}
