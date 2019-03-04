package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class Pass implements Serializable {
    private String id;
    private String name;
    private String description;
    private Integer maxQuantity;
    private Price price;
    private List<Question> questions;

    private Pass(String id, String name, String description, Integer maxQuantity, Price price, List<Question> questions) {
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

    public List<Question> getQuestions() {
        return questions;
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
            String id;
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
                        id = reader.nextString();
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

            return new Pass(null, name, description, maxQuantity, price, questions);
        }
    }
}
