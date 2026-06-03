package ru.otus.vinakov.metric.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.otus.vinakov.metric.domain.Period;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Converter//todo кеш периодов
public class PeriodListConverter implements AttributeConverter<List<Period>, String> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class JsonPeriod {
        private String start;
        private String end;
    }

    @Override
    public String convertToDatabaseColumn(List<Period> attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return MAPPER.writeValueAsString(attribute.stream()
                    .map(p -> new JsonPeriod(p.getStart().toString(), p.getEnd().toString()))
                    .collect(Collectors.toList()));
        } catch (JacksonException e) {
            //todo rte
            throw new RuntimeException("Error serializing time periods", e);
        }
    }

    @Override
    public List<Period> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            List<JsonPeriod> periods = MAPPER.readValue(dbData, new TypeReference<>() {
            });
            return periods.stream().map(jp -> Period.fromString(jp.start, jp.end)).collect(Collectors.toList());
        } catch (JacksonException e) {
            throw new RuntimeException("Error deserializing time periods", e);
        }
    }
}