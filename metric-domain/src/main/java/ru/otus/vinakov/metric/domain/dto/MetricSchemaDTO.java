package ru.otus.vinakov.metric.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.vinakov.metric.domain.MetricSchema;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MetricSchemaDTO {

    private Long id;
    private String key;
    private String name;

    public static MetricSchemaDTO from(MetricSchema schema) {
        return new MetricSchemaDTO(schema.getId(), schema.getKey(), schema.getName());
    }

}
