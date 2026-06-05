package ru.otus.vinakov.metric.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Component
@Converter
public class MapStringObjectConverter implements AttributeConverter<Map<String, Object>, String> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attributes) {
        if (attributes == null) {
            return "{}";
        }
        try {
            return MAPPER.writeValueAsString(attributes);
        } catch (JacksonException e) {
            //todo rte
            throw new RuntimeException("Error serializing attributes", e);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            return MAPPER.readValue(dbData, new TypeReference<Map<String, Object>>() {
            });
        } catch (JacksonException e) {
            throw new RuntimeException("Error deserializing time periods", e);
        }
    }
}
