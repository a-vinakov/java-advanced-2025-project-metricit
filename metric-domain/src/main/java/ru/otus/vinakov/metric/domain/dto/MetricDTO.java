package ru.otus.vinakov.metric.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.vinakov.metric.domain.Metric;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MetricDTO {

    private Long id;
    private String key;
    private String name;
    private Integer index;
    private String type;
    private Metric.Category category;
    private MetricSchemaDTO schema;
    private List<MetricConditionDTO> conditions;

    public MetricDTO(Long id, String key, String name, Integer index, String type, Metric.Category category) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.index = index;
        this.type = type;
        this.category = category;
    }

    public static MetricDTO from(Metric metric, boolean includeSchema, boolean includeConditions) {
        MetricDTO dto = new MetricDTO(metric.getId(), metric.getKey(), metric.getName(), metric.getIndex(), metric.getType(), metric.getCategory());
        if (includeSchema) {
            dto.setSchema(MetricSchemaDTO.from(metric.getSchema()));
        }
        if (includeConditions) {
            dto.setConditions(metric.getConditions().stream().map(c -> MetricConditionDTO.from(c, false)).collect(Collectors.toList()));
        }
        return dto;
    }

}
