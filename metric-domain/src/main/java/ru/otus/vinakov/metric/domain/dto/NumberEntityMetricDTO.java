package ru.otus.vinakov.metric.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.vinakov.metric.domain.NumberEntityMetric;

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

    public static NumberEntityMetricDTO from(NumberEntityMetric metric, boolean withMetric) {
        NumberEntityMetricDTO dto = new NumberEntityMetricDTO(metric.getId(), metric.getEntityKey(), metric.getActualValue(), metric.getTargetValue());
        if (withMetric) {
            dto.setMetric(MetricDTO.from(metric.getMetric(), false, false));
        }
        return dto;
    }

}
