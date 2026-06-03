package ru.otus.vinakov.jira.plugin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NumberEntityMetricDTO extends EntityMetricDTO {

    private Double actualValue;
    private Double targetValue;

    public NumberEntityMetricDTO(Long id, String entityKey, Double actualValue, Double targetValue) {
        super(id, entityKey);
        this.actualValue = actualValue;
        this.targetValue = targetValue;
    }

}
