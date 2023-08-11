package kaif.finchesterjava.entities;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.SneakyThrows;

import java.io.IOException;

public class TrimStringDeserializer extends JsonDeserializer<String> {

    @Override
    @SneakyThrows
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String value = p.getValueAsString();
        if (value != null) {
            return value.trim();
        }
        return null;
    }
}
