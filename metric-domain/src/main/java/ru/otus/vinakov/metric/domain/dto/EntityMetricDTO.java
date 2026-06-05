package ru.otus.vinakov.metric.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntityMetricDTO {

    private Long id;
    private String entityKey;
    private MetricDTO metric;

    public EntityMetricDTO(Long id, String entityKey) {
        this.id = id;
        this.entityKey = entityKey;
    }
}
