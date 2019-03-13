package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pass implements Serializable {
    private String id;
    private String name;
    private String description;
    private Integer maxQuantity;
    private Price price;
    private List<Question> questions;

    private Pass(String id, String name, String description, Integer maxQuantity, Price price, List<Question> questions) {
        if (id == null) {
            throw new IllegalArgumentException("id can not be empty");
        } else {
            this.id = id;
        }
        if (price == null) {
            throw new IllegalArgumentException("price can not be empty");
        } else {
            this.price = price;
        }
        this.name = name;
        this.description = description;
        this.maxQuantity = maxQuantity;
        if (questions == null) {
            this.questions = new ArrayList<>();
        } else {
            this.questions = questions;
        }
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public String getDescription() {
        return description;
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

    public Price getPrice() {
        return price;
    }

    List<Question> getQuestions() {
        return questions;
    }

    /**
     * Factory class to construct Pass model from {@code JsonReader}.
     */

    static class PassObjectMappingFactory implements ObjectMappingFactory<Pass> {

        /**
         * Parses a json reader object into a Pass model.
         *
         * @param reader to read from.
         * @return Pass model.
         * @throws ObjectMappingException if mapping fails or any required field is missing.
         */
        @Override
        public Pass instantiate(JsonReader reader) throws ObjectMappingException {
            try {
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
                            id = JsonReaderHelper.readNonNullString(reader);
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
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            } catch (IllegalArgumentException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, e.getMessage()));

            }
        }
    }
}
